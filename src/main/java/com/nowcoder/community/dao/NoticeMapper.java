package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


//用于查询系统通知
@Mapper
public interface NoticeMapper {

    //根据userId查通知
    List<Message> selectNoticeByUserId(int userId);

    //查询未读通知数量
    int selectUnreadNoticeCountByConversation(int userId,String conversationId);

    //查询未读通知总数量
    int selectUnreadNoticeCount(int userId);

    //查询会话的每一条通知
    List<Message> selectNotices(int userId,String conversationId,int offset,int limit);

    //更新通知状态
    int updateNoticeStatus(int userId,int status);

    //添加通知
    int insertNotice(Message message);

    Message selectFirstNoticeByTopic(int userId,String topic);

    int selectNoticeCountByConversationId(int userId,String conversationId);    //用来分页

}
