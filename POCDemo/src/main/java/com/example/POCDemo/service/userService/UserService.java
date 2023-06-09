package com.example.POCDemo.service.userService;

import com.example.POCDemo.domain.Role;
import com.example.POCDemo.domain.User;
import com.example.POCDemo.repository.IRoleRepository;
import com.example.POCDemo.repository.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserService implements IUserService{
    private final IUserRepository userRepository;
    private final IRoleRepository   roleRepository;
    @Override
    public User saveUser(User user) {
       try
       {
           if(user==null){
               return null ;
           }
           else {

            var savedUser=   userRepository.save(user);
            return savedUser;
           }
       }catch (Exception ex){
          return null;
       }
    }
    @Override
    public Role saveRole(Role role) {
        try
        {
            if(role==null){
                return null ;
            }
            else {
                var savedRole=   roleRepository.save(role);
                return savedRole;
            }
        }catch (Exception ex){
            return null;
        }
    }

    @Override
    public void attachRoleToUser(String rolename, String username) {
        try
        {
            if(username==null||rolename==null){
                throw new NullPointerException();
            }
            else {
                User user=userRepository.findByuserName(username).orElse(null);
                Role role=roleRepository.findByRole(rolename).orElse(null);
                if(user==null||role==null){
                    throw new NullPointerException();
                }
                else {
                  user.getRoles().add(role);
                }
            }
        }catch (Exception ex){
            throw new NullPointerException();
        }

    }

    @Override
    public Optional<User> getUser(String username) {
        try
        {
            if(username==null){
                return null ;
            }
            else {
                var savedUser=   userRepository.findByuserName(username);
                return savedUser;
            }
        }catch (Exception ex){
            return null;
        }
    }

    @Override
    public Optional<List<User>> getUsers() {
        try
        {
           return Optional.of(userRepository.findAll());
        }catch (Exception ex){
            return null;
        }
    }
}
