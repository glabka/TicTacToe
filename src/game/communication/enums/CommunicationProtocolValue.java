package game.communication.enums;

public enum CommunicationProtocolValue {
	// server values
	OPPONENTS_MOVE, MOVE_RESULT,
	ERROR,
	PLAY_FIRT_MOVE,
	// client values
	MY_MOVE,
	CREATE_GAME, // automatically registers you as player
	REGISTER_FOR_GAME
	// shared values
}
