package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;


    @Service
    public class UserServiceImpl implements UserService {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Autowired
        public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
        }

        @Override
        public List<User> allUsers() {
            return userRepository.findAll();
        }

        @Override
        public User getUserById(Long id) {
            return userRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("User with id " + id + " not found"));
        }

        @Override
        public User findUserByUsername(String username) {
            return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
        }

        @Transactional
        @Override
        public void addUser(User user)  {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
        }

        @Transactional
        @Override
        public void removeUser(Long id) {
            if (!userRepository.existsById(id)) {
                throw new EntityNotFoundException("User with id " + id + " not found");
            }
            userRepository.deleteById(id);
        }

        @Transactional
        @Override
        public void updateUser(User user, String newPassword) {
            if (!userRepository.existsById(user.getId())) {
                throw new EntityNotFoundException("User with id " + user.getId() + " not found");
            }
            if (!passwordEncoder.matches(newPassword, user.getPassword())) {
                // Пароль изменился, перекодируем его
                user.setPassword(passwordEncoder.encode(newPassword));
            }
            userRepository.save(user);
        }

    }