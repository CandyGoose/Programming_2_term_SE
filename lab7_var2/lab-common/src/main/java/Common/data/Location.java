package Common.data;

import java.io.Serializable;

public class Location implements Serializable {

    private final Double x;

    private final int y;
    private final double z;

    public Location(Double x, int y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Double getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public double getZ() {
        return y;
    }
}