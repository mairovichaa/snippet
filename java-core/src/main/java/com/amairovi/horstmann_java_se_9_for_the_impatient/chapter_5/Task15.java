package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_5;

import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Task15 {

    static class TestHandler extends Handler {

        final StringBuilder result = new StringBuilder();

        private boolean doneHeader = false;

        @Override
        public void publish(LogRecord record) {
            if (!doneHeader) {
                doneHeader = true;
                String header = getFormatter().getHead(this);
                result.append(header);
            }

            Formatter formatter = getFormatter();
            String message = formatter.format(record);
            result.append(message);
        }

        @Override
        public void flush() {

        }

        @Override
        public void close() throws SecurityException {
            String tail = getFormatter().getTail(this);
            result.append(tail);
        }

        public String getResult() {
            return result.toString();
        }

    }

    static class HtmlFormatter extends Formatter {

        private final static String MESSAGE_PATTERN = "<div class=\"log-entry\" level=\"%s\" time=\"DATE TIME\">%s</div>%n";
        //        private final static String MESSAGE_PATTERN = "<div class=\"log-entry\" level=\"%s\" time=\"%2$tF %12tT\">%s</div>%n";

        @Override
        public String format(LogRecord record) {
            String message = record.getMessage();
            Level level = record.getLevel();
            return String.format(MESSAGE_PATTERN, level, message);

//            long millis = record.getMillis();
//            return String.format(MESSAGE_PATTERN, level, new Date(millis), message);
        }

        @Override
        public String getHead(Handler h) {
            return String.format("<!DOCTYPE html>%n" +
                    "<html>%n" +
                    "<head>%n" +
                    "<title>Logs</title>%n" +
                    "</head>%n" +
                    "<body>%n");
        }

        @Override
        public String getTail(Handler h) {
            return String.format("</body>%n</html>");
        }

    }

}
