package job;

import app.Cancellable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PointDrawer implements Runnable, Cancellable {
 
    private List<Point> points;
    private List<Point> startingPoints;
    private double coefficient;
    private int pointNum;
    private volatile boolean working = true;


    @Override
    public void run() {

        Random rand = new Random();
        Point previousPoint = startingPoints.get(rand.nextInt(startingPoints.size()));
        while (working) {
            int heightDir = -1;
            int widthDir = -1;
            Point chosenPoint = startingPoints.get(rand.nextInt(startingPoints.size())) ;

            if ( previousPoint.getxAxis() <= chosenPoint.getxAxis()) {
                heightDir = 1;
            }

            if ( previousPoint.getyAxis() <= chosenPoint.getyAxis()) {
                widthDir = 1;
            }

            Point newPoint = new Point( (int) ( previousPoint.getxAxis() + heightDir * Math.abs(chosenPoint.getxAxis() - previousPoint.getxAxis()) * coefficient ),
                    (int)(  previousPoint.getyAxis() + widthDir * Math.abs(chosenPoint.getyAxis() - previousPoint.getyAxis())  * coefficient ));


            points.add(newPoint);
            previousPoint = newPoint;
            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public PointDrawer(List<Point> startingPoints, double coefficient, int pointNum) {
        this.startingPoints = startingPoints;
        this.coefficient = coefficient;
        this.pointNum = pointNum;
        points = new ArrayList<>();
    }

    public int getPointNum() {
        return pointNum;
    }

    public void setPointNum(int pointNum) {
        this.pointNum = pointNum;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public List<Point> getStartingPoints() {
        return startingPoints;
    }

    public void setStartingPoints(List<Point> startingPoints) {
        this.startingPoints = startingPoints;
    }

    @Override
    public void stop() {
        this.working = false;
    }
}
