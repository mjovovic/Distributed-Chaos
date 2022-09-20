package cli.command;

import app.AppConfig;
import job.Job;
import servent.message.HailMessage;
import servent.message.Message;
import servent.message.util.MessageUtil;

import java.util.Map;

public class StartCommand implements CLICommand{
    @Override
    public String commandName() {
        return "start";
    }

    @Override
    public void execute(String args) {
        String name = args;

        int numOfPoints = -1;
        for ( Map.Entry<Integer, Job> tmp : AppConfig.jobMap.entrySet() ) {
            if ( tmp.getValue().getName().equals(name) ) {
                numOfPoints = tmp.getValue().getPointNum();
                break;
            }
        }

        Message hailMessage = new HailMessage(AppConfig.myServentInfo, AppConfig.bsInfo, numOfPoints);
        MessageUtil.sendMessage(hailMessage);

    }
}
