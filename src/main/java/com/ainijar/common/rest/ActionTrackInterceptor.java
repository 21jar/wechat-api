package com.ainijar.common.rest;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ActionTrackInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
//        HttpHeaders headers = request.getHeaders();
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
//                .getRequestAttributes();
//        HttpServletRequest req = attributes.getRequest();
//        Enumeration<String> headerNames = req.getHeaderNames();
//        Map<String, String> map = new HashMap();
//        if (headerNames != null) {
//            while (headerNames.hasMoreElements()) {
//                String name = headerNames.nextElement();
//                // 将所有自定义http header传递给上游服务
//                if (name.toLowerCase().startsWith("x")) {
//                    String values = req.getHeader(name);
//                    map.put(name, values);
//                }
//            }
//        }
//        // 加入自定义字段
//        headers.setAll(map);
        // 保证请求继续被执行
        return execution.execute(request, body);
    }

}