package Server.commands;

import Common.exception.DatabaseException;
import Common.util.Request;
import Common.util.Response;
import Common.util.TextWriter;
import Server.db.DBManager;
import Server.util.CollectionManager;

public class FilterStartsWithNameCommand extends AbstractCommand {

    public FilterStartsWithNameCommand(CollectionManager collectionManager) {
        super("filter_starts_with_name", "вывести элементы, значение поля name которых начинается с заданной подстроки", 1);
        this.collectionManager = collectionManager;
    }

    /**
     * Выполнение команды.
     * @param request объект запроса.
     * @return объект ответа.
     */
    @Override
    public Response execute(Request request) {
        try {
            if (collectionManager.getCollection().isEmpty()) {
                return new Response("Коллекция пуста.");
            } else {
                return new Response(TextWriter.getWhiteText("Элементы, значение поля name которых начинается с заданной подстроки: " +
                        collectionManager.getFilterName(request.getTextArgument())));
            }

        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
}

