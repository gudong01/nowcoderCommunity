package com.nowcoder.community.util;

public class RedisKeyUtil {
    public static final String SPLIT = ":";
    public static final String PREFIX_ENTITY_LIKE = "like:entity";
    public static final String PREFIX_USER_LIKE = "like:user";
    public static final String PREFIX_FOLLOWER = "follower";
    public static final String PREFIX_FOLLOWEE = "followee";
    public static String getEntityLikeKey(int entityType,int entityId){
        return PREFIX_ENTITY_LIKE+SPLIT+entityType+SPLIT+entityId;
    }
    public static String getUserLikeKey(int userid){
        return PREFIX_USER_LIKE + SPLIT + userid;
    }
    public static String getFollowerKey(int entityType,int entityId){
        //某个实体的粉丝       存放userId
        return PREFIX_FOLLOWER +SPLIT + entityType + SPLIT + entityId;
    }

    public static String getFolloweeKey(int entityType,int userId){
        // 用户关注的实体      存放entityId
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }
}
