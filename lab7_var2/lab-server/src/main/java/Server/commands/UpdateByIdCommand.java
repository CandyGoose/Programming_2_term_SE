package Server.commands;

import Common.exception.DatabaseException;
import Common.data.Person;import Common.util.Request;
import Common.util.Response;
import Server.db.DBManager;
import Server.util.CollectionManager;

/**
 * Команда для обновления значения элемента коллекции, идентификатор которого равен заданному.
 */
public class UpdateByIdCommand extends AbstractCommand {

    /**
     * Конструктор класса.
     * @param collectionManager менеджер коллекции.
     * @param dbManager менеджер БД.
     */
    public UpdateByIdCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("update", "обновить значение элемента коллекции, id которого равен заданному", 1,
                collectionManager, dbManager);
    }

    /**
     * Выполнение команды на сервере.
     * @param request запрос, полученный от клиента.
     * @return ответ клиенту.
     */
    @Override
    public Response execute(Request request) {
        try {
            if (dbManager.validateUser(request.getLogin(), request.getPassword())) {
                Integer id = request.getNumericArgument();
                if (dbManager.checkPersonExistence(id)) {
                    if (dbManager.updateById(request.getPersonArgument(), id, request.getLogin())) {
                        Person updatedPerson = request.getPersonArgument();
                        updatedPerson.setId(id);
                        collectionManager.removeById(id);
                        collectionManager.addToCollection(request.getPersonArgument());
                        return new Response("Элемент с ИД " + id + " был успешно обновлен.");
                    } else {
                        return new Response("Элемент был создан другим пользователем. У вас нет прав для его обновления.");
                    }
                } else {
                    return new Response("Нет элемента с таким идентификатором.");
                }
            } else {
                return new Response("Несоответствие логина и пароля.");
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }
}
