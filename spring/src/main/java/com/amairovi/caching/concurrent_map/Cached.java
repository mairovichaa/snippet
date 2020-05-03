package com.amairovi.caching.concurrent_map;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@CacheConfig(cacheNames = "default")
public class Cached {


    @Cacheable
    public String method(String param){
        return param;
    }

}
