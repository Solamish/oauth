package com.example.oauth.controller;

import com.example.oauth.bean.User;
import com.example.oauth.exception.ValidException;
import com.example.oauth.response.UserResponse;
import com.example.oauth.service.OAuthService;
import com.example.oauth.service.UserService;
import com.example.oauth.utils.CastUtil;
import com.example.oauth.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;



@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    private OAuthService oAuthService;

    @PostMapping(value = "/sign", produces = "application/json")
    public UserResponse regist(User user) throws ValidException {
        if (StringUtils.isEmpty(user.getUsername())) {
            log.error("failed to sign up ");
            //throw new ValidException("请输入用户信息");
            return new UserResponse(101, "用户名不能为空", null);
        } else if (StringUtils.isEmpty(user.getPassword())) {
            log.error("failed to sign up ");
            return new UserResponse(101, "密码不能为空", null);
        } else {
//            long nowTime = (long) Math.ceil(new Date().getTime() / 1000);
//            user.setCreateTime(nowTime);
//            user.setUpdateTime(nowTime);
            boolean flag = userService.signUp(user);
            if (flag) {
                return new UserResponse(100, "success", user);
            } else {
                return new UserResponse(101, "此用户已存在", null);
            }
        }

    }

    /**
     * （C）假设用户给予授权，认证服务器将用户导向客户端事先指定的"重定向URI"（redirection URI），同时附上一个授权码。
     */
   @PostMapping(value = "/login", produces = "application/json")
    public UserResponse login(User user, HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(user.getUsername())) {
            log.error("failed to login");
            return new UserResponse(101, "用户名不能为空", null);
        } else if (StringUtils.isEmpty(user.getPassword())) {
            log.error("failed to sign up ");
            return new UserResponse(101, "密码不能为空", null);
        } else {
//            long nowTime = (long) Math.ceil(new Date().getTime() / 1000);
//            user.setUpdateTime(nowTime);
            boolean flag = userService.login(user);
            if (flag) {
                String uid = String.valueOf(user.getUid());
                response.sendRedirect(oAuthService.getRedirect_uri() + "?code="+oAuthService.getCode(uid)+"&state=hehe");
                return new UserResponse(100, "success", null);
            } else {
                return new UserResponse(101, "用户名或密码错误", null);
            }
        }
    }

}
