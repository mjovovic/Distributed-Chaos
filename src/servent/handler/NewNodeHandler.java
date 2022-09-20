package servent.handler;

import app.AppConfig;
import job.Point;
import app.ServentInfo;
import job.PointDrawer;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.NewNodeMessage;
import servent.message.WelcomeMessage;
import servent.message.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public class NewNodeHandler implements MessageHandler {

	private Message clientMessage;
	
	public NewNodeHandler(Message clientMessage) {
		this.clientMessage = clientMessage;
	}
	
	@Override
	public void run() {

			if (clientMessage.getMessageType() == MessageType.NEW_NODE) {


				NewNodeMessage message = (NewNodeMessage) clientMessage;
				int numOfPoint = message.getPointNum();

				ServentInfo myServentInfo = AppConfig.jobMap.get(numOfPoint).getServentInfo();
				PointDrawer myPointDrawer = AppConfig.jobMap.get(numOfPoint).getPointDrawer();

				double coefficient = AppConfig.jobMap.get(numOfPoint).getCoefficient();

				// setting up childern with new points and fractal indexes
				try{

					for (int i = 1; i < numOfPoint; i++) {

						ServentInfo tmp = message.getServents().get(i);

						if ( !myServentInfo.getNeighbours().contains(tmp) ) {
							myServentInfo.getNeighbours().add(tmp);
						}

						List<Point> newPoints = new ArrayList<>();
						Point fixedPoint = myPointDrawer.getStartingPoints().get(i);
						newPoints.add(fixedPoint);

						for ( int j = (1 + i)%numOfPoint; j != i; j = (j+1)% numOfPoint ) {
							int heightDir = -1;
							int widthDir = -1;
							Point chosenPoint = myPointDrawer.getStartingPoints().get(j) ;

							if ( fixedPoint.getxAxis() <= chosenPoint.getxAxis()) {
								heightDir = 1;
							}

							if ( fixedPoint.getyAxis() <= chosenPoint.getyAxis()) {
								widthDir = 1;
							}

							Point newPoint = new Point( (int) ( fixedPoint.getxAxis() + heightDir * Math.abs(chosenPoint.getxAxis() -fixedPoint.getxAxis())* coefficient ),
									(int)(  fixedPoint.getyAxis() + widthDir * Math.abs(chosenPoint.getyAxis() -fixedPoint.getyAxis())  * coefficient ));


							newPoints.add(newPoint);
						}

						AppConfig.timestampedStandardPrint(tmp + " " + AppConfig.jobMap.get(numOfPoint).getServentInfo().getFractalId()+i+" " + newPoints);
						Message welcome = new WelcomeMessage(message.getReceiver(), tmp, AppConfig.jobMap.get(numOfPoint).getServentInfo().getFractalId()+i,
								message.getServents(), newPoints,
								message.getPointNum());

						MessageUtil.sendMessage(welcome);
					}

					//setting up our parent node
					List<Point> parentPoints = new ArrayList<>();
					Point fixedPoint = myPointDrawer.getStartingPoints().get(0);
					parentPoints.add(fixedPoint);
					for (int j = 1; j < numOfPoint; j++ ) {
						int heightDir = -1;
						int widthDir = -1;
						Point chosenPoint = myPointDrawer.getStartingPoints().get(j) ;

						if ( fixedPoint.getxAxis() <= chosenPoint.getxAxis()) {
							heightDir = 1;
						}

						if ( fixedPoint.getyAxis() <= chosenPoint.getyAxis()) {
							widthDir = 1;
						}

						Point newPoint = new Point( (int) ( fixedPoint.getxAxis() + heightDir * Math.abs(chosenPoint.getxAxis() -fixedPoint.getxAxis()) * coefficient ),
								(int)(  fixedPoint.getyAxis() + widthDir * Math.abs(chosenPoint.getyAxis() -fixedPoint.getyAxis())  * coefficient ) );
						parentPoints.add(newPoint);

					}
					myPointDrawer.setStartingPoints( parentPoints );
					myServentInfo.setFractalId( myServentInfo.getFractalId() + "0" );

				}catch (Exception e){
					AppConfig.timestampedErrorPrint(e.getMessage());
					e.printStackTrace();
				}
			}

	}

}
