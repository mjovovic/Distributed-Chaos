package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.StatusAskMessage;
import servent.message.StatusReplyMessage;
import servent.message.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public class StatusAskHandler implements MessageHandler{

    private Message clientMessage;

    public StatusAskHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        try {

            if ( clientMessage.getMessageType() == MessageType.STATUS_ASK) {
                StatusAskMessage message = (StatusAskMessage) clientMessage;
                int numOfPoints = message.getPointNum();
                ServentInfo myServentInfo = AppConfig.jobMap.get(numOfPoints).getServentInfo();

                //edge case for when id is here
                if ( message.getAlreadySent() == null ) {
                    Message statusReply = new StatusReplyMessage(myServentInfo, message.getSender(),
                            numOfPoints, myServentInfo.getFractalId(),
                            AppConfig.jobMap.get(numOfPoints).getPointDrawer().getPoints() );
                    MessageUtil.sendMessage(statusReply);

                    return;
                }

                List<ServentInfo> alreadySent = message.getAlreadySent();
                List<ServentInfo> neighbourList = myServentInfo.getNeighbours();
                List<ServentInfo> toSend = new ArrayList<>();
                toSend.addAll(alreadySent);
                toSend.addAll(neighbourList);

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

                        Message nextAskMessage = new StatusAskMessage(message.getSender(), neighbour, numOfPoints, toSend);
                        MessageUtil.sendMessage(nextAskMessage);
                    }
                }

                AppConfig.timestampedStandardPrint(AppConfig.jobMap.get(numOfPoints).getPointDrawer().getPoints().size()+" ");
                Message statusReply = new StatusReplyMessage(myServentInfo, message.getSender(),
                        numOfPoints, myServentInfo.getFractalId(),
                        AppConfig.jobMap.get(numOfPoints).getPointDrawer().getPoints() );
                MessageUtil.sendMessage(statusReply);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
