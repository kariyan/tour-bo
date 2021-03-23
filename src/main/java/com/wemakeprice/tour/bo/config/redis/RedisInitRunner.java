package com.wemakeprice.tour.bo.config.redis;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class RedisInitRunner implements ApplicationRunner {
    private final TourBoRedisClusterRepository tourBoRedisClusterRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        tourBoRedisClusterRepository.get(RedisKeyManager.getRedisPrefixKey() + "connect");
        log.info("tourBoRedisClusterRepository client initialize success");
    }
}
