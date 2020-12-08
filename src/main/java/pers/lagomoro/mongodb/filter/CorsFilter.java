package pers.lagomoro.mongodb.filter;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "corsFilter", urlPatterns = {"/*"})
public class CorsFilter implements Filter{

    private static Logger logger = Logger.getLogger(CorsFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("初始化 Filter");
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8083"); //  这里最好明确的写允许的域名
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token,Authorization,ybg");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        filterChain.doFilter(servletRequest, servletResponse);
        logger.info("Filter Attached");
    }
    @Override
    public void destroy() {
        logger.info("销毁 Filter");
    }
}