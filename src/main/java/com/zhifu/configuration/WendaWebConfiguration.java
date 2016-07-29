package com.zhifu.configuration;

import com.zhifu.intercepter.LoginRequiredIntercepter;
import com.zhifu.intercepter.PassportIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 注册自定义的拦截器
 * Created by thinkpad on 2016/7/23.
 */
@Component
public class WendaWebConfiguration extends WebMvcConfigurerAdapter{

    @Autowired
    PassportIntercepter passportIntercepter;

    @Autowired
    LoginRequiredIntercepter loginRequiredIntercepter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截器有先后顺序
        registry.addInterceptor(passportIntercepter);
        //对那些页面设置该拦截器
        registry.addInterceptor(loginRequiredIntercepter).addPathPatterns("/user/*");
        super.addInterceptors(registry);
    }
}
