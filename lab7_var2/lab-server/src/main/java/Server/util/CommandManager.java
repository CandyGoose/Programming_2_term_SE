package Server.util;

import Common.util.Request;
import Server.commands.AbstractCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Менеджер команд.
 */
public class CommandManager {

    /**
     * Список доступных команд.
     * Ключ - название команды, значение - объект команды.
     */
    public static final Map<String, AbstractCommand> AVAILABLE_COMMANDS = new HashMap<>();


    public CommandManager(AbstractCommand addCommand, AbstractCommand clearCommand, AbstractCommand executeScriptCommand, AbstractCommand exitCommand, AbstractCommand helpCommand, AbstractCommand infoCommand, AbstractCommand filterStartsWithNameCommand, AbstractCommand averageOfHeightCommand, AbstractCommand removeAnyByHairColorCommand, AbstractCommand removeByIdCommand, AbstractCommand removeFirstCommand, AbstractCommand showCommand, AbstractCommand history, AbstractCommand headCommand, AbstractCommand updateByIdCommand) {
        AVAILABLE_COMMANDS.put(addCommand.getName(), addCommand);
        AVAILABLE_COMMANDS.put(clearCommand.getName(), clearCommand);
        AVAILABLE_COMMANDS.put(executeScriptCommand.getName(), executeScriptCommand);
        AVAILABLE_COMMANDS.put(exitCommand.getName(), exitCommand);
        AVAILABLE_COMMANDS.put(helpCommand.getName(), helpCommand);
        AVAILABLE_COMMANDS.put(infoCommand.getName(), infoCommand);
        AVAILABLE_COMMANDS.put(filterStartsWithNameCommand.getName(), filterStartsWithNameCommand);
        AVAILABLE_COMMANDS.put(averageOfHeightCommand.getName(), averageOfHeightCommand);
        AVAILABLE_COMMANDS.put(removeAnyByHairColorCommand.getName(), removeAnyByHairColorCommand);
        AVAILABLE_COMMANDS.put(showCommand.getName(), showCommand);
        AVAILABLE_COMMANDS.put(removeByIdCommand.getName(), removeByIdCommand);
        AVAILABLE_COMMANDS.put(removeFirstCommand.getName(), removeFirstCommand);
        AVAILABLE_COMMANDS.put(history.getName(), history);
        AVAILABLE_COMMANDS.put(headCommand.getName(), headCommand);
        AVAILABLE_COMMANDS.put(updateByIdCommand.getName(), updateByIdCommand);
    }

    /**
     * Метод для получения объекта команды по имени команды из запроса.
     * @param request запрос от клиента
     * @return объект команды, соответствующий имени команды из запроса
     */
    public AbstractCommand initCommand(Request request) {
        String commandName = request.getCommandName();
        return AVAILABLE_COMMANDS.get(commandName);
    }
}

