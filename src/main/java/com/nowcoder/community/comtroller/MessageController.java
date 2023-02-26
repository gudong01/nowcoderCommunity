package com.nowcoder.community.comtroller;

import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.service.NoticeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private SensitiveFilter filter;
    //私信列表
    @RequestMapping(path = "/letter/list",method = RequestMethod.GET)
    //会话列表
    public String getLetterList(Model model, Page page){
        User user = hostHolder.getUsers();
        //分页信息
        page.setPath("/letter/list");
        page.setLimit(5);
        page.setRows(messageService.findConversationCount(user.getId()));

        //会话列表
        List<Message> conversationList = messageService.findConversations(user.getId(),page.getOffset(),page.getLimit());
        List<Map<String,Object>> conversations = new ArrayList<>();
        if(conversationList != null){
            for(Message conversation : conversationList){
                Map<String,Object> map = new HashMap<>();
                map.put("conversation",conversation);
                String conversationId = conversation.getConversationId();
                //会话的消息数量
                int letterCount = messageService.findLetterCount(conversationId);
                map.put("letterCount",letterCount);
                //未读消息的数量
                int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), conversationId);
                map.put("letterUnreadCount",letterUnreadCount);
                //对方User
                int targerId = user.getId() == conversation.getFromId() ? conversation.getToId(): conversation.getFromId();
                map.put("targetUser",userService.findUserById(targerId));
                conversations.add(map);
            }
            model.addAttribute("noticeUnreadCount",noticeService.findUnreadNoticeCount(user.getId()));
            model.addAttribute("conversations",conversations);
            model.addAttribute("letterUnreadSum",messageService.findLetterUnreadCount(user.getId(),null));
        }
        return "/site/letter";
    }

    //会话详情
    @RequestMapping(path = "/letter/detail/{conversationId}",method = RequestMethod.GET)
    public String getLetters(@PathVariable("conversationId") String conversationId, Model model,Page page){
        page.setPath("/letter/detail/" + conversationId);
        page.setLimit(5);
        page.setRows(messageService.findLetterCount(conversationId));
        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String,Object>> letters = new ArrayList<>();
        if(!letterList.isEmpty()){
            for(Message letter: letterList){
                Map<String,Object> map = new HashMap<>();
                map.put("letter",letter);
                map.put("fromUser",userService.findUserById(letter.getFromId()));
                letters.add(map);
            }
            Message letter = letterList.get(0);
            User user = hostHolder.getUsers();
            int targetId = user.getId() == letter.getFromId() ? letter.getToId() : letter.getFromId();
            model.addAttribute("target",userService.findUserById(targetId));
//            model.addAttribute("user",user);
            model.addAttribute("letters",letters);
        }
        //未读消息归0
        List<Integer> ids = getLetterUnreadIds(letterList);
        if(!ids.isEmpty()){
            messageService.updateStatus(ids,1);
        }
        return "site/letter-detail";
    }
    //获取会话中的未读消息id
    private List<Integer> getLetterUnreadIds(List<Message> letterList){
        List<Integer> ids = new ArrayList<>();
        if (!letterList.isEmpty()){
            for (Message letter : letterList){
                if(letter.getStatus() == 0 && hostHolder.getUsers().getId() == letter.getToId()){
                    ids.add(letter.getId());
                }
            }
        }
        return ids;
    }

    @RequestMapping(path = "/letter/send",method = RequestMethod.POST)
    @ResponseBody
    public String sendMessage(String toName,String content){
        User user = hostHolder.getUsers();
        if(content.isEmpty() || toName .isEmpty())  return CommunityUtil.getJSONString(1,"内容或收件人不能为空");
        User toUser = userService.findUserByName(toName);
        if(toUser == null)  return CommunityUtil.getJSONString(2,"找不到收件人");
        Message message = new Message();
        content = filter.filter(content);
        message.setContent(content);
        message.setCreateTime(new Date());
        message.setFromId(user.getId());
        message.setToId(toUser.getId());
        if(user.getId() < toUser.getId()){
            message.setConversationId(user.getId() + "_" + toUser.getId());
        }else{
            message.setConversationId(toUser.getId() + "_" + user.getId());
        }
        message.setStatus(0);
        messageService.addMessage(message);
        return CommunityUtil.getJSONString(0);
    }

}
