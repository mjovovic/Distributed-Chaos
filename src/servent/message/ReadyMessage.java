package servent.message;

import app.ServentInfo;

public class ReadyMessage extends BasicMessage{

    public ReadyMessage(ServentInfo senderPort, ServentInfo receiverPort, int pointNum) {
        super(MessageType.READY, senderPort, receiverPort, pointNum);
    }
}
