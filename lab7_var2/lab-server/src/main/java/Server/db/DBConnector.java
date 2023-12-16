package Server.db;

import Common.exception.DatabaseException;
import Server.ServerConfig;
import Server.interfaces.DBConnectable;
import Server.interfaces.SQLConsumer;
import Server.interfaces.SQLFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;


/**
 * Класс для соединения с базой данных.
 */
public class DBConnector implements DBConnectable {

    /**
     * Имя базы данных.
     */
    private final String dbName =  System.getenv("SV_DB"); // studs

    /**
     * Имя пользователя базы данных.
     */
    private final String user =  System.getenv("SV_LOGIN"); // sXXXXXX

    /**
     * Пароль пользователя базы данных.
     */
    private final String pass =  System.getenv("SV_PASS"); // пароль из ./pgpass

    /**
     * URL-адрес для подключения к базе данных.
     */
    private final String dbUrl = "jdbc:postgresql://127.0.0.1:5432/" + dbName;

    /**
     * Конструктор класса DBConnector.
     */
    public DBConnector() {
        try {
            Class.forName("org.postgresql.Driver");
            initializeDB();
        } catch (ClassNotFoundException e) {
            ServerConfig.logger.error("Нет драйвера БД.");
            System.exit(1);
        } catch (SQLException e) {
            ServerConfig.logger.warn("Произошла ошибка при инициализации таблиц.");
            System.exit(1);
        }
    }

    /**
     * Обрабатывает запросы без возвращаемого значения.
     * @param queryBody тело запроса.
     * @throws DatabaseException в случае ошибки при работе с базой данных.
     */
    public void handleQuery(SQLConsumer<Connection> queryBody) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, user, pass)) {
            queryBody.accept(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при работе с БД: " + Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Обрабатывает запросы с возвращаемым значением.
     * @param queryBody тело запроса.
     * @param <T> тип возвращаемого значения.
     * @return результат выполнения запроса.
     * @throws DatabaseException в случае ошибки при работе с базой данных.
     */
    public <T> T handleQuery(SQLFunction<Connection, T> queryBody) throws DatabaseException {
        try (Connection connection = DriverManager.getConnection(dbUrl, user, pass)) {
            return queryBody.apply(connection);
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при работе с БД: " + Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Создает таблицы в базе данных, если они не существуют.
     * @throws SQLException в случае ошибки при создании таблиц.
     */
    private void initializeDB() throws SQLException {

        Connection connection = DriverManager.getConnection(dbUrl, user, pass);

        Statement statement = connection.createStatement();

        statement.execute("CREATE SEQUENCE IF NOT EXISTS persons_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1");

        statement.execute("CREATE SEQUENCE IF NOT EXISTS users_id_seq INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1");

        statement.execute("CREATE TABLE IF NOT EXISTS users "
                + "("
                + "login varchar(255) NOT NULL UNIQUE CHECK(login<>''),"
                + "password varchar(255) NOT NULL CHECK(password<>''),"
                + "id int NOT NULL PRIMARY KEY DEFAULT nextval('users_id_seq')"
                + ");");

        statement.execute("CREATE TABLE IF NOT EXISTS persons "
                + "("
                + "id int NOT NULL PRIMARY KEY DEFAULT nextval('persons_id_seq'),"
                + "creation_date date NOT NULL,"
                + "name varchar(50) NOT NULL CHECK(name <> ''),"
                + "x float NOT NULL,"
                + "y float NOT NULL,"
                + "height float NOT NULL CHECK(height > 0),"
                + "eye_color varchar(6) NOT NULL CHECK(eye_color = 'GREEN' "
                + "OR eye_color = 'RED' "
                + "OR eye_color = 'YELLOW' "
                + "OR eye_color = 'ORANGE' "
                + "OR eye_color = 'BROWN'),"
                + "hair_color varchar(6) NOT NULL CHECK(hair_color = 'RED' "
                + "OR hair_color = 'BLACK' "
                + "OR hair_color = 'ORANGE'),"
                + "nationality varchar(11) NOT NULL CHECK(nationality = 'CHINA' "
                + "OR nationality = 'INDIA' "
                + "OR nationality = 'THAILAND' "
                + "OR nationality = 'SOUTH_KOREA'),"
                + "xLoc bigint NOT NULL,"
                + "yLoc int NOT NULL,"
                + "zLoc bigint NOT NULL,"
                + "owner_id bigint NOT NULL REFERENCES users (id)"
                + ");");

        connection.close();
    }
}

