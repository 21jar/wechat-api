package com.ainijar.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class AccessLogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put("startTime", new Date());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String queryString = request.getQueryString();

        String userId = (String) MDC.get("userId");
        HttpSession session = request.getSession(false);

        Date requestStartTime = (Date) MDC.get("startTime");
        Date end = new Date();
        long time = end.getTime() - requestStartTime.getTime();

        log.info(
                (session == null ? "" : session.getId()) + "|" +
                        (userId == null ? "" : userId) + "|" +
                        request.getMethod() + "|" +
                        request.getRequestURL().toString() + (queryString == null ? "" : "?" + queryString) + "|" +
                        request.getHeader("User-Agent") + "|" +
                        request.getHeader("Referer") + "|" +
                        request.getHeader("Accept-Language") + "|" +
                        (request.getHeader("x-forwarded-for") == null ? request.getRemoteAddr() : request.getHeader("x-forwarded-for")) + "|" +
                        (requestStartTime == null ? "" : new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(requestStartTime)) + "|" +
                        (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(end)) + "|" +
                        time + "|" +
                        response.getStatus() + "|" +
                        request.getHeader("unique-id")
        );
    }
}
