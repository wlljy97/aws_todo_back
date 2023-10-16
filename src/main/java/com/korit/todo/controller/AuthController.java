package com.korit.todo.controller;

import com.korit.todo.dto.SigninReqDto;
import com.korit.todo.dto.SignupReqDto;
import com.korit.todo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class AuthController {

//    @Autowired
//    private AuthService authService;

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupReqDto signupReqDto, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for(FieldError fieldError : bindingResult.getFieldErrors()) {
                String fieldName = fieldError.getField();
                String message = fieldError.getDefaultMessage();
                errorMap.put(fieldName, message);
            }
            return ResponseEntity.badRequest().body(errorMap);
        }

        if(authService.isDuplicatedEmail(signupReqDto.getEmail())) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("email", "이미 사용중인 이메일입니다.");
            return ResponseEntity.badRequest().body(errorMap);
        }

        return ResponseEntity.ok().body(authService.insertUser(signupReqDto));
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<?> signin(@RequestBody SigninReqDto signinReqDto) {
        System.out.println("test");
        return ResponseEntity.ok().body(authService.signin(signinReqDto));
    }

    @GetMapping("/authenticated")
    public ResponseEntity<?> authenticated() {
        return ResponseEntity.ok(true);
    }

}
