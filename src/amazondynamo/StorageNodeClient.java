/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package amazondynamo;

import java.io.IOException;

/**
 *
 * @author cristiana
 */
public class StorageNodeClient extends BaseClient{
    
    String hostLB;
    int portLB;
    
    
    public StorageNodeClient(String hostLB, int portLB) {
        this.hostLB = hostLB;
        this.portLB = portLB;
    }

    public void sendToLB(StorageNodeMetadata metadata) throws IOException{
        send(metadata, hostLB, portLB);
    }
}
