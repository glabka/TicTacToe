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
public class OnlineGameNotInitialized extends RuntimeException{
    
    public OnlineGameNotInitialized(){
        super();
    }
    
    public OnlineGameNotInitialized(String s){
        super(s);
    }
}
