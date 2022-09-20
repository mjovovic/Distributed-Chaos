package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.NeighbourQuitMessage;
import servent.message.QuitJobMessage;
import servent.message.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public class QuitJobHandler implements MessageHandler{

    private Message clientMessage;

    public QuitJobHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if( clientMessage.getMessageType() == MessageType.QUIT_JOB){
            QuitJobMessage message = (QuitJobMessage)clientMessage;
            int numOfPoints = message.getPointNum();
            String fractalId = AppConfig.jobMap.get(numOfPoints).getServentInfo().getFractalId();
            String parentFractalID = fractalId.substring(0, fractalId.length()-1) + "0";

            List<ServentInfo> neighbours = AppConfig.jobMap.get(numOfPoints).getServentInfo().getNeighbours();
            for ( ServentInfo neighbour :neighbours) {
                Message neighbourQuit = new NeighbourQuitMessage(message.getReceiver(), neighbour, numOfPoints, parentFractalID, neighbours);
                MessageUtil.sendMessage(neighbourQuit);

            }

            synchronized (AppConfig.jobMap.get(numOfPoints).getLock()) {
                AppConfig.jobMap.get(numOfPoints).getPointDrawer().setPoints(new ArrayList<>());
                //send to neighbours list to disconnect
                //set servent info
                AppConfig.myServentInfo = message.getServentToSet();
                AppConfig.jobMap.get(numOfPoints).setServentInfo(message.getServentToSet());
                AppConfig.jobMap.get(numOfPoints).setIdle(false);

            }

        }


    }
}
