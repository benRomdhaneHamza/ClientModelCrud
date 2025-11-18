package com.hamzabnr.ClientModelCrud.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RequestResponseLogger extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(RequestResponseLogger.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    ContentCachingRequestWrapper wrappedRequest =
        new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper wrappedResponse =
        new ContentCachingResponseWrapper(response);

    long start = System.currentTimeMillis();
    filterChain.doFilter(wrappedRequest, wrappedResponse);
    long duration = System.currentTimeMillis() - start;

    String requestBody = new String(wrappedRequest.getContentAsByteArray());
    String responseBody = new String(wrappedResponse.getContentAsByteArray());

    // logger.info("Request body: {}" + requestBody.toString());
    // logger.info("Response body: {}" + responseBody);

    wrappedResponse.copyBodyToResponse();

  }
}
