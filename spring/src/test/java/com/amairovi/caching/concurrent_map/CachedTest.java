package com.amairovi.caching.concurrent_map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CacheConfiguration.class, CachedTest.Config.class})
class CachedTest {

    @Configuration
    static class Config {

        @Bean
        public Cached cached() {

            return mock(Cached.class);
        }
    }

    @Autowired CacheManager manager;
    @Autowired Cached cached;

    @Test
    void when_call_cacheable_then_cache_it() {
        when(cached.method(any(String.class))).thenReturn("first", "second");

        // First invocation returns object returned by the method
        String result = cached.method("foo");
        assertThat(result).isEqualTo("first");

        // Second invocation should return cached value, *not* second (as set up above)
        result = cached.method("foo");
        assertThat(result).isEqualTo("first");

        // Verify repository method was invoked once
        verify(cached, times(1)).method("foo");
        assertThat(manager.getCache("default").get("foo").get()).isEqualTo("first");

        // Third invocation with different key is triggers the second invocation of the repo method
        result = cached.method("bar");
        assertThat(result).isEqualTo("second");
    }
}
