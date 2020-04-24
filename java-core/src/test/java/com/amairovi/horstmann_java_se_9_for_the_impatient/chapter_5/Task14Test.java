package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_5;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

class Task14Test {

    @Test
    void test() {
        Logger logger = Logger.getLogger(Task14Test.class.getName());

        class TestHandler extends Handler {

            final List<String> messages = new ArrayList<>();

            @Override
            public void publish(LogRecord record) {
                messages.add(record.getMessage());
            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }

        }

        TestHandler handler = new TestHandler();
        logger.addHandler(handler);


        List<String> badWords = Arrays.asList("sex", "drugs", "c++");
        logger.setFilter(new Task14.BadWordFilter(badWords));


        logger.info("Contains sex");
        logger.info("Contains drugs");
        logger.info("Contains c++");
        logger.info("Contains hello world");

        assertThat(handler.messages).containsExactly("Contains hello world");

    }
}
