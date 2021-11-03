package experimental;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import game.Game;
import game.Result;
import game_components.Grid;
import game_components.Square.SVal;
import players.AIPlayer;
import players.Player;
import players.UIPlayer;
import strategies.AbstractStrategy;
import strategies.DepthStrategy;
import strategies.NaiveBlockAttackStrategy;
import strategies.OneStepStrategy;
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
import strategies.support_classes.LengthCooValEstimator;
import strategies.support_classes.OneValueEstimator;
import strategies.support_classes.PoweredLengthCooValEstimator;

/**
 *
 * @author glabka
 */
public class Experiments {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO compare with https://www.quora.com/What-are-some-good-project-ideas-for-an-undergraduate-object-oriented-programming-course-using-Java
//    	LocalGameForTwoManager lgftm = new LocalGameForTwoManager();
//    	lgftm.play();
    	
    	
//        Grid g = new Grid(5);
//        g.insert(1, 0, Square.SVal.CIRCLE);
//        g.insert(1, 1, Square.SVal.CIRCLE);
//        g.insert(1, 2, Square.SVal.CIRCLE);
//        g.insert(1, 3, Square.SVal.CIRCLE);
//        
//        g.insert(0, 1, Square.SVal.CIRCLE);
//        g.insert(0, 2, Square.SVal.CIRCLE);
//        g.insert(0, 4, Square.SVal.CIRCLE);
//        
////        g.insert(3,2, SVal.CIRCLE);
//        
//        g.insert(2, 4, Square.SVal.CROSS);
//        g.insert(3, 4, Square.SVal.CROSS);
//        g.insert(4, 4, Square.SVal.CROSS);
//        
//        g.insert(2, 2, Square.SVal.CROSS);
//        g.insert(3, 3, Square.SVal.CROSS);
//        
////        g.insert(4,0, SVal.CROSS);
//        g.printGridDebug();
//        
//        ////////////////////////////////////////////////////////////////////////////
//        //----------------------JSON and MESSAGE testing----------------------------
//        ////////////////////////////////////////////////////////////////////////////
//        Message message = Message.createMessage(CommunicationProtocolValue.GRID_REPRESENTATION);
//        message.setGrid(GridTransferRepresentation.getGridTransferRepresentation(g, SVal.CROSS));
//        Gson gson = new Gson();
//        String messageStr = gson.toJson(message);
//        System.out.println(messageStr);
//        Message message2 = gson.fromJson(messageStr, Message.class);
//        Grid g2 = GridTransferRepresentation.getGrid(message2.getGrid(), SVal.CROSS);
//        g2.printGridDebug();
//        
        
        
//////        Rules.test(g);
////        System.out.println(Rules.findWinner(g, 4));
//        
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
//        Collection<FullStreak> allCirclesStreaks = Computations.getAllFullStreaks(g, SVal.CIRCLE);
//        System.out.println("all circle streaks");
//        for (FullStreak streak : allCirclesStreaks) {
//        	System.out.println(streak);
//        }	
//      
//        Collection<FullStreak> allCrossStreaks = Computations.getAllFullStreaks(g, SVal.CROSS);
//        System.out.println("all cross streaks");
//        for (FullStreak streak : allCrossStreaks) {
//        	System.out.println(streak);
//        }
//        
//        // Potential streaks
//        
//        Collection<PotentialStreak> allPotStreaksCircle = Computations.getAllPotentialStreaks(g, SVal.CIRCLE, 3, 0);
//        System.out.println("all potential CIRCLE streaks");
//        for (PotentialStreak streak : allPotStreaksCircle) {
//        	System.out.println(streak);
//        }
//        
//        Collection<PotentialStreak> allPotStreaksCross = Computations.getAllPotentialStreaks(g, SVal.CROSS, 3, 0);
//        System.out.println("all potential CROSS streaks");
//        for (PotentialStreak streak : allPotStreaksCross) {
//        	System.out.println(streak);
//        }
//        
//		////////////////////////////////////////////////////////////////////////////
//		//----------------------PotentialStreak's Comparators-----------------------
//		////////////////////////////////////////////////////////////////////////////
//        List<PotentialStreak> allPotStreaksCircle0 = Computations.getAllPotentialStreaks(g, SVal.CIRCLE, 3, 0);
//        Collections.sort(allPotStreaksCircle0, new PotStreakFilledLengthComparator());
//        System.out.println("PotStreakFilledLengthComparator");
//        for (PotentialStreak streak : allPotStreaksCircle0) {
//        	System.out.println(streak);
//        }
        
        
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
//        
//    	// test deep copy
//    	AbstractAIPlayer aiPlayer= new TreeEvaluationAIPlayer(SVal.CROSS, "tree eval sqMBAH gDiffPH", 5, sqDMBAH, gDiffPH, fewBestAIFilter, 2);
//    	System.out.println(aiPlayer.getName());
//    	System.out.println(aiPlayer.getStreakLength());
//    	System.out.println(aiPlayer.getSVal());
//    	System.out.println(aiPlayer.getGridHeuristic());
//    	System.out.println(aiPlayer.getGridHeuristic() == gDiffPH);
//    	System.out.println(aiPlayer.getRatedCoosFilter());
//    	System.out.println(aiPlayer.getRatedCoosFilter() == fewBestAIFilter);
//    	System.out.println(aiPlayer.getSquareHeuristic());
//    	System.out.println(aiPlayer.getSquareHeuristic() == sqDMBAH);

    	
        
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
        
