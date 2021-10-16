package com.guzuro;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guzuro.Commentary.Commentary;
import com.guzuro.Commentary.CommentaryDao;
import com.guzuro.DaoFactory.DAOFactory;
import com.guzuro.Todo.Todo;
import com.guzuro.Todo.TodoDao;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {

    final DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);

    @Override
    public void start(Promise<Void> startPromise) {
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        final TodoDao todoDAO = postgresFactory.getTodoDAO(vertx);
        final CommentaryDao commentaryDAO = postgresFactory.getCommentaryDAO(vertx);

        router.get("/").handler(routingContext -> {
            todoDAO.getAllTodos().thenAccept(resTodos -> {
                String todosJsonString = null;
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    todosJsonString = objectMapper.writeValueAsString(resTodos);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=UTF-8")
                        .end(todosJsonString);
            });
        });

        router.post("/").handler(BodyHandler.create()).handler(routingContext -> {
            String todoTitle = routingContext.getBodyAsJson().getString("title");
            String todoDescription = routingContext.getBodyAsJson().getString("description");
            Todo requestTodo = new Todo(null, todoTitle, todoDescription);

            todoDAO.createTodo(requestTodo).thenAccept(resTodo -> {
                String todosJsonString = null;
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    todosJsonString = objectMapper.writeValueAsString(resTodo);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=UTF-8")
                        .setStatusCode(201)
                        .end(todosJsonString);
            });
        });

        router.put("/:id").handler(BodyHandler.create()).handler(routingContext -> {
            Number todoId = Integer.parseInt(routingContext.pathParam("id"));
            String todoTitle = routingContext.getBodyAsJson().getString("title");
            String todoDescription = routingContext.getBodyAsJson().getString("description");

            Todo todoToUpdate = new Todo(todoId, todoTitle, todoDescription);

            todoDAO.updateTodo(todoToUpdate).thenAccept(resTodo -> {
                String todoJsonString = null;
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    todoJsonString = objectMapper.writeValueAsString(resTodo);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=UTF-8")
                        .end(todoJsonString);
            });
        });

        router.delete("/:id").handler(routingContext -> {
            Number todoId = Integer.parseInt(routingContext.pathParam("id"));
            todoDAO.deleteTodo(todoId).thenAccept(result -> {
                JsonObject response = new JsonObject();
                response.put("result", "ok");
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=UTF-8")
                        .end(response.toString());
            });
        });

        router.post("/commentaries").handler(BodyHandler.create()).handler(routingContext -> {
            if (routingContext.getBodyAsJson().containsKey("todo_id")) {
                Number todoId = Integer.parseInt(routingContext.getBodyAsJson().getString("todo_id"));

                commentaryDAO.getCommentariesByTodoId(todoId).thenAccept(resComment -> {
                    String commentariesJson = null;
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        commentariesJson = objectMapper.writeValueAsString(resComment);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    routingContext.response()
                            .putHeader("content-type", "application/json; charset=UTF-8")
                            .end(commentariesJson);
                });
            }
        });

        router.post("/addcommentary").handler(BodyHandler.create()).handler(routingContext -> {
            if (routingContext.getBodyAsJson().containsKey("text") && routingContext.getBodyAsJson().containsKey("todo_id")) {
                String commentaryText = routingContext.getBodyAsJson().getString("text");
                Number todoId = Integer.parseInt(routingContext.getBodyAsJson().getString("todo_id"));

                Commentary commentary = new Commentary(null, commentaryText, todoId);
                commentaryDAO.addCommentary(commentary).thenAccept(resComment -> {
                    String commentaryJson = null;
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        commentaryJson = objectMapper.writeValueAsString(resComment);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    routingContext.response()
                            .putHeader("content-type", "application/json; charset=UTF-8")
                            .end(commentaryJson);
                });
            }
        });

        router.delete("/commentary/:id").handler(routingContext -> {
            Number commenId = Integer.parseInt(routingContext.pathParam("id"));

            commentaryDAO.removeCommentaryById(commenId).thenAccept(result -> {
                JsonObject response = new JsonObject();
                response.put("result", "ok");
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=UTF-8")
                        .end(response.toString());
            });
        });


        server.requestHandler(router).listen(8080, httpServerAsyncResult -> {
            if (httpServerAsyncResult.succeeded()) {
                startPromise.complete();
                System.out.println("HTTP server started on port 8080");
            } else {
                startPromise.fail(httpServerAsyncResult.cause());
            }
        });
    }
}
