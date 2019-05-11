package com.example.oauth.controller;

import com.example.oauth.bean.User;
import com.example.oauth.exception.ValidException;
import com.example.oauth.response.UserResponse;
import com.example.oauth.service.OAuthService;
import com.example.oauth.service.UserService;
import com.example.oauth.utils.Base64;
import com.example.oauth.utils.CastUtil;
import com.example.oauth.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.UnknownHostException;

@RestController
@Slf4j
public class OAuthController {

    @Autowired
    private OAuthService oAuthService;
    private UserService userService;


    /**
     * （A）用户访问客户端，后者将前者导向认证服务器，即跳转到登录页面
     */
    @RequestMapping("authorize")
    public String authorize(String response_type, String redirect_uri, String state) {

        return "user/login";
    }


    /**
     * （E）认证服务器核对了授权码和重定向URI，确认无误后，向客户端发送访问令牌（access token）和更新令牌（refresh token）。
     */
    @RequestMapping("getTokenByCode")
    @ResponseBody
    public String getTokenByCode(String grant_type, String code, String redirect_uri) throws Exception {
        String uid = null;
        // 判断redirect_uri、code是否正确
        if (redirect_uri.equals(oAuthService.getRedirect_uri())) {
            boolean flag = oAuthService.checkCode(code);

            if (flag) {
                //解析授权码，获得用户id
                uid = Base64.getInstance().parseBASE64(code);
            } else {
                throw new ValidException("failed to parse code");
            }
        }

        // 返回token
        return "{access_token:" + oAuthService.getAccessToken(code, uid) +
                "token_type:bearer," + grant_type +
                "expires_in:900}";
    }

    /**
     * （F）资源服务器确认令牌无误，同意向客户端开放资源。。
     */
    @RequestMapping("getUserinfoByToken")
    @ResponseBody
    public UserResponse getUserinfoByToken(String token) throws Exception {
        // 判断token是否正确
        User user = oAuthService.checkToken(token);
        while (user != null) {
            return new UserResponse(100, "success", user);
        }
        return new UserResponse(101, "error", null);
    }
}
