package game;

/**
 * Class made for holding and sorting results for finding best AI combination
 * @author glabk
 *
 */
public class Result implements Comparable<Result>{
	
	private String combination;
	private int numberOfLosses;
	private int numberOfWins;
	private int numberOfTies;

	public Result(String combination, int numberOfWins, int numberOfLosses, int numberOfTies) {
		super();
		this.combination = combination;
		this.numberOfLosses = numberOfLosses;
		this.numberOfWins = numberOfWins;
		this.numberOfTies = numberOfTies;
	}
	
	public String getCombination() {
		return combination;
	}
	
	public int getNumOfLosses() {
		return numberOfLosses;
	}
	
	public int getNumOfWins() {
		return numberOfWins;
	}

	public int getNumOfTies() {
		return numberOfTies;
	}

	@Override
	public int compareTo(Result arg0) {
		double propDiff = (numberOfWins - numberOfLosses) / (double) (numberOfWins + numberOfLosses + numberOfTies);
		double arg0PropDiff = (arg0.getNumOfWins() - arg0.getNumOfLosses()) / (double) (arg0.getNumOfWins() + arg0.getNumOfLosses() + arg0.getNumOfTies());
		
		if(propDiff < arg0PropDiff) {
			return -1;
		} else if (propDiff > arg0PropDiff) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public String toString() {
		return "w: " + this.getNumOfWins() + ", l: " + this.getNumOfLosses() + " , t: " + this.getNumOfTies() + ", " +this.getCombination();
	}
}
