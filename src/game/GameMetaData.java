package game;

public class GameMetaData {
	
	private final int gridSize;
	private final int streakLength;
	
	public GameMetaData(int gridSize, int streakLength) {
		this.gridSize = gridSize;
		this.streakLength = streakLength;
	}

	public int getGridSize() {
		return gridSize;
	}

	public int getStreakLength() {
		return streakLength;
	}
	
	
}
