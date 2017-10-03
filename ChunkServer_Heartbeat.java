/**
* Created and edited by: Sahil Pethe (ssp5329@g.rit.edu)
*                        Sukraat Ahluwalia (sxa4430@g.rit.edu)
*/
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ChunkServer_Heartbeat implements Runnable {

    @Override
    public void run() {
        while(true){
            try {
                // initialzing the socket
                ServerSocket ss = new ServerSocket(10000);
                // accepting the socket
                Socket s = ss.accept();
                
                // initializing the data output stream
                DataOutputStream sender = new DataOutputStream(s.getOutputStream());
                sender.writeUTF("Alive");
                
                
                // closing the socket connections
                s.close();
                ss.close();
            } catch (IOException ex) {
                Logger.getLogger(ChunkServer_Heartbeat.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
