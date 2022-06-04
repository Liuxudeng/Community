package com.nowcoder.community.util;

/**
 * 用于向redis存储数据，生成redisKey
 */
public class RedisKeyUtil {

        private static final String SPLIT = ":";

    /**
     * 存实体（这里实体包括帖子、评论、点赞）
     */
    private static final String PREFIX_ENTITY_LIKE ="like:entity";

    private static final String PREFIX_USER_LIKE = "like:user";
    private static final String PREFIX_FOLLOWEE = "followee";
    private static final String PREFIX_FOLLOWER = "follower";
    private static final String PREFIX_KAPTCHA = "kaptcha";
    private static final String PREFIX_TICKET = "ticket";
    private static final String PREFIX_USER = "user";

    private static final String PREFIX_UV = "uv";
    private static final String PREFIX_DAU = "dau";

    private static final String PREFIX_POST = "post";


    /**
     * 某个实体的赞
     */

    /**
     * 存储的格式：   like：entity:entityType:entityId  ->  set(userId)
     */
    /**
     * 本方法的参数是通过discuss.js传入
     * @param entityType
     * @param entityId
     * @return 某个实体(帖子或回复)的赞的key值
     */

    public static String getEntityLikeKey(int entityType, int entityId){

        return PREFIX_ENTITY_LIKE+SPLIT+entityType+SPLIT+entityId;
    }

    // 某个用户收到的赞的个数
    // like:user:userId -> int
    public static String getUserLikeKey(int userId) {
        return PREFIX_USER_LIKE + SPLIT + userId;
    }

    // 某个用户关注的实体  （关注数）
    // followee:userId:entityType -> zset(entityId,now)
    public static String getFolloweeKey(int userId, int entityType) {
        return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    // 某个实体拥有的粉丝 （被关注数）
    // follower:entityType:entityId -> zset(userId,now)
    public static String getFollowerKey(int entityType, int entityId) {
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    // 登录验证码
    public static String getKaptchaKey(String owner) {
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    // 登录的凭证
    public static String getTicketKey(String ticket) {
        return PREFIX_TICKET + SPLIT + ticket;
    }

    // 用户
    public static String getUserKey(int userId) {
        return PREFIX_USER + SPLIT + userId;
    }

    /**
     * 返回单日uv
     */
    public static String getUVKey(String date){
        return PREFIX_UV+SPLIT+date;
    }

    /**
     * 区间UV
     */

    public static String getUVKey(String satrtDate, String endDate){
        return PREFIX_UV+SPLIT+satrtDate+SPLIT+endDate;
    }

    /**
     * 单日活跃用户
     */
    public static String getDAUKey(String date){
        return PREFIX_DAU+SPLIT+date;
    }

    /**
     * 区间活跃用户
     */

    public static String getDAUKey(String startDate,String endDate){
        return PREFIX_UV+SPLIT+startDate+SPLIT+endDate;
    }


/**
 * 帖子分数
 */

public static String getPostScoreKey(){
    return PREFIX_POST+SPLIT+"score";
}

}
