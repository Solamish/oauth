package com.example.oauth.service;

import com.alibaba.fastjson.JSONObject;
import com.example.oauth.bean.User;
import com.example.oauth.mapper.UserMapper;
import com.example.oauth.utils.Base64;
import com.example.oauth.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;


@Service
public class OAuthService {

    @Autowired
    UserMapper userMapper;

    private static String getLocalHost() throws UnknownHostException {
        //return InetAddress.getLocalHost().getHostAddress();
        return "10.100.19.211";
    }

    public static String getRedirect_uri() throws UnknownHostException {
        return "http://" + getLocalHost() + ":7001/index";
    }


    /**
     * 将用户id写进授权码中
     *
     * @param uid
     * @return
     * @throws UnknownHostException
     */
    public static String getCode(String uid) throws UnknownHostException {
        //String key = getRedirect_uri();
        return Base64.getInstance().createBASE64(uid);
    }

    public boolean checkCode(String code) {
        if (code == null) {
            return false;
        }
        return true;
    }

    /**
     * 获取Token令牌
     *
     * @param code
     * @param uid
     * @return
     * @throws Exception
     */
    public String getAccessToken(String code, String uid) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("uid", uid);

        return JwtUtil.getInstance().createJWT(code, jsonObject.toJSONString(), 15 * 60 * 1000);  //15分钟后token过期
    }

    /**
     * 校验token是否正确
     * @param AccessToken
     * @return
     * @throws Exception
     */
    public User checkToken(String AccessToken) throws Exception {
        Claims claims = JwtUtil.getInstance().parseJWT(AccessToken);
        JSONObject jsonObject = JSONObject.parseObject(claims.getSubject());
        int uid = Integer.parseInt(jsonObject.getString("uid"));
        boolean flag = userMapper.checkById(uid);

        if (AccessToken != null) {
            if (flag) {
                return userMapper.findById(uid);
            }
        }
        return null;
    }

    /**
     * 通过Token令牌获取用户id
     *
     * @param AccessToken
     * @return
     * @throws Exception
     */
    public static Integer getUserId(String AccessToken) throws Exception {
        Claims claims = JwtUtil.getInstance().parseJWT(AccessToken);
        JSONObject jsonObject = JSONObject.parseObject(claims.getSubject());

        return Integer.parseInt(jsonObject.getString("uid"));
    }
}
