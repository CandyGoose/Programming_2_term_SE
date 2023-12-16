package Common.util;

import Common.data.Person;
import Common.interfaces.Data;

import java.io.Serializable;

public class Request implements Serializable, Data {

    private String commandName;
    private Integer numericArgument;
    private String textArgument;
    private Person personArgument;

    private String login;

    private String password;

    private final RequestType type;

    public Request(String commandName, RequestType type) {
        this.commandName = commandName;
        this.type = type;
    }

    public Request(String commandName, Integer numericArgument, RequestType type) {
        this.commandName = commandName;
        this.numericArgument = numericArgument;
        this.type = type;
    }

    public Request(String commandName, Person personArgument, RequestType type) {
        this.commandName = commandName;
        this.personArgument = personArgument;
        this.type = type;
    }

    public Request(String commandName, Integer numericArgument, Person personArgument, RequestType type) {
        this.commandName = commandName;
        this.numericArgument = numericArgument;
        this.personArgument = personArgument;
        this.type = type;
    }

    public Request(String commandName, String argument, RequestType type) {
        if (type == RequestType.LOGIN || type == RequestType.REGISTER) {
            this.login = commandName;
            this.password = argument;
        } else {
            this.commandName = commandName;
            this.textArgument = argument;
        }
        this.type = type;
    }


    public String getTextArgument() {
        return textArgument;
    }

    public String getCommandName() {
        return commandName;
    }

    public Integer getNumericArgument() {
        return numericArgument;
    }

    public Person getPersonArgument() {
        return personArgument;
    }
    public RequestType getType() {
        return type;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Метод getData() возвращает строковое представление данных объекта в виде имени команды и соответствующих аргументов.
     */
    @Override
    public String getData(){
        return "Имя команды для отправки: " + commandName
                + (personArgument == null ? "" : ("\nИнформация о персоне для отправки:\n " + personArgument))
                + (numericArgument == null ? "" : ("\nЧисловой аргумент для отправки:\n " + numericArgument));
    }

    /**
     * Возвращает строковое представление объекта в формате "Ответ[имя команды]".
     */
    @Override
    public String toString() {
        return "Ответ[" + commandName + "]" ;
    }
}
