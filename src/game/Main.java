/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import game_mechanics.Rules;
import grid_computations.Computations;
import grid_computations.FullStreak;
import grid_computations.PotStreakFilledLengthComparator;
import grid_computations.PotentialStreak;
import players.Player;
import players.ai_players.MergeAIPlayer;
import players.ai_players.AbstractAIPlayer;
import players.ai_players.DepthAIPlayer;
import players.ai_players.DumbAIPlayer;
import players.ai_players.DumbAIPlayer2;
import players.ai_players.OneStepAIPlayer;
import players.ai_players.TreeEvaluationAIPlayer;
import players.ai_players.heuristics.SquareBlockAttackHeuristic;
import players.ai_players.heuristics.AbstractGridHeuristic;
import players.ai_players.heuristics.AbstractSquareHeuristic;
import players.ai_players.heuristics.GridDiffHeuristic;
import players.ai_players.heuristics.SquareAttackHeuristic;
import players.ai_players.heuristics.SquareMergedHeuristic;
import players.ai_players.support_classes.AbstractCooValFromStreakEstimator;
import players.ai_players.support_classes.AbstractRatedCoosFilter;
import players.ai_players.support_classes.FewBestRatedCoosFilter;
import players.ai_players.support_classes.PoweredLengthCooValEstimator;
import players.ai_players.support_classes.SortedNodesTree;
import players.ai_players.support_classes.SortedTreeNode;
import players.ui_players.UIPlayer;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import game_components.Grid;
import game_components.Square;
import game_components.Square.SVal;

