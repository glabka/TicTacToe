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
public class NotPlayersTurnException extends RuntimeException{
    
    public NotPlayersTurnException(){
        super();
    }
    
    public NotPlayersTurnException(String s){
        super(s);
    }
}
