package servent.message;

import app.ServentInfo;

import java.util.List;

public class StatusSpecificMessage extends BasicMessage{

    private String fractalID;
    private List<ServentInfo> alreadySent;

    public StatusSpecificMessage(ServentInfo senderPort, ServentInfo receiverPort, int pointNum, String fractalID, List<ServentInfo> alreadySent) {
        super(MessageType.STATUS_SPECIFIC, senderPort, receiverPort, pointNum);
        this.fractalID = fractalID;
        this.alreadySent = alreadySent;
    }

    public List<ServentInfo> getAlreadySent() {
        return alreadySent;
    }

    public String getFractalID() {
        return fractalID;
    }
}
