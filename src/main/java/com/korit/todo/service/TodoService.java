package com.korit.todo.service;

import com.korit.todo.dto.AddTodoReqDto;
import com.korit.todo.dto.GetTodoListRespDto;
import com.korit.todo.dto.UpdateTodoReqDto;
import com.korit.todo.entity.Todo;
import com.korit.todo.repository.TodoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoMapper todoMapper;

    @Transactional(rollbackFor = Exception.class)
    public Boolean addTodo(AddTodoReqDto addTodoReqDto) {
        // JwtAuthenticationFilter에서 인증받으려고 넣어둔 Authentication을 다시 꺼내서 email가져옴.
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Todo todo = Todo.builder()
                .content(addTodoReqDto.getContent())
                .email(email)
                .build();

        return todoMapper.saveTodo(todo) > 0;
    }

    public List<GetTodoListRespDto> getTodoList() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        List<GetTodoListRespDto> getTodoListRespDtos = new ArrayList<>();

        todoMapper.getTodoListByEmail(email).forEach(todo -> {
            getTodoListRespDtos.add(todo.toTodoListRespDto());
        });

        return getTodoListRespDtos;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean removeTodo(int todoId) {
        return todoMapper.deleteTodo(todoId) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateTodo(int todoId, UpdateTodoReqDto updateTodoReqDto) {
        Todo todo = Todo.builder()
                .todoId(todoId)
                .content(updateTodoReqDto.getUpdateContent())
                .build();
        return todoMapper.updateTodo(todo) > 0;
    }

}
