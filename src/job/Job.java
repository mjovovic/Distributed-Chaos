package job;

import app.ServentInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Job {

    private final String name;
    private final int pointNum;
    private final double coefficient;
    private final int height;
    private final int width;
    private List<Point> startingPoints;
    private ServentInfo serventInfo;
    private PointDrawer pointDrawer;
    private boolean isIdle = true;
    private Thread drawingThread;
    private AtomicInteger numOfActiveJobs = new AtomicInteger(0);
    private List<Point>accumulatedPoints;
    private Object lock = new Object();


    public Job(String name, int pointNum, double coefficient, int height, int width, List<Point> startingPoints, ServentInfo serventInfo) {
        this.name = name;
        this.pointNum = pointNum;
        this.coefficient = coefficient;
        this.height = height;
        this.width = width;
        this.startingPoints = startingPoints;
        this.serventInfo = serventInfo;
        this.pointDrawer = new PointDrawer(startingPoints, coefficient, pointNum);
        this.drawingThread = new Thread(pointDrawer);
        this.accumulatedPoints = new ArrayList<>();

    }

    public Object getLock() {
        return lock;
    }

    public AtomicInteger getNumOfActiveJobs() {
        return numOfActiveJobs;
    }

    public void setNumOfActiveJobs(AtomicInteger numOfActiveJobs) {
        this.numOfActiveJobs = numOfActiveJobs;
    }

    public List<Point> getAccumulatedPoints() {
        return accumulatedPoints;
    }

    public void setAccumulatedPoints(List<Point> accumulatedPoints) {
        this.accumulatedPoints = accumulatedPoints;
    }

    public boolean isIdle() {
        return isIdle;
    }

    public void setIdle(boolean idle) {
        isIdle = idle;

        if ( idle == false) {
            drawingThread.start();
        }
    }

    public PointDrawer getPointDrawer() {
        return pointDrawer;
    }

    public void setPointDrawer(PointDrawer pointDrawer) {
        this.pointDrawer = pointDrawer;
    }

    public int getPointNum() {
        return pointNum;
    }


    public String getName() {
        return name;
    }

    public ServentInfo getServentInfo() {
        return serventInfo;
    }

    public void setServentInfo(ServentInfo serventInfo) {
        this.serventInfo = serventInfo;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public List<Point> getStartingPoints() {
        return startingPoints;
    }

    public void setStartingPoints(List<Point> startingPoints) {
        this.startingPoints = startingPoints;
    }
}
