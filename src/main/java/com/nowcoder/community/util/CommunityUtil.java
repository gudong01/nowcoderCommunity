package com.nowcoder.community.util;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CommunityUtil {
    //生成随机字符串
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
    //MD5加密
    //只能加密没法解密 且每次加密结果都一样
    //所以在密码后面加上一个随机字符串
    public static String md5(String key){
        if(StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getJSONString(int code, String msg, Map<String,Object> map){
        JSONObject json = new JSONObject();
        //封装数据
        json.put("code",code);
        json.put("msg",msg);
        if(map != null){
            for(String key :map.keySet()){
                json.put(key,map.get(key));
            }
        }
        System.out.println(json);
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg){
        return getJSONString(code,msg,null);
    }

    public static String getJSONString(int code){
        return getJSONString(code,null,null);
    }

//    public static void main(String[] args) {
//        Map<String,Object> map = new HashMap<>();
//        map.put("name","张三");
//        map.put("age",25);
//        System.out.println(getJSONString(0, "ok", map));
//    }

}
