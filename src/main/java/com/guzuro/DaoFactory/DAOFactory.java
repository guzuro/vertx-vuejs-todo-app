package com.guzuro.DaoFactory;

import com.guzuro.Todo.TodoDao;

public abstract class DAOFactory {

    public static final int POSTGRES = 1;

    public abstract TodoDao getTodoDAO();

    public static DAOFactory getDAOFactory(int factoryType) {
        switch (factoryType) {
            case POSTGRES:
                return new PostgresDAOFactory();
            default:
                return null;
        }
    }
}
