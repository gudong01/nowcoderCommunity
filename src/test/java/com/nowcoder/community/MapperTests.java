package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {
    @Autowired
    private UserMapper mapper;
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Test
    public void testSelect(){
        User user0  = mapper.selectById(101);
        System.out.println(user0);
        User user1 = mapper.selectByEmail("nowcoder1@sina.com");
        System.out.println(user1);
        User user2 = mapper.selectByName("liubei");
        System.out.println(user2);
    }
    @Test
    public void testUpdate(){
        int res = mapper.updatePassword(120,"wang9264");
        System.out.println(res);
    }

//    @Test
//    public void testSelectPost(){
//        int count = discussPostMapper.selectDiscussPostRows(149);
//        List<DiscussPost> res = discussPostMapper.selectDiscussPost(149,0,10);
//        System.out.println(count);
//        for(DiscussPost post :res){
//            System.out.println(post);
//        }
//    }
    @Test
    public void testInsertLoginTicket(){
        //
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(1);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));

        loginTicketMapper.insertLoginTicket(loginTicket);

    }
    @Test
    public  void testSelectByTicket(){
        String ticket1 = "abc";
        String ticket2 = "abcd";
        System.out.println(loginTicketMapper.selectByTicket(ticket1));
        System.out.println(loginTicketMapper.selectByTicket(ticket2));
    }
    @Test
    public void testupdateStatus(){
        loginTicketMapper.updateStatus("abc",0);
    }

    @Test
    public void testMessage01(){
        List<Message> messages = messageMapper.selectConversations(111, 0, Integer.MAX_VALUE);
        for(Message m : messages){
            System.out.println(m);
        }
        System.out.println("***********");

        List<Message> letters = messageMapper.selectLetters("111_112", 0, Integer.MAX_VALUE);
        for(Message l : letters){
            System.out.println(l);
        }
        System.out.println("**********");

        int lettersCount = messageMapper.selectLetterCount("111_112");
        System.out.println(lettersCount);

        System.out.println("**********");

        int count = messageMapper.selectLetterUnreadCount(131,"111_131");
        System.out.println(count);
    }
}
