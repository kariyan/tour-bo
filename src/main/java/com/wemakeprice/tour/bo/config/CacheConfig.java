package com.wemakeprice.tour.bo.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<CaffeineCache> caches = Arrays.stream(CacheType.values())
                .map(cache -> new CaffeineCache(cache.getCacheName(), Caffeine.newBuilder().recordStats()
                        .expireAfterWrite(cache.getExpireAfterWrite(), TimeUnit.MINUTES)
                        .maximumSize(cache.getMaximumSize())
                        .build())
                )
                .collect(Collectors.toList());
        cacheManager.setCaches(caches);
        return cacheManager;
    }

    @Getter
    private enum CacheType {
        NAVIGATION_BARS("navigation-bars", 5, 8000);

        private final String cacheName;
        private final int expireAfterWrite;
        private final int maximumSize;

        CacheType(String cacheName, int expireAfterWrite, int maximumSize) {
            this.cacheName = cacheName;
            this.expireAfterWrite = expireAfterWrite;
            this.maximumSize = maximumSize;
        }
    }
}
