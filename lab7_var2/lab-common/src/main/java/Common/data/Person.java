package Common.data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Person implements Comparable<Person>, Serializable{

    @NotNull
    @Positive(message = "ID должен быть больше нуля!")
    private Integer id;

    @NotNull(message = "Имя не может быть null")
    @NotBlank(message = "Имя должно содержать хотя бы 1 символ")
    private String name;

    @NotNull(message = "Координаты не могут быть null")
    private Coordinates coordinates;

    @NotNull(message = "Дата не может быть null")
    @PastOrPresent
    private ZonedDateTime creationDate;

    @NotNull(message = "Рост не может быть null")
    @Positive(message = "Рост должен быть больше нуля!")
    private Float height;

    @NotNull(message = "Цвет глаз не может быть null")
    private ColorEye eyeColor;

    @NotNull(message = "Цвет волос не может быть null")
    private ColorHair hairColor;

    @NotNull(message = "Национальность не может быть null")
    private Country nationality;

    @NotNull(message = "Локация не может быть null")
    private Location location;

    public Person(Integer id, String name, Coordinates coordinates, ZonedDateTime creationDate, Float height, ColorEye colorEye, ColorHair colorHair, Country nationality, Location location) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.height = height;
        this.eyeColor = colorEye;
        this.hairColor = colorHair;
        this.nationality = nationality;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Float getHeight() {
        return height;
    }


    public ColorEye getEyeColor() {
        return eyeColor;
    }
    public ColorHair getHairColor() {
        return hairColor;
    }
    public Country getCountry() {
        return nationality;
    }
    public Location getLocation() {
        return location;
    }

    @Override
    public int compareTo(Person o) {
        return Integer.compare(this.getId(), o.getId());
    }

    @Override
    public String toString() {
        String result = String.format("ID: %d\nНазвание: %s\nКоординаты: {x: %.2f, y: %.2f}\nВремя создания: %s\nРост: %.2f\nЦвет глаз: %s\nЦвет волос: %s\nНациональность: %s\nЛокация: {x: %f, y: %d, z: %f}\n",
                getId(), getName(), getCoordinates().getX(), getCoordinates().getY(), getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.y H:mm:ss")), getHeight(), getEyeColor(), getHairColor(), getCountry(), getLocation().getX(), getLocation().getY(), getLocation().getZ());
        return result;
    }
}
