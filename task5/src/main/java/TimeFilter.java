import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TimeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long startTime = System.currentTimeMillis();
        filterChain.doFilter(servletRequest, servletResponse);
        long finishTime = System.currentTimeMillis();
        long time = finishTime - startTime;

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = String.valueOf(request.getRequestURL());
        String method = request.getMethod();

        response.getWriter().write("<h3>Time: " + time + "ms URL: " + url + " Method: " + method + "</h3>");
    }

    @Override
    public void destroy() {

    }
}
