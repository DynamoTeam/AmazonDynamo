/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package storagenode;

import client.BaseClient;
import java.io.IOException;

/**
 *
 * @author cristiana
 */
public class StorageNodeClient extends BaseClient{
    
    String host;
    int port;
    
    
    public StorageNodeClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void send(StorageNodeMetadata metadata) throws IOException{
        send(metadata, host, port);
    }
    
    public void send(String result) throws IOException{
        send(result, host, port);
    }
}
