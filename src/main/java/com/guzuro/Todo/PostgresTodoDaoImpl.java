package com.guzuro.Todo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guzuro.DaoFactory.PostgresDAOFactory;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Tuple;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public class PostgresTodoDaoImpl implements TodoDao {
    SqlClient pgClient;

    public PostgresTodoDaoImpl(Vertx vertx) {
        pgClient = PostgresDAOFactory.createConnection(vertx);
    }

    @Override
    public CompletableFuture<CopyOnWriteArrayList<Todo>> getAllTodos() {

        CopyOnWriteArrayList<Todo> todoList = new CopyOnWriteArrayList<>();

        CompletableFuture<CopyOnWriteArrayList<Todo>> future = new CompletableFuture<>();

        pgClient
                .query("SELECT id, title, description from todo_item")
                .execute(ar -> {
                    if (ar.succeeded()) {
                        RowSet<Row> result = ar.result();
                        ObjectMapper objectMapper = new ObjectMapper();
                        for (Row row : result) {
                            try {
                                Todo todo = objectMapper.readValue(row.toJson().toString(), Todo.class);
                                todoList.add(todo);
                            } catch (JsonProcessingException e) {
                                e.getStackTrace();
                            }
                        }
                        future.complete(todoList);
                    } else {
                        future.completeExceptionally(new Throwable(ar.cause()));
                    }
                });
        return future;
    }

    @Override
    public CompletableFuture<Todo> createTodo(Todo todo) {
        CompletableFuture<Todo> future = new CompletableFuture<>();
        pgClient
                .preparedQuery("INSERT INTO todo_item(title, description) VALUES ($1, $2) returning id, title, description")
                .execute(Tuple.of(todo.getTitle(), todo.getDescription()), ar -> {
                    if (ar.succeeded()) {
                        Row result = ar.result().iterator().next();
                        ObjectMapper objectMapper = new ObjectMapper();
                        try {
                            Todo resultTodo = objectMapper.readValue(result.toJson().toString(), Todo.class);
                            future.complete(resultTodo);
                        } catch (JsonProcessingException e) {
                            future.completeExceptionally(new Throwable(e.getCause()));
                        }
                    } else {
                        future.completeExceptionally(new Throwable(ar.cause()));
                    }
                });
        return future;
    }

    @Override
    public void updateTodo(Todo todo) {

    }

    @Override
    public void deleteTodo(Todo todo) {

    }
}
