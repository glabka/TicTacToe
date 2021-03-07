/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package custom_exceptions;

/**
 *
 * @author glabka
 */
public class GameDoesntExistExeption extends RuntimeException{
    
    public GameDoesntExistExeption(){
        super();
    }
    
    public GameDoesntExistExeption(String s){
        super(s);
    }
}
