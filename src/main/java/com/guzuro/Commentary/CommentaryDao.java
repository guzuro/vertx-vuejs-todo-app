package com.guzuro.Commentary;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;


public interface CommentaryDao {
    CompletableFuture<CopyOnWriteArrayList<Commentary>> getCommentariesByTodoId(Number todoId);

    CompletableFuture<Commentary> addCommentary(Commentary commentary);

    CompletableFuture<Boolean> removeCommentaryById(Number id);
}
