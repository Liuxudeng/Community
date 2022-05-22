
package com.nowcoder.community;

import com.nowcoder.CommunityApplication;

import com.nowcoder.community.dao.*;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import org.junit.Test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;
import java.util.List;

@SpringBootTest

@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {
private ApplicationContext applicationContext;


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			this.applicationContext = applicationContext;
	}



@Autowired
	UserMapper userMapper;


	@Test
	public void  selectId(){
		User user = new User();
		System.out.println(userMapper.selectById(12).getEmail());
	}
	@Autowired
	DiscussPostMapper discussPostMapper;
	@Test
	public void selectDiscussPost(){
		List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(101, 0, 10,0);
		for (int i = 0; i < 10; i++) {

			System.out.println("第"+i+"行---"+discussPosts.get(i).toString());
		}
	}

//	@Test
//	public void selectDate(){
//
//		System.out.println(discussPostMapper.selectDate(277));
//	}

	@Test
	public void selectid(int id){

	}
}

