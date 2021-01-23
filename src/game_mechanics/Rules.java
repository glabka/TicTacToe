/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_mechanics;

import java.util.Collection;

import custom_exceptions.IllegalSetOfArgumentsException;
import game_components.Grid;
import game_components.Square.SVal;
import grid_computations.Computations;
import grid_computations.FullStreak;

/**
 *
 * @author glabka
 */
public class Rules {

    /**
     * Function finds winner of Tic Tac Toe. Returns CIRCLE, CROSS or null (in
     * case of no winner yet or at all).
     *
     * @param g
     * @param streakLength
     * @return
     */
    public static SVal findWinner(Grid g, int streakLength) {
        if (g.size() < streakLength) {
            throw new IllegalArgumentException("streakLength can't be bigger then g.size().");
        }
        if (checkEverything(g, SVal.CROSS, streakLength)) {
            return SVal.CROSS;
        } else if (checkEverything(g, SVal.CIRCLE, streakLength)) {
            return SVal.CIRCLE;
        } else {
            return null;
        }
    }
    
    public static boolean endOfGame(Grid g, int streakLengtt) {
    	if(findWinner(g, streakLengtt) != null) {
    		return true;
    	} else if (g.isGridFull()) {
    		return true;
    	} else {
    		return false;
    	}
    }

    public static void test(Grid g) {
//        for (int i = 0; i < g.size(); i++) {
//            System.out.println(checkRow(g, SVal.CIRCLE, 3, i));
//        }
//        for (int i = 0; i < g.size(); i++) {
//            System.out.println(checkColumn(g, SVal.CROSS, 3, i));
//        }
//        System.out.println(checkLeftDiagonal(g, SVal.CIRCLE, 2, 0, 0));
//        System.out.println(checkLeftDiagonal(g, SVal.CIRCLE, 2, 0, 1));
//        System.out.println(checkLeftDiagonal(g, SVal.CIRCLE, 2, 0, 2));
//        System.out.println("");
//        System.out.println(checkLeftDiagonal(g, SVal.CROSS, 3, 0, 0));

//        System.out.println(checkLeftDiagonals(g, SVal.CIRCLE, 2));
//        System.out.println(checkLeftDiagonals(g, SVal.CIRCLE, 3));
//        System.out.println(checkLeftDiagonals(g, SVal.CROSS, 3));
//        System.out.println(checkRightDiagonal(g, SVal.CIRCLE, 2, 0, 2));
//        System.out.println(checkRightDiagonal(g, SVal.CROSS, 2, 2, 4));
//        System.out.println(checkRightDiagonal(g, SVal.CROSS, 2, 3, 4));
//        System.out.println(checkRightDiagonals(g, SVal.CROSS, 3));
//        System.out.println(checkRightDiagonals(g, SVal.CROSS, 2));
//        System.out.println(checkRightDiagonals(g, SVal.CIRCLE, 3));
//        System.out.println(checkRightDiagonals(g, SVal.CIRCLE, 2));
    }

    private static boolean checkEverything(Grid g, SVal val, int streakLength) {
        Collection<FullStreak> streaks = Computations.getAllFullStreaks(g, val);
        
        for(FullStreak streak : streaks) {
        	if(streak.getLength() >= streakLength) {
        		return true;
        	}
        }
        
        return false;
    }

}
