package servent.message;

import job.PointDrawer;
import app.ServentInfo;

import java.util.List;

public class NewNodeMessage extends BasicMessage {

	private static final long serialVersionUID = 3899837286642127636L;

	private List<ServentInfo> servents;


	public NewNodeMessage(ServentInfo sender, ServentInfo receiver, List<ServentInfo> servents, int pointNum) {
		super(MessageType.NEW_NODE, sender, receiver, pointNum);
		this.servents = servents;
	}

	public List<ServentInfo> getServents() {
		return servents;
	}
}
