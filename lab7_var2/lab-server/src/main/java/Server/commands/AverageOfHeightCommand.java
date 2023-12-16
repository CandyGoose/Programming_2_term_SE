package Server.commands;

import Common.util.Request;
import Common.util.Response;
import Common.util.TextWriter;
import Server.util.CollectionManager;


public class AverageOfHeightCommand extends AbstractCommand {
    /**
     Менеджер коллекции.
     */
    private final CollectionManager collectionManager;

    /**
     * Создает новый объект команды.
     * @param collectionManager менеджер коллекции
     */
    public AverageOfHeightCommand(CollectionManager collectionManager) {
        super("average_of_height", "вывести среднее значение поля height для всех элементов коллекции", 0);
        this.collectionManager = collectionManager;
    }


    @Override
    public Response execute(Request request) {
        if (collectionManager.getCollection().isEmpty())
            return new Response(TextWriter.getRedText("Коллекция пуста."));
        else {
            return new Response("Среднее значение height: " + collectionManager.getAverageHeight());
        }
    }
}
