package com.nowcoder.community.event;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.ElasticsearchService;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 *
 * 消费者类
 * 将该bean注入到like follow comment中
 */
@Component
public class EventConsumer implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);


    @Autowired
    private MessageService messageService;

    @Autowired
    private ElasticsearchService elasticsearchService;
    @Autowired
    private DiscussPostService discussPostService;

    @Value("${wk.image.command}")
private String wkImageCommand;


    @Value("${wk.image.storage}")
    private String wkImageStorage;





    @Value("${qiniu.key.access}")
    private String accessKey;

    @Value("${qiniu.key.secret}")
    private String secretKey;

    @Value("${qiniu.bucket.share.name}")
    private String shareBucketName;


    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    /**
     * 对三个主题进行监听 TOPIC_COMMENT,TOPIC_LIKE,TOPIC_FOLLOW
     * 收到这三个event后就构建message对象
     *
     */
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
        //消息发送方的ID为1 表示是有系统发送的消息
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        //conversationId设为topic
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

        //把content保存为json字符串
        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);




    }


    /**
     * 消费者将新发的帖子更新到Es服务器中
     */
    @KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlePublishMessage(ConsumerRecord record){


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
         * 拿到帖子数据
         */
        DiscussPost post = discussPostService.findDiscussPostById(event.getEntityId());

        /**
         *更新
         */
        elasticsearchService.saveDiscussPost(post);



    }



    /**
     * 消费删帖事件
     */
    @KafkaListener(topics = {TOPIC_DELETE})
    public void handleDeleteMessage(ConsumerRecord record){


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
         *删帖
         */
        elasticsearchService.deleteDiscussPost(event.getEntityId());



    }

    //消费分享事件
    @KafkaListener(topics = TOPIC_SHARE)
    public void handleShareMessage(ConsumerRecord record){


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


      String htmlUrl  = (String) event.getData().get("htmlUrl");
      String fileName  = (String) event.getData().get("fileName");
      String suffix  = (String) event.getData().get("suffix");

      String cmd = wkImageCommand+" --quality 75 "+htmlUrl+" "+wkImageStorage+"/"+fileName+suffix;


        try {
            Runtime.getRuntime().exec(cmd);
            logger.info("生成长图成功： "+cmd);
        } catch (IOException e) {
           logger.error("生成长图失败"+e.getMessage());
        }


        /**
         * 使用定时器等命令完成
         */

        UploadTask task = new UploadTask(fileName,suffix);
     Future future = taskScheduler.scheduleAtFixedRate(task,500);

        task.setFuture(future);



    }

    class UploadTask implements Runnable{

        /**
         * 文件名称
         * 文件后缀
         * 启动任务的返回值
         */

        private String fileName;
        private String suffix;

        private Future future;


        /**
         * 开始时间
         *
         */
        private Long startTime;

        /**
         * 上传次数
         */
        private int uploadTimes;



        /**
         *
         * @param fileName
         * @param suffix
         */

        public UploadTask(String fileName, String suffix) {
            this.fileName = fileName;
            this.suffix = suffix;
            this.startTime = System.currentTimeMillis();
        }

        public void setFuture(Future future){
            this.future = future;
        }

        @Override
        public void run() {
            /**
             * 生成图片失败情况
             */
            if(System.currentTimeMillis()-startTime>30000){
                logger.error("执行时间过长，终止任务："+fileName);

                future.cancel(true);
                return;
            }

            //上传失败
            if(uploadTimes>=3){
                logger.error("上传次数过多，终止任务:"+fileName);
                future.cancel(true);
                return;
            }

            String path = wkImageStorage+"/"+fileName+suffix;

            File file = new File(path);
            if(file.exists()){
                logger.info(String.format("开始第%d次上传[%s].",++uploadTimes,fileName));

                //设置响应信息
                StringMap policy =  new StringMap();
                policy.put("returnBody", CommunityUtil.getJSONString(0));
                //生成上传凭证
                Auth auth = Auth.create(accessKey,secretKey);
                String uploadToken = auth.uploadToken(shareBucketName,fileName,3600,policy);

                //指定上传机房
                UploadManager manager = new UploadManager(new Configuration(Zone.zone0()));

                try {
                    //开始上传图片


                    // 开始上传图片
                    Response response = manager.put(
                            path, fileName, uploadToken, null, "image/" + suffix, false);
                    //处理响应结果
                    JSONObject json = JSONObject.parseObject(response.bodyString());

                    if(json==null||json.get("code")==null||!json.get("code").toString().equals("0")){
                        logger.info(String.format("第%d次上传失败[%s].",uploadTimes,fileName));
                    }else {
                        logger.info(String.format("第%d次上传成功[%s].",uploadTimes,fileName));
                        future.cancel(true);
                    }

                }catch (QiniuException e){
                    logger.info(String.format("第%d次上传失败[%s].",uploadTimes,fileName));
                }







            }else {
                logger.info("等待图片生成["+fileName+"].");
            }


        }
    }

}
