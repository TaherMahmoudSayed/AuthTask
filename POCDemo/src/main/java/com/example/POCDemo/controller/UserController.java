package com.example.POCDemo.controller;

import com.example.POCDemo.domain.Role;
import com.example.POCDemo.domain.User;
import com.example.POCDemo.service.userService.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1")

public class UserController {
    private final UserService userService;
    @GetMapping(path = "/users/{username}")
    public ResponseEntity<User> saveUser(@PathVariable String username) {
        try {
            return ResponseEntity.ok().body((userService.getUser(username).get()));
        } catch (Exception ex) {
            return null;
        }
    }
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        try {
            return ResponseEntity.ok().body((userService.getUsers().orElse(List.of())));
        } catch (Exception ex) {
            return null;
        }
    }

    @PostMapping(path = "/user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        try {
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/users/{user.getUserName()}").toUriString());
            return ResponseEntity.created(uri).body((userService.saveUser(user)));
        } catch (Exception ex) {
            return null;
        }
    }

    @PostMapping(path = "/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        try {
            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/role/save").toUriString());

            return ResponseEntity.created(uri).body((userService.saveRole(role)));
        } catch (Exception ex) {
            return null;
        }
    }

    @PostMapping(path = "/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form) {
        try {
            userService.attachRoleToUser(form.getRoleName(), form.getUsername());
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return null;
        }
    }

}

@Data
class RoleToUserForm {
    private String username;
    private String roleName;
}

