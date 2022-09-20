package job;

import java.io.Serial;
import java.io.Serializable;

public class Point implements Serializable {


    @Serial
    private static final long serialVersionUID = 5716579041197659067L;
    public int xAxis;
    public int yAxis;

    public Point(int xAxis, int yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }


    public int getxAxis() {
        return xAxis;
    }

    public void setxAxis(int xAxis) {
        this.xAxis = xAxis;
    }

    public int getyAxis() {
        return yAxis;
    }

    public void setyAxis(int yAxis) {
        this.yAxis = yAxis;
    }

    @Override
    public String toString() {
        return "Point{" +
                "X=" + xAxis +
                ", Y=" + yAxis +
                '}';
    }
}
