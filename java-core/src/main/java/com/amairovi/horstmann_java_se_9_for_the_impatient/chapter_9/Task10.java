package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class Task10 {

    private static final String NUMBER_PATTERN = "(^|\\s+)[-+]?\\d+(\\.\\d+)?(\\s+|$)";

    private static List<Double> parse1(String line) {
        Pattern p = Pattern.compile(NUMBER_PATTERN);
        Matcher m = p.matcher(line);

        List<Double> result = new ArrayList<>();

        while (m.find()) {
            String group = m.group();
            double number = Double.parseDouble(group);
            result.add(number);
        }

        return result;
    }

    private static List<Double> parse2(String line) {
        String[] split = line.split("\\s+[-+]\\s+");

        return Arrays.stream(split)
                .map(Double::parseDouble)
                .collect(Collectors.toList());
    }

    private static final String DELIMITER_PATTERN = "\\s+[-+]\\s+";

    private static List<Double> parse3(String line) {
        Pattern p = Pattern.compile(DELIMITER_PATTERN);
        Matcher m = p.matcher(line);

        List<Double> result = new ArrayList<>();

        int prevStart = 0;
        while (m.find()) {
            int prevEnd = m.start();
            String strNumberRepresentation = line.substring(prevStart, prevEnd);
            double number = Double.parseDouble(strNumberRepresentation);
            result.add(number);

            prevStart = m.end();
        }

        String strNumberRepresentation = line.substring(prevStart);
        double number = Double.parseDouble(strNumberRepresentation);
        result.add(number);

        return result;
    }

    public static void main(String[] args) {
        String str = "1 + -1 - +1 + 1.2 + -1.2 + +1.2";

        List<Double> integers = parse1(str);
        assertThat(integers).containsExactly(1.0, -1.0, 1.0, 1.2, -1.2, 1.2);

        integers = parse2(str);
        assertThat(integers).containsExactly(1.0, -1.0, 1.0, 1.2, -1.2, 1.2);

        integers = parse3(str);
        assertThat(integers).containsExactly(1.0, -1.0, 1.0, 1.2, -1.2, 1.2);
    }

}