/**
 *
 * @author glabka
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO compare with https://www.quora.com/What-are-some-good-project-ideas-for-an-undergraduate-object-oriented-programming-course-using-Java
        
        Grid g = new Grid(5);
        g.insert(1, 0, Square.SVal.CIRCLE);
        g.insert(1, 1, Square.SVal.CIRCLE);
        g.insert(1, 2, Square.SVal.CIRCLE);
        g.insert(1, 3, Square.SVal.CIRCLE);
        
        g.insert(0, 1, Square.SVal.CIRCLE);
        g.insert(0, 2, Square.SVal.CIRCLE);
        g.insert(0, 4, Square.SVal.CIRCLE);
        
//        g.insert(3,2, SVal.CIRCLE);
        
        g.insert(2, 4, Square.SVal.CROSS);
        g.insert(3, 4, Square.SVal.CROSS);
        g.insert(4, 4, Square.SVal.CROSS);
        
        g.insert(2, 2, Square.SVal.CROSS);
        g.insert(3, 3, Square.SVal.CROSS);
        
//        g.insert(4,0, SVal.CROSS);
        g.printGrid();
//        Rules.test(g);
        System.out.println(Rules.findWinner(g, 4));
        
		////////////////////////////////////////////////////////////////////////////
		//-------------------------------Streak testing-----------------------------
		////////////////////////////////////////////////////////////////////////////
//          
//		Collection<FullStreak> rowCirclesStreaks = Computations.getFullStreaksFromStripes(Computations.getAllRows(g), SVal.CIRCLE);
//		System.out.println("row circle streaks");
//		for (FullStreak streak : rowCirclesStreaks) {
//			System.out.println(streak);
//		}
//		
//		Collection<FullStreak> rowCrossStreaks = Computations.getFullStreaksFromStripes(Computations.getAllRows(g), SVal.CROSS);
//		System.out.println("row cross streaks");
//		for (FullStreak streak : rowCrossStreaks) {
//			System.out.println(streak);
//		}
//      
        Collection<FullStreak> allCirclesStreaks = Computations.getAllFullStreaks(g, SVal.CIRCLE);
        System.out.println("all circle streaks");
        for (FullStreak streak : allCirclesStreaks) {
        	System.out.println(streak);
        }	
      
        Collection<FullStreak> allCrossStreaks = Computations.getAllFullStreaks(g, SVal.CROSS);
        System.out.println("all cross streaks");
        for (FullStreak streak : allCrossStreaks) {
        	System.out.println(streak);
        }
        
        // Potential streaks
        
        Collection<PotentialStreak> allPotStreaksCircle = Computations.getAllPotentialStreaks(g, SVal.CIRCLE, 3);
        System.out.println("all potential CIRCLE streaks");
        for (PotentialStreak streak : allPotStreaksCircle) {
        	System.out.println(streak);
        }
        
        Collection<PotentialStreak> allPotStreaksCross = Computations.getAllPotentialStreaks(g, SVal.CROSS, 3);
        System.out.println("all potential CROSS streaks");
        for (PotentialStreak streak : allPotStreaksCross) {
        	System.out.println(streak);
        }
        
		////////////////////////////////////////////////////////////////////////////
		//----------------------PotentialStreak's Comparators-----------------------
		////////////////////////////////////////////////////////////////////////////
        List<PotentialStreak> allPotStreaksCircle0 = Computations.getAllPotentialStreaks(g, SVal.CIRCLE, 3);
        Collections.sort(allPotStreaksCircle0, new PotStreakFilledLengthComparator());
        System.out.println("PotStreakFilledLengthComparator");
        for (PotentialStreak streak : allPotStreaksCircle0) {
        	System.out.println(streak);
        }
        
        
		////////////////////////////////////////////////////////////////////////////
		//-------------------------some things in development-----------------------
		////////////////////////////////////////////////////////////////////////////
//        SortedNodesTree<Integer> tree = new SortedNodesTree<Integer>(0, new IntegerComparator());
//        
//        SortedTreeNode<Integer> child1 = tree.addChild(tree.getRoot(), Integer.valueOf(5));
//        SortedTreeNode<Integer> child2 = tree.addChild(tree.getRoot(), Integer.valueOf(6));
//        SortedTreeNode<Integer> child3 = tree.addChild(tree.getRoot(), Integer.valueOf(1));
//        
//        
//        SortedTreeNode<Integer> child11 = tree.addChild(child1, Integer.valueOf(10));
//        SortedTreeNode<Integer> child12 = tree.addChild(child1, Integer.valueOf(12));
//        
//        SortedTreeNode<Integer> child21 = tree.addChild(child2, Integer.valueOf(10));
//        SortedTreeNode<Integer> child22 = tree.addChild(child2, Integer.valueOf(11));
//        
//        SortedTreeNode<Integer> child211 = tree.addChild(child21, Integer.valueOf(100));
//        
//        System.out.println(tree);
//        System.out.println("leafs " + tree.getLeafs());
        
        
        // test
        
//        int streakLength0 = 3;
//        AbstractCooValFromStreakEstimator cooEstimator = new PoweredLengthCooValEstimator(2);
//        AbstractSquareHeuristic sqH = new SquareMergedHeuristic(cooEstimator);
//        AbstractRatedCoosFilter AIFilter = new FewBestRatedCoosFilter(2);
//        AbstractRatedCoosFilter heuristicFilter = new FewBestRatedCoosFilter(5);
//        AbstractGridHeuristic gH = new GridDiffHeuristic(sqH, cooEstimator, heuristicFilter);
//        int depth = 2;
//        TreeEvaluationAIPlayer treeSAIPlayer = new TreeEvaluationAIPlayer(SVal.CROSS, "tree search ai", streakLength0, sqH,
//        		gH, AIFilter, depth);
//        g = new Grid(7);
//        g.insert(3, 3, SVal.CROSS);
//        g.insert(2, 2, SVal.CIRCLE);
//        treeSAIPlayer.test(g);
        
        
		////////////////////////////////////////////////////////////////////////////
		//---------------------------------AI GAME----------------------------------
		////////////////////////////////////////////////////////////////////////////
//        g = new Grid(5);
//        int reqStreakLength = 3;
//        AbstractAIPlayer aiPlayer1 = new DumbAIPlayer(SVal.CROSS, "dumb ai player 1", reqStreakLength);
//        AbstractAIPlayer aiPlayer2 = new AIPlayer(SVal.CIRCLE, "ai player", reqStreakLength);
////        Player aiPlayer2 = new DumbAIPlayer2(SVal.CIRCLE, "dumb ai 2 player 2", reqStreakLength);
//      Game aiGame = new Game(aiPlayer1, aiPlayer2, g, reqStreakLength);
//      aiGame.play();

        testAIs();
        

        
		////////////////////////////////////////////////////////////////////////////
		//----------------------------AI vs Person Game ----------------------------
		////////////////////////////////////////////////////////////////////////////
        
//        g = new Grid(5);
//        int streakLength1 = 3;
//        Player aiPlayer1 = new DumbAIPlayer(SVal.CROSS, "dumb ai player 1", streakLength1);
//        Player aiPlayer2 = new UIPlayer(SVal.CIRCLE, "ui player");
//        
//        Game aiGame = new Game(aiPlayer1, aiPlayer2, g, reqStreakLength);
//        aiGame.play();
        
        
		////////////////////////////////////////////////////////////////////////////
		//--------------------------AI Players' methods-----------------------------
		////////////////////////////////////////////////////////////////////////////
        
//        AIPlayer aiPlayer = new AIPlayer(SVal.CIRCLE, "name", 3);
//        aiPlayer.test();
        
        ////////////////////////////////////////////////////////////////////////////
        //----------------------------------GAME------------------------------------
        ////////////////////////////////////////////////////////////////////////////
        
        System.out.println("What should be a size of grid?");
        int size;
        while (true) {
            size = readInt();
            if (size < 1) {
                System.out.println("Enter number greater than zero.");
            } else {
                break;
            }
        }
        g = new Grid(size);

        
        int streakLength;
        System.out.println("What should be a size of required streak?");
        while (true) {
            streakLength = readInt();
            if (streakLength > size) {
                System.out.println("Enter number less than or equal to entered size, i.e. " + size + ".");
            } else {
                break;
            }
        }
        
        
        Player p1 = new UIPlayer(SVal.CROSS, "player 1");
        Player p2 = new UIPlayer(SVal.CIRCLE, "player 2");
        Game game = new Game(p1, p2, g, streakLength);
        game.play();
        
        // closing the standard stream for whole program
        Scanner in = new Scanner(System.in);
        in.close();
    }
    
    private static int readInt() {
        Scanner in = new Scanner(System.in);
        while (true) {
            if (in.hasNextInt()) {
                int a = in.nextInt();
                in.nextLine();
                return a;
            } else {
                System.out.println("There's been problem with entered number, please enter correct number.");
                in.nextLine();
            }
        }
    }
    
    private static void testAIs() {
//    	int[][] gameSizeAndStreakSizes = new int[][] {{8,4}};
//    	int[][] gameSizeAndStreakSizes = new int[][] {{5,3}};
    	int[][] gameSizeAndStreakSizes = new int[][] {{5,3}, {5,4}, {6,4}, {7,4}, {8,4}, {8,5}, {8,6}, {9,6}, {10,5}, {10,6}};
    	
    	int p1Wins = 0;
    	int p2Wins = 0;
    	int ties = 0;
  		StringBuilder strB = new StringBuilder();
  		Player p1 = null;
  		Player p2 = null;
  		
  		AbstractCooValFromStreakEstimator estimator = new PoweredLengthCooValEstimator(2);
  		AbstractRatedCoosFilter fewBestAIFilter = new FewBestRatedCoosFilter(3);
  		AbstractSquareHeuristic sqH = new SquareMergedHeuristic(estimator);
  		AbstractRatedCoosFilter fewBestHeuristicFilter = new FewBestRatedCoosFilter(7);
  		AbstractGridHeuristic gH = new GridDiffHeuristic(sqH, estimator, fewBestHeuristicFilter);
  		
  		
    	for(int[] size : gameSizeAndStreakSizes) {
  
    		Grid g = new Grid(size[0]);
    		int streakLength = size[1];
//    		p1 = new DumbAIPlayer(SVal.CROSS, "dumb ai player 1", streakLength);
    		p1 = new DepthAIPlayer(SVal.CROSS, "depth ai player", streakLength, sqH, gH, fewBestAIFilter, 3);
//    		p1 = new DumbAIPlayer2(SVal.CROSS, "dumb ai player 1", streakLength);
//    		p1 = new OneStepAIPlayer(SVal.CROSS, "one step with ABheuristic", streakLength, new AttackBlockHeuristic());
//    		p1 = new OneStepAIPlayer(SVal.CROSS, "one step with Attackheuristic", streakLength, new AttackHeuristic());
//    		p1 = new OneStepAIPlayer(SVal.CROSS, "one step with mergeHeuristic", streakLength, new MergedHeuristic());
//    		p1 = new MergeAIPlayer(SVal.CROSS, "merge ai player 1", streakLength);
    		
//     		p2 = new MergeAIPlayer(SVal.CIRCLE, "merge ai player 2", streakLength);
//    		p2 = new DumbAIPlayer2(SVal.CIRCLE, "dumb ai 2 player 2", streakLength);
//    		p2 = new DumbAIPlayer(SVal.CIRCLE, "dumb ai player 2", streakLength);
//    		p2 = new MergeAIPlayer(SVal.CIRCLE, "merge ai player 2", streakLength);
//    		p2 = new FewBestDepthAIPlayer(SVal.CIRCLE, "Few Best Depth ai 2", streakLength, new SquareMergedHeuristic(), 3, 3);
//    		p2 = new FewBestDepthAIPlayer(SVal.CIRCLE, "AB Depth ai 2", streakLength, new AttackBlockHeuristic(), 3, 3);
//    		p2 = new DepthAIPlayer(SVal.CIRCLE, "depth ai player", streakLength, sqH, gH, fewBestFilter, 3);
    		p2 = new TreeEvaluationAIPlayer(SVal.CIRCLE, "tree eval ai player", streakLength, sqH, gH, fewBestAIFilter, 2);
    		
    		Game game = new Game(p1, p2, g, streakLength);
    		System.out.println("\n\n\n\n" + game);
    		SVal winner = game.play();
    		
    		strB.append("{" + size[0] +"}" + "{" + size[1] + "}" + " the winner is ");
    		if(winner == p1.getSVal()) {
    			strB.append(p1.getSVal() + "\n");
    			p1Wins++;
    		} else if(winner == p2.getSVal()) {
    			strB.append(p2.getSVal() + "\n");
    			p2Wins++;
    		} else {
    			strB.append("noone \n");
    			ties++;
    		}
    	}
    	
    	System.out.println(strB.toString());
    	System.out.println("p1Wins = " + p1.getName() + " = " + p1Wins + "\np2Wins = " + p2.getName() +" = " + p2Wins + "\nties = " + ties);
    }
    
    
    static class IntegerComparator implements Comparator<Integer> {

		@Override
		public int compare(Integer arg0, Integer arg1) {
			return arg0.compareTo(arg1);
		}
    	
    }
}
