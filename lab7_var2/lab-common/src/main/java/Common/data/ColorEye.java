package Common.data;

import java.io.Serializable;

public enum ColorEye implements Serializable {
    GREEN,
    RED,
    YELLOW,
    ORANGE,
    BROWN;

    public static String nameList() {
        StringBuilder nameList = new StringBuilder();
        for (ColorEye category : values()) {
            nameList.append(category.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
}

