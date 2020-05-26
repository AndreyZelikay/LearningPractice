package org.bsu.twiter.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Expose-Headers","*");
        response.setHeader("Access-Control-Allow-Methods","*");
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
