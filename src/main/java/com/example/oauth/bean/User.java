package com.example.oauth.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String username;
    private String password;
    private int createTime;      //创建时间
    private int updateTime;       //最后修改时间
    private int uid;       //用户id
    private String mobile;   //手机号码
    private String sex;
}
