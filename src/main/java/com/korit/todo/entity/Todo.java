package com.korit.todo.entity;

import com.korit.todo.dto.GetTodoListRespDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Todo {
    private int todoId;
    private String content;
    private String email;

    public GetTodoListRespDto toTodoListRespDto() {
        return GetTodoListRespDto.builder()
                .todoId(todoId)
                .content(content)
                .email(email)
                .build();
    }
}
