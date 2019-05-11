package com.example.oauth.response;

import com.example.oauth.bean.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    int status;
    String info;
    User user;

}
