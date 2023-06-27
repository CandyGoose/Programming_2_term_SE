package client;

import javafx.application.Application;


/**
 * Главный класс для запуска клиента.
 */
public final class Client {
    private Client() {
        throw new UnsupportedOperationException("Это служебный класс, и его экземпляр не может быть создан");
    }

    /**
     * Главный метод, запускающий клиент.
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        Application.launch(GraphicClient.class);
    }
}
