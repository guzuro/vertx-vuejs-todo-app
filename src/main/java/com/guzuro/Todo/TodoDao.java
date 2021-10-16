package com.guzuro.Todo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public interface TodoDao {

    CompletableFuture<Todo> createTodo(Todo todo);

    CompletableFuture<CopyOnWriteArrayList<Todo>> getAllTodos();

    CompletableFuture<Todo> updateTodo(Todo todo);

    CompletableFuture<Boolean> deleteTodo(Number todoId);
}
