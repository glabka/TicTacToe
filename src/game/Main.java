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
import players.ai_players.DumbAIPlayer;
import players.ai_players.DumbAIPlayer2;
import players.ui_players.UIPlayer;

import java.util.Collection;
import java.util.Collections;
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
		//---------------------------------AI GAME----------------------------------
		////////////////////////////////////////////////////////////////////////////
        g = new Grid(10);
        int reqStreakLength = 4;
        Player aiPlayer1 = new DumbAIPlayer(SVal.CROSS, "dumb ai player 1", reqStreakLength);
//        Player aiPlayer2 = new DumbAIPlayer(SVal.CIRCLE, "dumb ai player 2", reqStreakLength);
        Player aiPlayer2 = new DumbAIPlayer2(SVal.CIRCLE, "dumb ai 2 player 2", reqStreakLength);
        
        Game aiGame = new Game(aiPlayer1, aiPlayer2, g, reqStreakLength);
        aiGame.play();
        
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
    
}
