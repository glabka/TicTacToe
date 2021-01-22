/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import game_mechanics.Rules;
import grid_computations.Computer;
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
        
        g.insert(2, 4, Square.SVal.CROSS);
        g.insert(3, 4, Square.SVal.CROSS);
        g.insert(4, 4, Square.SVal.CROSS);
        
        g.insert(2, 2, Square.SVal.CROSS);
        g.insert(3, 3, Square.SVal.CROSS);
        g.printGrid();
//        Rules.test(g);
        System.out.println(Rules.findWinner(g, 4));
        
		////////////////////////////////////////////////////////////////////////////
		//-------------------------------Streak testing-----------------------------
		////////////////////////////////////////////////////////////////////////////
        Collection<FullStreak> circlesRowStreaks = Computer.getFullRowStreaks(g, SVal.CIRCLE);
        System.out.println("circle streaks");
        for (FullStreak streak : circlesRowStreaks) {
        	System.out.println(streak);
        }
        
        Collection<FullStreak> crossRowStreaks = Computer.getFullRowStreaks(g, SVal.CROSS);
        System.out.println("cross streaks");
        for (FullStreak streak : crossRowStreaks) {
        	System.out.println(streak);
        }
        
        Collection<PotentialStreak> potRowStreaksCircle = Computer.getPotentialRowStreaks(g, SVal.CIRCLE, 3);
        System.out.println("potential CIRCLE streaks");
        for (PotentialStreak streak : potRowStreaksCircle) {
        	System.out.println(streak);
        }
        
        Collection<PotentialStreak> potRowStreaksCross = Computer.getPotentialRowStreaks(g, SVal.CROSS, 3);
        System.out.println("potential CIRCLE streaks");
        for (PotentialStreak streak : potRowStreaksCross) {
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
