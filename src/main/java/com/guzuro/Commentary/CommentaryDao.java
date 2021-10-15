package com.guzuro.Commentary;

import io.vertx.core.Vertx;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;


public interface CommentaryDao {
    CompletableFuture<CopyOnWriteArrayList<Commentary>> getCommentariesByTodoId(Number todoId);

    Boolean removeCommentaryById(Number id);
}
