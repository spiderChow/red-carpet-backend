package cn.edu.fudan.yst.config;

import cn.edu.fudan.yst.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author gzdaijie
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestLog());
    }
}

class RequestLog extends HandlerInterceptorAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLog.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        LOGGER.info("Remote Address:" + request.getHeader(Utils.REMOTE_ADDR_HEADER));
        LOGGER.info("Target:" + method.getDeclaringClass().getName() + "." + method.getName());
        LOGGER.warn("------------------------------");
        return true;
    }
}
