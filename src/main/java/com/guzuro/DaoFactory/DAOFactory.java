package com.guzuro.DaoFactory;

import com.guzuro.Commentary.CommentaryDao;
import com.guzuro.Todo.TodoDao;
import io.vertx.core.Vertx;

public abstract class DAOFactory {

    public static final int POSTGRES = 1;

    public abstract TodoDao getTodoDAO(Vertx vertx);
    public abstract CommentaryDao getCommentaryDAO(Vertx vertx);

    public static DAOFactory getDAOFactory(int factoryType) {
        switch (factoryType) {
            case POSTGRES:
                return new PostgresDAOFactory();
            default:
                return null;
        }
    }
}
