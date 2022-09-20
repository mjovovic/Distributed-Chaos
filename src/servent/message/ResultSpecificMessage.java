package servent.message;

import app.ServentInfo;

import java.util.List;

public class ResultSpecificMessage extends BasicMessage{


    private String fractalID;
    private List<ServentInfo> alreadySent;

    public ResultSpecificMessage(ServentInfo senderPort, ServentInfo receiverPort, int pointNum, String fractalID, List<ServentInfo> alreadySent) {
        super(MessageType.RESULT_SPECIFIC, senderPort, receiverPort, pointNum);
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
