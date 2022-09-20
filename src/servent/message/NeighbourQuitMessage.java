package servent.message;

import app.ServentInfo;

import java.util.List;

public class NeighbourQuitMessage extends BasicMessage{

    String  parentFractalId;
    private List<ServentInfo> quitServentNeighbours;


    public NeighbourQuitMessage(ServentInfo senderPort, ServentInfo receiverPort, int pointNum, String  parentFractalId, List<ServentInfo> quitServentNeighbours) {
        super(MessageType.NEIGHBOUR_QUIT, senderPort, receiverPort, pointNum);
        this.parentFractalId = parentFractalId;
        this.quitServentNeighbours = quitServentNeighbours;
    }

    public List<ServentInfo> getQuitServentNeighbours() {
        return quitServentNeighbours;
    }

    public String getParentFractalId() {
        return parentFractalId;
    }
}
