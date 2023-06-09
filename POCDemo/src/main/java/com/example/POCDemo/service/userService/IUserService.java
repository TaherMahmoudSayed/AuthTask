package com.example.POCDemo.service.userService;

import com.example.POCDemo.domain.Role;
import com.example.POCDemo.domain.User;

import java.util.List;
import java.util.Optional;

public interface IUserService  {

    User saveUser(User user);
    Role saveRole(Role role);
    void attachRoleToUser(String role,String username);
    Optional<User> getUser(String username);
    Optional<List<User>>getUsers();

}
