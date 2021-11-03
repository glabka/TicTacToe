package strategies;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import game_components.ValuedMove;
import grid_computations.Coordinate;
import game_components.Grid;
import game_components.Square.SVal;
import strategies.heuristics.HeuristicCommon;
import strategies.support_classes.MoveAndDepth;
import strategies.support_classes.RatedCoordinate;

public class StrategiesCommon {

	public static List<MoveAndDepth> mdfromRatedCoosList(List<RatedCoordinate> ratedCoos, SVal val, int depth) {
		List<MoveAndDepth> list = new LinkedList<>();
		
		for(RatedCoordinate coo : ratedCoos) {
			list.add(new MoveAndDepth(new ValuedMove(coo, val), depth));
		}
		
		return list;
	}
	
	public static SVal getPlayer(int depth, SVal sValOf0thDepth) {
		if(depth % 2 == 0) {
			return sValOf0thDepth;
		} else {
			return SVal.getOpposite(sValOf0thDepth);
		}
	}
	
	public static boolean opponentsTurn(int depth, SVal sValOf0thDepth) {
		return getPlayer(depth, sValOf0thDepth) == sValOf0thDepth ? false : true;
	}
	
	public static <T> void addToBeginingOfList(List<T> listForAddingTo, List<T> listToBeAdded) {
		Collections.reverse(listToBeAdded);
		for(T t : listToBeAdded) {
			listForAddingTo.add(0, t);
		}
	}
	
	public static Coordinate getMiddleOrFirstEmptyCoo(Grid g) {
		return HeuristicCommon.getMiddleOrFirstEmptyCoo(g);
	}
	
}
