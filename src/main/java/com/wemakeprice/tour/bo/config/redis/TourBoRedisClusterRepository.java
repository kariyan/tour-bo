package com.wemakeprice.tour.bo.config.redis;

import io.lettuce.core.KeyValue;
import io.lettuce.core.SetArgs;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TourBoRedisClusterRepository {

    private final GenericObjectPool<StatefulRedisClusterConnection<String, String>> statefulRedisClusterConnectionGenericObjectPool;
    private Duration commandTimeoutMilliSecondsDuration;

    @Value("${redis.cluster.commands.timeoutMilliSeconds}")
    private long commandTimeoutMilliSeconds;

    @PostConstruct
    public void init() {
        this.commandTimeoutMilliSecondsDuration = Duration.ofMillis(commandTimeoutMilliSeconds);
    }

    public void set(String key, String value) throws Exception {
        if (!StringUtils.hasText(key)) {
            return;
        }
        if (Objects.isNull(value)) {
            return;
        }
        try (StatefulRedisClusterConnection<String, String> connection = statefulRedisClusterConnectionGenericObjectPool.borrowObject()) {
            RedisAdvancedClusterCommands<String, String> clusterSyncCommands = connection.sync();
            clusterSyncCommands.setTimeout(commandTimeoutMilliSecondsDuration);
            String result = clusterSyncCommands.set(key, value);
            log.debug("cache set success={} key={}", result, key);
        } catch (Exception e) {
            log.error("cache set Failed={}", key, e);
            throw e;
        }
    }

    public void setWithExpire(String key, String value, long expireMilliSeconds) throws Exception {
        if (!StringUtils.hasText(key)) {
            return;
        }
        if (Objects.isNull(value)) {
            return;
        }
        try (StatefulRedisClusterConnection<String, String> connection = statefulRedisClusterConnectionGenericObjectPool.borrowObject()) {
            RedisAdvancedClusterCommands<String, String> clusterSyncCommands = connection.sync();
            clusterSyncCommands.setTimeout(commandTimeoutMilliSecondsDuration);
            String result = clusterSyncCommands.set(key, value, SetArgs.Builder.ex(expireMilliSeconds));
            log.debug("cache set success={} key={}", result, key);
        } catch (Exception e) {
            log.error("cache set Failed={}", key, e);
            throw e;
        }
    }

    public String get(String key) throws Exception {
        if (!StringUtils.hasText(key)) {
            return null;
        }
        String value;
        try (StatefulRedisClusterConnection<String, String> connection = statefulRedisClusterConnectionGenericObjectPool.borrowObject()) {
            RedisAdvancedClusterCommands<String, String> clusterSyncCommands = connection.sync();
            clusterSyncCommands.setTimeout(commandTimeoutMilliSecondsDuration);
            value = clusterSyncCommands.get(key);
            log.debug("cache get success={}, value={}", key, value);
        } catch (Exception e) {
            log.error("cache get failed={}", key, e);
            throw e;
        }
        return value;
    }

    public void del(String key) throws Exception {
        if (!StringUtils.hasText(key)) {
            return;
        }
        try (StatefulRedisClusterConnection<String, String> connection = statefulRedisClusterConnectionGenericObjectPool.borrowObject()) {
            RedisAdvancedClusterCommands<String, String> clusterSyncCommands = connection.sync();
            clusterSyncCommands.setTimeout(commandTimeoutMilliSecondsDuration);
            clusterSyncCommands.del(key);
            log.debug("cache del success={}, ", key);
        } catch (Exception e) {
            log.error("cache del failed={}", key, e);
            throw e;
        }
    }

    public List<KeyValue<String, String>> mget(String... keys) throws Exception {
        List<KeyValue<String, String>> keyValues;
        try (StatefulRedisClusterConnection<String, String> connection = statefulRedisClusterConnectionGenericObjectPool.borrowObject()) {
            RedisAdvancedClusterCommands<String, String> clusterSyncCommands = connection.sync();
            clusterSyncCommands.setTimeout(commandTimeoutMilliSecondsDuration);
            keyValues = clusterSyncCommands.mget(keys);
            log.debug("cache mget success={}, values={}", keys, keyValues);
        } catch (Exception e) {
            log.error("cache mget failed={}", keys, e);
            throw e;
        }
        return keyValues;
    }

    public void mset(Map<String, String> keyValues) throws Exception {
        if (CollectionUtils.isEmpty(keyValues)) {
            log.warn("empty data");
            return;
        }
        try (StatefulRedisClusterConnection<String, String> connection = statefulRedisClusterConnectionGenericObjectPool.borrowObject()) {
            RedisAdvancedClusterCommands<String, String> clusterSyncCommands = connection.sync();
            clusterSyncCommands.setTimeout(commandTimeoutMilliSecondsDuration);
            String result = clusterSyncCommands.mset(keyValues);
            log.debug("cache mset success={}", result);
        } catch (Exception e) {
            log.error("cache mset failed={}", keyValues.keySet(), e);
            throw e;
        }
    }

    public Set<String> smembers(String key) throws Exception {
        if (StringUtils.isEmpty(key)) {
            log.warn("empty key={}", key);
            return Collections.emptySet();
        }
        try (StatefulRedisClusterConnection<String, String> connection = statefulRedisClusterConnectionGenericObjectPool.borrowObject()) {
            RedisAdvancedClusterCommands<String, String> clusterSyncCommands = connection.sync();
            clusterSyncCommands.setTimeout(commandTimeoutMilliSecondsDuration);
            Set<String> smembers = clusterSyncCommands.smembers(key);
            log.debug("cache smembers key={}, successSize={}", key, smembers.size());
            return smembers;
        } catch (Exception e) {
            log.error("cache smembers failed={}", key, e);
            throw e;
        }
    }

    public long sadd(String key, String... members) throws Exception {
        if (StringUtils.isEmpty(key)) {
            log.warn("empty key={}", key);
            return 0L;
        }
        if (ArrayUtils.isEmpty(members)) {
            log.warn("empty data");
            return 0L;
        }
        try (StatefulRedisClusterConnection<String, String> connection = statefulRedisClusterConnectionGenericObjectPool.borrowObject()) {
            RedisAdvancedClusterCommands<String, String> clusterSyncCommands = connection.sync();
            clusterSyncCommands.setTimeout(commandTimeoutMilliSecondsDuration);
            Long result = clusterSyncCommands.sadd(key, members);
            log.debug("cache sadd success={}", result);
            return Optional.ofNullable(result).orElse(0L);
        } catch (Exception e) {
            log.error("cache sadd failed={}", key, e);
            throw e;
        }
    }

    public long srem(String key, String... members) throws Exception {
        if (ArrayUtils.isEmpty(members)) {
            log.warn("empty data srem key={}", key);
            return 0;
        }
        try (StatefulRedisClusterConnection<String, String> connection = statefulRedisClusterConnectionGenericObjectPool.borrowObject()) {
            RedisAdvancedClusterCommands<String, String> clusterSyncCommands = connection.sync();
            clusterSyncCommands.setTimeout(commandTimeoutMilliSecondsDuration);
            Long result = clusterSyncCommands.srem(key, members);
            log.debug("cache srem key={} success={}", key, result);
            return Optional.ofNullable(result).orElse(0L);
        } catch (Exception e) {
            log.error("cache srem failed={}", key, e);
            throw e;
        }
    }

    public boolean expire(String key, long seconds) throws Exception {
        try (StatefulRedisClusterConnection<String, String> connection = statefulRedisClusterConnectionGenericObjectPool.borrowObject()) {
            RedisAdvancedClusterCommands<String, String> clusterSyncCommands = connection.sync();
            clusterSyncCommands.setTimeout(commandTimeoutMilliSecondsDuration);
            boolean result = clusterSyncCommands.expire(key, seconds);
            log.debug("cache expire key={} success={}", key, result);
            return result;
        } catch (Exception e) {
            log.error("cache expire failed={}", key, e);
            throw e;
        }
    }

}
