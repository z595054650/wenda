package com.zhifu.service;

import com.zhifu.dao.QuestionDao;
import com.zhifu.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by thinkpad on 2016/7/20.
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDao questionDao;

    @Autowired
    SensitiveService sensitiveService;

    //得到一个问题对象
    public Question getById(int id){
        return questionDao.selectById(id);
    }

    public int addQuestion(Question question){

        //1.对话html进行过滤
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        //敏感词过滤,例如一些低俗的文字
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));
        return questionDao.addQuestion(question)>0 ? question.getId():0;
    }

    public List<Question> getLatestQuestions(int userId,int offset,int limit){
        return questionDao.selectLatestQuestions(userId,offset,limit);
    }

    public int updateCommentCount(int id,int count){
        return questionDao.updateCommentCount(id,count);
    }

}
