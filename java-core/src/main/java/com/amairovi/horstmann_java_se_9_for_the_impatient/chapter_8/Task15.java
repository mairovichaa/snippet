package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_8;

import java.util.DoubleSummaryStatistics;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Task15 {

    public static void main(String[] args) {
        Stream<Double> numbers = Stream.of(1.5, 2.5, 2.5, 3.5);

        Double sum = numbers.mapToDouble(d -> d).sum();
        assertThatThrownBy(() -> numbers.count())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("stream has already been operated upon or closed");

        Stream<Double> numbers2 = Stream.of(1.5, 2.5, 2.5, 3.5);

        DoubleSummaryStatistics statistics = numbers2.reduce(new DoubleSummaryStatistics(), (stats, n) -> {
            stats.accept(n);
            return stats;
        }, (stats1, stats2) -> {
            stats1.combine(stats2);
            return stats1;
        });

        assertThat(statistics.getAverage()).isEqualTo(2.5);
    }

}
