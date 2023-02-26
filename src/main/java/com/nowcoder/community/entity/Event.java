package com.nowcoder.community.entity;

import java.util.HashMap;
import java.util.Map;

public class Event {
    private String topic;
    private int userId;
    private int entityType;
    private int EntityId;
    private int EntityUserId;
    private Map<String,Object> data = new HashMap<>();


    public String getTopic() {
        return topic;
    }

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return EntityId;
    }

    public Event setEntityId(int entityId) {
        EntityId = entityId;
        return this;
    }

    public int getEntityUserId() {
        return EntityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        EntityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Event{" +
                "topic='" + topic + '\'' +
                ", userId=" + userId +
                ", entityType=" + entityType +
                ", EntityId=" + EntityId +
                ", EntityUserId=" + EntityUserId +
                ", data=" + data +
                '}';
    }
}
