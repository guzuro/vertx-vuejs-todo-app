package com.guzuro.Todo;

import io.vertx.core.Vertx;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public interface TodoDao {
    CompletableFuture<CopyOnWriteArrayList<Todo>> getAllTodos(Vertx vertx);

    void updateTodo(Todo todo);

    void deleteTodo(Todo todo);
}
