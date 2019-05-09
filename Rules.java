/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

import tictactoe.Square.SVal;

/**
 *
 * @author glabka
 */
public class Rules {

    /**
     * Function finds winner of Tic Tac Toe. Returns CIRCLE, CROSS or null (in
     * case of no winner).
     *
     * @param g
     * @param streakLength
     * @return
     */
    public static SVal findWinner(Grid g, int streakLength) {
        if (g.size() < streakLength) {
            throw new IllegalArgumentException("streakLength can't be bigger then g.size().");
        }
        if (checkEverything(g, SVal.CROSS, streakLength)) {
            return SVal.CROSS;
        } else if (checkEverything(g, SVal.CIRCLE, streakLength)) {
            return SVal.CIRCLE;
        } else {
            return null;
        }
    }

    public static void test(Grid g) {
//        for (int i = 0; i < g.size(); i++) {
//            System.out.println(checkRow(g, SVal.CIRCLE, 3, i));
//        }
//        for (int i = 0; i < g.size(); i++) {
//            System.out.println(checkColumn(g, SVal.CROSS, 3, i));
//        }
//        System.out.println(checkLeftDiagonal(g, SVal.CIRCLE, 2, 0, 0));
//        System.out.println(checkLeftDiagonal(g, SVal.CIRCLE, 2, 0, 1));
//        System.out.println(checkLeftDiagonal(g, SVal.CIRCLE, 2, 0, 2));
//        System.out.println("");
//        System.out.println(checkLeftDiagonal(g, SVal.CROSS, 3, 0, 0));

//        System.out.println(checkLeftDiagonals(g, SVal.CIRCLE, 2));
//        System.out.println(checkLeftDiagonals(g, SVal.CIRCLE, 3));
//        System.out.println(checkLeftDiagonals(g, SVal.CROSS, 3));
//        System.out.println(checkRightDiagonal(g, SVal.CIRCLE, 2, 0, 2));
//        System.out.println(checkRightDiagonal(g, SVal.CROSS, 2, 2, 4));
//        System.out.println(checkRightDiagonal(g, SVal.CROSS, 2, 3, 4));
//        System.out.println(checkRightDiagonals(g, SVal.CROSS, 3));
//        System.out.println(checkRightDiagonals(g, SVal.CROSS, 2));
//        System.out.println(checkRightDiagonals(g, SVal.CIRCLE, 3));
//        System.out.println(checkRightDiagonals(g, SVal.CIRCLE, 2));
    }

    private static boolean checkEverything(Grid g, SVal val, int streakLength) {
        if (checkRows(g, val, streakLength)
                || checkColumns(g, val, streakLength)
                || checkLeftDiagonals(g, val, streakLength)
                || checkRightDiagonals(g, val, streakLength)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check for continuous streak of length streakLength of values val in rows of
     * Grid g
     *
     * @param g
     * @param val
     * @param streakLength
     * @return Returns true if streak is found
     */
    private static boolean checkRows(Grid g, SVal val, int streakLength) {
        for (int i = 0; i < g.size(); i++) {
            if (checkRow(g, val, streakLength, i)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkRow(Grid g, SVal val, int streakLength, int row) {
        int counter = 0;

        for (int j = 0; j < g.size(); j++) {
            if (g.getVal(row, j) == null) {
                counter = 0;
            } else if (g.getVal(row, j) == val) {
                counter++;
            }
            if (counter == streakLength) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check for continuous streak of length streakLength of values val in columns
     * of Grid g
     *
     * @param g
     * @param val
     * @param streakLength
     * @return Returns true if streak is found
     */
    private static boolean checkColumns(Grid g, SVal val, int streakLength) {
        for (int i = 0; i < g.size(); i++) {
            if (checkColumn(g, val, streakLength, i)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkColumn(Grid g, SVal val, int streakLength, int column) {
        int counter = 0;

        for (int j = 0; j < g.size(); j++) {
            if (g.getVal(j, column) == null) {
                counter = 0;
            } else if (g.getVal(j, column) == val) {
                counter++;
            }
            if (counter == streakLength) {
                return true;
            }
        }

        return false;
    }

    private static boolean checkLeftDiagonals(Grid g, SVal val, int streakLength) {
        // checking diagonals starting on left side
        for (int i = 0; i < g.size(); i++) {
            if (checkLeftDiagonal(g, val, streakLength, i, 0)) {
                return true;
            }
        }
        // checking diagonals starting on upper side
        for (int i = 0; i < g.size(); i++) {
            if (checkLeftDiagonal(g, val, streakLength, 0, i)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkLeftDiagonal(Grid g, SVal val, int streakLength, int startingRow, int startingColumn) {
        if (startingRow != 0 && startingColumn != 0) {
            throw new IllegalSetOfArgumentsException();
        }

        int counter = 0;
        int row = startingRow;
        int column = startingColumn;

        while (row < g.size() && column < g.size()) {
            if (g.getVal(row, column) == null) {
                counter = 0;
            } else if (g.getVal(row, column) == val) {
                counter++;
            }
            if (counter == streakLength) {
                return true;
            }
            row++;
            column++;
        }

        return false;
    }

    private static boolean checkRightDiagonals(Grid g, SVal val, int streakLength) {
        // checking diagonals starting on right side
        for (int i = 0; i < g.size(); i++) {
            if (checkRightDiagonal(g, val, streakLength, i, g.size() - 1)) {
                return true;
            }
        }
        // checking diagonals starting on upper side
        for (int i = 0; i < g.size(); i++) {
            if (checkRightDiagonal(g, val, streakLength, 0, i)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkRightDiagonal(Grid g, SVal val, int streakLength, int startingRow, int startingColumn) {
        if (startingRow != 0 && startingColumn != g.size() - 1) {
            throw new IllegalSetOfArgumentsException();
        }

        int counter = 0;
        int row = startingRow;
        int column = startingColumn;

        while (row < g.size() && column >= 0) {
            if (g.getVal(row, column) == null) {
                counter = 0;
            } else if (g.getVal(row, column) == val) {
                counter++;
            }
            if (counter == streakLength) {
                return true;
            }
            row++;
            column--;
        }

        return false;
    }

}
