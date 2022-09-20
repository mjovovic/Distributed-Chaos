package servent.message;

import app.ServentInfo;

import java.util.List;

public class StatusAskMessage extends BasicMessage {

    private List<ServentInfo> alreadySent;

    public StatusAskMessage(ServentInfo senderPort, ServentInfo receiverPort, int pointNum, List<ServentInfo> alreadySent) {
        super(MessageType.STATUS_ASK, senderPort, receiverPort, pointNum);
        this.alreadySent = alreadySent;
    }

    public List<ServentInfo> getAlreadySent() {
        return alreadySent;
    }
}
