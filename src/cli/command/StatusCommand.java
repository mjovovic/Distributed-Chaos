package cli.command;

import app.AppConfig;
import app.ServentInfo;
import job.Job;
import servent.message.Message;
import servent.message.NumsMessage;
import servent.message.StatusAskMessage;
import servent.message.StatusSpecificMessage;
import servent.message.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatusCommand implements CLICommand{
    @Override
    public String commandName() {
        return "status";
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
            } else {
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
            AppConfig.timestampedStandardPrint("ACTIVE JOBS: " + AppConfig.jobMap.get(numOfPoints).getNumOfActiveJobs());


            List<ServentInfo> neighbourList =  myServentInfo.getNeighbours();
            //get all the snapshots form nodes in the job

            for ( ServentInfo neighbour : neighbourList ){
                if (myServentInfo.getNodeId() == neighbour.getNodeId())
                    continue;
                Message statusAskMessage = new StatusAskMessage( myServentInfo, neighbour, numOfPoints, neighbourList);
                MessageUtil.sendMessage(statusAskMessage);
            }

            AppConfig.jobMap.get(numOfPoints).getAccumulatedPoints().addAll(AppConfig.jobMap.get(numOfPoints).getPointDrawer().getPoints());

            while ( AppConfig.jobMap.get(numOfPoints).getNumOfActiveJobs().get() > 1 ) {
                try {
                    AppConfig.timestampedStandardPrint("waiting for servents to send all messages");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            AppConfig.timestampedStandardPrint("POINT NUM STATUS: " + AppConfig.jobMap.get(numOfPoints).getAccumulatedPoints().size() + " points");



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

                if (myServentInfo.getNodeId() == neighbour.getNodeId())
                    continue;

                Message statusSpecific = new StatusSpecificMessage( myServentInfo, neighbour, numOfPoints, fractalID, neighbourList);
                MessageUtil.sendMessage(statusSpecific);

            }



        }
        AppConfig.jobMap.get(numOfPoints).setAccumulatedPoints(new ArrayList<>());
        AppConfig.jobMap.get(numOfPoints).getNumOfActiveJobs().getAndSet(0);
    }
}
