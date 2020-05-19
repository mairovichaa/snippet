package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import java.util.BitSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BitSetTest {

    public static void main(String[] args) {
        BitSet bitSet = new BitSet();
        bitSet.set(10);
        bitSet.set(14);
        bitSet.set(100);

        assertThat(bitSet.toString()).isEqualTo("{10, 14, 100}");
        assertThat(bitSet.toByteArray()).containsExactly(0, 68, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 16);
        assertThat(bitSet.toLongArray()).containsExactly(17408L, 68719476736L);

        assertThatThrownBy(() -> bitSet.set(-1))
                .isInstanceOf(IndexOutOfBoundsException.class)
                .hasMessage("bitIndex < 0: -1");
    }
}
