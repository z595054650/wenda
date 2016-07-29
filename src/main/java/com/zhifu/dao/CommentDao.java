package com.zhifu.dao;

import com.zhifu.model.Comment;
import com.zhifu.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by thinkpad on 2016/7/28.
 */
@Mapper
public interface CommentDao {

    String TABLE_NAMR="comment";
    String INSERT_FILEDS="user_id, content, created_date, entity_id,entity_type,status";
    String SELECT_FILEDS="id, "+INSERT_FILEDS;

    @Insert({"insert into ",TABLE_NAMR, "(", INSERT_FILEDS,
            ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})"})
    int addComment(Comment comment);

    //查询某个实体的所有评论
    @Select({"select ",SELECT_FILEDS," from ",TABLE_NAMR,
            " where entity_id=#{entityId} and entity_type=#{entityType} order by created_date desc"})
    List<Comment> selectCommentsByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({"select count(id) from ",TABLE_NAMR, " where entity_id=#{entityId} and entity_type=#{entityType}"})
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);

    //一般删除评论不是真的删除数据库中的数据，只是改变了显示状态
    @Update({"update ",TABLE_NAMR," set status=#{status} where id= #{id}"})
    int deleteComment(@Param("id")int id,@Param("status")int status);
}
