package com.zhifu.controller;

import com.zhifu.model.HostHolder;
import com.zhifu.model.Question;
import com.zhifu.model.ViewObject;
import com.zhifu.service.QuestionService;
import com.zhifu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thinkpad on 2016/7/20.
 */
@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(path={"/home","/"},method = {RequestMethod.GET})
    public String home(Model model){


        List<ViewObject> vos=getQuestions(0,0,10);

        model.addAttribute("vos",vos);

        return "index";
    }

    @RequestMapping(path={"/user/{userid}"},method = {RequestMethod.GET})
    public String userIndex(Model model,
                            @PathVariable("userid") int userid){
        List<ViewObject> vos=getQuestions(userid,0,10);

        model.addAttribute("vos",vos);

        return "index";
    }

    public List<ViewObject> getQuestions(int userId,int offset,int limit){
        List<Question> questionList=questionService.getLatestQuestions( userId, offset, limit);
        List<ViewObject> vos=new ArrayList<ViewObject>();
        for(Question question:questionList){
            ViewObject vo=new ViewObject();
            vo.set("question",question);
            vo.set("user",userService.getUserById(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }
}
