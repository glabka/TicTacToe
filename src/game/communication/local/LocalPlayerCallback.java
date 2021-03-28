package game.communication.local;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import game.communication.IPlayerCallback;
import game.communication.Message;

public class LocalPlayerCallback implements IPlayerCallback {

	private Object lock;
	private List<Message> messages = Collections.synchronizedList(new LinkedList<Message>());
	
	
	public LocalPlayerCallback(Object lock) {
		this.lock = lock;
	}
	
	@Override
	public void sendMessage(Message message) throws IOException {
		synchronized(messages) {
			messages.add(message);
		}
		synchronized (lock) {
			lock.notify();
		}
	}
	
	public List<Message> getMessages() {
		return tranferMessagesToNewList();
	}
	
	private List<Message> tranferMessagesToNewList() {
		List<Message> newList = new LinkedList<>();
		synchronized(messages) {
			for (int i = 0; i < messages.size(); i++) {
				newList.add(messages.remove(0));
			}
		}
		return newList;
	}
	
	public Message getMessage() {
		if (messages.size() > 0) {
			return messages.remove(0);
		} else {
			return null;
		}
	}
	
}
