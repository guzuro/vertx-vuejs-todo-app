package com.guzuro.Commentary;

public class Commentary {
    private Number id;

    private String text;

    public Commentary(){}

    public Commentary(Number id, String text, Number todo_id) {
        this.id = id;
        this.text = text;
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
}
