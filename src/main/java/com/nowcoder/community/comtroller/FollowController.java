package com.nowcoder.community.comtroller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FollowController {
    @Autowired
    UserService userService;
    @Autowired
    FollowService followService;
    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/follow",method = RequestMethod.POST)
    @ResponseBody
    @LoginRequired
    public String follow(int entityType,int entityId){
        User user = hostHolder.getUsers();
        followService.follow(entityType,entityId,user.getId());
        return CommunityUtil.getJSONString(0,"关注成功！");
    }

    @RequestMapping(path = "/unfollow",method = RequestMethod.POST)
    @ResponseBody
    @LoginRequired
    public String unfollow(int entityType,int entityId){
        User user = hostHolder.getUsers();
        followService.unfollow(entityType,entityId,user.getId());
        return CommunityUtil.getJSONString(0,"取消关注成功！");
    }

    @RequestMapping(path = "/followee/{userId}",method = RequestMethod.GET)
    @LoginRequired
    public String getFollowee(@PathVariable("userId")int userId, Model model, Page page){
        page.setLimit(5);
        page.setPath("/followee/"+userId);
        page.setRows((int)followService.findFolloweeCount(userId,CommunityConstant.ENTITY_TYPE_USER));
        User loginUser = hostHolder.getUsers();
        List<Map<String,Object>> followees = followService.findFolloweeById(CommunityConstant.ENTITY_TYPE_USER,userId);

        List<Map<String,Object>> followeeVo = new ArrayList<>();
        if(followees!= null){
            for(Map<String,Object> followeeMap : followees){
                Map<String,Object> map = new HashMap<>();
                User followee = (User)followeeMap.get("followee");
                String followeeTime = (String)followeeMap.get("followeeDate");
                boolean hasFollowed = followService.hasFollowed(loginUser.getId(),3,followee.getId());
                map.put("followee",followee);
                map.put("hasFollowed",hasFollowed);
                map.put("followeeTime",followeeTime);
                followeeVo.add(map);
            }
        }
        model.addAttribute("followees",followeeVo);
        model.addAttribute("loginUser",loginUser);
        model.addAttribute("user",userService.findUserById(userId));
        return "site/followee";
    }
    @RequestMapping(path = "/follower/{userId}",method = RequestMethod.GET)
    @LoginRequired
    public String getFollower(@PathVariable("userId")int userId, Model model, Page page){
        page.setLimit(5);
        page.setPath("/follower/"+userId);
        page.setRows((int)followService.findFollowerCount(3,userId));
        User loginUser = hostHolder.getUsers();
        List<Map<String,Object>> followers = followService.findFollowerById(userId);

        List<Map<String,Object>> followerVo = new ArrayList<>();
        if(followers!= null){
            for(Map<String,Object> followerMap : followers){
                Map<String,Object> map = new HashMap<>();
                User follower = (User)followerMap.get("follower");
                String followerTime = (String)followerMap.get("followerDate");
                boolean hasFollowed = followService.hasFollowed(loginUser.getId(),3,follower.getId());
                map.put("follower",follower);
                map.put("hasFollowed",hasFollowed);
                map.put("followerTime",followerTime);
                followerVo.add(map);
            }
        }
        model.addAttribute("followers",followerVo);
        model.addAttribute("loginUser",loginUser);
        model.addAttribute("user",userService.findUserById(userId));
        return "site/follower";
    }
}
