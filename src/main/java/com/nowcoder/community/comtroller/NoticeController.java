package com.nowcoder.community.comtroller;

import com.alibaba.fastjson2.JSONObject;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.service.NoticeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class NoticeController implements CommunityConstant {
    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private MessageService messageService;

    //通知列表
    @RequestMapping(path = "/notice/list", method = RequestMethod.GET)
    public String getNoticeList(Model model) {
        User user = hostHolder.getUsers();

        //获取第一条通知
        Message followNotice = noticeService.findFirstNoticeByTopic(user.getId(), EVENT_FOLLOW);
        Message commentNotice = noticeService.findFirstNoticeByTopic(user.getId(), EVENT_COMMENT);
        Message likeNotice = noticeService.findFirstNoticeByTopic(user.getId(), EVENT_LIKE);

        Object followContent = null, commentContent = null, likeContent = null;
        if (followNotice != null) {
            followContent = JSONObject.parseObject(followNotice.getContent());
        }
        if (commentNotice != null) {
            commentContent = JSONObject.parseObject(commentNotice.getContent());
        }
        if (likeNotice != null) {
            likeContent = JSONObject.parseObject(likeNotice.getContent());
        }


        int followCount = noticeService.findUnreadNoticeCountByConversation(user.getId(), EVENT_FOLLOW);
        int commentCount = noticeService.findUnreadNoticeCountByConversation(user.getId(), EVENT_COMMENT);
        int likeCount = noticeService.findUnreadNoticeCountByConversation(user.getId(), EVENT_LIKE);

        Map<String, Object> followVo = new HashMap<>();
        Map<String, Object> commentVo = new HashMap<>();
        Map<String, Object> likeVo = new HashMap<>();
        followVo.put("followNotice", followNotice);
        followVo.put("followCount", followCount);
        followVo.put("followContent", followContent);

        commentVo.put("commentNotice", commentNotice);
        commentVo.put("commentCount", commentCount);
        commentVo.put("commentContent", commentContent);

        likeVo.put("likeNotice", likeNotice);
        likeVo.put("likeCount", likeCount);
        likeVo.put("likeContent", likeContent);

        model.addAttribute("followVo", followVo);
        model.addAttribute("commentVo", commentVo);
        model.addAttribute("likeVo", likeVo);
        model.addAttribute("noticeCount", likeCount + followCount + commentCount);

        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        //未读消息归0
        noticeService.updateNoticeStatus(user.getId(), 1);

        return "site/notice";
    }

    //获取会话中的未读消息id


    @RequestMapping(path = "/notice/detail/{conversationId}", method = RequestMethod.GET)
    public String getNotices(@PathVariable("conversationId") String conversationId, Model model, Page page) {
        User user = hostHolder.getUsers();
        //这里做分页
        page.setRows(noticeService.findNoticeCountByConversationId(user.getId(), conversationId));
        page.setLimit(5);
        List<Message> noticeList = noticeService.findNotice(user.getId(), conversationId, page.getOffset(), page.getLimit());
        //处理content
        List<Map<String,Object>> notices = new ArrayList<>();
        for(Message notice :noticeList){
            Map<String,Object> map = new HashMap<>();
            Object content = JSONObject.parseObject(notice.getContent());
            map.put("notice",notice);
            map.put("content",content);
            notices.add(map);
        }
        model.addAttribute("notices",notices);
        model.addAttribute("user",user);
        return "site/notice-detail";
    }

}
