package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import servent.message.*;
import servent.message.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public class ResultAskHandler implements MessageHandler{

    private Message clientMessage;

    public ResultAskHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        try {

            if ( clientMessage.getMessageType() == MessageType.RESULT_ASK) {
                ResultAskMessage message = (ResultAskMessage) clientMessage;
                int numOfPoints = message.getPointNum();
                ServentInfo myServentInfo = AppConfig.jobMap.get(numOfPoints).getServentInfo();

                //edge case for when id is here
                if ( message.getAlreadySent() == null ) {
                    Message resultReply = new ResultReplyMessage(myServentInfo, message.getSender(),
                            numOfPoints, myServentInfo.getFractalId(),
                            AppConfig.jobMap.get(numOfPoints).getPointDrawer().getPoints() );
                    MessageUtil.sendMessage(resultReply);

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

                        Message nextAskMessage = new ResultAskMessage(message.getSender(), neighbour, numOfPoints, toSend);
                        MessageUtil.sendMessage(nextAskMessage);
                    }
                }

                //SET TO GET POINTS STARTING IS ONLY FOR DEBUG
                AppConfig.timestampedStandardPrint(AppConfig.jobMap.get(numOfPoints).getPointDrawer().getPoints().toString());

                Message resultReply = new ResultReplyMessage(myServentInfo, message.getSender(),
                        numOfPoints, myServentInfo.getFractalId(),
                        AppConfig.jobMap.get(numOfPoints).getPointDrawer().getPoints() );


                MessageUtil.sendMessage(resultReply);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}