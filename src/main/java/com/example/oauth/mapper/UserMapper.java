package com.example.oauth.mapper;

import com.example.oauth.bean.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;


@Mapper
@Component
public interface UserMapper {

    @Insert("Insert into user (username,password,mobile) value (#{username}, #{password}, #{mobile}")
    //boolean insertUser(@Param("username") String username, @Param("password") String password);
    boolean insertUser(User user);

    @Delete("Delete from user where uid = #{uid}")
    boolean deleteById(@Param("uid") int uid);

    @Select("Select * from user where uid = #{uid}")
    User findById(@Param("uid") int uid);

    @Select("Select * from user where username = #{username}")
    User findByName(@Param("username") String username);

    @Select("Select * from user where username = #{username} and password = #{password}")
    User checkUser(User user);

    @Select("Select * from user where uid = #{uid}")
    boolean checkById(@Param("uid") int uid);


}
