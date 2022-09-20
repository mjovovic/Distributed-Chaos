package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.ResultReplyMessage;

public class ResultReplyHandler implements MessageHandler{


    private Message clientMessage;

    public ResultReplyHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if ( clientMessage.getMessageType() == MessageType.RESULT_REPLY ) {
            ResultReplyMessage message = (ResultReplyMessage) clientMessage;
            int numOfPoint = message.getPointNum();
            AppConfig.timestampedStandardPrint( message.getSender() + " " + message.getFractalID() + " " +  message.getPointsDrawn().toString());

            AppConfig.jobMap.get(numOfPoint).getNumOfActiveJobs().getAndDecrement();

            synchronized ( AppConfig.jobMap.get(numOfPoint).getLock()){
                //AppConfig.jobMap.get(numOfPoint).getAccumulatedPoints().addAll(AppConfig.jobMap.get(numOfPoint).getPointDrawer().getPoints());
                AppConfig.jobMap.get(numOfPoint).getAccumulatedPoints().addAll(message.getPointsDrawn());
            }

        }
    }
}
