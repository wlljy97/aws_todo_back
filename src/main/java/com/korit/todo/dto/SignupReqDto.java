package com.korit.todo.dto;

import com.korit.todo.entity.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
public class SignupReqDto {

    @Email(message = "이메일 형식을 지켜주세요.")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]*$", message = "숫자, 문자 중 무조건 한글자를 포함하세요.")
    private String password;

    @Pattern(regexp = "^[가-힣]*$", message = "한글만 입력 가능합니다.")
    private String name;

    public User toUser(BCryptPasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .build();
    }
}
