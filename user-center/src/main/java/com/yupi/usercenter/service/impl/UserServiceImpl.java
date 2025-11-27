package com.yupi.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.usercenter.BusinessException.BusinessException;
import com.yupi.usercenter.commmons.BaseResponse;
import com.yupi.usercenter.commmons.ErrorCode;
import com.yupi.usercenter.contant.UserConstant;
import com.yupi.usercenter.model.User;
import com.yupi.usercenter.service.UserService;
import com.yupi.usercenter.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 12538
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2025-11-20 15:03:33
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    private final String salt = "kong";

    @Autowired
    private UserMapper userMapper;

    //用户注册
    @Override
    public long userRegister(String username, String password, String checkPassword, String code) {
        //检查是否为空和过短
        if (StringUtils.isAllBlank(username, password, checkPassword,code)) {
            throw  new BusinessException(ErrorCode.PARMAS_ERROR,"账号或密码为空");
        }
        if (username.length() < 4 || password.length() < 8) {
            throw  new BusinessException(ErrorCode.PARMAS_ERROR,"账号或密码过短");
        }
        //判断是否有特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (matcher.find())
            throw  new BusinessException(ErrorCode.PARMAS_ERROR,"用户名格式错误");

        //判断密码与校验码是否一致
        if (!password.equals(checkPassword))
            throw  new BusinessException(ErrorCode.PARMAS_ERROR,"密码不一致");

        //检查是否重复用户名
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        long count = this.count(queryWrapper);
        if (count != 0)
            throw  new BusinessException(ErrorCode.PARMAS_ERROR,"该用户名已存在");


        //检查是否重复编号
        QueryWrapper codewrapper = new QueryWrapper();
        queryWrapper.eq("code", code);
        long count_code = this.count(queryWrapper);
        if (count != 0)
            throw  new BusinessException(ErrorCode.PARMAS_ERROR,"该编号已注册");


        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((salt + password).getBytes());
        //写入数据
        User user = new User();
        user.setUsername(username);
        user.setUserPassword(encryptPassword);
        user.setCode(code);


        boolean result = this.save(user);
        if (!result)
            throw  new BusinessException(ErrorCode.PARMAS_ERROR);
        return user.getId();
    }

    //用户登录
    @Override
    public User doLogin(String username, String password, HttpServletRequest request) {
        //检查是否为空和过短
        if (StringUtils.isAllBlank(username, password)) {
            throw  new BusinessException(ErrorCode.PARMAS_ERROR);
        }
        if (username.length() < 4 || password.length() < 8) {
            throw  new BusinessException(ErrorCode.PARMAS_ERROR);
        }
        //判断是否有特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(username);
        if (matcher.find())
            throw  new BusinessException(ErrorCode.PARMAS_ERROR);
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((salt + password).getBytes());

        //查询用户信息
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", username);
        queryWrapper.eq("userPassword", encryptPassword);
        User user1 = userMapper.selectOne(queryWrapper);
        if(user1 == null) {
            log.info("username could not mach password");
            throw  new BusinessException(ErrorCode.PARMAS_ERROR);
        }

        User safeUser = getSafetyuser(user1);
        //记录登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, safeUser);
        return safeUser;
    }

    @Override
    public User getSafetyuser(User originalUser){
        //用户脱敏
        User safeUser = new User();
        safeUser.setId(originalUser.getId());
        safeUser.setUsername(originalUser.getUsername());
        safeUser.setUserAccount(originalUser.getUserAccount());
        safeUser.setAvatarUrl(originalUser.getAvatarUrl());
        safeUser.setGender(originalUser.getGender());
        safeUser.setPhone(originalUser.getPhone());
        safeUser.setEmail(originalUser.getEmail());
        safeUser.setUserRole(originalUser.getUserRole());
        safeUser.setUserStatus(originalUser.getUserStatus());
        safeUser.setCreateTime(originalUser.getCreateTime());
        safeUser.setIsDelete(0);
        safeUser.setCode(originalUser.getCode());
        return safeUser;
    }

    //用户注销
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return 1;
    }


}




