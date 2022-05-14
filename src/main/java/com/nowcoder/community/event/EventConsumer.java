package com.nowcoder.community.event;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.service.MessageService;
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

/**
 * 将该bean注入到like follow comment中
 */
@Component
public class EventConsumer implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);


    @Autowired
    private MessageService messageService;


    @KafkaListener(topics ={TOPIC_COMMENT,TOPIC_LIKE,TOPIC_FOLLOW})
      public void handleCommentMessage(ConsumerRecord record){
        if(record==null||record.value()==null){
            logger.error("消息的内容为空");
            return;
        }


        /**
         * 对record中的数据做一个转换
         */
        Event event = JSONObject.parseObject(record.value().toString(),Event.class);
        /**
         * 转换后再次判断
         */
        if(event==null){
            logger.error("消息格式错误！");
            return;
        }
        /**
         * 在为系统消息的时候 conversion_id存储的数据应该为topic
         *
         * content中存储的应该是指定的格式的字符串  比如 **评论了你的帖子 **点赞了你的帖子 **关注了你
         *
         * 也就是说message表存了两类数据 一类是人与人之间的私信 一类是系统通知
         */

        // 发送站内通知
        Message message = new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());

        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }

        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);




    }



}