package it.pagopa.selfcare.mscore.web.filter;

import it.pagopa.selfcare.mscore.utils.MaskDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Slf4j
@Component
public class LogFilter implements Filter {

    private static final int MAX_LENGTH_CONTENT = 150;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        final String httpUri = httpServletRequest.getRequestURI();

        if (httpUri.startsWith("/actuator/health")) {
            log.trace("request to health-check actuator");
            chain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        final String httpMethod = httpServletRequest.getMethod();
        long startTime = System.currentTimeMillis();

        ContentCachingRequestWrapper requestCacheWrapperObject = new ContentCachingRequestWrapper(httpServletRequest);
        ContentCachingResponseWrapper  responseCacheWrapperObject = new ContentCachingResponseWrapper(httpServletResponse);

        chain.doFilter(requestCacheWrapperObject, responseCacheWrapperObject);

        Long endTime = System.currentTimeMillis() - startTime;
        String requestBody = getContentAsString(requestCacheWrapperObject.getContentAsByteArray(), request.getCharacterEncoding());
        String responseBody = getContentAsString(responseCacheWrapperObject.getContentAsByteArray(), response.getCharacterEncoding());
        log.info("Request from URI : {} - method: {} - timelapse: {}ms - Request body {} - Response body {}", httpUri, httpMethod, endTime, MaskDataUtils.maskInformation(requestBody), MaskDataUtils.maskInformation(responseBody));
        responseCacheWrapperObject.copyBodyToResponse();
    }

    private String getContentAsString(byte[] buf, String charsetName) {
        if (buf == null || buf.length == 0) {
            return "";
        }
        try {
            String content = new String(buf, charsetName);
            return content.substring(0, Math.min(MAX_LENGTH_CONTENT, content.length()));
        } catch (UnsupportedEncodingException ex) {
            return "Unsupported Encoding";
        }
    }

}
