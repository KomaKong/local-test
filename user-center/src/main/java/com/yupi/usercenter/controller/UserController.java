package com.yupi.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.usercenter.BusinessException.BusinessException;
import com.yupi.usercenter.commmons.BaseResponse;
import com.yupi.usercenter.commmons.ErrorCode;
import com.yupi.usercenter.commmons.Result;
import com.yupi.usercenter.contant.UserConstant;
import com.yupi.usercenter.model.User;
import com.yupi.usercenter.model.UserLoginRequest;
import com.yupi.usercenter.model.UserRegistRequest;
import com.yupi.usercenter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/regist")
    public BaseResponse<Long> userRegist(@RequestBody UserRegistRequest userRegistRequest) {
        if (userRegistRequest == null)
            //return Result.error(ErrorCode.PARMAS_ERROR);
            throw new BusinessException(ErrorCode.PARMAS_ERROR);
        String username = userRegistRequest.getUsername();
        String password = userRegistRequest.getPassword();
        String checkPassword = userRegistRequest.getCheckPassword();
        String code = userRegistRequest.getCode();
        if (StringUtils.isAnyBlank(username, password, checkPassword, code))
            return Result.error(ErrorCode.PARMAS_ERROR);
        long result = userService.userRegister(username, password, checkPassword, code);
//        return new BaseResponse<Long>(0,"ok",l);
        return Result.success(result);
    }

    @RequestMapping("/login")
    public BaseResponse<User> userLoin(@RequestBody UserLoginRequest userLoginRequest,
                                       HttpServletRequest request) {
        if (userLoginRequest == null)
            throw new BusinessException(ErrorCode.PARMAS_ERROR);
        String username = userLoginRequest.getUsername();
        String password = userLoginRequest.getPassword();
        if (StringUtils.isAnyBlank(username, password))
            throw new BusinessException(ErrorCode.PARMAS_ERROR);
        User user = userService.doLogin(username, password, request);
//        return new BaseResponse<>(0,"ok",user);
        return Result.success(user);
    }

    //用户注销
    @RequestMapping("/logout")
    public Integer userLogout(HttpServletRequest request) {
        if (request == null)
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        return userService.userLogout(request);
    }

    //获取当前用户
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) attribute;
        if (user == null)
            throw new BusinessException(ErrorCode.PARMAS_ERROR);
        return Result.success(userService.getSafetyuser(user));
    }


    @RequestMapping("/search")
    public BaseResponse<List<User>> SearchUser(String username, HttpServletRequest request) {
        //用户校验
        Object objuser = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) objuser;
        if (user == null || user.getUserRole() != UserConstant.admin_role)
            throw new BusinessException(ErrorCode.PARMAS_ERROR);
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotBlank(username))
            queryWrapper.like("username", username);
        List userList = userService.list(queryWrapper);
        //return userList;
        List<User> result = (List<User>) userList.stream().map(User -> userService.getSafetyuser(user)).collect(Collectors.toList());
        return Result.success(result);
    }

    @RequestMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody Long id, HttpServletRequest request) {
        //用户校验
        Object objuser = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) objuser;
        if (user == null || user.getUserRole() != UserConstant.admin_role)
            throw new BusinessException(ErrorCode.PARMAS_ERROR);
        if (id <= 0)
            throw new BusinessException(ErrorCode.PARMAS_ERROR);
        return Result.success(userService.removeById(id));
    }
}
