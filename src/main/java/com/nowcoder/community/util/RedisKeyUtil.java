package com.nowcoder.community.util;

public class RedisKeyUtil {
    public static final String SPLIT = ":";
    public static final String PREFIX_ENTITY_LIKE = "like:entity";

    public static final String PREFIX_USER_LIKE = "like:user";
    public static String getEntityLikeKey(int entityType,int entityId){
        return PREFIX_ENTITY_LIKE+SPLIT+entityType+SPLIT+entityId;
    }
    public static String getUserLikeKey(int userid){
        return PREFIX_USER_LIKE + SPLIT + userid;
    }
}
