package com.guzuro.Commentary;

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
                .preparedQuery("SELECT id, text, todo_id FROM commentary WHERE todo_id=$1")
                .execute(Tuple.of(todoId), ar -> {
                    if (ar.succeeded()) {
                        RowSet<Row> result = ar.result();
                        ObjectMapper objectMapper = new ObjectMapper();
                        for (Row row : result) {
                            try {
                                Commentary commentary = objectMapper.readValue(row.toJson().toString(), Commentary.class);
                                commentaryList.add(commentary);
                            } catch (JsonProcessingException e) {
                                future.completeExceptionally(e.getCause());
                            }
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
                .preparedQuery("INSERT INTO commentary(text, todo_id) VALUES($1, $2) returning id, text, todo_id")
                .execute(Tuple.of(commentary.getText(), commentary.getTodo_id()), ar -> {
                    if (ar.succeeded()) {
                        Row result = ar.result().iterator().next();
                        Commentary commentFromDb = null;
                        try {
                            ObjectMapper objectMapper = new ObjectMapper();
                            commentFromDb = objectMapper.readValue(result.toJson().toString(), Commentary.class);
                            future.complete(commentFromDb);
                        } catch (JsonProcessingException e) {
                            future.completeExceptionally(e.getCause());
                        }
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
