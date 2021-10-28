package com.guzuro.Commentary;

import com.guzuro.DaoFactory.PostgresDAOFactory;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlClient;
import io.vertx.sqlclient.Tuple;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public class PostgresCommentaryDAOImpl implements CommentaryDao {

    SqlClient pgClient;

    public PostgresCommentaryDAOImpl(Vertx vertx) {
        pgClient = PostgresDAOFactory.createConnection(vertx);
    }

    @Override
    public CompletableFuture<CopyOnWriteArrayList<Commentary>> getCommentariesByTodoId(Number todoId) {

        CopyOnWriteArrayList<Commentary> commentaryList = new CopyOnWriteArrayList<>();
        CompletableFuture<CopyOnWriteArrayList<Commentary>> future = new CompletableFuture<>();

        pgClient
                .preparedQuery("SELECT id, text, todo_id, created_at FROM commentary WHERE todo_id=$1")
                .execute(Tuple.of(todoId), ar -> {
                    if (ar.succeeded()) {
                        RowSet<Row> result = ar.result();
                        for (Row row : result) {
                            String time = row.toJson().getString("created_at");
                            LocalDateTime created_at = null;
                            if (time != null) {
                                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                                created_at = LocalDateTime.parse(time, formatter);
                            }
                            Number id = row.toJson().getNumber("id");
                            Number todo_id = row.toJson().getNumber("todo_id");
                            String text = row.toJson().getString("text");
                            commentaryList.add(new Commentary(id, text, todo_id, created_at));
                        }
                        future.complete(commentaryList);
                    } else {
                        future.completeExceptionally(ar.cause());
                    }
                });
        return future;
    }

    @Override
    public CompletableFuture<Commentary> addCommentary(Commentary commentary) {
        CompletableFuture<Commentary> future = new CompletableFuture<>();

        pgClient
                .preparedQuery("INSERT INTO commentary(text, todo_id, created_at) VALUES($1, $2, $3) returning id, text, todo_id, created_at")
                .execute(Tuple.of(commentary.getText(), commentary.getTodo_id(), commentary.getCreated_at()), ar -> {
                    if (ar.succeeded()) {
                        Row result = ar.result().iterator().next();

                        String time = result.toJson().getString("created_at");
                        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

                        LocalDateTime created_at = LocalDateTime.parse(time, formatter);
                        Number id = result.toJson().getNumber("id");
                        Number todo_id = result.toJson().getNumber("todo_id");
                        String text = result.toJson().getString("text");

                        future.complete(new Commentary(id, text, todo_id, created_at));
                    } else {
                        future.completeExceptionally(ar.cause());
                    }
                });
        return future;
    }

    @Override
    public CompletableFuture<Boolean> removeCommentaryById(Number commentId) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        pgClient
                .preparedQuery("DELETE FROM commentary WHERE id=$1")
                .execute(Tuple.of(commentId), ar -> {
                    if (ar.succeeded()) {
                        future.complete(true);
                    } else {
                        future.completeExceptionally(ar.cause());
                    }
                });
        return future;
    }
}
