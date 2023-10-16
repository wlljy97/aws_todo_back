package com.korit.todo.repository;

import com.korit.todo.entity.Todo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TodoMapper {
    public int saveTodo(Todo todo);
    public List<Todo> getTodoListByEmail(String email);
    public int deleteTodo(int todoId);
    public int updateTodo(Todo todo);
}
