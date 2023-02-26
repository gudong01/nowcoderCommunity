package com.nowcoder.community;

import com.alibaba.fastjson2.JSONObject;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.*;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.service.NoticeService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nowcoder.community.util.CommunityConstant.SYSTEM_ID;

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

    @Autowired
    private MessageService messageService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private CommentService commentService;
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

    @Test
    public void TestInsert(){
        Message message = new Message();
        message.setFromId(SYSTEM_ID);
        message.setToId(111);
        message.setConversationId("follow");
        message.setCreateTime(new Date());
        message.setStatus(0);

        Map<String, Object> content = new HashMap<>();
        content.put("userId", 111);
        content.put("entityType", 3);
        content.put("entityId", 111);
        message.setContent(JSONObject.toJSONString(content));
        System.out.println(message);
        messageService.addMessage(message);
        System.out.println(message);
    }

    @Test
    public void NoticeTest(){
        Message commentNotice = noticeService.findFirstNoticeByTopic(111,"comment");
        System.out.println(commentNotice);
    }

    @Test
    public void CommentTest(){
        Comment comment = commentService.findCommentByEntityId(28);
        System.out.println(comment.getId());
    }
}
