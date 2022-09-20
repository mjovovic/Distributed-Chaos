package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import app.ServentMain;
import job.Point;
import job.PointDrawer;
import servent.message.*;
import servent.message.util.MessageUtil;

public class WelcomeHandler implements MessageHandler{

    private Message clientMessage;

    public WelcomeHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }


    @Override
    public void run() {

        if ( clientMessage.getMessageType() == MessageType.WELCOME ) {

            WelcomeMessage message = (WelcomeMessage) clientMessage;
            int pointNum = message.getPointNum();

            //edge case for the first node
            if ( message.getServents() == null /*&& (message).getFractalId() == null */) {
                //set our fractal id to 0 and start working
                AppConfig.jobMap.get(pointNum).getServentInfo().setFractalId("0");
                AppConfig.jobMap.get(pointNum).setIdle(false);

            } else {

                ServentInfo sender = clientMessage.getSender();
                PointDrawer myPointDrawer = AppConfig.jobMap.get(message.getPointNum()).getPointDrawer();
                ServentInfo myServentInfo = AppConfig.jobMap.get(pointNum).getServentInfo();

                myPointDrawer.setStartingPoints((message).getStartingPoints());
              //  AppConfig.timestampedStandardPrint( "AAAAAAAAAAAAAAAAA " +message.getPointNum()+" " +  AppConfig.jobMap.get(message.getPointNum()).getPointDrawer().getStartingPoints() + "\n");

                boolean check = false;
                for (ServentInfo tmp1 : myServentInfo.getNeighbours())
                    if ( tmp1.getNodeId() == sender.getNodeId() )
                        check = true;

                if(!check)
                    myServentInfo.getNeighbours().add(sender);


                myServentInfo.setFractalId( (message).getFractalId() );
                for ( ServentInfo tmp :  message.getServents() ) {
                    if ( tmp == message.getReceiver()  ) {
                        continue;
                    }
                    check = false;
                    //check to see if its in the list
                    for (ServentInfo tmp1 : myServentInfo.getNeighbours())
                        if ( tmp1.getNodeId() == tmp.getNodeId() )
                            check = true;

                    if(!check)
                        myServentInfo.getNeighbours().add(tmp);
                }
                AppConfig.jobMap.get(pointNum).setIdle(false);
                AppConfig.timestampedStandardPrint("Welcome Neighbours to send " + myServentInfo.getNeighbours() + " " + pointNum);
                Message readyMessage = new ReadyMessage(myServentInfo, sender, pointNum);
                MessageUtil.sendMessage(readyMessage);

            }
        }

    }
}