        // getRatedCoosBasedOnNumOfPotStreaksTheyAreIn
        
//        System.out.println(HeuristicCommon.getRatedCoosBasedOnNumOfPotStreaksTheyAreIn(g, SVal.CIRCLE, 3));
        
        // SquareMixedHeuristic
//        SquareMixedHeuristic.test();
    	
//////    	 test equals and hasCode of some classes
    	
    	AbstractCooValFromStreakEstimator oneValEstimator1a = new OneValueEstimator(2);
    	AbstractCooValFromStreakEstimator oneValEstimator1b = new OneValueEstimator(2);
    	AbstractCooValFromStreakEstimator oneValEstimator2a = new OneValueEstimator(3);
    	AbstractCooValFromStreakEstimator pLenEstimator1a = new PoweredLengthCooValEstimator(3.2);
    	AbstractCooValFromStreakEstimator pLenEstimator1b= new PoweredLengthCooValEstimator(3.2);
    	AbstractCooValFromStreakEstimator pLenEstimator2a= new PoweredLengthCooValEstimator(4);
    	
    	AbstractSquareHeuristic sNaviveBAHeuristic1a = new SquareNaiveBlockAttackHeuristic(oneValEstimator1a);
    	AbstractSquareHeuristic sNaviveBAHeuristic1b = new SquareNaiveBlockAttackHeuristic(oneValEstimator1b);
    	AbstractSquareHeuristic sNaviveBAHeuristic2a = new SquareNaiveBlockAttackHeuristic(oneValEstimator2a);
    	
    	// equals
    	System.out.println("oneValEstimator1a.equals(oneValEstimator1b): " + oneValEstimator1a.equals(oneValEstimator1b));
    	System.out.println("oneValEstimator1a.equals(oneValEstimator2a): " + oneValEstimator1a.equals(oneValEstimator2a));
    	System.out.println("oneValEstimator1a.equals(pLenEstimator1a): " + oneValEstimator1a.equals(pLenEstimator1a));
    	System.out.println("pLenEstimator1a.equals(pLenEstimator1b): " + pLenEstimator1a.equals(pLenEstimator1b));
    	System.out.println("pLenEstimator1a.equals(pLenEstimator2a): " + pLenEstimator1a.equals(pLenEstimator2a));
    	
