package com.movile.labuenavida.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.movile.labuenavida.util.LogUtil;

/**
 * @author Guillermo Varela (guillermo.varela@movile.com)
 */
public class LogFilter implements Filter {

    @Override
    public void destroy() {
        // Not implemented
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        LogUtil.createStamp();
        try {
            filterChain.doFilter(request, response);
        } finally {
            LogUtil.clearStamp();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Not implemented
    }
}