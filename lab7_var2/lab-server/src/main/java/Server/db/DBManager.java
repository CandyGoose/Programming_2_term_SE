package Server.db;

import Common.exception.DatabaseException;
import Common.data.*;
import Server.interfaces.DBConnectable;
import Server.util.Encryptor;

import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;


public class DBManager {

    private final DBConnectable dbConnector;

    public DBManager(DBConnectable dbConnector) {
        this.dbConnector = dbConnector;
    }

    public ConcurrentLinkedDeque<Person> loadCollection() throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String selectCollectionQuery = "SELECT * FROM persons";
            Statement statement = connection.createStatement();
            ResultSet collectionSet = statement.executeQuery(selectCollectionQuery);
            ConcurrentLinkedDeque<Person> resultDeque = new ConcurrentLinkedDeque<>();
            while (collectionSet.next()) {
                String creationDate = String.valueOf(collectionSet.getDate("creation_date"));
                LocalDate localDate = LocalDate.parse(creationDate);
                LocalTime localTime = LocalTime.NOON;
                LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
                ZoneId zoneId = ZoneId.of("Europe/Moscow");
                ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
                Coordinates coordinates = new Coordinates(
                        collectionSet.getFloat("x"),
                        collectionSet.getFloat("y")
                );
                ColorEye colorEye = ColorEye.valueOf(collectionSet.getString("eye_color"));
                ColorHair colorHair = ColorHair.valueOf(collectionSet.getString("hair_color"));
                Country nationality = Country.valueOf(collectionSet.getString("nationality"));
                Location location = new Location(
                        collectionSet.getDouble("xLoc"),
                        collectionSet.getInt("yLoc"),
                        collectionSet.getDouble("zLoc")
                );
                Person person = new Person(
                        collectionSet.getInt("id"),
                        collectionSet.getString("name"),
                        coordinates,
                        zonedDateTime,
                        collectionSet.getFloat("height"),
                        colorEye,
                        colorHair,
                        nationality,
                        location
                );
                resultDeque.add(person);
            }
            return resultDeque;
        });
    }

    public Integer addElement(Person person, String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String addElementQuery = "INSERT INTO persons "
                    + "(creation_date, name, x, y, height, eye_color, hair_color, nationality, xLoc, yLoc, zLoc, owner_id) "
                    + "SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, id "
                    + "FROM users "
                    + "WHERE users.login = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(addElementQuery,
                    Statement.RETURN_GENERATED_KEYS);

            Coordinates coordinates = person.getCoordinates();
            Location location = person.getLocation();

            ZonedDateTime zonedDateTime = person.getCreationDate();
            LocalDate localDate = zonedDateTime.toLocalDate();
            java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);

            preparedStatement.setDate(1, sqlDate);
            preparedStatement.setString(2, person.getName());
            preparedStatement.setFloat(3, coordinates.getX());
            preparedStatement.setFloat(4, coordinates.getY());
            preparedStatement.setFloat(5, person.getHeight());
            preparedStatement.setString(6, person.getEyeColor().toString());
            preparedStatement.setString(7, person.getHairColor().toString());
            preparedStatement.setString(8, person.getCountry().toString());
            preparedStatement.setDouble(9, location.getX());
            preparedStatement.setInt(10, location.getY());
            preparedStatement.setDouble(11, location.getZ());
            preparedStatement.setString(12, username);

            preparedStatement.executeUpdate();
            ResultSet result = preparedStatement.getGeneratedKeys();
            result.next();

            return result.getInt(1);
        });
    }

    public boolean checkPersonExistence(Integer id) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String existenceQuery = "SELECT COUNT (*) "
                    + "FROM persons "
                    + "WHERE persons.id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(existenceQuery);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return resultSet.getInt("count") > 0;
        });
    }


    public boolean removeById(Integer id, String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String removeQuery = "DELETE FROM persons "
                    + "USING users "
                    + "WHERE persons.id = ? "
                    + "AND persons.owner_id = users.id AND users.login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(removeQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, username);

            int deletedBands = preparedStatement.executeUpdate();
            return deletedBands > 0;
        });
    }

    public boolean removeByHair(String color, String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String removeQuery = "DELETE FROM persons "
                    + "USING users "
                    + "WHERE persons.hair_color = ? "
                    + "AND persons.owner_id = users.id AND users.login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(removeQuery);
            preparedStatement.setString(1, color);
            preparedStatement.setString(2, username);

            int deletedBands = preparedStatement.executeUpdate();
            return deletedBands > 0;
        });
    }

    public boolean updateById(Person person, Integer id, String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            connection.createStatement().execute("BEGIN TRANSACTION;");
            String updateQuery = "UPDATE persons "
                    + "SET creation_date = ?, "
                    + "name = ?, "
                    + "x = ?, "
                    + "y = ?, "
                    + "height = ?, "
                    + "eye_color = ?, "
                    + "hair_color = ?, "
                    + "nationality = ?, "
                    + "xLoc = ?, "
                    + "yLoc = ?, "
                    + "zLoc = ? "
                    + "FROM users "
                    + "WHERE persons.id = ? "
                    + "AND persons.owner_id = users.id AND users.login = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

            Coordinates coordinates = person.getCoordinates();
            Location location = person.getLocation();

            ZonedDateTime zonedDateTime = person.getCreationDate();
            LocalDate localDate = zonedDateTime.toLocalDate();
            java.sql.Date sqlDate = java.sql.Date.valueOf(localDate);

            preparedStatement.setDate(1, sqlDate);
            preparedStatement.setString(2, person.getName());
            preparedStatement.setFloat(3, coordinates.getX());
            preparedStatement.setFloat(4, coordinates.getY());
            preparedStatement.setFloat(5, person.getHeight());
            preparedStatement.setString(6, person.getEyeColor().toString());
            preparedStatement.setString(7, person.getHairColor().toString());
            preparedStatement.setString(8, person.getCountry().toString());
            preparedStatement.setDouble(9, location.getX());
            preparedStatement.setInt(10, location.getY());
            preparedStatement.setDouble(11, location.getY());
            preparedStatement.setLong(12, id);
            preparedStatement.setString(13, username);

            int updatedRows = preparedStatement.executeUpdate();
            connection.createStatement().execute("COMMIT;");

            return updatedRows > 0;
        });
    }

    public List<Integer> clear(String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String clearQuery = "DELETE FROM persons "
                    + "USING users "
                    + "WHERE persons.owner_id = users.id AND users.login = ? "
                    + "RETURNING persons.id;";
            PreparedStatement preparedStatement = connection.prepareStatement(clearQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Integer> resultingList = new ArrayList<>();
            while (resultSet.next()) {
                resultingList.add(resultSet.getInt("id"));
            }
            return resultingList;
        });
    }

    public void addUser(String username, String password) throws DatabaseException {
        dbConnector.handleQuery((Connection connection) -> {
            String addUserQuery = "INSERT INTO users (login, password) "
                    + "VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(addUserQuery,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, Encryptor.encryptString(password));

            preparedStatement.executeUpdate();
        });
    }

    public String getPassword(String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String getPasswordQuery = "SELECT (password) "
                    + "FROM users "
                    + "WHERE users.login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(getPasswordQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            }
            return null;
        });
    }

    public boolean checkUsersExistence(String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String existenceQuery = "SELECT COUNT (*) "
                    + "FROM users "
                    + "WHERE users.login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(existenceQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            return resultSet.getInt("count") > 0;
        });
    }

    public List<Integer> getIdsOfUsersElements(String username) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) -> {
            String getIdsQuery = "SELECT persons.id FROM persons, users "
                    + "WHERE persons.owner_id = users.id AND users.login = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(getIdsQuery);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Integer> resultingList = new ArrayList<>();
            while (resultSet.next()) {
                resultingList.add(resultSet.getInt("id"));
            }

            return resultingList;
        });
    }

    public boolean validateUser(String username, String password) throws DatabaseException {
        return dbConnector.handleQuery((Connection connection) ->
                Encryptor.encryptString(password).equals(getPassword(username)));
    }
}
