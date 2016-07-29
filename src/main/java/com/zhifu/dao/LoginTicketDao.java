package com.zhifu.dao;

import com.zhifu.model.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * Created by thinkpad on 2016/7/22.
 */
@Mapper
public interface LoginTicketDao {

    String TABLE_NAME="login_ticket";
    String INSERT_FILEDS="user_id,expired,status,ticket";
    String SELECT_FILEDS="id,"+INSERT_FILEDS;

    @Insert({"insert into ",TABLE_NAME," (",INSERT_FILEDS,
            ") values(#{userId},#{expired},#{status},#{ticket})"})
    int addTicket(LoginTicket ticket);

    @Select({"select ",SELECT_FILEDS," from ",TABLE_NAME," where ticket= #{ticket}"})
    LoginTicket selectByTicket(String ticket);


    @Update({"update ",TABLE_NAME," set status= #{status} where ticket=#{ticket}"})
    void updateStatu(@Param("ticket") String ticket,@Param("status") int status);



}
