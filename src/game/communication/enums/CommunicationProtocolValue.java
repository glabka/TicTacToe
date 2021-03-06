package game.communication.enums;

public enum CommunicationProtocolValue {
	// server values
	PLAYER_REGISTERED,
	OPPONENTS_MOVE,
	MOVE_RESULT,
	GAME_METADATA,
	GRID_REPRESENTATION,
	ERROR,
	PLAY_FIRT_MOVE,
	GAME_DOESNT_EXIST,
	GAME_EXISTS,
	GAME_CREATED, // and also player registered
	GAME_OVER,
	// client values
	MY_MOVE,
	CREATE_GAME, // automatically registers you as player
	REGISTER_PLAYER,
	GET_GAME_METADATA,
	GET_GRID_REPRESENTATION,
	DOES_GAME_EXIST
	// shared values
}
