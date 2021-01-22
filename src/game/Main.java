/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import game_mechanics.Rules;
import grid_computations.Computations;
import grid_computations.FullStreak;
import grid_computations.PotentialStreak;
import players.Player;
import players.ui_players.UIPlayer;

import java.util.Collection;
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

//	    Collection<FullStreak> circlesRightDiagonalStreaks = Computations.getFullRightDiagonalStreaks(g, SVal.CIRCLE);
//	    System.out.println("circle right diagonal streaks");
//	    for (FullStreak streak : circlesRightDiagonalStreaks) {
//	  	  System.out.println(streak);
//	    }
//	  
//	    Collection<FullStreak> crossRightDigonalStreaks = Computations.getFullRightDiagonalStreaks(g, SVal.CROSS);
//	    System.out.println("cross right diagonal streaks");
//	    for (FullStreak streak : crossRightDigonalStreaks) {
//	    	System.out.println(streak);
//	    }
//        
//        
//        Collection<FullStreak> triviaCirclelStreaks = Computations.getTrivialFullSteraksFromGrid(g, SVal.CIRCLE);
//        System.out.println("circle trivial streaks");
//        for (FullStreak streak : triviaCirclelStreaks) {
//  	  	  System.out.println(streak);
//  	    }
//        
//        Collection<FullStreak> trivialCrossStreaks = Computations.getTrivialFullSteraksFromGrid(g, SVal.CROSS);
//        System.out.println("circle trivial streaks");
//        for (FullStreak streak : trivialCrossStreaks) {
//  	  	  System.out.println(streak);
//  	    }
//        
//	    Collection<FullStreak> circlesLeftDiagonalStreaks = Computations.getFullLeftDiagonalStreaks(g, SVal.CIRCLE);
//	    System.out.println("circle left diagonal streaks");
//	    for (FullStreak streak : circlesLeftDiagonalStreaks) {
//	  	  System.out.println(streak);
//	    }
//	  
//	    Collection<FullStreak> crossLeftDigonalStreaks = Computations.getFullLeftDiagonalStreaks(g, SVal.CROSS);
//	    System.out.println("cross left diagonal streaks");
//	    for (FullStreak streak : crossLeftDigonalStreaks) {
//	    	System.out.println(streak);
//	    }
//
//        
//	    Collection<FullStreak> circlesColumnStreaks = Computations.getFullColumnStreaks(g, SVal.CIRCLE);
//	    System.out.println("circle column streaks");
//	    for (FullStreak streak : circlesColumnStreaks) {
//	  	  System.out.println(streak);
//	    }
//	  
//	    Collection<FullStreak> crossColumnStreaks = Computations.getFullColumnStreaks(g, SVal.CROSS);
//	    System.out.println("cross column streaks");
//	    for (FullStreak streak : crossColumnStreaks) {
//	    	System.out.println(streak);
//	    }
//      
//        Collection<FullStreak> circlesRowStreaks = Computations.getFullRowStreaks(g, SVal.CIRCLE);
//        System.out.println("circle row streaks");
//        for (FullStreak streak : circlesRowStreaks) {
//        	System.out.println(streak);
//        }
//        
//        Collection<FullStreak> crossRowStreaks = Computations.getFullRowStreaks(g, SVal.CROSS);
//        System.out.println("cross row streaks");
//        for (FullStreak streak : crossRowStreaks) {
//        	System.out.println(streak);
//        }
//        
        // Potential streaks
        
//        Collection<PotentialStreak> potRowStreaksCircle = Computations.getPotentialRowStreaks(g, SVal.CIRCLE, 3);
//        System.out.println("potential row CIRCLE streaks");
//        for (PotentialStreak streak : potRowStreaksCircle) {
//        	System.out.println(streak);
//        }
//        
//        Collection<PotentialStreak> potRowStreaksCross = Computations.getPotentialRowStreaks(g, SVal.CROSS, 3);
//        System.out.println("potential row CROSS streaks");
//        for (PotentialStreak streak : potRowStreaksCross) {
//        	System.out.println(streak);
//        }
//        
//        Collection<PotentialStreak> potColumnStreaksCircle = Computations.getPotentialColumnStreaks(g, SVal.CIRCLE, 3);
//        System.out.println("potential column CIRCLE streaks");
//        for (PotentialStreak streak : potColumnStreaksCircle) {
//        	System.out.println(streak);
//        }
//        
//        Collection<PotentialStreak> potColumnStreaksCross = Computations.getPotentialColumnStreaks(g, SVal.CROSS, 3);
//        System.out.println("potential column CROSS streaks");
//        for (PotentialStreak streak : potColumnStreaksCross) {
//        	System.out.println(streak);
//        }
//        
//        
//        
//        Collection<PotentialStreak> potLeftDiagonalStreaksCircle = Computations.getPotentialLeftDiagonalStreaks(g, SVal.CIRCLE, 3);
//        System.out.println("potential left diagonal CIRCLE streaks");
//        for (PotentialStreak streak : potLeftDiagonalStreaksCircle) {
//        	System.out.println(streak);
//        }
//        
//        Collection<PotentialStreak> potLeftDiagonalStreaksCross = Computations.getPotentialLeftDiagonalStreaks(g, SVal.CROSS, 3);
//        System.out.println("potential column CROSS streaks");
//        for (PotentialStreak streak : potLeftDiagonalStreaksCross) {
//        	System.out.println(streak);
//        }
        
        Collection<PotentialStreak> potRightDiagonalStreaksCircle = Computations.getPotentialRightDiagonalStreaks(g, SVal.CIRCLE, 3);
        System.out.println("potential right diagonal CIRCLE streaks");
        for (PotentialStreak streak : potRightDiagonalStreaksCircle) {
        	System.out.println(streak);
        }
        
        Collection<PotentialStreak> potRightDiagonalStreaksCross = Computations.getPotentialRightDiagonalStreaks(g, SVal.CROSS, 3);
        System.out.println("potential right CROSS streaks");
        for (PotentialStreak streak : potRightDiagonalStreaksCross) {
        	System.out.println(streak);
        }
        
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
