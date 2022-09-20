package servent.message;

import app.ServentInfo;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A default message implementation. This should cover most situations.
 * If you want to add stuff, remember to think about the modificator methods.
 * If you don't override the modificators, you might drop stuff.
 * @author bmilojkovic
 *
 */
public class BasicMessage implements Message {

	private static final long serialVersionUID = -9075856313609777945L;
	private final MessageType type;
	private final ServentInfo sender;
	private final ServentInfo receiver;
	private final String messageText;
	private final int pointNum;
	//This gives us a unique id - incremented in every natural constructor.
	private static AtomicInteger messageCounter = new AtomicInteger(0);
	private final int messageId;
	
	public BasicMessage(MessageType type, ServentInfo senderPort, ServentInfo receiverPort, int pointNum) {
		this.type = type;
		this.sender = senderPort;
		this.receiver = receiverPort;
		this.pointNum = pointNum;
		this.messageText = "";
		
		this.messageId = messageCounter.getAndIncrement();
	}
	
	public BasicMessage(MessageType type, ServentInfo sender, ServentInfo receiver, String messageText, int pointNum) {
		this.type = type;
		this.sender = sender;
		this.receiver = receiver;
		this.messageText = messageText;
		this.pointNum = pointNum;

		this.messageId = messageCounter.getAndIncrement();
	}

	public int getPointNum() {
		return pointNum;
	}

	@Override
	public MessageType getMessageType() {
		return type;
	}
	
	@Override
	public ServentInfo getReceiver() {
		return receiver;
	}
	
	@Override
	public String getReceiverIpAddress() {
		return "localhost";
	}
	
	@Override
	public ServentInfo getSender() {
		return sender;
	}

	@Override
	public String getMessageText() {
		return messageText;
	}
	
	@Override
	public int getMessageId() {
		return messageId;
	}
	
	/**
	 * Comparing messages is based on their unique id and the original sender port.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BasicMessage) {
			BasicMessage other = (BasicMessage)obj;
			
			if (getMessageId() == other.getMessageId() &&
				getSender().getListenerPort() == other.getSender().getListenerPort()) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Hash needs to mirror equals, especially if we are gonna keep this object
	 * in a set or a map. So, this is based on message id and original sender id also.
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getMessageId(), getSender().getListenerPort());
	}
	
	/**
	 * Returns the message in the format: <code>[sender_id|sender_port|message_id|text|type|receiver_port|receiver_id]</code>
	 */
	@Override
	public String toString() {
		return "["  + "|" + getSender().getListenerPort() + "|" + getMessageId() + "|" +
					getMessageText() + "|" + getMessageType() + "|" +
					getReceiver().getListenerPort() + "|" + "]";
	}

}
