package servent.message;

import job.Point;
import app.ServentInfo;

import java.util.List;

public class WelcomeMessage extends BasicMessage {

	private static final long serialVersionUID = -8981406250652693908L;

	private List<ServentInfo> servents;
	private String fractalId;
	private List<Point> startingPoints;


	public WelcomeMessage(ServentInfo sender, ServentInfo receiver, String fractalId, List<ServentInfo> servents, List<Point> startingPoints, int pointNum) {
		super(MessageType.WELCOME, sender, receiver, pointNum);
		this.fractalId = fractalId;
		this.servents = servents;
		this.startingPoints = startingPoints;
	}



	public List<Point> getStartingPoints() {
		return startingPoints;
	}

	public String getFractalId() {
		return fractalId;
	}

	public List<ServentInfo> getServents() {
		return servents;
	}
}
