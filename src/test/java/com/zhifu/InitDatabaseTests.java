package com.zhifu;

import com.zhifu.dao.UserDao;
import com.zhifu.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.UserDataHandler;

import java.util.Random;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests {

	@Autowired
	UserDao userDao;

	@Test
	public void initDatabase() {
		Random random=new Random();

		for(int i=0;i<11;i++){
			User user=new User();
			user.setHeadUrl(String.format("http://images.newcoder.com/head/%dt.png",random.nextInt(1000)));
			user.setName(String.format("user%d",i));
			user.setPassword("");
			user.setSalt("");
			userDao.addUser(user);

		}

	}

}
