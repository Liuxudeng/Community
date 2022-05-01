package com.nowcoder.community.util;



import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

/**
 * 用于注册用的工具类，含有两个方法
 */
public class CommunityUtil {
    //生成一个随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    //MD5加密
    public static String md5(String key){
        if(StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }



    /**
     * 获取json字符串
     * @param code  编码
     * @param msg  提示信息
     * @param map 用于封装业务数据
     * @return
     */

    public static String getJSONString(int code, String msg, Map<String,Object> map){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        if(map!=null){
            for (String key: map.keySet()) {
                json.put(key,map.get(key));
            }
        }

        return json.toJSONString();

    }

    /**
     * 重载
     */

    public static String getJSONString(int code,String msg){
        return getJSONString(code,msg,null);
    }
    public static String getJSON(int code){
        return getJSONString(code,null,null);

    }

}