package com.guzuro.Commentary;

public class Commentary {
    private Number id;

    private String text;

    private Number todo_id;

    public Commentary(){}

    public Commentary(Number id, String text, Number todo_id) {
        this.id = id;
        this.text = text;
        this.todo_id = todo_id;
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
}
