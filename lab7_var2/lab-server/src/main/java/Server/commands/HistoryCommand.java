package Server.commands;

import Common.util.Request;
import Common.util.Response;
import Common.util.TextWriter;
import Server.db.DBManager;
import Server.util.CollectionManager;

public class HistoryCommand extends AbstractCommand {

    public HistoryCommand() {
        super("history", "вывести последние 14 команд (без их аргументов)", 0);
    }

    /**
     * Перемешивает элементы коллекции
     * @param request объект запроса
     * @return объект ответа
     */
    @Override
    public Response execute(Request request) {
        return new Response(TextWriter.getRedText("История команд."));
    }
}