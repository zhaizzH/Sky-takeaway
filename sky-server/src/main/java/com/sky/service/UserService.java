package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

public interface UserService {
    /**
     * 微信登录
     * @param userLoginDTO 登录DTO
     * @return 用户实体
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
