package com.guzuro;

import com.guzuro.Commentary.Commentary;
import com.guzuro.Commentary.CommentaryDao;
import com.guzuro.DaoFactory.DAOFactory;
import com.guzuro.Todo.Todo;
import com.guzuro.Todo.TodoDao;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.HashSet;
import java.util.Set;

public class MainVerticle extends AbstractVerticle {

    final DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);

    @Override
    public void start(Promise<Void> startPromise) {
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        final TodoDao todoDAO = postgresFactory.getTodoDAO(vertx);
        final CommentaryDao commentaryDAO = postgresFactory.getCommentaryDAO(vertx);

        Set<HttpMethod> allowedMethods = new HashSet<>();
        allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.OPTIONS);
        allowedMethods.add(HttpMethod.DELETE);
        allowedMethods.add(HttpMethod.PUT);

        router.route().handler(CorsHandler.create(".*.").allowedMethods(allowedMethods));

        router.get("/").handler(routingContext -> {
            todoDAO.getAllTodos().thenAccept(resTodos -> {
                JsonArray todosJson = new JsonArray();
                resTodos.forEach(todosJson::add);
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=UTF-8")
                        .end(todosJson.encodePrettily());
            }).exceptionally(throwable -> {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=UTF-8")
                        .end(throwable.getCause().getMessage());
                return null;
            });
        });

        router.post("/").handler(BodyHandler.create()).handler(routingContext -> {
            String todoTitle = routingContext.getBodyAsJson().getString("title");
            String todoDescription = routingContext.getBodyAsJson().getString("description");

            Todo requestTodo = new Todo(null, todoTitle, todoDescription);

            todoDAO.createTodo(requestTodo).thenAccept(resTodo -> {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=UTF-8")
                        .setStatusCode(201)
                        .end(JsonObject.mapFrom(resTodo).encodePrettily());
            }).exceptionally(throwable -> {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=UTF-8")
                        .end(throwable.getCause().getMessage());
                return null;
            });
        });

        router.put("/:id").handler(BodyHandler.create()).handler(routingContext -> {
            Number todoId = Integer.parseInt(routingContext.pathParam("id"));
            String todoTitle = routingContext.getBodyAsJson().getString("title");
            String todoDescription = routingContext.getBodyAsJson().getString("description");

            Todo todoToUpdate = new Todo(todoId, todoTitle, todoDescription);

            todoDAO.updateTodo(todoToUpdate).thenAccept(resTodo -> {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=UTF-8")
                        .end(JsonObject.mapFrom(resTodo).encodePrettily());
            }).exceptionally(throwable -> {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=UTF-8")
                        .end(throwable.getCause().getMessage());
                return null;
            });
        });

        router.delete("/:id").handler(routingContext -> {
            Number todoId = Integer.parseInt(routingContext.pathParam("id"));
            todoDAO.deleteTodo(todoId).thenAccept(result -> {
                if (result) {
                    routingContext.response()
                            .putHeader("content-type", "application/json; charset=UTF-8")
                            .setStatusCode(200)
                            .end();
                } else {
                    routingContext.response()
                            .setStatusCode(404)
                            .setStatusMessage("NOT FOUND")
                            .end();
                }
            }).exceptionally(throwable -> {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=UTF-8")
                        .end(throwable.getCause().getMessage());
                return null;
            });
        });

        router.post("/commentaries").handler(BodyHandler.create()).handler(routingContext -> {
            if (routingContext.getBodyAsJson().containsKey("todo_id")) {
                Number todoId = Integer.parseInt(routingContext.getBodyAsJson().getString("todo_id"));

                commentaryDAO.getCommentariesByTodoId(todoId).thenAccept(resComment -> {
                    JsonArray commentariesJson = new JsonArray();
                    resComment.forEach(commentary -> commentariesJson.add(JsonObject.mapFrom(commentary)));
                    routingContext.response()
                            .putHeader("content-type", "application/json; charset=UTF-8")
                            .end(commentariesJson.encodePrettily());
                }).exceptionally(throwable -> {
                    routingContext.response()
                            .putHeader("content-type", "application/json; charset=UTF-8")
                            .end(throwable.getCause().getMessage());
                    return null;
                });
            }
        });

        router.post("/addcommentary").handler(BodyHandler.create()).handler(routingContext -> {
            if (routingContext.getBodyAsJson().containsKey("text") && routingContext.getBodyAsJson().containsKey("todo_id")) {
                commentaryDAO.addCommentary(JsonObject.mapFrom(routingContext.getBodyAsJson()).mapTo(Commentary.class)).thenAccept(resComment -> {
                    routingContext.response()
                            .putHeader("content-type", "application/json; charset=UTF-8")
                            .end(JsonObject.mapFrom(resComment).encodePrettily());
                }).exceptionally(throwable -> {
                    routingContext.response()
                            .putHeader("content-type", "application/json; charset=UTF-8")
                            .end(throwable.getCause().getMessage());
                    return null;
                });
            }
        });

        router.delete("/commentary/:id").handler(routingContext -> {
            Number commentId = Integer.parseInt(routingContext.pathParam("id"));
            commentaryDAO.removeCommentaryById(commentId).thenAccept(result -> {

                if (result) {
                    routingContext.response()
                            .putHeader("content-type", "application/json; charset=UTF-8")
                            .setStatusCode(200)
                            .end();
                } else {
                    routingContext.response()
                            .setStatusCode(404)
                            .setStatusMessage("NOT FOUND")
                            .end();
                }
            }).exceptionally(throwable -> {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=UTF-8")
                        .end(throwable.getCause().getMessage());
                return null;
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
