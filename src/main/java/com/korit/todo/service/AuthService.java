package com.korit.todo.service;

import com.korit.todo.dto.SigninReqDto;
import com.korit.todo.dto.SignupReqDto;
import com.korit.todo.entity.User;
import com.korit.todo.repository.UserMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public Boolean isDuplicatedEmail(String email) {
        Boolean result = false;

        result = userMapper.findUserByEmail(email) != null;
        result = userMapper.getUserCountByEmail(email) > 0;

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean insertUser(SignupReqDto signupReqDto) {
        Boolean result = false;
        User user = signupReqDto.toUser(passwordEncoder);
        result = userMapper.saveUser(user) > 0;
        return result;
    }

    public String signin(SigninReqDto signinReqDto) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(signinReqDto.getEmail(), signinReqDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(token);
        System.out.println("여기까지왔음!!!!");
        System.out.println(authentication.getName());

        Key kye = Keys.hmacShaKeyFor(Decoders.BASE64.decode("IgwBE5D+D+639nDdSwWj/Dvil3XPM2r0UXemY0+jPklLsjfA2PRZf7SvG47lQOAWSndSz4BwxfP7kXwb/0RRmQ=="));

        Date date = new Date(new Date().getTime() + (1000 * 60 * 60 * 24));
        String jwtToken = Jwts.builder()
                .claim("username", authentication.getName())
                .claim("auth", authentication.getAuthorities())
                .setExpiration(date)
                .signWith(kye, SignatureAlgorithm.HS256)
                .compact();
        System.out.println(jwtToken);
        return jwtToken;
    }
}











