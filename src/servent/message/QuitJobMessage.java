package servent.message;

import app.ServentInfo;

public class QuitJobMessage extends BasicMessage{

    ServentInfo serventToSet;

    public QuitJobMessage(ServentInfo senderPort, ServentInfo receiverPort, int pointNum, ServentInfo serventToSet) {
        super(MessageType.QUIT_JOB, senderPort, receiverPort, pointNum);
        this.serventToSet = serventToSet;
    }

    public ServentInfo getServentToSet() {
        return serventToSet;
    }
}
