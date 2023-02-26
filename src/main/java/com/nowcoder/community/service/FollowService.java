package com.nowcoder.community.service;

import com.nowcoder.community.Event.EventProducer;
import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FollowService implements CommunityConstant {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    EventProducer eventProducer;

    public void follow(int entityType,int entityId,int userId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityType,userId);
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();     //开启事务
                redisTemplate.opsForZSet().add(followerKey,userId,System.currentTimeMillis());
                redisTemplate.opsForZSet().add(followeeKey,entityId,System.currentTimeMillis());
                return operations.exec();
            }
        });
    }

    public void unfollow(int entityType,int entityId,int userId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityType,userId);
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();     //开启事务
                redisTemplate.opsForZSet().remove(followerKey,userId);
                redisTemplate.opsForZSet().remove(followeeKey,entityId);
                return operations.exec();
            }
        });
    }

    //查询当前用户是否已关注该实体
    public boolean hasFollowed(int userId,int entityType,int entityId){
        //Keys must not be empty followee:102:3
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityType,userId);    //是否关注了实体
        return redisTemplate.opsForZSet().score(followeeKey,entityId) != null;
    }

    //查询实体被关注数量
    public long findFollowerCount(int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);   //获取集合大小
    }

    //查询用户关注的实体的数量
    public long findFolloweeCount(int userId,int entityType){
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityType,userId);
        return redisTemplate.opsForZSet().zCard(followeeKey);
    }

    //查询用户的关注
    public List<Map<String,Object>> findFolloweeById(int entityType,int userId){
        String followeeKey = RedisKeyUtil.getFolloweeKey(entityType,userId);
        Set<Integer> set = redisTemplate.opsForZSet().range(followeeKey,0,-1);  //存的是关注的userId
        List<Map<String,Object>> followees = new ArrayList<>();
        if(set != null){
            for(Integer followeeId : set){
                Map<String,Object> map = new HashMap<>();
                User followee = userService.findUserById(followeeId);
                double score = redisTemplate.opsForZSet().score(followeeKey, followeeId);
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(score);
                map.put("followee",followee);
                map.put("followeeDate",date);
                followees.add(map);
            }
        }
        return followees;
    }
    //查询用户的粉丝
    public List<Map<String,Object>>  findFollowerById(int userId){
        String followerKey = RedisKeyUtil.getFollowerKey(3,userId);
        Set<Integer> set = redisTemplate.opsForZSet().range(followerKey,0,-1);  //存的是粉丝的userid
        List<Map<String,Object>> followers = new ArrayList<>();
        if(set != null){
            for(Integer followerId : set){
                Map<String,Object> map = new HashMap<>();
                User follower = userService.findUserById(followerId);
                double score = redisTemplate.opsForZSet().score(followerKey, followerId);
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(score);
                map.put("follower",follower);
                map.put("followerDate",date);
                followers.add(map);
            }
        }
        return followers;
    }

}
