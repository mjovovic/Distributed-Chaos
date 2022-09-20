package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.NumsMessage;

public class NumsHandler implements MessageHandler{

    Message clientMessage;

    public NumsHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if ( clientMessage.getMessageType() == MessageType.NUMS ) {
            NumsMessage message = (NumsMessage) clientMessage;
            AppConfig.jobMap.get(message.getPointNum()).getNumOfActiveJobs().getAndSet(message.getNodeNum());
        }
    }
}
