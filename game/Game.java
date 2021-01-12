/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import java.util.InputMismatchException;
import java.util.Scanner;
import tictactoe.Square.SVal;

/**
 *
 * @author glabka
 */
public class Game {

    private final Grid g;
    private int streakLength;
    private int cursorRow;
    private int cursorColumn;
    private SVal currentVal;

    public Game() {
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

        System.out.println("What should be a size of required streak?");
        while (true) {
            streakLength = readInt();
            if (streakLength > size) {
                System.out.println("Enter number less than or equal to entered size, i.e. " + size + ".");
            } else {
                break;
            }
        }

        currentVal = SVal.CROSS;
        cursorRow = size / 2;
        cursorColumn = size / 2;
    }

    private int readInt() {
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

    public void play() {
        SVal winner;
        while ((winner = Rules.findWinner(g, streakLength)) == null) {
            System.out.println("\n\n\n\n");
            printGrid();
            char nextChar = readChar();
            switch (nextChar) {
                case 'w':
                    if (cursorRow == 0) {
                        cursorRow = g.size() - 1;
                    } else {
                        cursorRow -= 1;
                    }
                    break;
                case 's':
                    if (cursorRow == g.size() - 1) {
                        cursorRow = 0;
                    } else {
                        cursorRow += 1;
                    }
                    break;
                case 'a':
                    if (cursorColumn == 0) {
                        cursorColumn = g.size() - 1;
                    } else {
                        cursorColumn -= 1;
                    }
                    break;
                case 'd':
                    if (cursorColumn == g.size() - 1) {
                        cursorColumn = 0;
                    } else {
                        cursorColumn += 1;
                    }
                    break;
                case 'i':
                    if (insertVal()) {
                        changePlayer();
                    }
                    break;
            }

        }

        System.out.println("\n\n\n\n");
        printGrid();
        printOutWinner(winner);
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
        if (currentVal == SVal.CROSS) {
            currentVal = SVal.CIRCLE;
        } else {
            currentVal = SVal.CROSS;
        }
    }

    private boolean insertVal() {
        if (g.getVal(cursorRow, cursorColumn) == null) {
            g.insert(cursorRow, cursorColumn, currentVal);
            return true;
        }
        return false;
    }

    private void printGrid() {
        for (int i = 0; i < g.size(); i++) {
            for (int j = 0; j < g.size(); j++) {
                SVal squareVal = g.getVal(i, j);
                if (cursorRow == i && cursorColumn == j) {
                    if (squareVal == null) {
                        System.out.print("+");
                    } else if (squareVal == SVal.CIRCLE) {
                        System.out.print("◦");
                    } else if (squareVal == SVal.CROSS) {
                        System.out.print("×");
                    }
                } else if (squareVal == Square.SVal.CIRCLE) {
                    System.out.print("O");
                } else if (squareVal == Square.SVal.CROSS) {
                    System.out.print("X");
                } else {
                    System.out.print(" ");
                }
                if (j != g.size() - 1) {
                    System.out.print("|");
                }
            }
            System.out.println("");
        }
    }

    private char readChar() {
        Scanner in = new Scanner(System.in);
        while (true) {
            if (in.hasNext()) {
                String str = in.next();
                in.nextLine();
                return str.charAt(0);
            } else {
                System.out.println("Enter w, s, a or d to change cursor position or enter i to insert cross or circle.");
                in.nextLine();
            }
        }
    }

}
