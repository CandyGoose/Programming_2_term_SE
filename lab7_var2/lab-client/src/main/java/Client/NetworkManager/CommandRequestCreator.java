package Client.NetworkManager;

import Client.CommandDispatcher.AvailableCommands;
import Client.CommandDispatcher.CommandToSend;
import Client.CommandDispatcher.CommandValidators;
import Client.util.ScannerManager;
import Common.exception.IncorrectInputInScriptException;
import Common.exception.WrongAmountOfArgumentsException;
import Common.exception.WrongArgException;
import Common.util.Request;
import Common.util.RequestType;
import Common.util.TextWriter;

import java.util.Scanner;

/**
 * Класс для создания запроса по команде, полученной от пользователя.
 */
public class CommandRequestCreator {

    /**
     * Создает запрос по переданной команде.
     * @param command команда
     * @param scanner сканер
     * @param scriptMode режим скрипта
     * @return запрос
     * @throws NullPointerException если команда не найдена
     */
    public Request createRequestOfCommand(CommandToSend command, Scanner scanner, boolean scriptMode) throws NullPointerException {
        String name = command.getCommandName();
        Request request;
        if (AvailableCommands.COMMANDS_WITHOUT_ARGS.contains(name)) {
            request = createRequestWithoutArgs(command);
        } else if (AvailableCommands.COMMANDS_WITH_ID_ARG.contains(name)) {
            request = createRequestWithID(command);
        } else if (AvailableCommands.COMMANDS_WITH_STRING_ARG.contains(name)) {
            request = createRequestWithString(command);
        } else if (AvailableCommands.COMMANDS_WITH_PERSON_ARG.contains(name)) {
            request = createRequestWithPerson(command, scanner, scriptMode);
        } else if (AvailableCommands.COMMANDS_WITH_PERSON_ID_ARGS.contains(name)) {
            request = createRequestWithPersonID(command, scanner, scriptMode);
        } else if (AvailableCommands.SCRIPT_ARGUMENT_COMMAND.contains(name)) {
            request = createRequestWithPersonID(command, scanner, scriptMode);
        } else {
            throw new NullPointerException("Команда не найдена. Напишите 'help' для просмотра всех доступных команд.");
        }
        return request;
    }

    /**
     * Создает запрос без аргументов.
     * @param command команда
     * @return запрос
     */
    private Request createRequestWithoutArgs(CommandToSend command) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 0);
            return new Request(command.getCommandName(), RequestType.COMMAND);
        } catch (WrongAmountOfArgumentsException e) {
            TextWriter.printErr(e.getMessage());
            return null;
        }
    }

    /**
     * Создает запрос с аргументом ID.
     * @param command команда
     * @return запрос
     */
    private Request createRequestWithID(CommandToSend command) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 1);
            int id = CommandValidators.validateArg(arg -> ((int) arg) > 0,
                    "ИД должен быть больше 0.",
                    Integer::parseInt,
                    command.getCommandArgs()[0]);
            return new Request(command.getCommandName(), id, RequestType.COMMAND);
        } catch (WrongAmountOfArgumentsException | WrongArgException e) {
            TextWriter.printErr(e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            TextWriter.printErr("Неправильный тип данных аргумента.");
            return null;
        }
    }

    private Request createRequestWithString(CommandToSend command) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 1);
            String string = command.getCommandArgs()[0];
            return new Request(command.getCommandName(), string, RequestType.COMMAND);
        } catch (WrongAmountOfArgumentsException e) {
            TextWriter.printErr(e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            TextWriter.printErr("Неправильный тип данных аргумента.");
            return null;
        }
    }


    /**
     * Класс для создания запроса с аргументом типа Person.
     * @param command команда
     * @param sc сканер
     * @param scriptMode режим скрипта
     * @return запрос
     */
    private Request createRequestWithPerson(CommandToSend command, Scanner sc, boolean scriptMode) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 0);
            ScannerManager scannerManager = new ScannerManager(sc, scriptMode);
            return new Request(command.getCommandName(), scannerManager.askPerson(), RequestType.COMMAND);
        } catch (WrongAmountOfArgumentsException | IncorrectInputInScriptException e) {
            TextWriter.printErr("Проверьте правильность данных в скрипте. Остановка приложения.");
            System.exit(1);
            return null;
        }
    }

    /**
     * Класс для создания запроса с аргументом типа Person и ID.
     * @param command команда
     * @param sc сканер
     * @param scriptMode режим скрипта
     * @return запрос
     */
    private Request createRequestWithPersonID(CommandToSend command, Scanner sc, boolean scriptMode) {
        try {
            CommandValidators.validateAmountOfArgs(command.getCommandArgs(), 1);
            ScannerManager scannerManager = new ScannerManager(sc, scriptMode);
            int id = CommandValidators.validateArg(arg -> ((int) arg) > 0,
                    "ID должен быть больше 0",
                    Integer::parseInt,
                    command.getCommandArgs()[0]);
            return new Request(command.getCommandName(), id, scannerManager.askPerson(), RequestType.COMMAND);
        } catch (WrongAmountOfArgumentsException | WrongArgException | IllegalArgumentException | IncorrectInputInScriptException e) {
            TextWriter.printErr("Проверьте правильность данных в скрипте. Остановка приложения.");
            System.exit(1);
            return null;
        }
    }
}
