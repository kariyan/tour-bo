package com.wemakeprice.tour.bo.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.StopWatch;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Configuration
public class WebClientConfig {
    @Bean
    public WebClient webClient() {
        return WebClient
                .builder()
                .filter(elapsedTimeFilter())
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(timeoutConnector())
                .build();
    }

    private ReactorClientHttpConnector timeoutConnector() {
        return new ReactorClientHttpConnector(HttpClient
                .create()
                .tcpConfiguration(client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10_000)
                        .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(60))
                                .addHandlerLast(new WriteTimeoutHandler(60))))
        );
    }

    private ExchangeFilterFunction elapsedTimeFilter() {
        return (request, next) -> {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            return next.exchange(request).map(response -> {
                stopWatch.stop();
                log.debug("method={}, uri={}, response_time={}, response_code={}", request.method(),
                        request.url(), stopWatch.getLastTaskTimeMillis(), response.rawStatusCode());
                return response;
            });
        };
    }
}
