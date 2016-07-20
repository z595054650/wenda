package com.zhifu.controller;

import com.zhifu.model.User;
import com.zhifu.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dylan on 2016/7/13.
 */
@Controller
public class IndexController {
    @Autowired
    WendaService wendaService;

    @RequestMapping(path={"/","/index"})
    @ResponseBody
    public String index(HttpSession session){
        //return "helloworld "+session.getAttribute("msg");
        return "helloworld"+wendaService.getMessage("zxf");
    }

    @RequestMapping(path="/profile/{groupid}/{userid}")
    @ResponseBody
    public String profile(@PathVariable("userid") int userid,
                          @PathVariable("groupid") String groupid ,
                          @RequestParam(value = "type",defaultValue = "11") int type,
                          @RequestParam(value = "key",defaultValue = "zangxuefeng",required = false) String key){
        return String.format("userid is %s, %d, type=%d, key=%s",groupid,userid,type,key);
    }

    @RequestMapping(path={"/vm"},method = {RequestMethod.GET})
    public String template(Model model){
        model.addAttribute("value","臧雪峰");
        List<String> colors=Arrays.asList(new String[]{"green","red","blue"});
        model.addAttribute("colors",colors);

        HashMap<String,String> map=new HashMap<String,String>();
        map.put("name","xiaoming");
        map.put("sex","20");
        model.addAttribute("map",map);

        model.addAttribute("user",new User("zxf"));
        System.out.println("1111222111333333331");
        return "home";
    }

    @RequestMapping(path={"/request"},method={RequestMethod.GET})
    @ResponseBody//加上还回的是文本，不加还回的是template
    public  String request(Model model , HttpServletRequest request,
                           HttpServletResponse response){
        StringBuilder sb=new StringBuilder();

        Enumeration<String> headernames=request.getHeaderNames();
        while(headernames.hasMoreElements()){
            String name=headernames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br>");
        }

        if(request.getCookies()!=null){
            for(Cookie cookie: request.getCookies()){
                sb.append("COOKIE:"+cookie.getName()+":"+cookie.getValue()+"<br>");
            }
        }

        sb.append(request.getMethod()+"<br>");
        sb.append(request.getQueryString()+"<br>");
        sb.append(request.getPathInfo()+"<br>");
        sb.append(request.getRequestURI()+"<br>");
        response.addHeader("username","zangxuefeng");
        response.addCookie(new Cookie("passcookie","newcookie"));
        return sb.toString();
    }

    @RequestMapping(path={"/redirect/{code}"},method = {RequestMethod.GET})
    public String redirect(@PathVariable("code") int code, HttpSession session){
    //默认的302临时跳转
        session.setAttribute("msg","just from redirect");
        return "redirect:/";
    }

    @RequestMapping(path={"/redirect301/{code}"},method={RequestMethod.GET})
    public RedirectView redirect301(@PathVariable("code") int code){

        RedirectView red=new RedirectView("/",true);
        if(code==301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
    }

    @RequestMapping(path={"/admin"})
    @ResponseBody
    public String admin(@RequestParam("key") String key){
        if("admin".equals(key)){
            return "hello admin";
        }

        throw new IllegalArgumentException("参数不对");
    }

    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        //此处可以定义异常页面
        return "error:"+e.getMessage();
    }
}
