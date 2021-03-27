package game.communication;

import custom_exceptions.WrongMessageException;
import game.GameMetaData;
import game.communication.enums.CommunicationError;
import game.communication.enums.CommunicationProtocolValue;

public class MessageConsistencyChecker {
	
	/**
	 * Method returns null if message gives a sense otherwise
	 * it returns appropriate message for sender
	 * @param message client message
	 * @return null if message gives a sense otherwise
	 * it returns appropriate message for sender
	 */
	public static Message checkClientMessage(Message message) {
		Message response = checkClientMessageInner(message);
		if (checkServerMessage(response)) {
			return response;
		} else {
			throw new WrongMessageException("illegal server message");
		}
	}
	
	private static Message checkClientMessageInner(Message message) {
		Message response = null;
		
		String gameName = message.getGameName();
		if (gameName == null || gameName.isEmpty()) {
			response = Message.createMessage(CommunicationProtocolValue.ERROR);
			response.setCommunicationError(CommunicationError.REQUIRED_FIELD_NULL_OR_EMPTY);
			response.setErrorInfo("gameName null or empty");
			return response;
		}
		
		if (message.getCommunicationProtocolValue() == null) {
			response = Message.createMessage(gameName, CommunicationProtocolValue.ERROR);
			response.setCommunicationError(CommunicationError.REQUIRED_FIELD_NULL_OR_EMPTY);
			response.setErrorInfo("Field communicationProtocolValue can not be null.");
			return response;
		} else if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.CREATE_GAME) {
			GameMetaData gameMetaData = message.getGameMetaData();
			if (gameMetaData == null) {
				response = Message.createMessage(gameName, CommunicationProtocolValue.ERROR);
				response.setCommunicationError(CommunicationError.REQUIRED_FIELD_NULL_OR_EMPTY);
				response.setErrorInfo("gameMetadata null or empty");
				return response;
			}
		} else if (message.getCommunicationProtocolValue() == CommunicationProtocolValue.MY_MOVE) {
			Move mv = message.getMove();
			if (mv == null) {
				response = Message.createMessage(gameName, CommunicationProtocolValue.ERROR);
				response.setCommunicationError(CommunicationError.REQUIRED_FIELD_NULL_OR_EMPTY);
				response.setErrorInfo("move was null");
				return response;
			}
		}
		
		return response;
	}
	
	public static boolean checkServerMessage(Message message) {
		if (message == null) {
			return true;
		}
		// TODO
		return true;
	}
}
