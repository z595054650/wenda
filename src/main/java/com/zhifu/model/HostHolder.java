package com.zhifu.model;

import org.springframework.stereotype.Component;

/**
 * Created by thinkpad on 2016/7/23.
 */
@Component
public class HostHolder {
    //让每个线程都有一个User的副本
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
