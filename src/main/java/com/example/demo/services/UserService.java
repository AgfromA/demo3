package com.example.demo.services;

import com.example.demo.models.User;

import java.util.List;

public interface UserService {
     List<User> allUsers();

   User getUserById(Long id);

     User findUserByUsername(String username);

    void  addUser(User user);

     void updateUser(User user, String newPassword);

   void removeUser(Long id);


}
