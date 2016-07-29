package com.zhifu.controller;

import com.zhifu.model.HostHolder;
import com.zhifu.service.UserService;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by thinkpad on 2016/7/22.
 */
@Controller
public class LoginController {

    private static final Logger logger= LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;


    @RequestMapping(path={"/reglogin"},method = {RequestMethod.GET})
    public String reglogin(Model model,@RequestParam(value="next",required = false)String next){
        //将next参数传到reglogin页面，用<input type="hidden",name="next",value="$!{next}"/>保存
        model.addAttribute("next",next);
        return "login";
    }

    @RequestMapping(path={"/reg/"},method = {RequestMethod.POST})
    public String reg(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam("next")String next,
                      HttpServletResponse response){
        try{
            Map<String,String> msg=userService.register(username,password);

            if(msg.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",msg.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                if(StringUtils.isNotBlank(next)){
                    return "redirect:"+next;
                }
                return "redirect:/";
            }else{
                model.addAttribute("msg",msg.get("msg"));
                return "login";
            }
        }catch (Exception e){
            logger.error("注册异常"+e.getMessage());
            return "login";
        }
    }



    @RequestMapping(path={"/login/"},method = {RequestMethod.POST})
    public String login(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                        @RequestParam(value="next",required = false)String next,
                        @RequestParam(value="rememberme",defaultValue = "false")boolean rememberme,
                        HttpServletResponse response){
        try{
            Map<String,String> msg=userService.login(username,password);
            if(msg.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",msg.get("ticket"));
                cookie.setPath("/");
                response.addCookie(cookie);
                if(StringUtils.isNotBlank(next)){
                    return "redirect:"+next;
                }
                return "redirect:/";
            }else{
                model.addAttribute("msg",msg.get("msg"));
                return "login";
            }
        }catch (Exception e){
            logger.error("登录异常"+e.getMessage());
            return "login";
        }
    }

    /**
     * 退出的时候就是使token状态设为1
     * */
    @RequestMapping(path={"/logout"},method={RequestMethod.GET})
    public String logout(Model model,
                         @CookieValue("ticket")String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }
}
