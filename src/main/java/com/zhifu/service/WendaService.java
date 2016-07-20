package com.zhifu.service;

import org.springframework.stereotype.Service;

/**
 * Created by thinkpad on 2016/7/18.
 */
@Service
public class WendaService {

    public String getMessage(String userid){
        return "the message is :"+userid;
    }
}
