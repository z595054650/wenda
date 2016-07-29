package com.zhifu.intercepter;

import com.zhifu.dao.LoginTicketDao;
import com.zhifu.dao.UserDao;
import com.zhifu.model.HostHolder;
import com.zhifu.model.LoginTicket;
import com.zhifu.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 有些页面要先登录才能访问
 * Created by thinkpad on 2016/7/22.
 */
@Component
public class LoginRequiredIntercepter implements HandlerInterceptor{

    @Autowired
    UserDao userDao;

    @Autowired
    HostHolder hostHolder;

    //一个请求的处理流程
    //preFilter -->  servlet中的service处理请求-->请求分发dispatcher--> 到达controller之前执行preHandle
    //--> 然后处理controller中的方法--> 在controller中方法return之前执行postHandle --> 在服务响应到达客户端之前执行afterCompletion
    //-->z最后执行afterFilter
    //可见拦截器被过滤器包裹
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        if(hostHolder.getUser()==null){
            //传递请求路径
            httpServletResponse.sendRedirect("/reglogin?next="+httpServletRequest.getRequestURL());
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {


    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
