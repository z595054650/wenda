package com.zhifu.dao;

import com.zhifu.model.Question;
import com.zhifu.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by thinkpad on 2016/7/19.
 * 使用接口作为映射工具
 */
@Mapper
public interface QuestionDao {
    String TABLE_NAMR="question";
    String INSERT_FILEDS="title, content, created_date, user_id,comment_count";
    String SELECT_FILEDS="id, "+INSERT_FILEDS;

    @Insert({"insert into ",TABLE_NAMR, "(", INSERT_FILEDS,
            ") values (#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    //通过配置文件访问数据库selectLatestQuestions
   List<Question> selectLatestQuestions(@Param("userId") int userId,
                                        @Param("offset") int offset,
                                        @Param("limit") int limit);

    @Select({"select ",SELECT_FILEDS," from ",TABLE_NAMR," where id=#{id}"})
    Question selectById(int id);

    @Update({"update ",TABLE_NAMR," set comment_count=#{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id")int id,@Param("commentCount")int commentCount);
}