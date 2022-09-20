package app;

import cli.CLIParser;
import job.Job;
import job.PointDrawer;
import servent.SimpleServentListener;
import servent.message.HailMessage;
import servent.message.Message;
import servent.message.util.MessageUtil;

import java.util.Map;

/**
 * Describes the procedure for starting a single Servent
 *
 * @author bmilojkovic
 */
public class ServentMain {

	/**
	 * Command line arguments are:
	 * 0 - path to servent list file
	 * 1 - this servent's id
	 */

	public static void main(String[] args) {
		if (args.length != 2) {
			AppConfig.timestampedErrorPrint("Please provide servent list file and id of this servent.");
		}
		
		int serventId = -1;
		int portNumber = -1;
		
		String serventListFile = args[0];
		
		try {
			serventId = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			AppConfig.timestampedErrorPrint("Second argument should be an int. Exiting...");
			System.exit(0);
		}
		
		AppConfig.readConfig(serventListFile, serventId);
		
		try {
			portNumber = AppConfig.myServentInfo.getListenerPort();
			
			if (portNumber < 1000 || portNumber > 2000) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException e) {
			AppConfig.timestampedErrorPrint("Port number should be in range 1000-2000. Exiting...");
			System.exit(0);
		}
		
		AppConfig.timestampedStandardPrint("Starting servent " + AppConfig.myServentInfo);
		
		SimpleServentListener simpleListener = new SimpleServentListener();
		Thread listenerThread = new Thread(simpleListener);
		listenerThread.start();
		
		CLIParser cliParser = new CLIParser(simpleListener);
		Thread cliThread = new Thread(cliParser);
		cliThread.start();


		/*for (Map.Entry<Integer, Job> tmp : AppConfig.jobMap.entrySet() ){
			Message hailMessage = new HailMessage(AppConfig.myServentInfo, AppConfig.bsInfo, tmp.getKey());
			MessageUtil.sendMessage(hailMessage);
		}
*/

	}
}
