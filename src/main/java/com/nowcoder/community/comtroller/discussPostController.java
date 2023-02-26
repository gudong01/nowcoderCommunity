package com.nowcoder.community.comtroller;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/discuss")
public class discussPostController implements CommunityConstant {
    @Autowired
    private DiscussPostService discussPostService;
    @Autowired
    private UserService userService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String add(String title,String content){
        if(content.isEmpty()||title.isEmpty()){
            return CommunityUtil.getJSONString(1,"标题或内容不能为空");
        }
        User user = hostHolder.getUsers();
        if(user == null){
            return CommunityUtil.getJSONString(403,"你还没有登录");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setCreateTime(new Date());
        post.setContent(content);
        discussPostService.addDiscussPost(post);
        return CommunityUtil.getJSONString(0,"发布成功");
    }


    //查询帖子内容
    @RequestMapping(path = "/detail/{discussPostId}",method = RequestMethod.GET)
    public String getDisCussPostDetail(@PathVariable("discussPostId") int discussPostId, Model model, Page page){
        User hostUser = hostHolder.getUsers();
        //获取帖子 和 作者
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post",post);
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user",user);
        long postLikeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId());
        model.addAttribute("likeCount",postLikeCount);
        int postLikeStatus = likeService.findEntityLikeStatus(hostUser.getId(),ENTITY_TYPE_POST,post.getId());
        model.addAttribute("likeStatus",postLikeStatus);

        //查评论的分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/"+discussPostId);
        page.setRows(post.getCommentCount());


        //给帖子的评论称为评论
        //给评论的评论称为回复
        List<Comment> commentList =
                commentService.findCommentByEntity(ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        List<Map<String,Object>> commentVoList = new ArrayList<>();
        if(commentList != null){
            for(Comment comment : commentList){
                Map<String,Object> commentVo = new HashMap<>();
                long commentLikeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,comment.getId());
                int commentLikeStatus  = likeService.findEntityLikeStatus(hostUser.getId(),ENTITY_TYPE_COMMENT,comment.getId());
                commentVo.put("likeStatus",commentLikeStatus);
                commentVo.put("likeCount",commentLikeCount);
                commentVo.put("comment",comment);
                commentVo.put("user",userService.findUserById(comment.getUserId()));
                //回复列表
                List<Comment> replyList =
                        commentService.findCommentByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                List<Map<String,Object>> replyVoList = new ArrayList<>();
                if(replyList != null){
                    for(Comment reply :replyList){
                        Map<String,Object> replyVo = new HashMap<>();
                        long replyLikeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT,reply.getId());
                        int replyLikeStatus  = likeService.findEntityLikeStatus(hostUser.getId(),ENTITY_TYPE_COMMENT,reply.getId());
                        replyVo.put("likeStatus",replyLikeStatus);
                        replyVo.put("likeCount",replyLikeCount);
                        replyVo.put("reply",reply);
                        replyVo.put("user",userService.findUserById(reply.getUserId()));
                        //回复的目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVo.put("target",target);
                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys",replyVoList);
                //回复的数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("replyCount",replyCount);
                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments",commentVoList);
        return "/site/discuss-detail";
    }




}
