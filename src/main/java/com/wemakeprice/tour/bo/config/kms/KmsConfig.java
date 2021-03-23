package com.wemakeprice.tour.bo.config.kms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemakeprice.tour.bo.common.util.AES256;
import com.wemakeprice.tour.bo.config.kms.dto.EncryptKmsDTO;
import com.wemakeprice.tour.bo.config.kms.dto.KmsDTO;
import com.wemakeprice.tour.bo.config.kms.dto.RedisKmsDTO;
import com.wemakeprice.tour.bo.config.kms.loader.IdcKmsLoader;
import com.wemakeprice.tour.bo.config.kms.loader.KmsLoader;
import com.wemakeprice.tour.bo.config.kms.vo.AwsVO;
import com.wemakeprice.tour.bo.config.kms.vo.DatabaseVO;
import com.wemakeprice.tour.bo.config.kms.vo.DbUserVO;
import com.wemakeprice.tour.bo.config.kms.vo.RedisVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KmsConfig {

    private final WebClient webClient;
    private final Environment environment;
    private final ObjectMapper objectMapper;

    private KmsLoader kmsLoader;
    private DatabaseVO databaseVO;
    private RedisVO redisVO;
    private AwsVO awsVO;

    @Value("${database.username-ref:#{null}}")
    private String usernameReference;
    @Value("${database.password-ref:#{null}}")
    private String passwordReference;

    @PostConstruct
    public void init() {
        initializeKmsLoader();
        initializeDatabaseAndAwsCredential();
        initializeAES256();
        initializeRedis();
    }

    @Bean
    public KmsProperties kmsProperties() {
        return new KmsProperties(databaseVO, redisVO, awsVO);
    }

    private void initializeKmsLoader() {
        kmsLoader = new IdcKmsLoader(environment, webClient, objectMapper);
    }

    private void initializeDatabaseAndAwsCredential() {

        String key = environment.getProperty("kms.secret-name");
        KmsDTO kmsDTO = kmsLoader.loadPropertiesFromKms(key, KmsDTO.class);
        if (kmsDTO != null) {
            databaseVO = kmsDTO.getDatabase();
            awsVO = kmsDTO.getAws();
        }

        if (!StringUtils.isEmpty(usernameReference)
                && !StringUtils.isEmpty(passwordReference)
                && !StringUtils.isEmpty(System.getenv(usernameReference))
                && !StringUtils.isEmpty(System.getenv(passwordReference))) {
            DbUserVO dbUserVO = new DbUserVO();
            dbUserVO.setUsername(System.getenv(usernameReference));
            dbUserVO.setPassword(System.getenv(passwordReference));
            databaseVO = new DatabaseVO();
            databaseVO.setCluster(dbUserVO);
            databaseVO.setReader(dbUserVO);
        }
    }

    private void initializeAES256() {
        String key = environment.getProperty("kms.encrypt-secret-name");
        EncryptKmsDTO encryptKmsDTO = kmsLoader.loadPropertiesFromKms(key, EncryptKmsDTO.class);
        AES256.getInstance().initialize(encryptKmsDTO);
    }

    private void initializeRedis() {
        String key = environment.getProperty("kms.redis-secret-name");
        RedisKmsDTO redisKmsDTO = kmsLoader.loadPropertiesFromKms(key, RedisKmsDTO.class);
        if (redisKmsDTO != null) {
            redisVO = redisKmsDTO.getRedis();
        }
    }
}
