package com.guzuro.Todo;

import com.guzuro.DaoFactory.PostgresDAOFactory;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlClient;

import java.util.concurrent.CopyOnWriteArrayList;

public class PostgresTodoDaoImpl implements TodoDao {

    @Override
    public CopyOnWriteArrayList<Todo> getAllTodos(Vertx vertx) {
        SqlClient pgClient = PostgresDAOFactory.createConnection(vertx);

        pgClient
                .query("SELECT * from todo_item")
                .execute(ar -> {
                    if (ar.succeeded()) {
                        RowSet<Row> result = ar.result();
                        for (Row row : result) {
                            System.out.println(row.toJson());
                        }
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
