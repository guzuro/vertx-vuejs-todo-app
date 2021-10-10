package com.guzuro;

import com.guzuro.DaoFactory.DAOFactory;
import com.guzuro.Todo.PostgresTodoDaoImpl;
import com.guzuro.Todo.TodoDao;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

    final DAOFactory postgresFactory = DAOFactory.getDAOFactory(DAOFactory.POSTGRES);
    final TodoDao todoDAO = postgresFactory.getTodoDAO();

    @Override
    public void start(Promise<Void> startPromise) {
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        TodoDao todoDao = new PostgresTodoDaoImpl();
        router.get("/todos").handler(routingContext -> {
            todoDAO.getAllTodos(vertx);
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
