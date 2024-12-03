package ru.assist.migrate.slack2pachka.v1.service.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import ru.assist.migrate.slack2pachka.v1.model.messages.FileMeta;
import ru.assist.migrate.slack2pachka.v1.service.RecoveryUtils;
import ru.assist.migrate.slack2pachka.exceptions.ServiceRuntimeException;
import ru.assist.migrate.slack2pachka.exceptions.TooManyRequestsRuntimeException;

import java.io.File;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileServiceImpl implements FileService {
    final RestClient restClient;

    @Autowired
    public FileServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public FileMeta upload(File file) {
        FileMeta fileMeta = getMeta();
        return uploadFile(file, fileMeta);
    }

    @Retryable(retryFor = {TooManyRequestsRuntimeException.class, ServiceRuntimeException.class}, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.delay}", maxDelayExpression = "${retry.maxDelay}", multiplierExpression = "${retry.multiplier}"))
    private FileMeta getMeta() {
        ResponseEntity<FileMeta> responseEntity = restClient.post()
                .uri("/uploads")
                .accept(APPLICATION_JSON)
                .retrieve()
                .toEntity(FileMeta.class);

        return responseEntity.getBody();
    }

    @Recover
    private FileMeta getMetaRecover(TooManyRequestsRuntimeException e) {
        RecoveryUtils.waitIfError(5);
        return getMeta();
    }

    @Recover
    private FileMeta getMetaRecover(ServiceRuntimeException e) {
        RecoveryUtils.printResponseCode(e);
        RecoveryUtils.waitIfError(10);
        return getMeta();
    }


    @Retryable(retryFor = {TooManyRequestsRuntimeException.class, ServiceRuntimeException.class}, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.delay}", maxDelayExpression = "${retry.maxDelay}", multiplierExpression = "${retry.multiplier}"))
    private FileMeta uploadFile(File file, FileMeta fileMeta) {

        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.addAll(fileMeta.toMultiValueMap());
        body.add("file", new FileSystemResource(file));

        RestClient fileUploadRestClient = RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA.toString())
                .build();

        fileUploadRestClient.post()
                .uri(fileMeta.getDirect_url())
                .body(body)
                .retrieve()
                .toBodilessEntity();

        return fileMeta;
    }

    @Recover
    private FileMeta uploadFileRecover(TooManyRequestsRuntimeException e, File file, FileMeta fileMeta) {
        RecoveryUtils.waitIfError(5);
        return uploadFile(file, fileMeta);
    }

    @Recover
    private FileMeta uploadFileRecover(ServiceRuntimeException e, File file, FileMeta fileMeta) {
        RecoveryUtils.printResponseCode(e);
        RecoveryUtils.waitIfError(10);
        return uploadFile(file, fileMeta);
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
}
