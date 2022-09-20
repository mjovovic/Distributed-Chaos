package servent.message;

import app.ServentInfo;
import job.Point;

import java.util.List;

public class StatusReplyMessage extends BasicMessage{

    private List<Point> pointsDrawn;
    private String fractalID;


    public StatusReplyMessage(ServentInfo senderPort, ServentInfo receiverPort, int pointNum, String fracatlID, List<Point> pointsDrawn) {
        super(MessageType.STATUS_REPLY, senderPort, receiverPort, pointNum);
        this.pointsDrawn = pointsDrawn;
        this.fractalID = fracatlID;
    }

    public String getFractalID() {
        return fractalID;
    }

    public List<Point> getPointsDrawn() {
        return pointsDrawn;
    }
}
