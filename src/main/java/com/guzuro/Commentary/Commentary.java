package com.guzuro.Commentary;

import java.time.LocalDateTime;

public class Commentary {
    private Number id;

    private String text;

    private Number todo_id;

    private LocalDateTime created_at;

    public Commentary(){}

    public Commentary(Number id, String text, Number todo_id, LocalDateTime created_at) {
        this.id = id;
        this.text = text;
        this.todo_id = todo_id;
        this.created_at = created_at;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Number getId() {
        return id;
    }

    public void setId(Number id) {
        this.id = id;
    }

    public Number getTodo_id() {
        return todo_id;
    }

    public void setTodo_id(Number todo_id) {
        this.todo_id = todo_id;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
