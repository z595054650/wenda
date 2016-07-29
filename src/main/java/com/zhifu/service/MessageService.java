package com.zhifu.service;

import com.zhifu.dao.MessageDao;
import com.zhifu.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by thinkpad on 2016/7/28.
 */
@Service
public class MessageService {

    @Autowired
    MessageDao messageDao;

    @Autowired
    SensitiveService sensitiveService;

    /**
     *发送一条信息
     */
    public int addMessage(Message message){
        //敏感词过滤
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDao.addMessage(message)>0 ? message.getId() : 0;
    }

    /**
     * 两个人的通话详细记录
     * */
    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return messageDao.selectConversationDetail(conversationId,offset,limit);
    }

    /**
     * 该用户与其他人的消息记录
     * */
    public  List<Message> getConversationList(int userid,int offset,int limit){
        return messageDao.selectConversationList(userid,offset,limit);
    }

    public int getUnreadCount(int userid,String conversationId){
        return messageDao.getUnreadCount(userid,conversationId);
    }

    public void updateUnreadStatus(int userid,String conversationid){
       messageDao.updateUnreadStatus(userid,conversationid);
    }
}
