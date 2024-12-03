package ru.assist.migrate.slack2pachka.v1.model.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Content-Disposition	string	Используемый заголовок (в данном запросе - attachment)
 * acl	string	Уровень безопасности (в данном запросе - private)
 * policy	string	Уникальный policy для загрузки файла
 * x-amz-credential	string	x-amz-credential для загрузки файла
 * x-amz-algorithm	string	Используемый алгоритм (в данном запросе - AWS4-HMAC-SHA256)
 * x-amz-date	string	Уникальный x-amz-date для загрузки файла
 * x-amz-signature	string	Уникальная подпись для загрузки файла
 * key	string	Уникальный ключ для загрузки файла
 * direct_url	string	Адрес для загрузки файла
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileMeta {
    @JsonProperty("Content-Disposition")
    String contentDisposition;
    String acl;
    String policy;
    @JsonProperty("x-amz-credential")
    String xAmzCredential;
    @JsonProperty("x-amz-algorithm")
    String xAmzAlgorithm;
    @JsonProperty("x-amz-date")
    String xAmzDate;
    @JsonProperty("x-amz-signature")
    String xAmzSignature;
    String key;
    String direct_url;

    public MultiValueMap<String, Object> toMultiValueMap() {
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();

        multiValueMap.add("Content-Disposition", contentDisposition);
        multiValueMap.add("acl", acl);
        multiValueMap.add("policy", policy);
        multiValueMap.add("X-Amz-Credential", xAmzCredential);
        multiValueMap.add("X-Amz-Algorithm", xAmzAlgorithm);
        multiValueMap.add("X-Amz-Date", xAmzDate);
        multiValueMap.add("X-Amz-Signature", xAmzSignature);
        multiValueMap.add("key", key);

        return multiValueMap;
    }

}
