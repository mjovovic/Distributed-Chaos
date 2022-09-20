package servent.handler;

import app.AppConfig;
import servent.message.IdleMessage;
import servent.message.Message;
import servent.message.MessageType;

public class IdleHandler implements MessageHandler{

    private Message clientmessage;

    public IdleHandler(Message clientmessage) {
        this.clientmessage = clientmessage;
    }

    @Override
    public void run() {

    }
}
