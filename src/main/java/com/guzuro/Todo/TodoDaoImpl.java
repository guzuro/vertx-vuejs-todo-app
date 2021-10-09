package com.guzuro.Todo;

import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlClient;

import java.util.concurrent.CopyOnWriteArrayList;

public class TodoDaoImpl implements TodoDao {

    @Override
    public CopyOnWriteArrayList<Todo> getAllTodos(SqlClient client) {
        client
                .query("select * from todo_item")
                .execute(ar -> {
                    if (ar.succeeded()) {
                        RowSet<Row> result = ar.result();
                        System.out.println("Got " + result.size() + " rows ");
                    } else {
                        System.out.println("Failure: " + ar.cause().getMessage());
                    }
                });
        return null;
    }

    @Override
    public void updateTodo(Todo todo) {

    }

    @Override
    public void deleteTodo(Todo todo) {

    }
}
