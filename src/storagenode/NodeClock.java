/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package storagenode;

import java.io.Serializable;

/**
 *
 * @author cristiana
 */
public class NodeClock implements Serializable{
    int nodeId;
    long clock;
    
    public NodeClock(int nodeId){
        clock = System.currentTimeMillis();
        this.nodeId = nodeId;
    }
    
    @Override
    public String toString(){
        return "NodeID: " + nodeId + " with clock: " + clock;
                
    }
    
           
}