    	System.out.println("sNaviveBAHeuristic1a.equals(sNaviveBAHeuristic1b): " + sNaviveBAHeuristic1a.equals(sNaviveBAHeuristic1b));
    	System.out.println("sNaviveBAHeuristic1a.equals(sNaviveBAHeuristic2a): " + sNaviveBAHeuristic1a.equals(sNaviveBAHeuristic2a));
    	// hashCode
    	System.out.println("oneValEstimator1a.hashCode() == (oneValEstimator1b.hashCode()): " + oneValEstimator1a.hashCode() + " == " + oneValEstimator1b.hashCode() + ": "+ (oneValEstimator1a.hashCode() == oneValEstimator1b.hashCode()));
    	System.out.println("oneValEstimator1a.hashCode() == (oneValEstimator2a.hashCode()): " + oneValEstimator1a.hashCode() + " == " + oneValEstimator2a.hashCode() + ": "+ (oneValEstimator1a.hashCode() == oneValEstimator2a.hashCode()));
    	System.out.println("oneValEstimator1a.hashCode() == (pLenEstimator1a.hashCode()): " + oneValEstimator1a.hashCode() + " == " + pLenEstimator1a.hashCode() + ": "+ (oneValEstimator1a.hashCode() == pLenEstimator1a.hashCode()));
    	System.out.println("pLenEstimator1a.hashCode() == (pLenEstimator1b.hashCode()): " + pLenEstimator1a.hashCode() + " == " + pLenEstimator1b.hashCode() + ": "+ (pLenEstimator1a.hashCode() == pLenEstimator1b.hashCode()));
    	System.out.println("pLenEstimator1a.hashCode() == (pLenEstimator2a.hashCode()): " + pLenEstimator1a.hashCode() + " == " + pLenEstimator2a.hashCode() + ": "+ (pLenEstimator1a.hashCode() == pLenEstimator2a.hashCode()));
    	
    	System.out.println("sNaviveBAHeuristic1a.hashCode() == (sNaviveBAHeuristic1b.hashCode()): " + sNaviveBAHeuristic1a.hashCode() + " == " + sNaviveBAHeuristic1b.hashCode() + ": "+ (sNaviveBAHeuristic1a.hashCode() == sNaviveBAHeuristic1b.hashCode()));
    	System.out.println("sNaviveBAHeuristic1a.hashCode() == (sNaviveBAHeuristic2a.hashCode()): " + sNaviveBAHeuristic1a.hashCode() + " == " + sNaviveBAHeuristic2a.hashCode() + ": "+ (sNaviveBAHeuristic1a.hashCode() == sNaviveBAHeuristic2a.hashCode()));
		
    	
    	
    	////////////////////////////////////////////////////////////////////////////
		//---------------------------------AI GAME----------------------------------
		////////////////////////////////////////////////////////////////////////////

//    	testAI();
//    	findBestAITest();
        
		////////////////////////////////////////////////////////////////////////////
		//----------------------------AI vs Person Game ----------------------------
		////////////////////////////////////////////////////////////////////////////
        
        
//        uiVSai();
        
        
		////////////////////////////////////////////////////////////////////////////
		//--------------------------AI Players' methods-----------------------------
		////////////////////////////////////////////////////////////////////////////
        
//        AIPlayer aiPlayer = new AIPlayer(SVal.CIRCLE, "name", 3);
//        aiPlayer.test();
        
        ////////////////////////////////////////////////////////////////////////////
        //----------------------------------GAME------------------------------------
        ////////////////////////////////////////////////////////////////////////////
        
//        uiGame();
    	
        ////////////////////////////////////////////////////////////////////////////
        //------------------------BEST STRATEGY FINDER------------------------------
        ////////////////////////////////////////////////////////////////////////////
    	BestStrategyFinder bestStrategyFinder = new BestStrategyFinder();
    	bestStrategyFinder.findBestStrategy(10);
        
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
    
