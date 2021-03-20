/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_components;

import java.util.List;

import custom_exceptions.AlreadyFilledUpSquareException;
import custom_exceptions.IndexesOutOfRangeException;
import game_components.Square.SVal;
import grid_computations.Coordinate;

/**
 *
 * @author glabka
 */
public class Grid {

    private Square[][] grid;
    private Integer cursorRow;
    private Integer cursorColumn;

    public Grid(int size) {
        this.grid = new Square[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new Square();
            }
        }
    }

    public void insert(int row, int column, SVal val) {
        if (val == null) {
            throw new IllegalArgumentException("Value of SVal val can't be null.");
        }
        
        if(grid.length <= row) {
        	throw new IndexesOutOfRangeException("int row is " + row + " but max row index is " + (grid.length - 1));
        } else if (row < 0) {
        	throw new IndexesOutOfRangeException("int row is smaller then zero");
        } else if (grid[0].length <= column) {
        	throw new IndexesOutOfRangeException("int column is " + column + " but max column index is " + (grid[0].length - 1));
        } else if (column < 0) {
        	throw new IndexesOutOfRangeException("int column is smaller then zero");
        }
        
        Square s = this.grid[row][column];
        if (s.isEmpty()) {
            s.setVal(val);
        } else {
            throw new AlreadyFilledUpSquareException("Square of coordinates row = " + row + ", column = " + column + " is already filled.");
        }
    }
    
    public boolean verifyCoosInBoundies(int row, int column) {
    	if(grid.length <= row) {
        	return false;
        } else if (row < 0) {
        	return false;
        } else if (grid[0].length <= column) {
        	return false;
        } else if (column < 0) {
        	return false;
        } else {
        	return true;
        }
    }
    
    public void insert(ValuedMove mv) {
        insert(mv.getRow(), mv.getColumn(), mv.getVal());
    }
    
    public void delete(int row, int column) {
    	grid[row][column].setVal(null);
    }
    
    public void delete(List<ValuedMove> moves) {
    	for(ValuedMove move : moves) {
    		grid[move.getRow()][move.getColumn()].setVal(null);
    	}
    }
    
    public void delete(Coordinate coo) {
    	grid[coo.getRow()][coo.getColumn()] = null;
    }

    public SVal getVal(int row, int column) {
        return this.grid[row][column].getVal();
    }
    
    public boolean isSquareEmpty(int row, int column) {
    	return grid[row][column].getVal() == null;
    }
    
    public boolean isGridFull() {
    	for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid.length; j++) {
				if(grid[i][j].isEmpty()) {
					return false;
				}
			}
		}
    	return true;
    }

    public int size() {
        return this.grid.length;
    }
    
    public void setCursonRow(Integer row) {
    	if(row == null) {
    		cursorColumn = null;
    	}
    	cursorRow = row;
    }
    
    public void setCursonColumn(Integer column) {
    	if(column == null) {
    		cursorRow = null;
    	}
    	cursorColumn = column;
    }
    
    public Integer getCursorRow() {
    	return cursorRow;
    }
    
    public Integer getCursorColumn() {
    	return cursorColumn;
    }
    
    public void printGrid() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                SVal squareVal = grid[i][j].getVal();
                if (cursorRow != null && cursorColumn != null && cursorRow == i && cursorColumn == j) {
                    if (squareVal == null) {
                        System.out.print("+");
                    } else if (squareVal == SVal.CIRCLE) {
                        System.out.print("o");
                    } else if (squareVal == SVal.CROSS) {
                        System.out.print("x");
                    }
                } else if (squareVal == Square.SVal.CIRCLE) {
                    System.out.print("O");
                } else if (squareVal == Square.SVal.CROSS) {
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
    
    public void printGridDebug() {
        for (int i = 0; i < grid.length; i++) {
        	if(i == 0) {
        		System.out.print(" ");
        		for (int j = 0; j < grid.length; j++) {
    				System.out.print(j);
        			if (j != grid.length - 1) {
                        System.out.print("|");
                    }
				}
        		System.out.println();
        	}
        	System.out.print(i);
            for (int j = 0; j < grid.length; j++) {
                SVal squareVal = grid[i][j].getVal();
                if (cursorRow != null && cursorColumn != null && cursorRow == i && cursorColumn == j) {
                    if (squareVal == null) {
                        System.out.print("+");
                    } else if (squareVal == SVal.CIRCLE) {
                        System.out.print("o");
                    } else if (squareVal == SVal.CROSS) {
                        System.out.print("x");
                    }
                } else if (squareVal == Square.SVal.CIRCLE) {
                    System.out.print("O");
                } else if (squareVal == Square.SVal.CROSS) {
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
    
    public Grid gridsCopy() {
    	Grid newGrid = new Grid(this.size());
    	for (int i = 0; i < newGrid.size(); i++) {
			for (int j = 0; j < newGrid.size(); j++) {
				SVal val = grid[i][j].getVal();
				if(val != null) {
					newGrid.insert(i, j, val);
				}
			}
		}
    	
    	return newGrid;
    }

    public void test() {
//        System.out.println(this.grid[0]);
//        System.out.println(this.grid[1]);
//        System.out.println(this.grid[2]);
//        System.out.println(this.grid[0][2]);
    }
}
