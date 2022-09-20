package servent.message;

import app.ServentInfo;
import job.Point;

import java.util.List;

public class ResultReplyMessage extends BasicMessage{

    private List<Point> pointsDrawn;
    private String fractalID;

    public ResultReplyMessage(ServentInfo senderPort, ServentInfo receiverPort, int pointNum, String fracatlID, List<Point> pointsDrawn) {
        super(MessageType.RESULT_REPLY, senderPort, receiverPort, pointNum);
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
