package com.wemakeprice.tour.bo.config.kms.loader;

public interface KmsLoader {
    <T> T loadPropertiesFromKms(String key, Class<T> type);
}
