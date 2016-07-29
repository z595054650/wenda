package com.zhifu.controller;

import com.zhifu.model.Comment;
import com.zhifu.model.EntityType;
import com.zhifu.model.HostHolder;
import com.zhifu.service.CommentService;
import com.zhifu.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created by thinkpad on 2016/7/28.
 */
@Controller
public class CommentController {
    public static final Logger logger= LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path={"/addComment"},method = {RequestMethod.POST})
    public  String addComment(@RequestParam("content")String content,
                              @RequestParam("questionId")int questionId){
        try {
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            if (hostHolder.getUser() == null) {
                return "redirect:/reglogin";
            } else {
                comment.setUserId(hostHolder.getUser().getId());
            }
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            commentService.addComment(comment);

            int count=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            questionService.updateCommentCount(questionId,count);
        }catch (Exception e){
            logger.error("增加评论失败"+e.getMessage());
        }
        System.out.println("questionid====="+questionId);
        return "redirect:/question/" + String.valueOf(questionId);
    }

}
