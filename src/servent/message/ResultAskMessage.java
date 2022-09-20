package servent.message;

import app.ServentInfo;

import java.util.List;

public class ResultAskMessage extends BasicMessage {

    private List<ServentInfo> alreadySent;

    public ResultAskMessage(ServentInfo senderPort, ServentInfo receiverPort, int pointNum, List<ServentInfo> alreadySent) {
        super(MessageType.RESULT_ASK, senderPort, receiverPort, pointNum);
        this.alreadySent = alreadySent;
    }

    public List<ServentInfo> getAlreadySent() {
        return alreadySent;
    }
}