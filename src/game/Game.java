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
        g.setCursonRow(cursorRow);
        g.setCursonColumn(cursorColumn);
    }
    
    private void checkPlayers() {
    	if(player1.getSVal() == player2.getSVal()) {
    		throw new PlayersWithSameSValException();
    	}
    }

    public void play() {
        SVal winner;
        while ((winner = Rules.findWinner(g, streakLength)) == null) {
            System.out.println("\n\n\n\n");
            g.printGrid();
            Move nextMove = currentPlayer.nextMove(g);
            if(insertVal(nextMove)) {
            	changePlayer();
            }
        }

        System.out.println("\n\n\n\n");
        g.printGrid();
        printOutWinner(winner);
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
            throw new IllegalArgumentException();
        }
        if (winner == SVal.CROSS) {
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


}
