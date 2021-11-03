/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.Scanner;

import game_components.Grid;
import game_components.Square.SVal;
import players.AIPlayer;
import players.Player;
import players.UIPlayer;
import strategies.AbstractStrategy;
import strategies.NaiveBlockAttackStrategy;
import strategies.TreeEvaluationStrategy;
import strategies.heuristics.AbstractGridHeuristic;
import strategies.heuristics.AbstractSquareHeuristic;
import strategies.heuristics.GirdMergeHeuristic;
import strategies.heuristics.GridDiffHeuristic;
import strategies.heuristics.GridDiffPoweredHeuristic;
import strategies.heuristics.GridDiffRatedValuesHeuristic;
import strategies.heuristics.SquareBlockAttackHeuristic;
import strategies.heuristics.SquareDecidingAttackBlockHeuristic;
import strategies.heuristics.SquareDecidingMergeBlockAttackHeuristic;
import strategies.heuristics.SquareMergeBlockAttackHeuristic;
import strategies.heuristics.SquareMergedHeuristic;
import strategies.heuristics.SquareMixedHeuristic;
import strategies.heuristics.SquareNaiveBlockAttackHeuristic;
import strategies.support_classes.AbstractCooValFromStreakEstimator;
import strategies.support_classes.AbstractRatedCoosFilter;
import strategies.support_classes.FewBestRatedCoosFilter;
import strategies.support_classes.PoweredLengthCooValEstimator;

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
//    	LocalGameForTwoManager lgftm = new LocalGameForTwoManager();
//    	lgftm.play();
    	
		////////////////////////////////////////////////////////////////////////////
		//----------------------------AI vs Person Game ----------------------------
		////////////////////////////////////////////////////////////////////////////
        
        
        uiVSai();
        
        ////////////////////////////////////////////////////////////////////////////
        //----------------------------------GAME------------------------------------
        ////////////////////////////////////////////////////////////////////////////
        
//        uiGame();
        
        // closing the standard stream for whole program
        Scanner in = new Scanner(System.in);
        in.close();
    }
    
    private static void uiGame() {
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
        Grid g = new Grid(size);

        
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
    
    private static void uiVSai() {
    	
    	Grid g = new Grid(10);
        int streakLength = 5;
        AbstractStrategy strategy;
        strategy = new NaiveBlockAttackStrategy();
//        aiPlayer = new TreeEvaluationStrategy(SVal.CROSS, "tree eval ai player", streakLength, sqMergeH, gMergeH, fewBestAIFilter, 2);
//        aiPlayer = new TreeEvaluationStrategy(SVal.CROSS, "tree eval sqBAH gDiffH", streakLength, sqBAH, gDiffH, fewBestAIFilter, 2); // good (with HeuristicCommon.getMiddleOrFirstEmptyCoo)
//        aiPlayer = new TreeEvaluationStrategy(SVal.CROSS, "tree eval sqBAH gDiffH", streakLength, sqBAH, gMergeH, fewBestAIFilter, 2); // good (with HeuristicCommon.getMiddleOrFirstEmptyCoo)
//        aiPlayer = new TreeEvaluationStrategy(SVal.CROSS, "tree eval sqMBAH gMergeH", streakLength, sqMBAH, gMergeH, fewBestAIFilter, 2); // even better (with HeuristicCommon.getMiddleOrFirstEmptyCoo)
//        aiPlayer = new TreeEvaluationStrategy(SVal.CROSS, "tree eval sqMBAH gMergeH", streakLength, sqDMBAH, gMergeH, fewBestAIFilter, 2); // even better (0, 9, 11) vs NaiveBlockAttackAIPlayer (with HeuristicCommon.getMiddleOrFirstEmptyCoo)
//        strategy = new TreeEvaluationStrategy(sqDMBAH, gDiffPH, fewBestAIFilter, 2); // name = "tree eval sqMBAH gDiffPH" // even better (1, 12, 7) vs NaiveBlockAttackAIPlayer (with HeuristicCommon.getMiddleOrFirstEmptyCoo)
//        aiPlayer= new TreeEvaluationStrategy(SVal.CROSS, "tree eval sqMBAH gDiffPH", streakLength, sqDMBAH, gDiffPH, fewBestAIFilter, 2);
//        aiPlayer = new TreeEvaluationStrategy(SVal.CROSS, "tree eval sqBAH gDiffH", streakLength, sqDABH, gDiffH, fewBestAIFilter, 2);
        Player aiPlayer = new AIPlayer(SVal.CROSS, "ai player", streakLength, strategy);
        Player uiPlayer = new UIPlayer(SVal.CIRCLE, "ui player");
        
        Game aiGame = new Game(uiPlayer, aiPlayer, g, streakLength);
        aiGame.play();
    }
    
    private static AbstractCooValFromStreakEstimator estimator = new PoweredLengthCooValEstimator(2);
    private static AbstractRatedCoosFilter fewBestAIFilter = new FewBestRatedCoosFilter(3);
    private static AbstractRatedCoosFilter fewBestAIFilter4 = new FewBestRatedCoosFilter(4);
    private static AbstractSquareHeuristic sqMergeH = new SquareMergedHeuristic(estimator);
    private static AbstractSquareHeuristic sqNaiveBAH = new SquareNaiveBlockAttackHeuristic(estimator);
    private static AbstractRatedCoosFilter fewBestHeuristicFilter = new FewBestRatedCoosFilter(7);
    private static AbstractGridHeuristic gDiffMergeH = new GridDiffRatedValuesHeuristic(sqMergeH, estimator, fewBestHeuristicFilter);
    private static AbstractGridHeuristic gNaiveBAH = new GridDiffRatedValuesHeuristic(sqNaiveBAH, estimator, fewBestHeuristicFilter);
	private static AbstractSquareHeuristic sqBAH = new SquareBlockAttackHeuristic(estimator);
    private static AbstractGridHeuristic gDiffH = new GridDiffHeuristic(estimator);
    private static AbstractSquareHeuristic sqDABH = new SquareDecidingAttackBlockHeuristic(estimator);
    private static AbstractGridHeuristic gMergeH = new GirdMergeHeuristic(sqMergeH, estimator, fewBestHeuristicFilter);
    private static AbstractRatedCoosFilter fewBestHeuristicFilter2 = new FewBestRatedCoosFilter(3);
    private static AbstractSquareHeuristic sqMBAH = new SquareMergeBlockAttackHeuristic(estimator);
    private static AbstractSquareHeuristic sqMBAHFiltred = new SquareMergeBlockAttackHeuristic(estimator, fewBestHeuristicFilter2);
    private static AbstractSquareHeuristic sqDMBAH = new SquareDecidingMergeBlockAttackHeuristic(estimator);
    private static AbstractGridHeuristic gDiffPH = new GridDiffPoweredHeuristic(estimator, 2);
    private static AbstractSquareHeuristic sqMixedH = new SquareMixedHeuristic(estimator);
}
