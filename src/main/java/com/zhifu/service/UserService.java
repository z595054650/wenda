package com.zhifu.service;

import com.zhifu.dao.LoginTicketDao;
import com.zhifu.dao.UserDao;
import com.zhifu.model.LoginTicket;
import com.zhifu.model.User;
import com.zhifu.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.internal.util.CollectionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;

/**
 * Created by thinkpad on 2016/7/20.
 */
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    LoginTicketDao loginTicketDao;

    public User getUserById(int id){
        return userDao.selectById(id);
    }

    public User getUserByName(String name){
        return userDao.selectByName(name);
    }

    public Map<String,String> register(String username,String password){

        Map<String,String> msg= new HashMap<String,String>();
        //首先判断用户名和密码是否为空
        if(StringUtils.isBlank(username)){
            msg.put("msg","用户名不能为空");
            return msg;
        }

        if(StringUtils.isBlank(password)){
            msg.put("msg","密码不能为空");
            return msg;
        }
        //再判断用户名是否已经存在
        User user=userDao.selectByName(username);
        if(user!=null){
            msg.put("msg","用户名已存在");
            return msg;
        }

        //以上判断通过就可以创建
        User newUser=new User();
        newUser.setName(username);
        newUser.setSalt(UUID.randomUUID().toString().substring(0,5));
        //对密码进行加盐加密，数据库存储的密码不能是明文
        newUser.setPassword(WendaUtil.MD5(password+newUser.getSalt()));
        newUser.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",new Random().nextInt(1000)));
        userDao.addUser(newUser);
        String ticket=addLoginTicket(newUser.getId());
        msg.put("ticket",ticket);
        return msg;
    }

    public Map<String,String> login(String username,String password){

        Map<String,String> msg= new HashMap<String,String>();
        //首先判断用户名和密码是否为空
        if(StringUtils.isBlank(username)){
            msg.put("msg","用户名不能为空");
            return msg;
        }

        if(StringUtils.isBlank(password)){
            msg.put("msg","密码不能为空");
            return msg;
        }
        //再判断用户名是否已经存在
        User user=userDao.selectByName(username);
        if(user==null){
            msg.put("msg","用户名不存在");
            return msg;
        }

        //判断吧密码
        if(!WendaUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            msg.put("msg","密码错误");
            return msg;
        }
        String ticket=addLoginTicket(user.getId());
        msg.put("ticket",ticket);
        return msg;
    }

    public String addLoginTicket(int userId){
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(userId);
        Date now=new Date();
        now.setTime(3600*24*100+now.getTime());
        ticket.setExpired(now);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replace("-",""));
        loginTicketDao.addTicket(ticket);
        return ticket.getTicket();
    }

    public void logout(String ticket){
        loginTicketDao.updateStatu(ticket,1);
    }
}
