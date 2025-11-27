package com.yupi.usercenter.model;

import lombok.Data;

@Data
public class UserRegistRequest implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String checkPassword;

    private String code;

}
