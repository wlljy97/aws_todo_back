package com.korit.todo.repository;

import com.korit.todo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public User findUserByEmail(String email);
    public Integer getUserCountByEmail(String email);
    public Integer saveUser(User user);
}
