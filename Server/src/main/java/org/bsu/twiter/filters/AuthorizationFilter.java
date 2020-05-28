package org.bsu.twiter.filters;

import org.bsu.twiter.services.SecurityConstants;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/tweets/like", "/tweets"})
public class AuthorizationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession httpSession = request.getSession(false);

        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (httpSession == null ||
                httpSession.getCreationTime() - System.currentTimeMillis() > SecurityConstants.MAX_COOKIE_AGE * 1000 ||
                httpSession.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
