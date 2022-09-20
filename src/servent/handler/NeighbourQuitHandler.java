package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import servent.message.IdleMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.NeighbourQuitMessage;
import servent.message.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public class NeighbourQuitHandler implements MessageHandler{

    private Message clientMessage;

    public NeighbourQuitHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if (clientMessage.getMessageType() == MessageType.NEIGHBOUR_QUIT){
            NeighbourQuitMessage message = (NeighbourQuitMessage) clientMessage;
            int numOfPoints = message.getPointNum();
            ServentInfo myServentInfo = AppConfig.jobMap.get(numOfPoints).getServentInfo();
            List<ServentInfo> neighboursAffected = message.getQuitServentNeighbours();
            List<ServentInfo> parentNeighbours = AppConfig.jobMap.get(numOfPoints).getServentInfo().getNeighbours();
            parentNeighbours.remove(message.getSender());

            // if we are parents
            if (AppConfig.jobMap.get(numOfPoints).getServentInfo().getFractalId().equals(message.getParentFractalId())) {
                AppConfig.timestampedStandardPrint(parentNeighbours.toString());
                parentNeighbours.removeAll(neighboursAffected);
                AppConfig.timestampedStandardPrint(parentNeighbours.toString());
                //we are not the parent we need to go idle
            } else {

                AppConfig.jobMap.get(numOfPoints).getServentInfo().setFractalId("");
                AppConfig.jobMap.get(numOfPoints).getServentInfo().setNeighbours(new ArrayList<>());
                AppConfig.jobMap.get(numOfPoints).getPointDrawer().stop();
                AppConfig.jobMap.get(numOfPoints).getPointDrawer().setPoints(new ArrayList<>());
                AppConfig.jobMap.get(numOfPoints).setIdle(true);

                Message idleMessage = new IdleMessage(AppConfig.jobMap.get(numOfPoints).getServentInfo(), AppConfig.bsInfo, numOfPoints);
                MessageUtil.sendMessage(idleMessage);
            }
        }
    }
}
