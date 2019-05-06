/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe;

/**
 *
 * @author glabka
 */
public class TicTacToe {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO compare with https://www.quora.com/What-are-some-good-project-ideas-for-an-undergraduate-object-oriented-programming-course-using-Java
        
        Grid g = new Grid(5);
        g.insert(1, 0, Square.SVal.CIRCLE);
        g.insert(1, 1, Square.SVal.CIRCLE);
        g.insert(1, 2, Square.SVal.CIRCLE);
        
        g.insert(0, 1, Square.SVal.CIRCLE);
        g.insert(0, 2, Square.SVal.CIRCLE);
        g.insert(0, 4, Square.SVal.CIRCLE);
        
        g.insert(2, 4, Square.SVal.CROSS);
        g.insert(3, 4, Square.SVal.CROSS);
        g.insert(4, 4, Square.SVal.CROSS);
        
        g.insert(2, 2, Square.SVal.CROSS);
        g.insert(3, 3, Square.SVal.CROSS);
        g.printGrid();
        Rules.test(g);
        
    }
    
}
