package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.ReadyMessage;

public class ReadyHandler implements MessageHandler{

    private Message clientMessage;

    public ReadyHandler(Message clientMessage) {
        this.clientMessage = clientMessage;

    }

    @Override
    public void run() {
        if ( clientMessage.getMessageType() == MessageType.READY){
            ReadyMessage message = (ReadyMessage) clientMessage;
            int numOfPoints = message.getPointNum();
            ServentInfo myServentInfo = AppConfig.jobMap.get(numOfPoints).getServentInfo();

            AppConfig.timestampedStandardPrint( "READY " + message.getReceiver() + " " + numOfPoints + " " + myServentInfo.getNeighbours());
        }
    }
}
