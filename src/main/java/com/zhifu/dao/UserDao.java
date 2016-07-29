package com.zhifu.dao;

import com.zhifu.model.User;
import org.apache.ibatis.annotations.*;

/**
 * Created by thinkpad on 2016/7/19.
 * 使用接口作为映射工具
 */
@Mapper
public interface UserDao {
    String TABLE_NAMR="user";
    String INSERT_FILEDS="name, password, salt, head_url";
    String SELECT_FILEDS="id, "+INSERT_FILEDS;

    @Insert({"insert into ",TABLE_NAMR, "(", INSERT_FILEDS,
            ") values (#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select ",SELECT_FILEDS," from ",TABLE_NAMR," where id=#{id}"})
    User selectById(int id);

    @Select({"select ", SELECT_FILEDS," from ",TABLE_NAMR, " where name=#{name}"})
    User selectByName(String name);

    @Update({"update ",TABLE_NAMR," set password=#{password} where id=#{id}"})
    void updatePassword(User user);

    @Delete({"delete from ",TABLE_NAMR," where id= #{id}"})
    void deleteById(int id);
}