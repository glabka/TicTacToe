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
public class AlreadyFilledUpSquareException extends RuntimeException{
    
    public AlreadyFilledUpSquareException(){
        super();
    }
    
    public AlreadyFilledUpSquareException(String s){
        super(s);
    }
}
