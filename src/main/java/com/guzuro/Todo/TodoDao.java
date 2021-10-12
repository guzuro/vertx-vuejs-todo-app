package com.guzuro.Todo;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

public interface TodoDao {
    public Future<Object> getAllTodos(Vertx vertx);

    public void updateTodo(Todo todo);

    public void deleteTodo(Todo todo);
}
