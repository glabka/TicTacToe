/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.InputMismatchException;
import java.util.Scanner;

import custom_exceptions.PlayersWithSameSValException;
import game_components.Grid;
import game_components.Move;
import game_components.Square;
import game_components.Square.SVal;
import game_mechanics.Rules;
import players.Player;
import players.ui_players.UIPlayer;

/**
 *
 * @author glabka
 */
public class Game {

    private final Grid g;
    private int streakLength;
    private Player currentPlayer;
    private Player player1;
    private Player player2;

    public Game(Player player1, Player player2, Grid g, int streakLength) {
        this.g = g;
        this.streakLength = streakLength;
        this.player1 = player1;
        this.player2 = player2;
        currentPlayer = player1;
        checkPlayers();
        
        int cursorRow = g.size() / 2;
        int cursorColumn = g.size() / 2;
        if(player1 instanceof UIPlayer || player2 instanceof UIPlayer) {
        	g.setCursonRow(cursorRow);
        	g.setCursonColumn(cursorColumn);
        }
    }
    
    private void checkPlayers() {
    	if(player1.getSVal() == player2.getSVal()) {
    		throw new PlayersWithSameSValException();
    	}
    }

    public SVal play() {
    	return play(true);
    }
    
    public SVal play(boolean verbous) {
    	int moveNumberDebug = 0; // debug
        while (!Rules.endOfGame(g, streakLength)) {
        	if(verbous) {
	            System.out.println("\n\n\n\n");
//	            g.printGrid();
	            g.printGridDebug();
        	}
            Move nextMove = currentPlayer.nextMove(g);
//            System.out.println("nextMove = " + nextMove);
            if(insertVal(nextMove)) {
            	if(verbous) {
            		System.out.println("game grid size: " + g.size() + ", streakLength: "+ streakLength + ", move number = " + moveNumberDebug); // debug
            	}
            	moveNumberDebug++; // debug
            	changePlayer();
            }
        }

        if(verbous) {
	        System.out.println("\n\n\n\n");
//	        g.printGrid();
	        g.printGridDebug();
        }
        SVal winner = Rules.findWinner(g, streakLength);
        if(verbous) {
        	printOutWinner(winner);
        }
        return winner;
    }
    
    private boolean insertVal(Move mv) {
        if (g.isSquareEmpty(mv.getRow(), mv.getColumn())) {
            g.insert(mv);
            return true;
        }
        return false;
    }

    private void printOutWinner(SVal winner) {
        if (winner == null) {
            System.out.println("There is no winner.");
        } else if (winner == SVal.CROSS) {
            System.out.println("The winner is cross.");
        } else {
            System.out.println("The winner is circle.");
        }
    }

    private void changePlayer() {
        if (currentPlayer == player1) {
        	currentPlayer = player2;
        } else {
        	currentPlayer = player1;
        }
    }

    public String toString() {
    	return player1.getName() + " vs " + player2.getName() + ", grid = " + g.size() + ", streakLength = " + streakLength;
    }
    
}
