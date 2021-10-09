package com.guzuro.Todo;

import com.guzuro.Status;

public class Todo {
    private String title;
    private String description;
    private Status status;

    public Todo(String title, String description, Status priority) {
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setPriority(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
