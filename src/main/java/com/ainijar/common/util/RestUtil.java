package com.ainijar.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class RestUtil {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * RestAPI - 使用@RequestParam接收参数的GET接口
     * RestAPI - 使用@PathVariable提取url变量的GET接口
     */
    public <T> T restGet(String url, Class<T> type) {
        T object = restTemplate.getForObject(url, type);
        return object;
    }
    public <T> ResponseEntity<T> restGetEntity(String url, Class<T> type) {
        ResponseEntity<T> object = restTemplate.getForEntity(url, type);
        return object;
    }

    /**
     * RestAPI - 使用@RequestBody接收请求参数的POST接口
     */
    public <T> T restPostByBody(Object params, String url, Class<T> type) {
        T object = restTemplate.postForObject(url, params, type);
        return object;
    }
    public  <T> ResponseEntity<T> restPostByBodyEntity(Object params, String url, Class<T> type) {
        ResponseEntity<T> object = restTemplate.postForEntity(url, params, type);
        return object;
    }

    /**
     * RestAPI - 使用@RequestParam接收请求参数的POST接口
     */
    public <T> T restPostByParam(MultiValueMap params, String url, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(params, headers);
        T object = restTemplate.postForObject(url, httpEntity, type);
        return object;
    }
    public  <T> ResponseEntity<T> restPostByParamEntity(MultiValueMap params, String url, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<T> object = restTemplate.postForEntity(url, httpEntity, type);
        return object;
    }
}
