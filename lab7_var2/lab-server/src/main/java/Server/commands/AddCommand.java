package Server.commands;


import Common.exception.DatabaseException;
import Common.data.Person;
import Common.util.Request;
import Common.util.Response;
import Server.db.DBManager;
import Server.util.CollectionManager;

/**
 * Класс команды "add" - добавление новой персоны.
 */
public class AddCommand extends AbstractCommand {

    /**
     * Конструктор класса.
     * @param collectionManager менеджер коллекции.
     * @param dbManager менеджер БД.
     */
    public AddCommand(CollectionManager collectionManager, DBManager dbManager) {
        super("add", "добавить новый элемент в коллекцию", 0, collectionManager, dbManager);
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
                Person personToAdd = request.getPersonArgument();
                Integer id = dbManager.addElement(personToAdd, request.getLogin());
                personToAdd.setId(id);
                collectionManager.addToCollection(personToAdd);
                return new Response("Элемент был успешно добавлен с ИД: " + id);
            } else {
                return new Response("Несоответствие логина и пароля.");
            }
        } catch (DatabaseException e) {
            return new Response(e.getMessage());
        }
    }

}
