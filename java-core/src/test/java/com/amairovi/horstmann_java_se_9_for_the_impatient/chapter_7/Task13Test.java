package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7.Task13.Cache;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;

class Task13Test {

    @Test
    void when_zero_sized_cache_then_it_will_be_always_empty(){
        Cache<String, String> zeroSizeCache = new Cache<>(0);

        zeroSizeCache.put("key", "value");
        zeroSizeCache.put("key2", "value");
        zeroSizeCache.put("key3", "value");
        zeroSizeCache.put("key4", "value");
        assertThat(zeroSizeCache.isEmpty()).isTrue();
    }

    @Test
    void when_1_sized_cache_then_store_only_last(){
        Cache<String, String> zeroSizeCache = new Cache<>(1);

        zeroSizeCache.put("key", "value");
        assertThat(zeroSizeCache).containsExactly(entry("key", "value"));
        zeroSizeCache.put("key2", "value2");
        assertThat(zeroSizeCache).containsExactly(entry("key2", "value2"));
        zeroSizeCache.put("key3", "value3");
        assertThat(zeroSizeCache).containsExactly(entry("key3", "value3"));
    }

    @Test
    void when_2_sized_cache_then_store_last_2(){
        Cache<String, String> zeroSizeCache = new Cache<>(2);

        zeroSizeCache.put("key", "value");
        assertThat(zeroSizeCache).containsExactly(entry("key", "value"));
        zeroSizeCache.put("key2", "value2");
        assertThat(zeroSizeCache).containsExactly(entry("key", "value"), entry("key2", "value2"));
        zeroSizeCache.put("key3", "value3");
        assertThat(zeroSizeCache).containsExactly(entry("key2", "value2"), entry("key3", "value3"));
    }
}
