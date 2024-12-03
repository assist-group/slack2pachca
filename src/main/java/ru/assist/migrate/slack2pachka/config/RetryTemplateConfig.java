package ru.assist.migrate.slack2pachka.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.RetryTemplate;
import ru.assist.migrate.slack2pachka.exceptions.TooManyRequestsRuntimeException;

@EnableRetry
@Configuration
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RetryTemplateConfig {
    @Value("${retry.delay}")
    long initialInterval;
    @Value("${retry.maxDelay}")
    long maxInterval;
    @Value("${retry.multiplier}")
    double multiplier;
    @Value("${retry.random}")
    boolean withRandom;
    @Value("${retry.maxAttempts}")
    int maxAttempts;

    @Bean
    public RetryTemplate retryTemplate() {
        return RetryTemplate.builder()
                .maxAttempts(maxAttempts)
                .retryOn(TooManyRequestsRuntimeException.class)
                .exponentialBackoff(initialInterval, multiplier, maxInterval, withRandom)
//                .withListener(new DefaultListener())
                .build();
    }

    @Slf4j
    public static class DefaultListener implements RetryListener {

        @Override
        public <T, E extends Throwable> void close(RetryContext context,
                                                   RetryCallback<T, E> callback, Throwable throwable) {
//            log.info("onClose");
        }

        @Override
        public <T, E extends Throwable> void onError(RetryContext context,
                                                     RetryCallback<T, E> callback, Throwable throwable) {
//            log.info("onError");
        }

        @Override
        public <T, E extends Throwable> boolean open(RetryContext context,
                                                     RetryCallback<T, E> callback) {
//            log.info("onOpen");
            return true;
        }
    }
}
