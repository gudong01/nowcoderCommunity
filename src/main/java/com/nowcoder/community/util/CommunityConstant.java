package com.nowcoder.community.util;

public interface CommunityConstant {
    //激活成功
    int ACTIVATION_SUCCESS = 0;
    //重复激活
    int ACTIVATION_REPEAT =1;
    //激活失败
    int ACTIVATION_FAILURE = 2;


    //默认状态下的登陆凭证超时时间
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12; //12小时
    //记住状态下的登陆凭证超时时间
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100; //100天

    //实体类型 帖子
    int ENTITY_TYPE_POST = 1;
    //实体类型 评论
    int ENTITY_TYPE_COMMENT = 2;
    //实体类型 用户
    int ENTITY_TYPE_USER = 3;

    //事件类型 评论
    String EVENT_COMMENT = "comment";
    //事件类型 点赞
    String EVENT_LIKE = "like";
    //事件类型 关注
    String EVENT_FOLLOW = "follow";

    //系统ID
    int SYSTEM_ID = 1;

}
