package com.yupi.usercenter.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.usercenter.model.User;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author 12538
* @description 针对表【user】的数据库操作Service
* @createDate 2025-11-23 14:38:17
*/
public interface UserService extends IService<User> {

    com.yupi.usercenter.model.User doLogin(String username, String password, HttpServletRequest request);

    long userRegister(String username, String password, String checkPassword, String code);

    User getSafetyuser(User originalUser);

    int userLogout(HttpServletRequest request);
}
