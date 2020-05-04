package com.amairovi.caching.concurrent_map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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


    @Configuration
    @EnableCaching
    static class Test2Configuration {

        @Bean
        public CacheManager simpleCacheManager() {
            return new ConcurrentMapCacheManager("default", "c1");
        }

        @Bean
        public Cached2 cached2(){
            return new Cached2();
        }


    }

    @CacheConfig(cacheNames = "default")
    static class Cached2 {

        @Cacheable("c1")
        public String m1(String param) {
            return param + param;
        }

    }

    @Test
    void cacheable_cache_name_overrides_cache_config_ones() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Test2Configuration.class);

        Cached2 cached = context.getBean(Cached2.class);
        String result = cached.m1("foo");
        assertThat(result).isEqualTo("foofoo");

        CacheManager manager = context.getBean(CacheManager.class);
        assertThat(manager.getCache("c1").get("foo").get()).isEqualTo("foofoo");
        assertThat(manager.getCache("default").get("foo")).isNull();
    }

    @Configuration
    @EnableCaching
    static class Test3Configuration {

        @Bean
        public CacheManager simpleCacheManager() {
            return new ConcurrentMapCacheManager("default");
        }

        @Bean
        public Cached3 cached(){
            return new Cached3();
        }


    }

    @CacheConfig(cacheNames = "default")
    static class Cached3 {

        @Cacheable
        public String m1(String param) {
            return param + param;
        }

        @Cacheable
        public String m2(String param) {
            return param + param + param;
        }
    }

    @Test
    void when_two_methods_use_the_same_cache_then_they_will_override_value() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Test3Configuration.class);

        Cached3 cached = context.getBean(Cached3.class);
        String result = cached.m1("foo");
        assertThat(result).isEqualTo("foofoo");

        CacheManager manager = context.getBean(CacheManager.class);
        assertThat(manager.getCache("default").get("foo").get()).isEqualTo(result);

        String result2 = cached.m2("foo");

        // retrieve from cache
        assertThat(result2).isEqualTo("foofoo");
    }

    @Configuration
    @EnableCaching
    static class Test4Configuration {

        @Bean
        public CacheManager simpleCacheManager() {
            return new ConcurrentMapCacheManager("default");
        }

        @Bean
        public Cached4 cached(){
            return new Cached4();
        }


    }

    @CacheConfig(cacheNames = "default")
    static class Cached4 {

        @Cacheable(key = "'m1_'.concat(#param)")
        public String m1(String param) {
            return param + param;
        }

    }

    @Test
    void when_add_prefix_to_key_then_add_it() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Test4Configuration.class);

        Cached4 cached = context.getBean(Cached4.class);
        String result = cached.m1("foo");
        assertThat(result).isEqualTo("foofoo");

        CacheManager manager = context.getBean(CacheManager.class);
        assertThat(manager.getCache("default").get("foo")).isNull();
        assertThat(manager.getCache("default").get("m1_foo").get()).isEqualTo(result);
    }

}
