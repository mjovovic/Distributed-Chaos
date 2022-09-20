package servent.message;

import app.ServentInfo;

public class IdleMessage extends BasicMessage{


    public IdleMessage(ServentInfo senderPort, ServentInfo receiverPort, int pointNum) {
        super(MessageType.IDLE, senderPort, receiverPort, pointNum);
    }


}
