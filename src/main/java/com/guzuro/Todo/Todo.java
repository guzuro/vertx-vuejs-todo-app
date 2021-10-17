package com.guzuro.Todo;

public class Todo {

    private Number id;
    private String title;
    private String description;

    public Todo(){
    }

    public Todo (Number id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
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
