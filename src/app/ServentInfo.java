package app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This is an immutable class that holds all the information for a servent.
 *
 * @author bmilojkovic
 */
public class ServentInfo implements Serializable {

	private static final long serialVersionUID = 5304170042791281555L;
	private final String ipAddress;
	private final int listenerPort;
	private int nodeId;
	private String fractalId;
	private List<ServentInfo> neighbours;
	
	public ServentInfo(String ipAddress, int listenerPort, int nodeId) {
		this.ipAddress = ipAddress;
		this.listenerPort = listenerPort;
		this.nodeId = nodeId;
		this.neighbours = new ArrayList<>();
	}

	public void setNeighbours(List<ServentInfo> neighbours) {
		this.neighbours = neighbours;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public int getNodeId() {
		return nodeId;
	}

	public List<ServentInfo> getNeighbours() {
		return neighbours;
	}


	public String getIpAddress() {
		return ipAddress;
	}

	public int getListenerPort() {
		return listenerPort;
	}

	
	@Override
	public String toString() {
		return "[" + ipAddress + "|" + listenerPort + "]";
	}


	public String getFractalId() {
		return fractalId;
	}

	public void setFractalId(String fractalId) {
		this.fractalId = fractalId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ServentInfo that = (ServentInfo) o;
		return listenerPort == that.listenerPort && nodeId == that.nodeId && Objects.equals(ipAddress, that.ipAddress);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ipAddress, listenerPort, nodeId);
	}
}
