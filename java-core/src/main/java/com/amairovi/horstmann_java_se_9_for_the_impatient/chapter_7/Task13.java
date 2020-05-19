package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_7;

import java.util.LinkedHashMap;
import java.util.Map;

class Task13 {

    static class Cache<K, V> extends LinkedHashMap<K, V> {

        private final int cacheSize;

        Cache(int cacheSize) {
            this.cacheSize = cacheSize;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > cacheSize;
        }

    }

}
