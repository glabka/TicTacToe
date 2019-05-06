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
public class Grid {

    private Square[][] grid;

    public Grid(int length) {
        this.grid = new Square[length][length];
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                grid[i][j] = new Square();
            }
        }
    }

    public void insert(int row, int column, SVal val) {
        if (val == null) {
            throw new IllegalArgumentException("Value of SVal val can't be null.");
        }
        Square s = this.grid[row][column];
        if (s.isEmpty()) {
            s.setVal(val);
        } else {
            throw new AlreadyFilledUpSquareException("Square of coordinates row = " + row + ", column = " + column + " is already filled.");
        }
    }

    public SVal getVal(int row, int column) {
        return this.grid[row][column].getVal();
    }

    public int size() {
        return this.grid.length;
    }

    public void printGrid() {
        for (int i = 0; i < grid.length; i++) {
//            System.out.println("------------");
            for (int j = 0; j < grid.length; j++) {
                if (this.grid[i][j].getVal() == SVal.CIRCLE) {
                    System.out.print("O");
                } else if (this.grid[i][j].getVal() == SVal.CROSS) {
                    System.out.print("X");
                } else {
                    System.out.print(" ");
                }
                if (j != grid.length - 1) {
                    System.out.print("|");
                }
            }
            System.out.println("");
        }
    }

    public void test() {
//        System.out.println(this.grid[0]);
//        System.out.println(this.grid[1]);
//        System.out.println(this.grid[2]);
//        System.out.println(this.grid[0][2]);
    }
}
