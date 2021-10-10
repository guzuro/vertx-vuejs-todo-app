package com.guzuro.DaoFactory;

import com.guzuro.Todo.PostgresTodoDaoImpl;
import com.guzuro.Todo.TodoDao;
import io.vertx.core.Vertx;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlClient;

public class PostgresDAOFactory extends DAOFactory {

    public static SqlClient createConnection(Vertx vertx) {
        PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(5432)
                .setHost("********")
                .setDatabase("*********")
                .setUser("**********")
                .setPassword("*************");

        PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(5);

        SqlClient pgClient = PgPool.client(vertx, connectOptions, poolOptions);
        return pgClient;
    }

    public TodoDao getTodoDAO() {
        return new PostgresTodoDaoImpl();
    }
}