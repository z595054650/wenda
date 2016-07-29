package com.zhifu.dao;

import com.zhifu.model.Comment;
import com.zhifu.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by thinkpad on 2016/7/28.
 */
@Mapper
public interface MessageDao {
    String TABLE_NAMR="message";
    String INSERT_FILEDS="from_id, to_id, content, created_date,has_read,conversation_id";
    String SELECT_FILEDS="id, "+INSERT_FILEDS;

    @Insert({"insert into ",TABLE_NAMR, "(", INSERT_FILEDS,
            ") values (#{fromId},#{toId},#{content},#{createdDate},#{hasRead},#{conversationId})"})
    int addMessage(Message message);

    //查询一个用户与其他人的通信
    @Select({"select ",SELECT_FILEDS," from ",TABLE_NAMR,
            " where conversation_id=#{conversationId}  order by created_date desc limit #{offset},#{limit}"})
    List<Message> selectConversationDetail(@Param("conversationId") String conversationId,
                                           @Param("offset")int offset,
                                           @Param("limit")int limit);

    @Select({"select count(id) as id,",INSERT_FILEDS,
            " from (select * from ",TABLE_NAMR," where from_id=#{userId} or to_id=#{userId} order by id desc)tt " +
            " group by conversation_id order by created_date desc limit #{offset},#{limit}"})
    List<Message> selectConversationList(@Param("userId")int userId,
                                         @Param("offset")int offset,
                                         @Param("limit")int limit);


    /**
     * 得到未读信息数
     * */
    @Select({"select count(*) from ",TABLE_NAMR," where to_id=#{userId} and has_read=0 and conversation_id=#{conversationId}"})
    int getUnreadCount(@Param("userId")int userId,@Param("conversationId")String conversationId);

    /**
     * 将未读信息标记为已读
     * */
    @Select({"update ",TABLE_NAMR," set has_read=1 where has_read=0 and to_id=#{userId} and conversation_Id=#{conversationId}"})
    void updateUnreadStatus(@Param("userId")int userId,@Param("conversationId")String conversationId);
}
