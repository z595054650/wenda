package com.zhifu;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.zhifu.dao.QuestionDao;
import com.zhifu.dao.UserDao;
import com.zhifu.model.Question;
import com.zhifu.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.UserDataHandler;

import java.util.Date;
import java.util.Random;
import java.util.UUID;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests {

	@Autowired
	UserDao userDao;
	@Autowired
	QuestionDao questionDao;

	@Test
	public void initDatabase() {
		Random random=new Random();

		for(int i=0;i<11;i++){
			User user=new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(1000)));
			user.setName(String.format("user%d",i));
			user.setPassword("");
			user.setSalt("");
			userDao.addUser(user);

			//user.setSalt(UUID.randomUUID().toString().substring(0,5));
			user.setPassword("xx");
			userDao.updatePassword(user);

			Question question=new Question();

			question.setCommentCount(i);
			Date date=new Date();
			date.setTime(date.getTime()+1000*3600*i);
			question.setCreatedDate(date);
			question.setUserId(i+1);
			question.setTitle(String.format("title %d",i));
			question.setContent(String.format("bababbabababbabab %d",i));

			questionDao.addQuestion(question);
		}
		Assert.assertEquals("xx",userDao.selectById(1).getPassword());

		System.out.println(questionDao.selectLatestQuestions(1,0,10));

	}

}
