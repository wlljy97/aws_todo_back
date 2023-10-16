package com.korit.todo.service;

import com.korit.todo.entity.PrincipalUser;
import com.korit.todo.entity.User;
import com.korit.todo.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("loadUserByUsername<email>: " + email);
        User user = userMapper.findUserByEmail(email);

        if(user == null) {
            return null;
        }

        return new PrincipalUser(user);
    }

}
