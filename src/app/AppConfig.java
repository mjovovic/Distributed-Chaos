package app;

import job.Job;
import job.Point;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class contains all the global application configuration stuff.
 * @author bmilojkovic
 *
 */
public class AppConfig {

	/**
	 * Convenience access for this servent's information
	 */
	public static ServentInfo myServentInfo;
	public static ServentInfo bsInfo;
	/**
	 * Print a message to stdout with a timestamp
	 * @param message message to print
	 */
	public static void timestampedStandardPrint(String message) {
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		
		System.out.println(timeFormat.format(now) + " - " + message);
	}
	
	/**
	 * Print a message to stderr with a timestamp
	 * @param message message to print
	 */
	public static void timestampedErrorPrint(String message) {
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
		
		System.err.println(timeFormat.format(now) + " - " + message);
	}
	
	public static boolean INITIALIZED = false;
	public static int BOOTSTRAP_PORT;
	public static int SERVENT_COUNT;
	public static int JOB_COUNT;
	public static int WEAK_POINT;
	public static int STRONG_POINT;
	public static Map<Integer, Job> jobMap;



	public static void readConfig(String configName, int serventId){
		jobMap = new ConcurrentHashMap<>();
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(new File(configName)));
			
		} catch (IOException e) {
			timestampedErrorPrint("Couldn't open properties file. Exiting...");
			System.exit(0);
		}
		
		try {
			BOOTSTRAP_PORT = Integer.parseInt(properties.getProperty("bs.port"));
		} catch (NumberFormatException e) {
			timestampedErrorPrint("Problem reading bootstrap_port. Exiting...");
			System.exit(0);
		}
		
		try {
			SERVENT_COUNT = Integer.parseInt(properties.getProperty("servent_count"));
		} catch (NumberFormatException e) {
			timestampedErrorPrint("Problem reading servent_count. Exiting...");
			System.exit(0);
		}

		try {
			WEAK_POINT = Integer.parseInt(properties.getProperty("weak.point"));
		} catch (NumberFormatException e) {
			timestampedErrorPrint("Problem reading weak point. Exiting...");
			System.exit(0);
		}
		try {
			STRONG_POINT = Integer.parseInt(properties.getProperty("strong.point"));
		} catch (NumberFormatException e) {
			timestampedErrorPrint("Problem reading point. strong point...");
			System.exit(0);
		}

		String portProperty = "servent"+serventId+".port";
		
		int serventPort = -1;
		
		try {
			serventPort = Integer.parseInt(properties.getProperty(portProperty));
		} catch (NumberFormatException e) {
			timestampedErrorPrint("Problem reading " + portProperty + ". Exiting...");
			System.exit(0);
		}

		bsInfo = new ServentInfo("localhost", BOOTSTRAP_PORT, -3);
		myServentInfo = new ServentInfo("localhost", serventPort, serventId);
//jobString=<name=triangle-N=3-P=0.5-W=510-H=510-A=0,500;500,0;67,250<name=sqare-N=4-P=0.1-W=510-H=510-A=0,0;500,0;0,500;500,0;500,500
		try {
			String jobsString = properties.getProperty("jobs");
			String[] jobString = jobsString.split("<");
			JOB_COUNT = jobString.length-1;

			for( int i = 0; i < JOB_COUNT; i++ ) {

				String[] jobConfig = jobString[i+1].split("-");
				String name;
				int n;
				double coefficient;
				int width;
				int height;
				List<Point> startPoints = new ArrayList<>();

				name = jobConfig[0].split("=")[1];
				n = Integer.parseInt(jobConfig[1].split("=")[1]);
				coefficient = Double.parseDouble(jobConfig[2].split("=")[1]);
				width = Integer.parseInt(jobConfig[3].split("=")[1]);
				height = Integer.parseInt(jobConfig[4].split("=")[1]);
				String tmp = jobConfig[5].split("=")[1];
				String[] splitter = tmp.split(";");
				for(int j = 0; j < splitter.length; j++){
					String[] tmp1 = splitter[j].split(",");

					startPoints.add(new Point(Integer.parseInt(tmp1[0]), Integer.parseInt(tmp1[1])));
				}

				Job newJob = new Job(name, n, coefficient, width, height, startPoints, new ServentInfo("localhost", serventPort, serventId));
				jobMap.put(n, newJob);

			}

		} catch (NumberFormatException e) {
			timestampedErrorPrint("Problem reading Jobs. Exiting...");
			System.exit(0);
		}

	}

}
