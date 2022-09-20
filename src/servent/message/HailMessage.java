package servent.message;

import job.PointDrawer;
import app.ServentInfo;

public class HailMessage extends BasicMessage{


    public HailMessage(ServentInfo sender, ServentInfo receiver, int pointNum) {
        super(MessageType.HAIL, sender, receiver, pointNum);

    }
}