    public static void findBestAITest() {
    	SVal referenceAIVal = SVal.CROSS;
    	SVal testedAIVal = SVal.CIRCLE;
    	
    	AbstractRatedCoosFilter[] aiFilters = new AbstractRatedCoosFilter[4];
    	for (int i = 0; i < aiFilters.length; i++) {
			aiFilters[i] = new FewBestRatedCoosFilter(i + 1);
		}
    	
    	AbstractCooValFromStreakEstimator[] estimators = new AbstractCooValFromStreakEstimator[4];
    	estimators[0] = new LengthCooValEstimator();
    	estimators[1] = new PoweredLengthCooValEstimator(1.5);
    	estimators[2] = new PoweredLengthCooValEstimator(2);
    	estimators[3] = new PoweredLengthCooValEstimator(3);
    	
    	AbstractSquareHeuristic[] squareHeuristics = new AbstractSquareHeuristic[24];
    	squareHeuristics[0] = new SquareMergedHeuristic(estimators[0]);
    	squareHeuristics[1] = new SquareMergedHeuristic(estimators[1]);
    	squareHeuristics[2] = new SquareMergedHeuristic(estimators[2]);
    	squareHeuristics[3] = new SquareMergedHeuristic(estimators[3]);
    	squareHeuristics[4] = new SquareNaiveBlockAttackHeuristic(estimators[0]);
    	squareHeuristics[5] = new SquareNaiveBlockAttackHeuristic(estimators[1]);
    	squareHeuristics[6] = new SquareNaiveBlockAttackHeuristic(estimators[2]);
    	squareHeuristics[7] = new SquareNaiveBlockAttackHeuristic(estimators[3]);
    	squareHeuristics[8] = new SquareBlockAttackHeuristic(estimators[0]);
    	squareHeuristics[9] = new SquareBlockAttackHeuristic(estimators[1]);
    	squareHeuristics[10] = new SquareBlockAttackHeuristic(estimators[2]);
    	squareHeuristics[11] = new SquareBlockAttackHeuristic(estimators[3]);
    	squareHeuristics[12] = new SquareDecidingAttackBlockHeuristic(estimators[0]);
    	squareHeuristics[13] = new SquareDecidingAttackBlockHeuristic(estimators[1]);
    	squareHeuristics[14] = new SquareDecidingAttackBlockHeuristic(estimators[2]);
    	squareHeuristics[15] = new SquareDecidingAttackBlockHeuristic(estimators[3]);
    	squareHeuristics[16] = new SquareMergeBlockAttackHeuristic(estimators[0]);
    	squareHeuristics[17] = new SquareMergeBlockAttackHeuristic(estimators[1]);
    	squareHeuristics[18] = new SquareMergeBlockAttackHeuristic(estimators[2]);
    	squareHeuristics[19] = new SquareMergeBlockAttackHeuristic(estimators[3]);
    	squareHeuristics[16] = new SquareMergeBlockAttackHeuristic(estimators[0], new FewBestRatedCoosFilter(7));
    	squareHeuristics[17] = new SquareMergeBlockAttackHeuristic(estimators[1], new FewBestRatedCoosFilter(7));
    	squareHeuristics[18] = new SquareMergeBlockAttackHeuristic(estimators[2], new FewBestRatedCoosFilter(7));
    	squareHeuristics[19] = new SquareMergeBlockAttackHeuristic(estimators[3], new FewBestRatedCoosFilter(7));
    	squareHeuristics[20] = new SquareDecidingMergeBlockAttackHeuristic(estimators[0]);
    	squareHeuristics[21] = new SquareDecidingMergeBlockAttackHeuristic(estimators[1]);
    	squareHeuristics[22] = new SquareDecidingMergeBlockAttackHeuristic(estimators[2]);
    	squareHeuristics[23] = new SquareDecidingMergeBlockAttackHeuristic(estimators[3]);
    	
    	AbstractGridHeuristic[] gridHeuristics = new AbstractGridHeuristic[2];
    	gridHeuristics[0] = new GirdMergeHeuristic(sqMergeH, estimator, fewBestHeuristicFilter);
    	gridHeuristics[1] = new GridDiffPoweredHeuristic(estimator, 2);
    	
    	
    	int[][] gameSizeAndStreakSizes = new int[][] {{5,3}, {5,4}, {6,4}, {7,4}, {8,4}, {8,5}, {8,6}, {9,6}, {10,5}, {10,6}};
    	Map<Result, String> resultsAndCombination = new HashMap<>();
    	
    	int filersIndex;
    	int estimatorsIndex;
    	int squareHeuristicsIndex;
    	int gridHeuristicsIndex;
    	AIPlayer p1;
    	AIPlayer p2;
    	
    	for (AbstractRatedCoosFilter filter : aiFilters) {
    		filersIndex = Arrays.asList(aiFilters).indexOf(filter);
			for(AbstractCooValFromStreakEstimator estimator : estimators) {
				estimatorsIndex = Arrays.asList(estimators).indexOf(estimator);
				for(AbstractSquareHeuristic squareHeuristic : squareHeuristics) {
					squareHeuristicsIndex = Arrays.asList(squareHeuristics).indexOf(squareHeuristic);
					for(AbstractGridHeuristic gridHeuristic : gridHeuristics) {
						gridHeuristicsIndex = Arrays.asList(gridHeuristics).indexOf(gridHeuristic);
						
						System.out.println("filersIndex = "  + filersIndex + ", estimatorsIndex = " + estimatorsIndex + ", squareHeuristicsIndex = " + squareHeuristicsIndex + ", gridHeuristicsIndex = " + gridHeuristicsIndex);
						String combination = "filersIndex = "  + filersIndex + ", estimatorsIndex = " + estimatorsIndex + ", squareHeuristicsIndex = " + squareHeuristicsIndex + ", gridHeuristicsIndex = " + gridHeuristicsIndex;
								
						int p2Losses = 0;
						int p2Wins = 0;
						int ties = 0 ;
						
						String aiPlayerName = "DepthAIPlayer";
						System.out.println(aiPlayerName);
						for (int i = 0; i < gameSizeAndStreakSizes.length; i++) {
							System.out.println("g.size = " + gameSizeAndStreakSizes[i][0] + ", streakLength = " + gameSizeAndStreakSizes[i][1]);
							Grid g = new Grid(gameSizeAndStreakSizes[i][0]);
							int streakLength = gameSizeAndStreakSizes[i][1];
							
							p1 = new AIPlayer(referenceAIVal, "NaiveBlockAttackAIPlayer", streakLength, new NaiveBlockAttackStrategy());
							p2 = new AIPlayer(testedAIVal, aiPlayerName, streakLength, new DepthStrategy(squareHeuristic, gridHeuristic, filter, 3));
							SVal winner = aiGame(g, streakLength, p1, p2);
							
							if(winner == testedAIVal) {
								p2Wins++;
							} else if (winner == referenceAIVal) {
								p2Losses++;
							} else {
								ties++;
							}
							
							// switched start
							winner = aiGame(g, streakLength, p2, p1);
							
							if(winner == testedAIVal) {
								p2Wins++;
							} else if (winner == referenceAIVal) {
								p2Losses++;
							} else {
								ties++;
							}
							
						}
						String fullCombination = aiPlayerName + ", " + combination;
						Result res = new Result(p2Wins, p2Losses, ties);
						resultsAndCombination.put(res, fullCombination);
						System.out.println(res + ", " + fullCombination);
						
						// second AI
						p2Losses = 0;
						p2Wins = 0;
						ties = 0 ;
						
						aiPlayerName = "OneStepAIPlayer";
						System.out.println(aiPlayerName);
						for (int i = 0; i < gameSizeAndStreakSizes.length; i++) {
							System.out.println("g.size = " + gameSizeAndStreakSizes[i][0] + ", streakLength = " + gameSizeAndStreakSizes[i][1]);
							Grid g = new Grid(gameSizeAndStreakSizes[i][0]);
							int streakLength = gameSizeAndStreakSizes[i][1];
							
							p1 = new AIPlayer(referenceAIVal, "NaiveBlockAttackAIPlayer", streakLength, new NaiveBlockAttackStrategy());
							p2 = new AIPlayer(testedAIVal, aiPlayerName, streakLength, new OneStepStrategy(squareHeuristic));
							SVal winner = aiGame(g, streakLength, p1, p2);
							
							if(winner == testedAIVal) {
								p2Wins++;
							} else if (winner == referenceAIVal) {
								p2Losses++;
							} else {
								ties++;
							}
							
							// switched start
							winner = aiGame(g, streakLength, p2, p1);
							
							if(winner == testedAIVal) {
								p2Wins++;
							} else if (winner == referenceAIVal) {
								p2Losses++;
							} else {
								ties++;
							}
						}
						fullCombination = aiPlayerName + ", " + combination;
						res = new Result(p2Wins, p2Losses, ties);
						resultsAndCombination.put(res, fullCombination);
						System.out.println(res + ", " + fullCombination);
						
						// third AI player
						p2Losses = 0;
						p2Wins = 0;
						ties = 0 ;
						
						aiPlayerName = "TreeEvaluationAIPlayer";
						System.out.println(aiPlayerName);
						for (int i = 0; i < gameSizeAndStreakSizes.length; i++) {
							System.out.println("g.size = " + gameSizeAndStreakSizes[i][0] + ", streakLength = " + gameSizeAndStreakSizes[i][1]);
							Grid g = new Grid(gameSizeAndStreakSizes[i][0]);
							int streakLength = gameSizeAndStreakSizes[i][1];
							
							p1 = new AIPlayer(referenceAIVal, "NaiveBlockAttackAIPlayer", streakLength, new NaiveBlockAttackStrategy());
							p2 = new AIPlayer(testedAIVal, aiPlayerName, streakLength, new TreeEvaluationStrategy(squareHeuristic, gridHeuristic, filter, 2));
							SVal winner = aiGame(g, streakLength, p1, p2);
							
							if(winner == testedAIVal) {
								p2Wins++;
							} else if (winner == referenceAIVal) {
								p2Losses++;
							} else {
								ties++;
							}
							
							// switched start
							winner = aiGame(g, streakLength, p2, p1);
							
							if(winner == testedAIVal) {
								p2Wins++;
							} else if (winner == referenceAIVal) {
								p2Losses++;
							} else {
								ties++;
							}
						}
						fullCombination = aiPlayerName + ", " + combination;
						res = new Result(p2Wins, p2Losses, ties);
						resultsAndCombination.put(res, fullCombination);
						System.out.println(res + ", " + fullCombination);
					}
				}
			}
		}
    	
    	List<Result> results = new ArrayList<Result>(resultsAndCombination.keySet());
    	Collections.sort(results);
    	System.out.println("---------------------------------------------------------------------");
    	System.out.println("--------------------------SORTED RESULTS-----------------------------");
    	System.out.println("---------------------------------------------------------------------");
    	for(Result result : results) {
    		System.out.println(result + ", " + resultsAndCombination.get(result));
    	}
    }
    
