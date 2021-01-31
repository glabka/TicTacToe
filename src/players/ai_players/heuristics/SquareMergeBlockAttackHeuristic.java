package players.ai_players.heuristics;

import java.util.List;
import java.util.stream.Collectors;

import game_components.Grid;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.PotentialStreak;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.AbstractRatedCoosFilter;
import players.ai_players.support_classes.RatedCoordinate;

public class SquareMergeBlockAttackHeuristic extends AbstractSquareHeuristic {

	private AbstractRatedCoosFilter filter;	
	
	public SquareMergeBlockAttackHeuristic(AbstractCooValFromStreakEstimator cooEstimator) {
		super(cooEstimator);
	}
	
	public SquareMergeBlockAttackHeuristic(AbstractCooValFromStreakEstimator cooEstimator, AbstractRatedCoosFilter filter) {
		super(cooEstimator);
		this.filter = filter;
	}

	@Override
	public List<RatedCoordinate> getRatedCoos(SVal playersSVal, Grid g, int streakLength) {
		List<PotentialStreak> opponentsPotStreaks = Computations.getAllPotentialStreaks(g, SVal.getOpposite(playersSVal), streakLength);
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, playersSVal, streakLength);
		
		List<PotentialStreak> threateningStreaks = opponentsPotStreaks.stream().filter(n -> n.filledCoosSize() >= streakLength - 2).collect(Collectors.toList());
		
//		Comparator<PotentialStreak> mostFullStreakComparator = (PotentialStreak s1, PotentialStreak s2) -> Integer.compare(s1.filledCoosSize(), s2.filledCoosSize());
		List<RatedCoordinate> ratedCoos;
		if(threateningStreaks.size() > 1) {
			// best defense values form point of view of defending
			System.out.println("Best defense according to defense"); // debug
			ratedCoos = filter == null ? HeuristicCommon.getBestCoosFromStreaksRatedFromAllStreaks(opponentsPotStreaks, threateningStreaks, cooEstimator) :
				HeuristicCommon.getBestCoosFromStreaksRatedFromAllStreaksFiltered(opponentsPotStreaks, threateningStreaks, cooEstimator, filter);
		} else if (threateningStreaks.size() == 1) {
			// best defense values from point of view of offense
			System.out.println("Best defense according to offense"); // debug
			ratedCoos = filter == null ? HeuristicCommon.getBestCoosFromStreaksRatedFromAllStreaks(potStreaks, threateningStreaks, cooEstimator) :
				HeuristicCommon.getBestCoosFromStreaksRatedFromAllStreaksFiltered(potStreaks, threateningStreaks, cooEstimator, filter);
			
			if(ratedCoos == null || ratedCoos.isEmpty()) {
				// best values form point of view of defending
				System.out.println("Best defense according to defense"); // debug
				ratedCoos = filter == null ? HeuristicCommon.getBestCoosFromStreaksRatedFromAllStreaks(opponentsPotStreaks, threateningStreaks, cooEstimator) :
					HeuristicCommon.getBestCoosFromStreaksRatedFromAllStreaksFiltered(opponentsPotStreaks, threateningStreaks, cooEstimator, filter);
			}
		} else {
			// best values from point of view of attacking
			System.out.println("attack"); // debug
			List<PotentialStreak> streaksForAttack = HeuristicCommon.getAllMostFilledStreaks(potStreaks);
			ratedCoos = filter == null ? HeuristicCommon.getBestCoosFromStreaksRatedFromAllStreaks(potStreaks, streaksForAttack, cooEstimator) :
				HeuristicCommon.getBestCoosFromStreaksRatedFromAllStreaksFiltered(potStreaks, streaksForAttack, cooEstimator, filter);
		}
		
		
		if(ratedCoos == null || ratedCoos.isEmpty()) {
			System.out.println("ratedCoos == null || ratedCoos.isEmpty()"); // debug
			return HeuristicCommon.getMiddleOrFirstEmptyCoo(g);
		} else {		
			return ratedCoos;
		}
	}

}
