package Common.util;

import Common.data.Person;
import Common.interfaces.Data;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Класс Response - класс, содержащий информацию для ответа на запрос.
 */
public class Response implements Serializable, Data {

    /**
     * Сообщение, отправляемое в ответ на запрос.
     */
    private String messageToResponse;

    /**
     * Данные о персоне, отправляемые в ответ на запрос.
     */
    private Person personToResponse;

    /**
     * Данные коллекции, отправляемые в ответ на запрос.
     */
    private ConcurrentLinkedDeque<Person> collectionToResponse;

    /**
     * Коллекция элементов, не принадлежащих клиенту
     */
    private ConcurrentLinkedDeque<Person> alienElements;

    /**
     * Список дополнительной информации, содержащейся в ответе сервера
     */
    private List<String> info;

    /**
     * Конструктор класса Response, принимающий сообщение для ответа.
     *
     * @param messageToResponse сообщение для ответа
     */
    public Response(String messageToResponse) {
        this.messageToResponse = messageToResponse;
    }

    /**
     * Конструктор класса Response, принимающий сообщение и информацию о персоне для ответа.
     *
     * @param messageToResponse сообщение для ответа
     * @param personToResponse информация о персоне для ответа
     */
    public Response(String messageToResponse, Person personToResponse) {
        this.messageToResponse = messageToResponse;
        this.personToResponse = personToResponse;
    }

    
    public Response(String messageToResponse, ConcurrentLinkedDeque<Person> collectionToResponse) {
        this.messageToResponse = messageToResponse;
        this.collectionToResponse = collectionToResponse;
    }

    /**
     * Конструктор класса для создания ответа сервера, содержащего только сообщение
     * @param messageToResponse сообщение, которое будет отправлено клиенту
     * @param info список дополнительной информации для клиента
     */
    public Response(String messageToResponse, List<String> info) {
        this.messageToResponse = messageToResponse;
        this.info = info;
    }

    /**
     * Конструктор класса для создания ответа сервера, содержащего сообщение и коллекцию элементов
     * @param messageToResponse сообщение, которое будет отправлено клиенту
     * @param usersElements коллекция элементов для отправки клиенту
     * @param alienElements коллекция элементов, не принадлежащих клиенту
     */
    public Response(String messageToResponse, ConcurrentLinkedDeque<Person> usersElements, ConcurrentLinkedDeque<Person> alienElements) {
        this.messageToResponse = messageToResponse;
        collectionToResponse = usersElements;
        this.alienElements = alienElements;
    }

    /**
     * Конструктор класса Response, принимающий информацию о персоне для ответа.
     *
     * @param personToResponse информация о персоне для ответа
     */
    public Response(Person personToResponse) {
        this.personToResponse = personToResponse;
    }


    public Response(ConcurrentLinkedDeque<Person> collectionToResponse) {
        this.collectionToResponse = collectionToResponse;
    }

    /**
     * Метод, возвращающий сообщение для ответа.
     *
     * @return сообщение для ответа
     */
    public String getMessageToResponse() {
        return messageToResponse;
    }

    /**
     * Метод, возвращающий информацию о персоне для ответа.
     *
     * @return информация о персоне для ответа
     */
    public Person getPersonToResponse() {
        return personToResponse;
    }

    public ConcurrentLinkedDeque<Person> getCollectionToResponse() {
        return collectionToResponse;
    }

    /**
     * Возвращает список дополнительной информации, содержащейся в ответе сервера
     * @return список дополнительной информации
     */
    public List<String> getInfo() {
        return info;
    }

    /**
     * Возвращает коллекцию элементов, не принадлежащих клиенту
     * @return коллекция элементов
     */
    public ConcurrentLinkedDeque<Person> getAlienElements() {
        return alienElements;
    }


    /**
     * Метод, возвращающий информацию для отправки.
     *
     * @return информация для отправки
     */
    @Override
    public String getData() {
        return (messageToResponse == null ? "" : (getMessageToResponse()))
                + (personToResponse == null ? "" : ("\nДанные персоны:\n" +  getPersonToResponse().toString()))
                + (collectionToResponse == null ? "" : ("\nКоллекция:\n" + getCollectionToResponse()))
                + (alienElements == null ? "" :("\nКоллекции других пользователей:\n" +
                (getAlienElements().isEmpty() ? "В коллекциях других пользователей нет элементов" : getAlienElements())));
    }

    /**
     * Представляет ответ, полученный от сервера, в формате, удобном для чтения.
     */
    @Override
    public String toString() {
        return "Ответ[" + messageToResponse + "]";
    }
}