package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_5;

import com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_5.Task15.HtmlFormatter;
import com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_5.Task15.TestHandler;
import org.junit.jupiter.api.Test;

import java.util.logging.Formatter;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

class Task15Test {


    @Test
    void test() {
        TestHandler testHandler = new TestHandler();
        Formatter htmlFormatter = new HtmlFormatter();
        testHandler.setFormatter(htmlFormatter);


        Logger global = Logger.getGlobal();
        global.setUseParentHandlers(false);
        global.addHandler(testHandler);

        global.warning("Warning message");
        global.info("Info message");


        testHandler.close();
        String result = testHandler.getResult();

        System.out.println(result);
        assertThat(result).isEqualTo("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<title>Logs</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"log-entry\" level=\"WARNING\" time=\"DATE TIME\">Warning message</div>\n" +
                "<div class=\"log-entry\" level=\"INFO\" time=\"DATE TIME\">Info message</div>\n" +
                "</body>\n" +
                "</html>");
    }

}
