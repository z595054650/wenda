package com.zhifu.intercepter;

import com.zhifu.dao.LoginTicketDao;
import com.zhifu.dao.UserDao;
import com.zhifu.model.HostHolder;
import com.zhifu.model.LoginTicket;
import com.zhifu.model.User;
import org.apache.catalina.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 验证用户的身份
 * Created by thinkpad on 2016/7/22.
 */
@Component
public class PassportIntercepter implements HandlerInterceptor{

    @Autowired
    LoginTicketDao loginTicketDao;

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
        //有一种情况是，当return false时后续的动作将不再行
        /*if(2>1){
            return false;
        }*/

       String ticket=null;
        if(httpServletRequest.getCookies()!=null){
            for(Cookie cookie : httpServletRequest.getCookies()){
                if(cookie.getName().equals("ticket")){
                    ticket=cookie.getValue();
                    break;
                }
            }
        }

        if(ticket!=null){
            LoginTicket loginTicket=loginTicketDao.selectByTicket(ticket);
            //判断token是否有效：1.token不存在，2.token超出有效期，3.用户退出token状态失效
            if(loginTicket==null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus()!=0){
                return true;
            }
            //获得用户信息后存储在上下文中，供其他请求使用
            User user=userDao.selectById(loginTicket.getUserId());
            hostHolder.setUser(user);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //通过执行过程可以看出modelAndView相当于controller方法中的Model，向模板中传递参数
        if(modelAndView!=null){
            modelAndView.addObject("user",hostHolder.getUser());
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        hostHolder.clear();
    }
}
