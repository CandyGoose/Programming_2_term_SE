package Server;

import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;

import Common.data.Person;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import org.slf4j.LoggerFactory;

import Common.exception.DatabaseException;
import Server.commands.*;
import Server.db.DBConnector;
import Server.db.DBManager;
import Server.db.UsersManager;
import Server.interfaces.DBConnectable;
import Server.util.CollectionManager;
import Server.util.CommandManager;

/**
 Класс, представляющий конфигурацию сервера.
 */
public class ServerConfig {

    /**
     Переменная, указывающая на то, работает ли сервер.
     */
    public static boolean isRunning = true;

    /**
     Объект класса Scanner для считывания ввода пользователя.
     */
    public static final Scanner scanner = new Scanner(System.in);

    /**
     Объект класса CollectionManager для работы с коллекцией элементов.
     */
    public static CollectionManager collectionManager = new CollectionManager();

    /**
     Порт, используемый сервером.
     */
    public static int PORT = 65435;

    /**
     Объект класса DBManager для работы с базой данных.
     */
    public static DBManager dbManager = new DBManager(new DBConnector());

    /**
     Объект класса UsersManager для работы с пользователями.
     */
    public static UsersManager usersManager = new UsersManager(dbManager);

    /**
     * Логгер для записи сообщений о работе сервера.
     */
    public static final Logger logger =  (Logger) LoggerFactory.getLogger(Server.class.getName());
    static {
        try {
            Logger logbackLogger = logger;
            LoggerContext loggerContext = logbackLogger.getLoggerContext();

            FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
            fileAppender.setFile("logs/log.log");
            fileAppender.setAppend(true);

            PatternLayoutEncoder encoder = new PatternLayoutEncoder();
            encoder.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
            encoder.setContext(loggerContext);
            encoder.start();

            fileAppender.setEncoder(encoder);
            fileAppender.setContext(loggerContext);
            fileAppender.start();

            logbackLogger.addAppender(fileAppender);
        } catch (SecurityException e) {
            ServerConfig.logger.warn("Отказано в доступе при открытии файла журнала: " + e.getMessage());
        } catch (Exception e) {
            ServerConfig.logger.warn("Произошла ошибка при открытии обработчика файла журнала: " + e.getMessage());
        }
    }

    static {
        try {
            collectionManager.setPersonCollection((ConcurrentLinkedDeque<Person>) dbManager.loadCollection());
        } catch (DatabaseException e) {
            logger.error(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Объект класса CommandManager для работы с командами.
     */
    public static CommandManager commandManager = new CommandManager(
            new AddCommand(collectionManager, dbManager),
            new ClearCommand(collectionManager, dbManager),
            new ExecuteScriptCommand(),
            new ExitCommand(),
            new HelpCommand(),
            new InfoCommand(collectionManager),
            new FilterStartsWithNameCommand(collectionManager),
            new AverageOfHeightCommand(collectionManager),
            new RemoveAnyByHairColorCommand(collectionManager, dbManager),
            new RemoveByIdCommand(collectionManager, dbManager),
            new RemoveFirstCommand(collectionManager, dbManager),
            new ShowCommand(collectionManager, dbManager),
            new HistoryCommand(),
            new HeadCommand(collectionManager, dbManager),
            new UpdateByIdCommand(collectionManager, dbManager)
    );

    /**
     * Метод для изменения переменной isRunning, указывающего на то, работает ли сервер.
     */
    public static void toggleStatus() {
        isRunning = !isRunning;
    }
}
