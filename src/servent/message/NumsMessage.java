package servent.message;

import app.ServentInfo;

public class NumsMessage extends BasicMessage{

    private int nodeNum;

    public NumsMessage( ServentInfo senderPort, ServentInfo receiverPort, int pointNum, int nodeNum) {
        super(MessageType.NUMS, senderPort, receiverPort, pointNum);
        this.nodeNum = nodeNum;
    }

    public int getNodeNum() {
        return nodeNum;
    }

}
