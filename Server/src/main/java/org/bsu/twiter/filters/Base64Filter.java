package org.bsu.twiter.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.stream.Collectors;

@WebFilter("/*")
public class Base64Filter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (request.getMethod().equals("POST")) {
            String base64 = request.getReader().lines().collect(Collectors.joining());

            RewritableServletRequestWrapper requestWrapper = new RewritableServletRequestWrapper((HttpServletRequest) servletRequest);

            requestWrapper.resetInputStream(Base64.getDecoder().decode(base64));

            filterChain.doFilter(requestWrapper, servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
