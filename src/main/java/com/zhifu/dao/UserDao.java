package com.zhifu.dao;

import com.zhifu.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by thinkpad on 2016/7/19.
 * 使用接口作为映射
 */
@Mapper
public interface UserDao {
    String TABLE_NAMR="user";
    String INSERT_FILEDS="name, password, salt, head_url";
    String SELECT_FILEDS="id, "+INSERT_FILEDS;

    @Insert({"insert into ",TABLE_NAMR, "(", INSERT_FILEDS,
            ") values (#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);
}
