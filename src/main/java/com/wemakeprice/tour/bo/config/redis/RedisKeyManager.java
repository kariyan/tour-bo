package com.wemakeprice.tour.bo.config.redis;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class RedisKeyManager {
    private static String redisServiceKeyPrefix;

    public static String getRedisPrefixKey() {
        return RedisKeyManager.redisServiceKeyPrefix;
    }

    static void setRedisPrefixKey(String redisServiceKeyPrefix) {
        RedisKeyManager.redisServiceKeyPrefix = redisServiceKeyPrefix;
    }
}
