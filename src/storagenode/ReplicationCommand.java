/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package storagenode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 *
 * @author cristiana
 */
public class ReplicationCommand implements Serializable{
    int key;
    Object obj;
    ArrayList<NodeClock> clocks;

    public ReplicationCommand(int key, Object obj, ArrayList<NodeClock> clocks) {
        this.key = key;
        this.obj = obj;
        this.clocks = clocks;
    }

    @Override
    public String toString() {
        return "ReplicationCommand{" + "key=" + key + ", obj=" + obj + ", clocks=" + clocks + '}';
    }
    
    
    
}
