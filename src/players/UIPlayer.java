package players;

import java.util.Scanner;

import game.CursorController;
import game_components.Grid;
import game_components.ValuedMove;
import game_components.Square.SVal;

public class UIPlayer extends Player {
	
	private String inputMessage;

	public UIPlayer(SVal playersSVal, String name) {
		super(playersSVal, name);
		this.inputMessage = "Enter w, s, a or d to change cursor position or enter i to insert " + this.playersSVal;
	}


	@Override
	public ValuedMove nextMove(Grid g) {
		if (g.getCursorRow ()== null || g.getCursorColumn() == null) {
			int cursorRow = g.size() / 2;
	        int cursorColumn = g.size() / 2;
	        g.setCursonRow(cursorRow);
	        g.setCursonColumn(cursorColumn);
		}
		
		System.out.println("Enter move for player " + name);
		System.out.println(inputMessage);
		g.printGrid();
		char readChar;
		
		while (true) {
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
			
			if(g.getVal(g.getCursorRow(), g.getCursorColumn()) == null) {
				return new ValuedMove(g.getCursorRow(), g.getCursorColumn(), this.playersSVal);
			} else {
				System.out.println("On given coordinates there is already a filled up square. Please choose different coordinates.");
			}
		}
		
		
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
