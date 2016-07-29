package com.zhifu.controller;

import com.zhifu.model.*;
import com.zhifu.service.CommentService;
import com.zhifu.service.QuestionService;
import com.zhifu.service.UserService;
import com.zhifu.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by thinkpad on 2016/7/26.
 */
@Controller
public class QuestionController {
    private static final Logger logger= LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    /**
     * 使用ajax提交问题
     *
     * */
    @RequestMapping(path={"/question/add"},method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title")String title,@RequestParam("content")String content){

        try{
            Question question=new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if(hostHolder.getUser()==null){
               // question.setUserId(WendaUtil.ANONYMOUS_USERID);
                //用户未登录的时候跳转到登录页面
                return WendaUtil.getJSONString(999);
            }else{
                question.setUserId(hostHolder.getUser().getId());
            }
            if(questionService.addQuestion(question)>0){
                return WendaUtil.getJSONString(0);
            }
        }catch(Exception e){
            logger.error("增加项目失败");
        }
        return WendaUtil.getJSONString(1,"失败");
    }

    @RequestMapping(path={"/question/{qid}"})
    public String questionDetail(Model model, @PathVariable("qid")int qid){
        Question question=questionService.getById(qid);
        model.addAttribute("question",question);

        List<Comment> commentList=commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments=new ArrayList<ViewObject>();
        for(Comment comment: commentList){
            ViewObject vo=new ViewObject();
            vo.set("comment",comment);
            vo.set("user",userService.getUserById(comment.getUserId()));
            comments.add(vo);
        }
       model.addAttribute("comments",comments);
        return "detail";
    }


}
