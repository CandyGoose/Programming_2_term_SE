package Server.commands;

import Common.exception.DatabaseException;
import Common.util.Request;
import Common.util.Response;
import Server.db.DBManager;
import Server.util.CollectionManager;

import java.util.List;

public class RemoveAnyByHairColorCommand extends AbstractCommand {

    /**
     * Конструктор класса.
     * @param collectionManager менеджер коллекции.
     * @param dbManager менеджер БД.
     */
    public RemoveAnyByHairColorCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("remove_any_by_hair_color", "удалить из коллекции один элемент, значение поля hairColor которого эквивалентно заданному", 1,
                collectionManager, dbManager);
    }

    /**
     * Выполнение команды.
     * @param request объект запроса.
     * @return объект ответа.
     */
    @Override
    public Response execute(Request request) {
        try {
            if (dbManager.validateUser(request.getLogin(), request.getPassword())) {
                List<Integer> ids = dbManager.getIdsOfUsersElements(request.getLogin());
                if (dbManager.removeByHair(request.getTextArgument().toUpperCase(), request.getLogin())) {
                    collectionManager.removeByHair(request.getTextArgument(), ids);

                    return new Response("Элемент с цветом " + request.getTextArgument() + " был удален из коллекции.");
                } else {
                    return new Response("Элементов не существует.");
                }
            } else {
                return new Response("Несоответствие логина и пароля.");
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }
}