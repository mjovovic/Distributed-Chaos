package cli.command;

import app.AppConfig;
import app.ServentInfo;
import job.Job;
import job.Point;
import servent.message.*;
import servent.message.util.MessageUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultCommand implements CLICommand{
    @Override
    public String commandName() {
        return "result";
    }

    @Override
    public void execute(String args) {

        String name = args;
        int numOfPoints = -1;


        if ( !name.contains(" ") ) {
            //finding the num of points go get all the things we need
            for ( Map.Entry<Integer, Job> tmp : AppConfig.jobMap.entrySet() ) {
                if ( tmp.getValue().getName().equals(name) ) {
                    numOfPoints = tmp.getValue().getPointNum();
                    break;
                }
            }
            ServentInfo myServentInfo = AppConfig.jobMap.get(numOfPoints).getServentInfo();
            if ( myServentInfo == null){
                Message numMessage = new NumsMessage(AppConfig.myServentInfo, AppConfig.bsInfo, numOfPoints,0);
                MessageUtil.sendMessage(numMessage);
            }else {
                Message numMessage = new NumsMessage(myServentInfo, AppConfig.bsInfo, numOfPoints,0);
                MessageUtil.sendMessage(numMessage);
            }
            while (AppConfig.jobMap.get(numOfPoints).getNumOfActiveJobs().get() == 0){
                try {
                    AppConfig.timestampedStandardPrint("waiting for num of active srvents...");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            AppConfig.timestampedStandardPrint("Num of active jobs: " + AppConfig.jobMap.get(numOfPoints).getNumOfActiveJobs());

            List<ServentInfo> neighbourList =  myServentInfo.getNeighbours();
            //get all the snapshots form nodes in the job

            for ( ServentInfo neighbour : neighbourList ){
                if (myServentInfo.getNodeId() == neighbour.getNodeId())
                    continue;
                Message resultAskMessage = new ResultAskMessage( myServentInfo, neighbour, numOfPoints, neighbourList);
                MessageUtil.sendMessage(resultAskMessage);
            }

            AppConfig.jobMap.get(numOfPoints).getAccumulatedPoints().addAll(AppConfig.jobMap.get(numOfPoints).getPointDrawer().getPoints());

            while ( AppConfig.jobMap.get(numOfPoints).getNumOfActiveJobs().get() > 1 ) {
                try {
                    AppConfig.timestampedStandardPrint("waiting for servents to send all messages");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            AppConfig.timestampedStandardPrint("All servents sent messages all points are " + AppConfig.jobMap.get(numOfPoints).getAccumulatedPoints());
            try{
                printPoint(numOfPoints);
            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            String fractalID = name.split(" ")[1];

            //finding the num of points go get all the things we need
            for ( Map.Entry<Integer, Job> tmp : AppConfig.jobMap.entrySet() ) {
                if ( tmp.getValue().getName().equals( name.split(" ")[0]) ) {
                    numOfPoints = tmp.getValue().getPointNum();
                    break;
                }
            }
            ServentInfo myServentInfo = AppConfig.jobMap.get(numOfPoints).getServentInfo();
            List<ServentInfo> neighbourList =  myServentInfo.getNeighbours();

            //if he is us
            if ( myServentInfo.getFractalId().equals(fractalID) ) {
                AppConfig.timestampedStandardPrint(myServentInfo.getFractalId() + " " +fractalID +
                        " " +  AppConfig.jobMap.get(numOfPoints).getPointDrawer().getPoints().toString());
                return;

            }

            //go get it from th right neighbour
            for ( ServentInfo neighbour : neighbourList ){

                if (myServentInfo.getNodeId() == neighbour.getNodeId()) {
                    continue;
                }

                Message resultSpecific = new ResultSpecificMessage( myServentInfo, neighbour, numOfPoints, fractalID, neighbourList);
                MessageUtil.sendMessage(resultSpecific);

            }

            while ( AppConfig.jobMap.get(numOfPoints).getAccumulatedPoints().size() < 1 ) {
                try {
                    AppConfig.timestampedStandardPrint("waiting for servent to be found...");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            try{
                printPoint(numOfPoints);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        AppConfig.jobMap.get(numOfPoints).setAccumulatedPoints(new ArrayList<>());
        AppConfig.jobMap.get(numOfPoints).getNumOfActiveJobs().getAndSet(0);
    }

    private void printPoint (int numOfPoints) {


            List<Point> pointsToPrint = AppConfig.jobMap.get(numOfPoints).getAccumulatedPoints() ;

            int width = AppConfig.jobMap.get(numOfPoints).getWidth();
            int height = AppConfig.jobMap.get(numOfPoints).getHeight();

            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            Graphics2D g = ( Graphics2D ) bufferedImage.getGraphics();
            g.setColor( Color.WHITE );
            g.fillRect( 0, 0, width, height );

            for( Point point: pointsToPrint ) {
                g.setColor( Color.BLUE );

                g.fillOval( point.getxAxis(), point.getyAxis(), 10, 10 );

            }

            g.setColor( Color.BLACK );
            // AppConfig.timestampedStandardPrint("RRRRRRRRRR "+numOfPoints+" " +AppConfig.jobMap.get(numOfPoints).getStartingPoints()+ "\n");

            for( Point mainPoint: AppConfig.jobMap.get(numOfPoints).getStartingPoints() ) {
                // AppConfig.timestampedStandardPrint("WWWWWWWWWWW" + mainPoint.width + " " + mainPoint.height);
                g.fillOval(  mainPoint.xAxis, mainPoint.yAxis, 15, 15 );
            }

            File outputFile = new File( "pngOutput/"+ AppConfig.jobMap.get(numOfPoints).getName() +".jpg"  );

            try {
                outputFile.getParentFile().mkdirs();
                outputFile.createNewFile();
                ImageIO.write( bufferedImage, "jpg", outputFile );
            } catch ( IOException e ) {
                e.printStackTrace();
            }

    }
}
