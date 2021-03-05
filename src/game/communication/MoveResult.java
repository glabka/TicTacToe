package game.communication;

public enum MoveResult {
	GAME_ALREADY_FULL, NOT_YOUR_TURN,
	ALREADY_FILLED_UP_SQUARE, MOVE_OUT_OF_RANGE,
	GRID_SIZE_TOO_SMALL, GRID_SIZE_TOO_BIG, STREAK_LENGTH_TOO_SMALL,
	STREAK_LENGTH_BIGGER_THAN_GRID
}
