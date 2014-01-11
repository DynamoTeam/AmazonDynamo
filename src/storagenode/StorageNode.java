/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package storagenode;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author laura
 */
public class StorageNode{
    
    HashMap keyValueStore;
    public static StorageNodeMetadata metadata;
    
    public StorageNode(int port, String ip, int ID){
        keyValueStore = new HashMap();
        metadata = new  StorageNodeMetadata(port, ip, ID);
    }
    
    public String computeMD5(String key)
    {
        StringBuffer sb = new StringBuffer();
        
        try {            
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
     
            byte byteData[] = md.digest();
     
            //convert the byte to hex            
            for (int i = 0; i < byteData.length; i++) {
             sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(StorageNode.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        return sb.toString();
    }   
    
    public static void main(String args[]) throws IOException{
            String ip = "localhost";
                      
            Random rand = new Random();
            int max = 2999;
            int min = 2225;
            int port = rand.nextInt((max - min) + 1) + min;
            
            max = 30;
            min = 10;
            int ID = rand.nextInt((max - min) + 1) + min;
            StorageNode node = new StorageNode(port, ip, ID);            
            
            int portLB = 2223;
            StorageNodeClient clientToLB = new StorageNodeClient("localhost", portLB);
            clientToLB.sendToLB(node.metadata);
            
            StorageNodeServer snServer = new StorageNodeServer(node.metadata.getPort());         
            snServer.startAcceptingConnections();            
    }
}
