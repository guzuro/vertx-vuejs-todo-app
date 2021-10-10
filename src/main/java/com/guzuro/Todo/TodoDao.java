package com.guzuro.Todo;

import io.vertx.core.Vertx;

import java.util.concurrent.CopyOnWriteArrayList;

public interface TodoDao {
    public CopyOnWriteArrayList<Todo> getAllTodos(Vertx vertx);

    public void updateTodo(Todo todo);

    public void deleteTodo(Todo todo);
}
