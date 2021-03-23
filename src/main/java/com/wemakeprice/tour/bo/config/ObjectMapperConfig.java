package com.wemakeprice.tour.bo.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.nhncorp.lucy.security.xss.XssFilter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class ObjectMapperConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer objectMapperBuilder() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.deserializerByType(String.class,
                new JsonDeserializer<String>() {
                    @Override
                    public String deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
                        String origin = p.getValueAsString();
                        XssFilter filter = XssFilter.getInstance("lucy-xss-superset.xml", true);
                        return filter.doFilter(origin);
                    }
                });
    }
}
