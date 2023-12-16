package Common.data;

import java.io.Serializable;

public enum ColorHair implements Serializable {
    RED,
    BLACK,
    ORANGE;

    public static String nameList() {
        StringBuilder nameList = new StringBuilder();
        for (ColorHair category : values()) {
            nameList.append(category.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
}

