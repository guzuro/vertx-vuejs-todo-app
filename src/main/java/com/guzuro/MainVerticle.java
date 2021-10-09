package com.guzuro;

import com.guzuro.Todo.TodoDao;
import com.guzuro.Todo.TodoDaoImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlClient;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        System.out.println("start");
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(5432)
                .setHost("******")
                .setDatabase("*******")
                .setUser("********")
                .setPassword("*********");

        // Pool options
        PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(5);

        // Create the client pool
        SqlClient client = PgPool.client(vertx, connectOptions, poolOptions);

        TodoDao todoDao = new TodoDaoImpl();
        router.get("/todos").handler(routingContext -> {
            todoDao.getAllTodos(client);
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
