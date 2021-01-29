package players.ai_players.heuristics;

import java.util.List;
import java.util.stream.Collectors;

import game_components.Grid;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.PotentialStreak;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.RatedCoordinate;

public class SquareBlockAttackHeuristic extends AbstractSquareHeuristic {

	
	public SquareBlockAttackHeuristic(AbstractCooValFromStreakEstimator cooEstimator) {
		super(cooEstimator);
	}

	@Override
	public List<RatedCoordinate> getRatedCoos(SVal playersSVal, Grid g, int streakLength) {
		List<PotentialStreak> opponentsPotStreaks = Computations.getAllPotentialStreaks(g, SVal.getOpposite(playersSVal), streakLength);
		List<PotentialStreak> potStreaks = Computations.getAllPotentialStreaks(g, playersSVal, streakLength);
		
		List<PotentialStreak> threateningStreaks = opponentsPotStreaks.stream().filter(n -> n.filledCoosSize() >= streakLength - 2).collect(Collectors.toList());
		
		
		if(!threateningStreaks.isEmpty()) {
			return HeuristicCommon.getBestCoosFromStreaksForAttackingOrBlocking(opponentsPotStreaks, threateningStreaks, cooEstimator);
		} else {
			if(!potStreaks.isEmpty()) {
				List<PotentialStreak> streaksForAtacking = HeuristicCommon.getAllMostFilledStreaks(potStreaks);
				return HeuristicCommon.getBestCoosFromStreaksForAttackingOrBlocking(potStreaks, streaksForAtacking, cooEstimator);
			} else {
				return HeuristicCommon.getMiddleOrFirstEmptyCoo(g);
			}
		}
	}
	

	
	
	

	
	
	
}
