package com.nowcoder.community.comtroller;

import com.nowcoder.community.Event.EventProducer;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {
    @Autowired
    private CommentService commentService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private EventProducer eventProducer;

    @RequestMapping(path = "/add/{discussPostId}",method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId")int discussPostId, Comment comment){
        comment.setUserId(hostHolder.getUsers().getId());
        comment.setStatus(0);   //有效的
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        Event event = new Event()
                .setTopic(EVENT_COMMENT)
                .setUserId(comment.getUserId())             //触发的人
                .setEntityUserId(comment.getTargetId())     //收到的人
                .setEntityType(ENTITY_TYPE_COMMENT)         //触发的实体类型
                .setEntityId(comment.getId());              //触发的实体ID

        Map<String,Object> data = new HashMap<>();          //存放postId
        data.put("postId",discussPostId);
        event.setData(data);

        eventProducer.fireEvent(event);

        return "redirect:/discuss/detail/{discussPostId}";
    }

}
