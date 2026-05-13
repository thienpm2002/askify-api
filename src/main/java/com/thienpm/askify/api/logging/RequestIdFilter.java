package com.thienpm.askify.api.logging;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestIdFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestIdFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String requestId = UUID.randomUUID().toString();

        response.setHeader("X-Request-Id", requestId);

        long start = System.currentTimeMillis();

        try {
            MDC.put("requestId", requestId);

            log.info("Request started: method={}, path={}", request.getMethod(), request.getRequestURI());

            filterChain.doFilter(request, response);

            long time = System.currentTimeMillis() - start;

            log.info("Request finished: method={}, path={}, status={}, time={}ms",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    time);

        } finally {
            MDC.clear();
        }
    }
}