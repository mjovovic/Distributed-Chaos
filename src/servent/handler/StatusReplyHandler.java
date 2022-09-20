package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.StatusReplyMessage;

public class StatusReplyHandler implements MessageHandler{

    private Message clientMessage;

    public StatusReplyHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if( clientMessage.getMessageType() == MessageType.STATUS_REPLY ) {
            StatusReplyMessage message = (StatusReplyMessage) clientMessage;
            int numOfPoint = message.getPointNum();
            AppConfig.jobMap.get(numOfPoint).getNumOfActiveJobs().getAndDecrement();
            synchronized ( AppConfig.jobMap.get(numOfPoint).getLock()){
                AppConfig.jobMap.get(numOfPoint).getAccumulatedPoints().addAll(message.getPointsDrawn());
            }
        }
    }
}
