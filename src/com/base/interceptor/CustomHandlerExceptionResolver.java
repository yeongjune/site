package com.base.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
 
 
public class CustomHandlerExceptionResolver extends DefaultHandlerExceptionResolver {
 
    private static final Log LOG = LogFactory.getLog(CustomHandlerExceptionResolver.class);
 
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        LOG.error(request.getRequestURI(), ex);
        
        return super.doResolveException(request, response, handler, ex);
    }
 
}