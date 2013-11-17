/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazondynamo;

import java.io.Serializable;

/**
 *
 * @author cristiana
 */
public class StorageNodeMetadata implements Serializable, Comparable<StorageNodeMetadata> {
    private int port;
    private String ip;
    private int ID; //hiw own key
    
    public StorageNodeMetadata(int port, String ip, int ID){
        this.port = port;
        this.ip = ip;
        this.ID = ID;
    }
    
    public String toString()
    {
        return "storage node ID=" + ID;
    }
    
    public int getID()
    {
        return ID;
    }
    
    public int getPort()
    {
        return port;
    }
    
    public String getIp()
    {
        return ip;
    }

    @Override
    public int compareTo(StorageNodeMetadata o) 
    {
        return this.ID - o.ID;
    }

}
