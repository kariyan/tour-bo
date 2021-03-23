package com.wemakeprice.tour.bo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@EnableCaching
@PropertySource("classpath:redis.properties")
@SpringBootApplication
@EnableAspectJAutoProxy
public class TourBoApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(TourBoApiApplication.class, args);
    }
}
