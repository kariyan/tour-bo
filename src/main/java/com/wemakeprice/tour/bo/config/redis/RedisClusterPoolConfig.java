package com.wemakeprice.tour.bo.config.redis;

import io.lettuce.core.ReadFrom;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.support.ConnectionPoolSupport;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.function.Supplier;

@Slf4j
@Configuration
@AllArgsConstructor
public class RedisClusterPoolConfig {

    private final RedisClusterProperties redisClusterProperties;

    @Bean
    public ClusterTopologyRefreshOptions clusterTopologyRefreshOptions() {
        return ClusterTopologyRefreshOptions.builder()
                .enablePeriodicRefresh(Duration.ofSeconds(30))
                .enableAllAdaptiveRefreshTriggers()
                .build();
    }

    @Bean
    public ClusterClientOptions clusterClientOptions(ClusterTopologyRefreshOptions clusterTopologyRefreshOptions) {
        return ClusterClientOptions.builder()
                .topologyRefreshOptions(clusterTopologyRefreshOptions)
                .build();
    }

    @Bean
    public RedisClusterClient redisClusterClient(ClusterClientOptions clusterClientOptions) {
        RedisClusterClient clusterClient = RedisClusterClient.create(redisClusterProperties.getRedisURIList());
        redisClusterProperties.getRedisURIList().forEach(redisURI ->
                log.info("RedisURI=({}:{})", redisURI.getHost(), redisURI.getPort())
        );
        clusterClient.setOptions(clusterClientOptions);
        return clusterClient;
    }

    @Bean
    public GenericObjectPoolConfig<StatefulRedisClusterConnection<String, String>> genericObjectPoolConfig() {
        final RedisClusterProperties.Pool pool = redisClusterProperties.getPool();
        log.info("genericObjectPoolConfig={}", pool);
        GenericObjectPoolConfig<StatefulRedisClusterConnection<String, String>> genericObjectPoolConfig = new GenericObjectPoolConfig<>();
        // 최소한으로 유지할 커넥션 개수
        genericObjectPoolConfig.setMinIdle(pool.getMinIdle());
        // 커넥션 풀에 반납할 때 최대로 유지될 수 있는 커넥션 개수
        genericObjectPoolConfig.setMaxIdle(pool.getMaxIdle());
        // 동시에 사용할 수 있는 최대 커넥션 개수
        genericObjectPoolConfig.setMaxTotal(pool.getMaxTotal());
        genericObjectPoolConfig.setJmxEnabled(pool.isJmxEnable());
        // 커넥션 풀 안의 커넥션이 고갈됐을 때 커넥션 반납을 대기하는 시간
        genericObjectPoolConfig.setMaxWaitMillis(pool.getMaxWaitMillis());
        // 커넥션 풀에서 커넥션을 얻어올 때 테스트 실행
        genericObjectPoolConfig.setTestOnBorrow(pool.isTestOnBorrow());
        // 커넥션 풀로 커넥션을 반환할 때 테스트 실행
        genericObjectPoolConfig.setTestOnReturn(pool.isTestOnReturn());
        // 스레드가 실행될 때 커넥션 풀 안에 있는 유휴 상태의 커넥션을 대상으로 테스트 실행
        genericObjectPoolConfig.setTestWhileIdle(pool.isTestWhileIdle());

        return genericObjectPoolConfig;
    }

    @Bean
    public GenericObjectPool<StatefulRedisClusterConnection<String, String>> statefulRedisClusterConnectionGenericObjectPool(
            GenericObjectPoolConfig<StatefulRedisClusterConnection<String, String>> genericObjectPoolConfig, RedisClusterClient redisClusterClient) {
        return ConnectionPoolSupport.createGenericObjectPool(redisClusterConnectionSupplier(redisClusterClient), genericObjectPoolConfig);
    }

    private Supplier<StatefulRedisClusterConnection<String, String>> redisClusterConnectionSupplier(RedisClusterClient redisClusterClient) {
        return () -> {
            StatefulRedisClusterConnection<String, String> redisClusterConnection = redisClusterClient
                    .connect(new StringCodec(StandardCharsets.UTF_8));
            redisClusterConnection.setReadFrom(ReadFrom.REPLICA_PREFERRED);
            return redisClusterConnection;
        };
    }
}
