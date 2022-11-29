package com.nowcoder.community.config;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.comtroller.interceptor.Alphainterceptor;
import com.nowcoder.community.comtroller.interceptor.LoginRequiredInterceptor;
import com.nowcoder.community.comtroller.interceptor.LoginTicketInterCeptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//拦截器配置

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private Alphainterceptor alphainterceptor;
    @Autowired
    private LoginTicketInterCeptor loginTicketInterCeptor;

    @Autowired

    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册接口,并且排除静态资源 拦截指定路径
        registry.addInterceptor(alphainterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg")
                .addPathPatterns("/register","/login");
        registry.addInterceptor(loginTicketInterCeptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");
        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");

    }
}
