package com.nowcoder.community.Event;

import com.alibaba.fastjson2.JSONObject;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.service.NoticeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class EventConsumer implements CommunityConstant {
    public static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private NoticeService noticeService;
    @Autowired
    private UserService userService;

    @KafkaListener(topics = {EVENT_COMMENT, EVENT_LIKE, EVENT_FOLLOW})
    public void handleMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息为空");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误");
            return;
        }
        Message message = new Message();
        message.setFromId(SYSTEM_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());
        message.setStatus(0);

        //这里的数据大概率用来拼链接
        Map<String, Object> content = new HashMap<>();
        content.put("userId",event.getUserId());
        content.put("fireUserName", userService.findUserById(event.getUserId()).getUserName());
        content.put("entityId", event.getEntityId());

        if (!event.getData().isEmpty()) {
//            for(Map.Entry<String,Object> data : event.getData().entrySet()){
//                content.put(data.getKey(),data.getValue());
//            }
            content.putAll(event.getData());
        }
        message.setContent(JSONObject.toJSONString(content));
        noticeService.addNotice(message);
    }
}
