package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import servent.message.*;
import servent.message.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public class StatausSpecificHandler implements MessageHandler{

    private Message clientMessage;


    public StatausSpecificHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }


    @Override
    public void run() {
        try {
            if ( clientMessage.getMessageType() == MessageType.STATUS_SPECIFIC ) {


                StatusSpecificMessage message = (StatusSpecificMessage) clientMessage;
                int numOfPoints = message.getPointNum();
                ServentInfo myServentInfo = AppConfig.jobMap.get(numOfPoints).getServentInfo();

                String fractalID = message.getFractalID();

                //if he is us we send it back to out master
                if (myServentInfo.getFractalId().equals(fractalID)) {
                    Message statusReply = new StatusReplyMessage(myServentInfo, message.getSender(), numOfPoints,
                            fractalID, AppConfig.jobMap.get(numOfPoints).getPointDrawer().getPoints());
                    MessageUtil.sendMessage(statusReply);
                    return;

                }
                List<ServentInfo> alreadySent = message.getAlreadySent();
                List<ServentInfo> neighbourList = myServentInfo.getNeighbours();
                List<ServentInfo> toSend = new ArrayList<>();
                toSend.addAll(alreadySent);
                toSend.addAll(neighbourList);

                //go get it from th right neighbour if he is not not in up conditions
                for ( ServentInfo neighbour: neighbourList ) {
                    boolean isThere = false;
                    if ( message.getSender().getNodeId() == neighbour.getNodeId() )
                        continue;
                    for ( ServentInfo test : alreadySent) {
                        if ( neighbour.getNodeId() == test.getNodeId() ) {
                            isThere = true;
                            break;
                        }
                    }
                    if ( !isThere ) {
                        Message statusSpecific = new StatusSpecificMessage(message.getSender(), neighbour, numOfPoints, fractalID, toSend);
                        MessageUtil.sendMessage(statusSpecific);

                    }
                }
            }
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }
}
