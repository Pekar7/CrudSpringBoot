package com.example.crudspringboot.service.userInfo;

import com.example.crudspringboot.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findUserInfoByUsername(String username);
    Boolean existsByUsername(String username);
    UserInfo findById(Long id);
    boolean existsById(Long userId);
}