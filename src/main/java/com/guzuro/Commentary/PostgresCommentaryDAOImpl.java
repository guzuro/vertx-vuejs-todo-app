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

    @Override
    public CompletableFuture<CopyOnWriteArrayList<Commentary>> getCommentariesByTodoId(Vertx vertx, Number todoId) {
        SqlClient pgClient = PostgresDAOFactory.createConnection(vertx);

        CopyOnWriteArrayList<Commentary> commentaryList = new CopyOnWriteArrayList<>();

        CompletableFuture<CopyOnWriteArrayList<Commentary>> future = new CompletableFuture<>();

        pgClient
                .preparedQuery("SELECT id, text FROM commentary WHERE todo_id = $1")
                .execute(Tuple.of(todoId), ar -> {
                    RowSet<Row> result = ar.result();
                    ObjectMapper objectMapper = new ObjectMapper();
                    for (Row row : result) {
                        try {
                            Commentary commentary = objectMapper.readValue(row.toJson().toString(), Commentary.class);
                            commentaryList.add(commentary);
                        } catch (JsonProcessingException e) {
                            e.getStackTrace();
                        }
                    }
                    System.out.println(commentaryList);
                    future.complete(commentaryList);
                });
        return future;
    }

    @Override
    public Boolean removeCommentaryById(Number id) {
        return null;
    }
}
