package com.zhifu.service;

import com.zhifu.dao.CommentDao;
import com.zhifu.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by thinkpad on 2016/7/28.
 */
@Service
public class CommentService {

    @Autowired
    CommentDao commentDao;

    @Autowired
    SensitiveService sensitiveService;


    public List<Comment> getCommentsByEntity(int entityId,int entityType){

        return commentDao.selectCommentsByEntity(entityId,entityType);

    }

    public int addComment(Comment comment){
        //对评论进行敏感词过滤
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDao.addComment(comment)>0 ? comment.getId() : 0;
    }

    public int getCommentCount(int entityId,int entityType){
        return commentDao.getCommentCount(entityId,entityType);
    }

    public boolean deleteComment(int id){
        return commentDao.deleteComment(id,1)>0;
    }

}
