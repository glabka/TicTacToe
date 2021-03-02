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
public class WebPlayerNotInitialized extends RuntimeException{
    
    public WebPlayerNotInitialized(){
        super();
    }
    
    public WebPlayerNotInitialized(String s){
        super(s);
    }
}