    private static SVal aiGame(Grid g, int streakLength, AIPlayer p1, AIPlayer p2) {  		
		Game game = new Game(p1, p2, g, streakLength);
		return game.play(false);
    }
    
    public static void testAI() {
    	String[] results1 = aiGame();
        String[] results2 = aiGame(true);
        System.out.println(results1[0]);
        System.out.println("\n" + results2[0]);
        
        int p1Wins = Integer.valueOf(results1[1]) + Integer.valueOf(results2[1]);
        int p2Wins = Integer.valueOf(results1[2]) + Integer.valueOf(results2[2]);
        int ties = Integer.valueOf(results1[3]) + Integer.valueOf(results2[3]);
        
        System.out.println(results1[4] + " = " + p1Wins);
        System.out.println(results1[5] + " = " + p2Wins);
        System.out.println("ties = " + ties);
    }
    
    private static String[] aiGame() {
    	return aiGame(false);
    }
    
    /**
     * 
     * @param switchAI
     * @return
     */
    private static String[] aiGame(boolean switchAI) {
//    	int[][] gameSizeAndStreakSizes = new int[][] {{3,3}};
//    	int[][] gameSizeAndStreakSizes = new int[][] {{8,4}};
//    	int[][] gameSizeAndStreakSizes = new int[][] {{5,3}};
    	int[][] gameSizeAndStreakSizes = new int[][] {{5,3}, {5,4}, {6,4}, {7,4}, {8,4}, {8,5}, {8,6}, {9,6}, {10,5}, {10,6}};
    	
    	int p1Wins = 0;
    	int p2Wins = 0;
    	int ties = 0;
  		StringBuilder strB = new StringBuilder();
  		Player p1 = null;
  		Player p2 = null;
  		
  		
  		boolean firstRunOfLoop = true;
    	for(int[] size : gameSizeAndStreakSizes) {
  
    		Grid g = new Grid(size[0]);
    		int streakLength = size[1];
    		p1 = new AIPlayer(SVal.CROSS, "NaiveBlockAttackAIPlayer", streakLength, new NaiveBlockAttackStrategy());
//    		p1 = new DepthAIPlayer(SVal.CROSS, "depth ai player", streakLength, sqMergeH, gMergeH, fewBestAIFilter, 3);
//    		p1 = new DepthAIPlayer(SVal.CROSS, "depth ai player", streakLength, sqBAH, gDiffH, fewBestAIFilter, 3); // even better
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
//    		p2 = new TreeEvaluationAIPlayer(SVal.CIRCLE, "tree eval sqMBAH gMergeH", streakLength, sqMBAHFiltred, gMergeH, fewBestAIFilter, 2);    		
//    		p2 = new TreeEvaluationAIPlayer(SVal.CIRCLE, "tree eval sqDBAH gMergeH", streakLength, sqDABH, gMergeH, fewBestAIFilter, 2);
//    		p2 = new TreeEvaluationAIPlayer(SVal.CIRCLE, "tree eval sqMerge DiffH", streakLength, sqMergeH, gDiffH, fewBestAIFilter, 2); 
//    		p2 = new DepthAIPlayer(SVal.CIRCLE, "depth ai player", streakLength, sqBAH, gDiffH, fewBestAIFilter, 3);
//    		p2 = new TreeEvaluationAIPlayer(SVal.CIRCLE, "tree eval ai player", streakLength, sqBAH, gBAH, fewBestAIFilter, 2);

//    		p2 = new TreeEvaluationAIPlayer(SVal.CIRCLE, "tree eval sqMergeH", streakLength, sqMergeH, gDiffMergeH, fewBestAIFilter, 2); // good start (with HeuristicCommon.getMiddleOrFirstEmptyCoo)
//    		p2 = new TreeEvaluationAIPlayer(SVal.CIRCLE, "tree eval sqBAH", streakLength, sqBAH, gDiffMergeH, fewBestAIFilter, 2); // good (3, 6, 11) vs NaiveBlockAttackAIPlayer (with HeuristicCommon.getMiddleOrFirstEmptyCoo)
//    		p2 = new TreeEvaluationAIPlayer(SVal.CIRCLE, "tree eval sqBAH gDiffH", streakLength, sqBAH, gDiffH, fewBestAIFilter, 2); // good (2, 7, 11) vs NaiveBlockAttackAIPlayer (with HeuristicCommon.getMiddleOrFirstEmptyCoo)
//    		p2 = new TreeEvaluationAIPlayer(SVal.CIRCLE, "tree eval sqBAH gDiffH", streakLength, sqBAH, gMergeH, fewBestAIFilter, 2); // good (1, 5, 14) vs NaiveBlockAttackAIPlayer (with HeuristicCommon.getMiddleOrFirstEmptyCoo)
//    		p2 = new TreeEvaluationAIPlayer(SVal.CIRCLE, "tree eval sqMBAH gMergeH", streakLength, sqMBAH, gMergeH, fewBestAIFilter, 2); // even better (0, 7, 13) vs NaiveBlockAttackAIPlayer (with HeuristicCommon.getMiddleOrFirstEmptyCoo)
//    		p2 = new TreeEvaluationAIPlayer(SVal.CIRCLE, "tree eval sqMBAH gMergeH", streakLength, sqDMBAH, gMergeH, fewBestAIFilter, 2); // even better (0, 9, 11) vs NaiveBlockAttackAIPlayer (with HeuristicCommon.getMiddleOrFirstEmptyCoo)
//    		p2 = new TreeEvaluationAIPlayer(SVal.CIRCLE, "tree eval sqMBAH gDiffPH", streakLength, sqDMBAH, gDiffPH, fewBestAIFilter, 2); // even better (1, 12, 7) vs NaiveBlockAttackAIPlayer (with HeuristicCommon.getMiddleOrFirstEmptyCoo)
//    		p2 = new TreeEvaluationAIPlayer(SVal.CIRCLE, "tree eval sqMBAH gDiffPH", streakLength, sqDMBAH, gDiffPH, fewBestAIFilter, 1); // even better (0, 12, 8) vs NaiveBlockAttackAIPlayer (with HeuristicCommon.getMiddleOrFirstEmptyCoo)
    		p2 = new AIPlayer(SVal.CIRCLE, "tree eval sqMBAH gDiffPH", streakLength, new TreeEvaluationStrategy(sqMixedH, gDiffPH, fewBestAIFilter4, 2)); 
    		
    		
    		if(switchAI) {
    			Player p;
    			p = p2;
    			p2 = p1;
    			p1 = p;
    		}
    		
    		if(firstRunOfLoop) {
    			firstRunOfLoop = false;
    			strB.append("p1 = " + p1.getName() + "\n");
    			strB.append("p2 = " + p2.getName() + "\n");
    		}
    		
    		Game game = new Game(p1, p2, g, streakLength);
    		System.out.println("\n\n\n\n" + game);
    		SVal winner = game.play();
    		
        	// switch back for evaluation
        	if(switchAI) {
    			Player p;
    			p = p2;
    			p2 = p1;
    			p1 = p;
    		}
        	
    		strB.append("{" + size[0] +"}" + "{" + size[1] + "}" + " the winner is ");
    		if(winner == p1.getSVal()) {
    			strB.append(p1.getName() + "\n");
    			p1Wins++;
    		} else if(winner == p2.getSVal()) {
    			strB.append(p2.getName() + "\n");
    			p2Wins++;
    		} else {
    			strB.append("noone \n");
    			ties++;
    		}
    	}

    	
//    	strB.append("p1Wins = " + p1.getName() + " = " + p1Wins + "\np2Wins = " + p2.getName() +" = " + p2Wins + "\nties = " + ties);

    	
    	String[] results = new String[6];
    	results[0] = strB.toString();
    	results[1] = String.valueOf(p1Wins);
    	results[2] = String.valueOf(p2Wins);
    	results[3] = String.valueOf(ties);
    	results[4] = p1.getName();
    	results[5] = p2.getName();
    	return results;
    }
    
    
    static class IntegerComparator implements Comparator<Integer> {

		@Override
		public int compare(Integer arg0, Integer arg1) {
			return arg0.compareTo(arg1);
		}
    	
    }
}
