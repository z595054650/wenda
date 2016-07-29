package com.zhifu.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by thinkpad on 2016/7/21.
 */
public class ViewObject {

    private Map<String,Object> obj=new HashMap<String,Object>();

    public void set(String key,Object value){
        obj.put(key,value);
    }

    public Object get(String key){
        return obj.get(key);
    }
}
