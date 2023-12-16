package Common.data;

import java.io.Serializable;

public enum Country implements Serializable {
    CHINA,
    INDIA,
    THAILAND,
    SOUTH_KOREA;

    public static String nameList() {
        StringBuilder nameList = new StringBuilder();
        for (Country category : values()) {
            nameList.append(category.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
}