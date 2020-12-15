package com.amairovi.spring_boot.transaction_logging;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BookingService {

    private final static Logger logger = LoggerFactory.getLogger(BookingService.class);

    private final JdbcTemplate jdbcTemplate;
    // to find out level of which logger has to be changed
    // check injected instance's class
    private final TransactionManager manager;

    public BookingService(JdbcTemplate jdbcTemplate, final TransactionManager manager) {
        this.jdbcTemplate = jdbcTemplate;
        this.manager = manager;
    }

    @Transactional
    public void book(String... persons) {
        for (String person : persons) {
            logger.info("Booking " + person + " in a seat...");
            jdbcTemplate.update("insert into BOOKINGS(FIRST_NAME) values (?)", person);
        }
    }

    public List<String> findAllBookings() {
        return jdbcTemplate.query("select FIRST_NAME from BOOKINGS",
                (rs, rowNum) -> rs.getString("FIRST_NAME"));
    }
}
