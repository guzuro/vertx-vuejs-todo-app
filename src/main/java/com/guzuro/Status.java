package com.guzuro;

public enum Status {
    Новый("Новый"),
    ВРаботе("В работе"),
    Тестируется("Тестируется"),
    Заверщена("Завершена");


    private final String statusTitle;

    Status(String statusTitle) {
        this.statusTitle = statusTitle;
    }

    public String getStatus() {
        return statusTitle;
    }

}
