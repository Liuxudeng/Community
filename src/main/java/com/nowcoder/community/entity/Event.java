package com.nowcoder.community.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息队列事件
 */
public class Event {
    /**
     * 主题
     */
    private String topic;
    /**
     * 事件触发的人
     */
    private int userId;
    /**
     * 实体类型
     */
    private int entityId;
    /**
     * 实体Id
     */
    private int entityType;
    /**
     * 实体作者
     */
    private int entityUserId;
    /**
     * 未知类型数据存入 增加扩展性
     */
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

    public int getEntityId() {
        return entityId;
    }

    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityUserId() {
        return entityUserId;
    }

    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public Event setData(String key,Object value) {
        this.data.put(key,value);
        return this;
    }
}