package com.wemakeprice.tour.bo.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class TourBoRedisClusterRepositoryTests {

    @Autowired
    private TourBoRedisClusterRepository tourBoRedisClusterRepository;

    @Test
    void redisSetAddTest() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);

        List<LocalDateTime> localDateTimes =
                LongStream.rangeClosed(0, 5).mapToObj(now::plusHours).collect(toList());

        List<String> localDateTimeStrs = localDateTimes.stream()
                .map(localDateTime -> localDateTime.format(dateTimeFormatter)).collect(toList());

        for (String localDateTimeStr : localDateTimeStrs) {
            tourBoRedisClusterRepository.sadd(RedisKeyManager.getRedisPrefixKey() + localDateTimeStr, RedisKeyManager.getRedisPrefixKey() + "exclude:1");
            tourBoRedisClusterRepository.sadd(RedisKeyManager.getRedisPrefixKey() + localDateTimeStr, RedisKeyManager.getRedisPrefixKey() + "exclude:2");
            tourBoRedisClusterRepository.sadd(RedisKeyManager.getRedisPrefixKey() + localDateTimeStr, RedisKeyManager.getRedisPrefixKey() + "exclude:3");
            tourBoRedisClusterRepository.sadd(RedisKeyManager.getRedisPrefixKey() + localDateTimeStr, RedisKeyManager.getRedisPrefixKey() + "null");
            tourBoRedisClusterRepository.expire(RedisKeyManager.getRedisPrefixKey() + localDateTimeStr, 7200L);
        }

        for (String localDateTimeStr : localDateTimeStrs) {
            Set<String> stringObjectSet = tourBoRedisClusterRepository.smembers(RedisKeyManager.getRedisPrefixKey() + localDateTimeStr);
            log.info("key={}, exclude={}", RedisKeyManager.getRedisPrefixKey() + localDateTimeStr, stringObjectSet);
            assertThat(stringObjectSet).isNotEmpty();
        }
    }
}
