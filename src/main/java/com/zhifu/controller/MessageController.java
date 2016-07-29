package com.zhifu.controller;

import com.zhifu.model.HostHolder;
import com.zhifu.model.Message;
import com.zhifu.model.User;
import com.zhifu.model.ViewObject;
import com.zhifu.service.MessageService;
import com.zhifu.service.UserService;
import com.zhifu.util.WendaUtil;
import org.apache.velocity.app.event.implement.EscapeXmlReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by thinkpad on 2016/7/28.
 */
@Controller
public class MessageController {

    private static  final Logger logger=LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @RequestMapping(path={"/msg/addMessage"},method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName")String toName,
                             @RequestParam("content")String content){

        try{
            if(hostHolder.getUser()==null){
                return WendaUtil.getJSONString(999,"未登录");
            }

            User toUser=userService.getUserByName(toName);
            if(toUser==null){
                return WendaUtil.getJSONString(1,"用户不存在");
            }

            Message message=new Message();

            message.setFromId(hostHolder.getUser().getId());
            message.setToId(toUser.getId());
            message.setContent(content);
            //此内容已经在model中初始化，即get方法中
            //msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) : String.format("%d_%d", toId, fromId));
            message.setCreatedDate(new Date());
            messageService.addMessage(message);
            return WendaUtil.getJSONString(0);

        }catch (Exception e){
            logger.error("发送消息失败 ！ "+e.getMessage());
            return WendaUtil.getJSONString(1, "插入站内信失败");
        }
    }

    @RequestMapping(path={"/msg/detail"},method = {RequestMethod.GET})
    public String getConversationDetail(Model model,
                                        @RequestParam("conversationId")String conversationId){
        try{
            List<Message> conversationList=messageService.getConversationDetail(conversationId,0,10);
            //更新未读状态
            messageService.updateUnreadStatus(hostHolder.getUser().getId(),conversationId);
            List<ViewObject> messages=new ArrayList<ViewObject>();
            for(Message message : conversationList){
                ViewObject vo=new ViewObject();
                vo.set("message",message);
                User user=userService.getUserById(message.getFromId());
                vo.set("user",user);
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        }catch(Exception e){
            logger.error("获取详细信息失败"+e.getMessage());
        }
        return "letterDetail";
    }


    @RequestMapping(path={"/msg/list"},method = {RequestMethod.GET})
    public String getConversationList(Model model){
        try{
            if(hostHolder.getUser()==null) {
                return "redirect:/reglogin";
            }
            List<Message> messageList=messageService.getConversationList(hostHolder.getUser().getId(),0,10);
            List<ViewObject> conversations=new ArrayList<ViewObject>();
            for(Message conversation : messageList){
                ViewObject vo=new ViewObject();
                int targetid=hostHolder.getUser().getId()==conversation.getFromId() ? conversation.getToId() : conversation.getFromId();
                User user=userService.getUserById(targetid);
                vo.set("user",user);
                vo.set("conversation",conversation);
                int unread=messageService.getUnreadCount(hostHolder.getUser().getId(),conversation.getConversationId());
                vo.set("unread",unread);
                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);

        }catch(Exception e){
            logger.error("获取消息列表失败");
        }
        return "letter";
    }
}
