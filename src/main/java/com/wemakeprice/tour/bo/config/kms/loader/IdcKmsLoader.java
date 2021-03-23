package com.wemakeprice.tour.bo.config.kms.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemakeprice.tour.bo.common.exception.KmsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Slf4j
public class IdcKmsLoader implements KmsLoader {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    private final String endPoint;
    private final String token;

    public IdcKmsLoader(Environment environment, WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
        this.endPoint = environment.getProperty("kms.idc.end-point");
        this.token = environment.getProperty("kms.idc.token");
    }

    @Override
    public <T> T loadPropertiesFromKms(String key, Class<T> type) {
        if (key == null) {
            throw new IllegalArgumentException("KMS key cannot be null!");
        }

        log.info("[IDC] KMS data retrieves : [{}]", key);
        String results = webClient
                .mutate()
                .baseUrl(endPoint)
                .build()
                .get()
                .uri("?key={key}", key)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(String.class)
                .flux()
                .toStream()
                .findFirst()
                .orElseThrow(() -> new KmsException("KMS body is empty!"));
        try {
            T value = objectMapper.treeToValue(objectMapper.readTree(results).get("data").get(0).get("value"), type);
            if (ObjectUtils.isEmpty(value)) {
                throw new KmsException(key + " data has null value.");
            }
            return value;

        } catch (IOException e) {
            throw new KmsException(e);
        }
    }
}
