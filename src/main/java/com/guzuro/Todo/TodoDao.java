package com.guzuro.Todo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public interface TodoDao {

    CompletableFuture<CopyOnWriteArrayList<Todo>> getAllTodos();

    CompletableFuture<Todo> createTodo(Todo todo);

    void updateTodo(Todo todo);

    void deleteTodo(Todo todo);
}
