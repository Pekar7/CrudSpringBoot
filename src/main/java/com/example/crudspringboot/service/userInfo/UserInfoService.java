package com.example.crudspringboot.service.userInfo;

import com.example.crudspringboot.model.UserInfo;

import java.util.List;

public interface UserInfoService {
    UserInfo findUserInfoById(Long id);
    List<UserInfo> findAllUserInfo();
    UserInfo addUserInfo(UserInfo existingUser);
    void deleteUserInfo(UserInfo user);
    boolean existsByUsername(String username);
}

