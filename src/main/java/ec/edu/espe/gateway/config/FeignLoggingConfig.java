package ec.edu.espe.gateway.config;

import feign.Logger;
import feign.Request;
import feign.Response;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

@Configuration
public class FeignLoggingConfig {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FeignLoggingConfig.class);

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    Logger feignLogger() {
        return new Logger() {
            @Override
            protected void log(String configKey, String format, Object... args) {
                log.info("[FEIGN] " + String.format(methodTag(configKey) + format, args));
            }

            @Override
            protected void logRequest(String configKey, Level logLevel, Request request) {
                log.info("==============================================");
                log.info("[FEIGN REQUEST] Detalles de la petición:");
                log.info("[FEIGN REQUEST] URI: {}", request.url());
                log.info("[FEIGN REQUEST] Method: {}", request.httpMethod());
                log.info("[FEIGN REQUEST] Headers: {}", request.headers());
                if (request.body() != null) {
                    log.info("[FEIGN REQUEST] Body: {}", new String(request.body()));
                }
                log.info("==============================================");
            }

            @Override
            protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
                log.info("==============================================");
                log.info("[FEIGN RESPONSE] Detalles de la respuesta:");
                log.info("[FEIGN RESPONSE] Status: {}", response.status());
                log.info("[FEIGN RESPONSE] Headers: {}", response.headers());
                if (response.body() != null) {
                    byte[] bodyData = IOUtils.toByteArray(response.body().asInputStream());
                    log.info("[FEIGN RESPONSE] Body: {}", new String(bodyData));
                    log.info("[FEIGN RESPONSE] Tiempo de respuesta: {}ms", elapsedTime);
                    log.info("==============================================");
                    return response.toBuilder().body(bodyData).build();
                }
                return response;
            }
        };
    }
} 