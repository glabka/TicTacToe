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
        CIRCLE, CROSS
    }
    
    private SVal val = null;

    public Square(){
        
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
