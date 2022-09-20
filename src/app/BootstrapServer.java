package app;

import servent.message.*;
import servent.message.util.MessageUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class BootstrapServer {

	private volatile boolean working = true;
	private Map<Integer, BootstrapServentList> serventListMap;
	private Map<Integer, Mapper> insertionMap;
	private Object lock = new Object();

	
	private class CLIWorker implements Runnable {
		@Override
		public void run() {
			Scanner sc = new Scanner(System.in);
			
			String line;
			while(true) {
				line = sc.nextLine();
				
				if (line.equals("stop")) {
					working = false;
					break;
				}
			}
			
			sc.close();
		}
	}
	
	public BootstrapServer() {
		/*activeServents = new ArrayList<>();
		deletedServents = new ArrayList<>();
		idleServents = new ArrayList<>();*/
		serventListMap = new ConcurrentHashMap<>();
		insertionMap = new ConcurrentHashMap<>();
	}

	public void doBootstrap(int bsPort) {
		Thread cliThread = new Thread(new CLIWorker());
		cliThread.start();

		ServerSocket listenerSocket = null;
		try {
			listenerSocket = new ServerSocket(bsPort);
			listenerSocket.setSoTimeout(1000);
		} catch (IOException e1) {
			AppConfig.timestampedErrorPrint("Problem while opening listener socket.");
			System.exit(0);
		}

		while (working) {
			try {
				Socket clientSocket = listenerSocket.accept();
				Message clientMessage = MessageUtil.readMessage(clientSocket);
				ServentInfo bsInfo = new ServentInfo("localhost", bsPort, -3);
				switch (clientMessage.getMessageType()) {

					case HAIL:

						HailMessage message = ((HailMessage)clientMessage);
						ServentInfo sender = message.getSender();
						//number of points is also the key to our job
						int numOfPoints = message.getPointNum();

						//for the first node init the insertion map
						if ( insertionMap.get(numOfPoints) == null ) {

							//creating a new map that has lists of servents for a specific job
							serventListMap.put(numOfPoints, new BootstrapServentList());
							//creating insertion map
							insertionMap.put(numOfPoints, new Mapper(numOfPoints));
							//adding sender to the active servent map
							serventListMap.get(numOfPoints).getActiveServents().add(sender);

							//sending message to the servent to let him know that he is active
							AppConfig.timestampedStandardPrint("Adding " + sender.getNodeId() +
									" to active list as first in " + message.getPointNum());
							Message welcomeFirst = new WelcomeMessage( bsInfo, sender, "0", null,
									null, numOfPoints);
							MessageUtil.sendMessage(welcomeFirst);


						} else if ( !insertionMap.get(numOfPoints).getMappingMap().containsKey(
								serventListMap.get(numOfPoints).getActiveServents().size() +
								serventListMap.get(numOfPoints).getIdleServents().size() + 1) ) {

							int activeSize = serventListMap.get(numOfPoints).getActiveServents().size();
							int idleSize = serventListMap.get(numOfPoints).getIdleServents().size();
							int deletedSize = serventListMap.get(numOfPoints).getDeletedServents().size();
							sender.setNodeId(activeSize + idleSize - deletedSize);
							//if we are not the first but we cant connect
							AppConfig.timestampedStandardPrint("Adding " + sender +
									" to idleServents in " + message.getPointNum()+"\n");
							serventListMap.get(numOfPoints).getIdleServents().add(sender);
						} else {
							int activeSize = serventListMap.get(numOfPoints).getActiveServents().size();
							int idleSize = serventListMap.get(numOfPoints).getIdleServents().size();
							int deletedSize = serventListMap.get(numOfPoints).getDeletedServents().size();
							sender.setNodeId(activeSize + idleSize - deletedSize);


							// if we are not the first but we can connect
							AppConfig.timestampedStandardPrint("ENTEERED HERE " + serventListMap.get(numOfPoints).getIdleServents() + numOfPoints + "\n");
							//finding the parent id trough insertion map
							int parentId = insertionMap.get(numOfPoints).getMappingMap().get(
									serventListMap.get(numOfPoints).getActiveServents().size() +
									serventListMap.get(numOfPoints).getIdleServents().size() + 1);

							//find the parent info
							ServentInfo parentInfo = null;
							for ( ServentInfo tmp1: serventListMap.get(numOfPoints).getActiveServents() ) {
								if (tmp1.getNodeId() == parentId) {
									parentInfo = tmp1;
									break;
								}
							}
							AppConfig.timestampedStandardPrint(parentId + " \n");
							//creating list of idle servents that will be sent to found parent in order to connect
							List<ServentInfo> sendList = new ArrayList<>();
							sendList.add(parentInfo);
							sendList.addAll(serventListMap.get(numOfPoints).getIdleServents());

							//get all idle servents for that job and put them in active list
							serventListMap.get(numOfPoints).getActiveServents().addAll(serventListMap.get(numOfPoints).getIdleServents());
							serventListMap.get(numOfPoints).getIdleServents().removeAll(sendList);
							sendList.add(sender);
							//add the sender to active nodes and to send list

							serventListMap.get(numOfPoints).getActiveServents().add(sender);

							AppConfig.timestampedStandardPrint("Neighbour list to send " + sendList.toString() + "in " + message.getPointNum());


							Message newNode = new NewNodeMessage( bsInfo , parentInfo, sendList, numOfPoints);
							MessageUtil.sendMessage(newNode);

						}
					break;


					case NUMS:
						numOfPoints = ((NumsMessage)clientMessage).getPointNum();
						sender = clientMessage.getSender();
						Message newNode = new NumsMessage( bsInfo , sender,
								numOfPoints, getServentListMap().get(numOfPoints).getActiveServents().size());
						MessageUtil.sendMessage(newNode);
					break;

					case QUIT_JOB:
						QuitJobMessage quitMessage = (QuitJobMessage)clientMessage;
						numOfPoints = quitMessage.getPointNum();
						List<ServentInfo> idleServents = serventListMap.get(numOfPoints).getIdleServents();
						List<ServentInfo> activeServents = serventListMap.get(numOfPoints).getActiveServents();
						serventListMap.get(numOfPoints).getDeletedServents().add(quitMessage.getSender());
						if (idleServents.size() == 0) {

							ServentInfo toSend = activeServents.get(activeServents.size()-1);
							Message newQuitMessage = new QuitJobMessage(bsInfo, toSend, numOfPoints, quitMessage.getSender());
							MessageUtil.sendMessage(newQuitMessage);
							activeServents.remove(toSend);

						} else {

							ServentInfo toSend = idleServents.get(idleServents.size()-1);
							Message newQuitMessage = new QuitJobMessage(bsInfo, toSend, numOfPoints, quitMessage.getSender());
							MessageUtil.sendMessage(newQuitMessage);
							idleServents.remove(toSend);
						}

					break;

					case IDLE:
						IdleMessage idleMessage = (IdleMessage) clientMessage;
						numOfPoints = idleMessage.getPointNum();
						idleServents = serventListMap.get(numOfPoints).getIdleServents();
						activeServents = serventListMap.get(numOfPoints).getActiveServents();

						idleServents.add(idleMessage.getSender());
						activeServents.remove(idleMessage.getSender());

						AppConfig.timestampedStandardPrint(idleServents.toString());
						AppConfig.timestampedStandardPrint(serventListMap.get(numOfPoints).getDeletedServents().toString());
					break;
				}


			} catch (SocketTimeoutException e) {

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Map<Integer, BootstrapServentList> getServentListMap() {
		return serventListMap;
	}

	/*/ number of nodes in system where n=3 1|3|5|7|9|11|13|15|17.. 1+(n-1)*i (i)iterator starts from 0
// what node it needs to attach to 	   0|0|1|2|0|1 |2 |3 |4 .. n^0, n^1, n^2... n^k% (k)iterator for power
//									  n^0| n^1|     n^2	...			
	public void mapCreator ( int n ) {
		int maxServents = 100;
		int powerCounter = 0;
		int[] a = new int[100];
		
		for(int i = 0; i < 100;) {
			for (int j = 0; j < (int)Math.pow(n, powerCounter); j++) {
				if ( i == 100 ){
					break;
				}
				a[i] = j;
				i++;
			}
			powerCounter++;
		}

		int k = 0;
		for( int i = 0; i < maxServents; i++ ) {
			int key = (1 + (n-1)*i);
			if( key % 2 != 0 ) {
				mappingMap.put(key, a[k]);
				k++;
			}
		}
	}
	*/

	/**
	 * Expects one command line argument - the port to listen on.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			AppConfig.timestampedErrorPrint("Bootstrap started without port argument.");
		}
		
		int bsPort = 0;
		try {
			bsPort = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			AppConfig.timestampedErrorPrint("Bootstrap port not valid: " + args[0]);
			System.exit(0);
		}

		
		AppConfig.timestampedStandardPrint("Bootstrap server started on port: " + bsPort);

		BootstrapServer bs = new BootstrapServer();
		bs.doBootstrap(bsPort);
	}
}
