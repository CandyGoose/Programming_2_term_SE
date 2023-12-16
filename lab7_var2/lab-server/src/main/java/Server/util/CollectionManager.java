package Server.util;

import Common.data.Person;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

import java.util.stream.Collectors;


public class CollectionManager {


    private ConcurrentLinkedDeque<Person> personCollection = new ConcurrentLinkedDeque<>();

    /**
     * Дата инициализации коллекции
     */
    private LocalDateTime lastInitTime;


    public ConcurrentLinkedDeque<Person> sort(ConcurrentLinkedDeque<Person> collection) {
        Comparator<Person> byX = Comparator.comparingDouble(p -> p.getCoordinates().getX());
        return collection.stream()
                .sorted(byX)
                .collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
    }

    public ConcurrentLinkedDeque<Person> getCollection() {
        return personCollection;
    }


    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }


    public void removeById(Integer id) {
            personCollection.removeIf(p -> p.getId().equals(id));
    }

    public void removeByHair(String color, List<Integer> ids) {
        Iterator<Person> iterator = personCollection.iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            System.out.println(ids);
            System.out.println(person.getId());
            System.out.println(ids.contains(person.getId()));
            if (person.getHairColor().toString().equals(color) && ids.contains(person.getId())) {
                iterator.remove();
            }
        }
    }



    /**
     * Возвращает ID первой персоны в коллекции.
     * @return ID первой персоны или 0, если коллекция пуста
     */
    public int getFirstId(){
        Optional<Integer> minId = personCollection.stream()
                .map(Person::getId)
                .min(Comparator.naturalOrder());
        return minId.orElse(0);
    }


    public void addToCollection(Person person) {
            personCollection.add(person);
            personCollection = new ConcurrentLinkedDeque<>(sort(personCollection));
            setLastInitTime(LocalDateTime.now());
    }

    /**
     * Устанавливает время последней инициализации.
     * @param lastInitTime время последней инициализации
     */
    public void setLastInitTime(LocalDateTime lastInitTime) {
            this.lastInitTime = lastInitTime;
    }

    public void clearCollection() {
            personCollection.clear();
    }


    public void setPersonCollection(ConcurrentLinkedDeque<Person> personCollection) {
            this.personCollection = personCollection;
    }




    public Person getFirstElement() {
        List<Person> list = new ArrayList<>(personCollection);
        Collections.sort(list);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<Person> getFilterName(String substring) {
        return personCollection.stream()
                .filter(person -> person.getName().startsWith(substring))
                .collect(Collectors.toList());
    }


    public List<Float> getListHeight(){
        return personCollection.stream() // создание потока данных
                .map(Person::getHeight) // получение значения поля Height для каждого объекта типа Person в коллекции
                .sorted(Collections.reverseOrder()) // сортировка в обратном порядке
                .collect(Collectors.toList()); // собирает все значения в List
    }

    public float getAverageHeight() {
        List<Float> heights = getListHeight();
        if (heights.isEmpty()) {
            return 0;
        }

        float sum = 0;
        for (float height : heights) {
            sum += height;
        }

        return sum / heights.size();
    }




    public ConcurrentLinkedDeque<Person> getUsersElements(List<Integer> ids) {
            return personCollection.stream().filter(p -> ids.contains(p.getId())).collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
    }

    public ConcurrentLinkedDeque<Person> getAlienElements(List<Integer> ids) {
            return personCollection.stream().filter(p -> !ids.contains(p.getId())).collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
    }

    public String getInfo() {
        return "Тип коллекции: " + ConcurrentLinkedDeque.class + ", тип элементов: "
                + Person.class
                + (getLastInitTime() == null ? "" : (", дата инициализации: "
                + getLastInitTime().format(DateTimeFormatter.ofPattern("dd.MM.y H:mm:ss"))))
                + ", количество элементов: " + personCollection.size();

    }


    @Override
    public String toString() {
        if (personCollection.isEmpty()) return "Коллекция пуста.";
        StringBuilder info = new StringBuilder();
        for (Person person : personCollection) {
            info.append(person);
            if (person != personCollection.getLast()) info.append("\n\n");
        }
        return info.toString();
    }
}
