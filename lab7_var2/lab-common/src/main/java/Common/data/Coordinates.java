package Common.data;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private final float x;

    private final float y;

    public Coordinates(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}