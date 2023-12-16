package Server.commands;

import Common.util.Request;
import Common.util.Response;
import Common.util.TextWriter;
import Server.db.DBManager;
import Server.util.CollectionManager;


public class HeadCommand extends AbstractCommand {

    /**
     * Конструктор класса.
     * @param collectionManager менеджер коллекции.
     * @param dbManager менеджер БД.
     */
    public HeadCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("head", "вывести первый элемент коллекции", 0, collectionManager, dbManager);
    }

    /**
     * Сортирует элементы коллекции
     * @param request объект запроса
     * @return объект ответа
     */
    @Override
    public Response execute(Request request) {
        if (collectionManager.getCollection().isEmpty())
            return new Response(TextWriter.getRedText("Коллекция пуста."));
        else {
            return new Response("Первый элемент коллекции:", collectionManager.getFirstElement());
        }
    }
}
