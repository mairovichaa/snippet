package com.amairovi.junit5;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.ParameterizedTest.INDEX_PLACEHOLDER;

public class ParameterizedTestExample {

    private final static String FOLDER = "com/amairovi/junit5/ParameterizedTest/";

    @ParameterizedTest(name = "[" + INDEX_PLACEHOLDER + "] {0}")
    @MethodSource("provider")
    void test(String testName, String input, String output) throws URISyntaxException, IOException {
        String in = readFile(FOLDER + input);
        String out = readFile(FOLDER + output);

        assertEquals(in, out);
    }


    private String readFile(String path) throws URISyntaxException, IOException {
        URI uri = Thread.currentThread().getContextClassLoader().getResource(path).toURI();
        byte[] bytes = Files.readAllBytes(Paths.get(uri));
        return new String(bytes);
    }


    static Stream<Arguments> provider() {
        return Stream.of(
                Arguments.of("Test #1", "input-1.txt", "output-1.txt"),
                Arguments.of("Test #2", "input-2.txt", "output-2.txt")
        );
    }

}
