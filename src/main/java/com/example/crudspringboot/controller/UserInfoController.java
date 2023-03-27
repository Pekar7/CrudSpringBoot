package com.example.crudspringboot.controller;

import com.example.crudspringboot.model.Card;
import com.example.crudspringboot.model.UserInfo;
import com.example.crudspringboot.service.UserService;
import com.example.crudspringboot.service.userInfo.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/users")
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final UserService passwordEncoder;

    @Autowired
    public UserInfoController(UserInfoService userInfoService, UserService passwordEncoder) {
        this.userInfoService = userInfoService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping()
    public ResponseEntity<List<UserInfo>> getAllUsers() {
        List<UserInfo> users = userInfoService.findAllUserInfo();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Object getUserById(@PathVariable Long id) {
        UserInfo user = userInfoService.findUserInfoById(id);
        if (user == null) {
            return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пользователь существует");
        } else {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
    }

    @PostMapping()
    public Object createUser(@RequestBody UserInfo requestUser, HttpServletRequest request) {
        String encodedRole = request.getHeader("Role");
        String decodedRole = new String(Base64.getDecoder().decode(encodedRole), StandardCharsets.UTF_8);
        if ("ADMIN".equals(decodedRole)) {
            if (userInfoService.existsByUsername(requestUser.getUsername())) {
                return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пользователь существует");
            }

            UserInfo user = new UserInfo();
            user.setUsername(requestUser.getUsername());
            user.setPassword(passwordEncoder.encodePassword((requestUser.getPassword())));
            user.setRoles(requestUser.getRoles());

            UserInfo savedUser = userInfoService.addUserInfo(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } else {
            return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ограничение прав доступа");
        }

    }

    @PutMapping("/{id}")
    public Object updateUser(@PathVariable Long id, @RequestBody UserInfo user, HttpServletRequest request) {
        String encodedRole = request.getHeader("Role");
        String decodedRole = new String(Base64.getDecoder().decode(encodedRole), StandardCharsets.UTF_8);
        if ("ADMIN".equals(decodedRole)) {
            UserInfo existingUser = userInfoService.findUserInfoById(id);
            if (existingUser == null) {
                return new ResponseEntity<>("Пользователь не найден", HttpStatus.NOT_FOUND);
            } else {
                existingUser.setUsername(user.getUsername());
                existingUser.setRoles(user.getRoles());
                existingUser.setPassword(passwordEncoder.encodePassword((user.getPassword())));
                userInfoService.addUserInfo(existingUser);
                return new ResponseEntity<>("Данные пользователя успешно добавлены", HttpStatus.OK);
            }
        } else {
            return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ограничение прав доступа");
        }
    }

    @DeleteMapping("/{id}")
    public Object deleteUserById(@PathVariable Long id, HttpServletRequest request) {
        String encodedRole = request.getHeader("Role");
        String decodedRole = new String(Base64.getDecoder().decode(encodedRole), StandardCharsets.UTF_8);
        if ("ADMIN".equals(decodedRole)) {
            UserInfo user = userInfoService.findUserInfoById(id);
            if (user == null) {
                return new ResponseEntity<>("Пользователь не найден", HttpStatus.NOT_FOUND);
            } else {
                userInfoService.deleteUserInfo(user);
                return new ResponseEntity<>("Пользователь успешно удален", HttpStatus.OK);
            }
        } else {
            return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ограничение прав доступа");
        }
    }
}
