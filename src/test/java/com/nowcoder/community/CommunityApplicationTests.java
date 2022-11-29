package com.nowcoder.community;

import com.nowcoder.community.config.AlphaConfig;
import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class CommunityApplicationTests implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	@Test
	public void testApplicationContext(){
		//输出该容器的信息
		System.out.println(applicationContext);
		//从容器中获取AlphaDao类型的Bean
		AlphaDao a1 = applicationContext.getBean(AlphaDao.class);
		AlphaDao a2 = (AlphaDao) applicationContext.getBean("alphaHibernate");
		String r1 = a1.select();
		String r2 = a2.select();
		System.out.println(r1+"  "+r2);
	}
	@Test
	public void testBeanManager(){
		AlphaService alphaService = applicationContext.getBean(AlphaService.class);
		AlphaService alphaService2 = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
		System.out.println(alphaService2);
	}
	@Test
	public void testBeanConfig(){
		SimpleDateFormat simpleDateFormat = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}
	//该操作是 Spring把AlphaDao注入给 alphaDao属性
	@Autowired
	private AlphaDao a1;

	@Autowired
	@Qualifier("alphaHibernate")
	private AlphaDao a2;

	@Autowired
	private SimpleDateFormat simpleDateFormat;

	@Test
	public void testDI(){
		System.out.println(a1);
		System.out.println(a2);
		//com.nowcoder.community.dao.AlphaDaoMyBatisImp@6a937336
		//com.nowcoder.community.dao.AlphaDaoHibernateImp@278667fd
	}

	@Test
	public void testBeanMapper(){
		UserMapper userMapper = applicationContext.getBean(UserMapper.class);
		//User user = userMapper.selectById(120);
		//System.out.println(user);

	}

}
