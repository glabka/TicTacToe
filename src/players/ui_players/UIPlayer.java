package players.ui_players;

import java.util.Scanner;

import game.CursorController;
import game_components.Grid;
import game_components.Move;
import game_components.Square.SVal;
import players.Player;

public class UIPlayer extends Player {
	
	private String inputMessage;

	public UIPlayer(SVal playersSVal, String name) {
		super(playersSVal, name);
		this.inputMessage = "Enter w, s, a or d to change cursor position or enter i to insert " + this.playersSVal;
	}


	@Override
	public Move nextMove(Grid g) {		
		char readChar;
		
		while((readChar = readChar()) != 'i') {
			if(readChar == 'w') {
				CursorController.moveUp(g);
			} else if (readChar == 's') {
				CursorController.moveDown(g);
			} else if (readChar == 'a') {
				CursorController.moveLeft(g);
			} else if (readChar == 'd') {
				CursorController.moveRight(g);
			} else {
				System.out.println(inputMessage);
			}
			g.printGrid();
		}
		
		return new Move(g.getCursorRow(), g.getCursorColumn(), this.playersSVal);
	}

	
	private char readChar() {
		Scanner in = new Scanner(System.in);
		boolean messagePrinted = false;
		
        while (true) {
            if (in.hasNext()) {
                String str = in.next();
                in.nextLine();
                return str.charAt(0);
            } else {
//            	if(!messagePrinted) {
            		System.out.println(inputMessage);
//            		messagePrinted = true;
//            	}
//            	if(in.hasNextLine()) {
                	in.nextLine();
//                }
            }
        }
    }	
}
