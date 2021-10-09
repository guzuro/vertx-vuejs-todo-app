package com.guzuro.Todo;

import io.vertx.sqlclient.SqlClient;

import java.util.concurrent.CopyOnWriteArrayList;

public interface TodoDao {
    public CopyOnWriteArrayList<Todo> getAllTodos(SqlClient client);

    public void updateTodo(Todo todo);

    public void deleteTodo(Todo todo);
}
