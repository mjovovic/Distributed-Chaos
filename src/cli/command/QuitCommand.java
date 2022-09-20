package cli.command;

import app.AppConfig;
import app.ServentInfo;
import job.Job;
import job.Point;
import servent.message.Message;
import servent.message.QuitJobMessage;
import servent.message.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuitCommand implements CLICommand{
    @Override
    public String commandName() {
        return "quit";
    }

    @Override
    public void execute(String args) {

        String name = args;

        int numOfPoints = -1;
        for ( Map.Entry<Integer, Job> tmp : AppConfig.jobMap.entrySet() ) {
            if ( tmp.getValue().getName().equals( name.split(" ")[0]) ) {
                numOfPoints = tmp.getValue().getPointNum();
                break;
            }
        }

        AppConfig.jobMap.get(numOfPoints).getPointDrawer().stop();
        AppConfig.jobMap.get(numOfPoints).getPointDrawer().setPoints( new ArrayList<>() );

        Message quitJobmessage = new QuitJobMessage(AppConfig.jobMap.get(numOfPoints).getServentInfo(), AppConfig.bsInfo, numOfPoints, null);
        MessageUtil.sendMessage(quitJobmessage);






    }
}
