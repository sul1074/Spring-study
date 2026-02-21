package com.cos.security1.service;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void saveUser(User requestUser) {
        requestUser.setPassword(passwordEncoder.encode(requestUser.getPassword()));
        requestUser.setRole("ROLE_USER");

        userRepository.save(requestUser);
    }
}
