/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game_components;

/**
 *
 * @author glabka
 */
public class Square {

    public enum SVal {
        CIRCLE, CROSS;
        
        public static SVal getOpposite(SVal val) {
        	if (val == CIRCLE) {
        		return CROSS;
        	} else {
        		return CIRCLE;
        	}
        }
    }
    
    private SVal val = null;

    public Square(){
        
    }
    
    public Square(SVal val) {
    	this.val = val;
    }
    
    public boolean isEmpty(){
        if(val == null){
            return true;
        } else {
            return false;
        }
    }

    public SVal getVal() {
        return val;
    }

    public void setVal(SVal val) {
        this.val = val;
    }
}
