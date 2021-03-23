package com.wemakeprice.tour.bo.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.context.request.RequestContextListener;

import javax.servlet.ServletRequestEvent;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Configuration
@WebListener
@Slf4j
public class RequestContextListenerLogger extends RequestContextListener {
    @Override
    public void requestInitialized(@NonNull ServletRequestEvent arg0) {
        try {
            MDC.put("uid", this.getUUID(arg0));
        } catch (Exception e) {
            log.error("Error RequestContextListenerLogger.requestInitialized", e);
        }
    }

    @Override
    public void requestDestroyed(@NonNull ServletRequestEvent arg0) {
        try {
            MDC.clear();
        } catch (Exception e) {
            log.error("Error RequestContextListenerLogger.requestDestroyed", e);
        }
    }

    private String getUUID(ServletRequestEvent arg0) {
        try {
            String uuid = ((HttpServletRequest) arg0.getServletRequest()).getHeader("WMP-TRACKING-ID");

            if (uuid != null && uuid.length() > 0) {
                return uuid;
            }
        } catch (Exception ignored) {
            //
        }
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
