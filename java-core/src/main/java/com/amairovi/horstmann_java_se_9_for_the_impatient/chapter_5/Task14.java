package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_5;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class Task14 {

    static class BadWordFilter implements Filter {

        private final Set<String> badWords;

        BadWordFilter(Collection<String> badWords) {
            this.badWords = new HashSet<>(badWords);
        }

        @Override
        public boolean isLoggable(LogRecord record) {
            String message = record.getMessage();
            return badWords.stream()
                    .noneMatch(message::contains);
        }

    }

}
