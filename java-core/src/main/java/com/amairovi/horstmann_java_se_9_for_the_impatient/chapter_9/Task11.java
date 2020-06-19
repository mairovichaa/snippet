package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_9;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

public class Task11 {

    private static final String PATTERN = "^(?<path>/?(\\w+/)*)(?<name>\\w+)\\.(?<extension>\\w*)$";

    public static void main(String[] args) {
        String line = "/home/cay/myfile.txt";
        matchAndCheck(line, "/home/cay/", "myfile", "txt");
        matchAndCheck("home/cay/myfile.txt", "home/cay/", "myfile", "txt");
        matchAndCheck("myfile.txt", "", "myfile", "txt");
        matchAndCheck("myfile.", "", "myfile", "");
    }


    private static void matchAndCheck(String line, String expectedPath, String expectedName, String expectedExtension) {
        Pattern p = Pattern.compile(PATTERN);
        Matcher m = p.matcher(line);

        assertThat(m.matches()).isTrue();

        String path = m.group("path");
        assertThat(path).isEqualTo(expectedPath);
        String name = m.group("name");
        assertThat(name).isEqualTo(expectedName);
        String extension = m.group("extension");
        assertThat(extension).isEqualTo(expectedExtension);

    }

}
