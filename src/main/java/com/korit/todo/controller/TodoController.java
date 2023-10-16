package com.korit.todo.controller;

import com.korit.todo.dto.AddTodoReqDto;
import com.korit.todo.dto.UpdateTodoReqDto;
import com.korit.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/todo")
    public ResponseEntity<?> addTodo(@RequestBody AddTodoReqDto addTodoReqDto) {
        return ResponseEntity.ok(todoService.addTodo(addTodoReqDto));
    }

    @GetMapping("/todo/list")
    public ResponseEntity<?> getTodoList() {
        return ResponseEntity.ok().body(todoService.getTodoList());
    }

    @DeleteMapping("/todo/{todoId}")
    public ResponseEntity<?> removeTodo(@PathVariable int todoId) {
        return ResponseEntity.ok(todoService.removeTodo(todoId));
    }

    @PutMapping("/todo/{todoId}")
    public ResponseEntity<?> updateTodo(@PathVariable int todoId, @RequestBody UpdateTodoReqDto updateTodoReqDto) {
        return ResponseEntity.ok(todoService.updateTodo(todoId, updateTodoReqDto));
    }
}








