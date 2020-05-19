package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7.Task10.Neighbor;
import lombok.Value;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7.Task10.algo;
import static org.assertj.core.api.Assertions.assertThat;

class Task10Test {

    @Value
    private static class Task {

        Map<String, Set<Neighbor>> graph;
        String startCity;
        List<Neighbor> expected;

    }

    @ParameterizedTest
    @MethodSource("provider")
    void test(String filename) {
        Task task = readTask(filename);

        List<Neighbor> actual = algo(task.getGraph(), task.getStartCity());

        assertThat(actual).containsExactlyInAnyOrder(task.getExpected().toArray(new Neighbor[0]));
    }

    static Stream<Arguments> provider() {
        return Stream.of(
                Arguments.of("test1.txt"),
                Arguments.of("test-2.txt"),
                Arguments.of("test-3.txt"),
                Arguments.of("test-4.txt")

        );
    }

    private Task readTask(String filename) {
        Scanner scanner = new Scanner(Task10.class.getResourceAsStream("/com/amairovi/horstmann_java_se_9_for_the_impatient/chapter_7/Task10/" + filename));

        int amountOfCities = scanner.nextInt();
        int startCity = scanner.nextInt();

        List<Neighbor> expected = IntStream.rangeClosed(1, amountOfCities)
                .mapToObj(i -> new Neighbor("c" + i, scanner.nextInt()))
                .collect(Collectors.toList());

        Map<String, Set<Neighbor>> graph = new HashMap<>();
        for (int i = 0; i < amountOfCities; i++) {
            Set<Neighbor> neighbors = new HashSet<>();

            for (int j = 0; j < amountOfCities; j++) {
                int distance = scanner.nextInt();
                if (distance != -1) {
                    neighbors.add(new Neighbor("c" + (j + 1), distance));
                }
            }
            graph.put("c" + (i + 1), neighbors);
        }

        scanner.close();

        String startCityName = "c" + startCity;
        return new Task(graph, startCityName, expected);
    }

}
