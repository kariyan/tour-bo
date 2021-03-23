package com.wemakeprice.tour.bo.config.redis;

import com.wemakeprice.tour.bo.config.kms.KmsProperties;
import io.lettuce.core.RedisURI;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "redis.cluster")
public class RedisClusterProperties {
    private final KmsProperties kmsProperties;
    private String clusterHosts;
    private Set<RedisURI> redisURIList;
    private Pool pool;

    @Value("${redis.key.prefix}")
    private String redisServiceKeyPrefix;

    @PostConstruct
    public void init() {
        redisURIList = Arrays.stream(clusterHosts.split(",")).map(host -> {
            String[] hostAndPort = host.split(":");
            RedisURI redisURI = RedisURI.create(hostAndPort[0], Short.parseShort(hostAndPort[1]));
            String password = kmsProperties.getRedis().getPassword();
            if (StringUtils.hasText(password)) {
                redisURI.setPassword(password);
            }
            return redisURI;
        }).collect(Collectors.toSet());

        RedisKeyManager.setRedisPrefixKey(redisServiceKeyPrefix);
    }

    @Getter
    @Setter
    @ToString
    public static class Pool {
        private int minIdle;
        private int maxIdle;
        private int maxTotal;
        private boolean jmxEnable;
        private long maxWaitMillis;
        private boolean testOnBorrow;
        private boolean testOnReturn;
        private boolean testWhileIdle;
    }
}
