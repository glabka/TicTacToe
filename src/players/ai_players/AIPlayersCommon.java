package players.ai_players;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import game_components.Move;
import game_components.Square.SVal;
import players.ai_players.support_classes.MoveAndDepth;
import players.ai_players.support_classes.RatedCoordinate;

public class AIPlayersCommon {

	public static List<MoveAndDepth> mdfromRatedCoosList(List<RatedCoordinate> ratedCoos, SVal val, int depth) {
		List<MoveAndDepth> list = new LinkedList<>();
		
		for(RatedCoordinate coo : ratedCoos) {
			list.add(new MoveAndDepth(new Move(coo, val), depth));
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
	
	public static <T> void addToBeginingOfList(List<T> listForAddingTo, List<T> listToBeAdded) {
		Collections.reverse(listToBeAdded);
		for(T t : listToBeAdded) {
			listForAddingTo.add(0, t);
		}
	}
	
}
