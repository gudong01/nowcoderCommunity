package com.nowcoder.community.service;

import com.nowcoder.community.dao.NoticeMapper;
import com.nowcoder.community.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    public List<Message> findNoticeByUserId(int userId) {
        return noticeMapper.selectNoticeByUserId(userId);
    }

    public int findUnreadNoticeCountByConversation(int userId, String conversationId) {
        return noticeMapper.selectUnreadNoticeCountByConversation(userId, conversationId);
    }

    public int findUnreadNoticeCount(int userId) {
        return noticeMapper.selectUnreadNoticeCount(userId);
    }

    public List<Message> findNotice(int userId, String conversationId, int offset, int limit) {
        return noticeMapper.selectNotices(userId, conversationId, offset, limit);
    }

    public int updateNoticeStatus(int userId, int status) {
        return noticeMapper.updateNoticeStatus(userId, status);
    }

    public int addNotice(Message message) {
        return noticeMapper.insertNotice(message);
    }

    public Message findFirstNoticeByTopic(int userId, String topic) {
        return noticeMapper.selectFirstNoticeByTopic(userId, topic);
    }

    public int findNoticeCountByConversationId(int userId, String conversationId) {
        return noticeMapper.selectNoticeCountByConversationId(userId,conversationId);
    }

}
