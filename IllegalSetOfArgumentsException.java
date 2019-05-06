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
public class IllegalSetOfArgumentsException extends RuntimeException{
    
    public IllegalSetOfArgumentsException(){
        super();
    }
    
    public IllegalSetOfArgumentsException(String s){
        super(s);
    }
}
