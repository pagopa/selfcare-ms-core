package it.pagopa.selfcare.mscore.web.filter;

import org.apache.catalina.connector.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class LogFilterTest {
    @InjectMocks
    private LogFilter logFilter;


    /**
     * Method under test: {@link LogFilter#doFilter(ServletRequest, ServletResponse, FilterChain)}
     */
    @Test
    void testDoFilter4() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);
        doNothing().when(filterChain).doFilter(any(), any());
        logFilter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(any(), any());
    }

    /**
     * Method under test: {@link LogFilter#doFilter(ServletRequest, ServletResponse, FilterChain)}
     */
    @Test
    void testDoFilter5() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Response response = new Response();
        FilterChain filterChain = mock(FilterChain.class);
        doThrow(new UnsupportedEncodingException()).when(filterChain)
                .doFilter(any(), any());
        assertThrows(UnsupportedEncodingException.class, () -> logFilter.doFilter(request, response, filterChain));
        verify(filterChain).doFilter(any(), any());
    }
}

