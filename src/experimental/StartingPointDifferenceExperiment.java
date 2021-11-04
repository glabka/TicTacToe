package experimental;

import game.Game;
import game.Result;
import game_components.Grid;
import game_components.Square.SVal;
import players.AIPlayer;
import strategies.AbstractStrategy;

public class StartingPointDifferenceExperiment {
	
	public void findSignificantIsItToStart() {
		
	}
	
	public void compareTwoStrategies(int[] gridSizes, int[] streakLengths, AbstractStrategy strategy1, AbstractStrategy strategy2) {
		AIPlayer p1 = new AIPlayer(SVal.CROSS, "player 1", 0, strategy1);
		AIPlayer p2 = new AIPlayer(SVal.CIRCLE, "player 2", 0, strategy2);
		Result resultP1Starts = calculateSomeGames(p1, p2, gridSizes, streakLengths);
		Result resultP2Starts = calculateSomeGames(p2, p1, gridSizes, streakLengths);
		
		System.out.println("Strategy: " + strategy1 + " vs " + strategy2);
		int firstDiff = resultP1Starts.getNumOfLosses() - resultP2Starts.getNumOfWins();
		int secondDiff = resultP1Starts.getNumOfWins() - resultP2Starts.getNumOfLosses();
		int thirdDiff = resultP1Starts.getNumOfTies() + resultP2Starts.getNumOfTies();
		double runsInTotal = resultP1Starts.getNumOfWins() + resultP1Starts.getNumOfLosses() + resultP1Starts.getNumOfTies();
		System.out.println(resultP1Starts + " vs " + resultP2Starts +
				" diff result: " + firstDiff + ", " + secondDiff + ", " + thirdDiff +
				" ratio results:" + (firstDiff/ (2 * runsInTotal)) + ", " + (secondDiff/ (2 * runsInTotal)) + ", " + (thirdDiff/ (2 * runsInTotal)));
		}
	
	/**
	 * Calculates only games when p1 starts.
	 */
	private Result calculateSomeGames(AIPlayer p1, AIPlayer p2, int[] gridSizes, int[] streakLengths) {
		Game game;
		int p1Winns = 0;
		int p1Losses = 0;
		int ties = 0;
		
		for (int i = 0; i < gridSizes.length; i++) {
			int gridSize = gridSizes[i];
			int streakLength = streakLengths[i];
			p1.setStreakLength(streakLength); 
			p2.setStreakLength(streakLength);
			Grid g = new Grid(gridSize);
			game = new Game(p1, p2, g, streakLength);
			SVal winner = game.play();
			
			// collecting results
			if (winner == null) {
				ties++;
			} else if (winner.equals(p1.getSVal())) {
				p1Winns++;
			} else {
				p1Losses++;
			}
		}
		
		return new Result(p1Winns, p1Losses, ties);
	}
}
