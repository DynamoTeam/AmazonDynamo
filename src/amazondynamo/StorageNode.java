/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package amazondynamo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author laura
 */
public class StorageNode {
    
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
}
