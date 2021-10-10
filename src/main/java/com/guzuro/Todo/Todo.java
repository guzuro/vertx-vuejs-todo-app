package com.guzuro.Todo;

import com.guzuro.Status;

public class Todo {

    private Number id;
    private String title;
    private String description;
    private Status statusId;

    public Todo(){
    }

    public Todo(Number id, String title, String description, Status statusId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.statusId = statusId;
    }

    public Status getStatusId() {
        return statusId;
    }

    public void setStatusId(Status status) {
        this.statusId = status;
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

    public Number getId() {
        return id;
    }

    public void setId(Number id) {
        this.id = id;
    }
}
