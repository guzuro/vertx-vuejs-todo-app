package com.guzuro.Todo;

import com.guzuro.DaoFactory.PostgresDAOFactory;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlClient;

import java.util.concurrent.CopyOnWriteArrayList;

public class PostgresTodoDaoImpl implements TodoDao {

    @Override
    public Future<Object> getAllTodos(Vertx vertx) {
        SqlClient pgClient = PostgresDAOFactory.createConnection(vertx);
        CopyOnWriteArrayList<JsonObject> todoList = new CopyOnWriteArrayList<JsonObject>();

        return vertx.executeBlocking(promise -> {
            pgClient
                    .query("SELECT * from todo_item")
                    .execute(ar -> {
                        if (ar.succeeded()) {
                            RowSet<Row> result = ar.result();
                            for (Row row : result) {
                                todoList.add(row.toJson());
                                System.out.println(todoList);
                            }
                            promise.complete(todoList);
                        } else {
                            System.out.println("Failure: " + ar.cause().getMessage());
                        }
                    });
        });
    }

    @Override
    public void updateTodo(Todo todo) {

    }

    @Override
    public void deleteTodo(Todo todo) {

    }
}
