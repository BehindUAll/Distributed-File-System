/**
* Created and edited by: Sahil Pethe (ssp5329@g.rit.edu)
*                        Sukraat Ahluwalia (sxa4430@g.rit.edu)
*/
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ChunkServer_permissionListener implements Runnable{

     public String sendFilePerms(String fileName){
        
         // we get the file path of the file that needs permission checking
        File temp = new File(Paths.get("").toAbsolutePath().toString()+"/cached_data/"+fileName);
        boolean isWrite = temp.canWrite();
        boolean isRead = temp.canRead();
        boolean isExec = temp.canExecute();
        
        // we keep on adding the file permissions one by one
        String perms = "";
        
        if(isWrite){
            perms += "-";
        }else{
            perms += "w";
        }
        
        if(isRead){
            perms += "-";
        }else{
            perms += "r";
        }
        
        if(isExec){
            perms += "-";
        }else{
            perms += "x";
        }
        
        return perms;
    }
     
    @Override
    public void run() {
        while(true){
            try {
                
                System.out.println("Chunk Server listening on port number 9000");
                // creating new server socket
                ServerSocket ss = new ServerSocket(9000);
                //accepting incoming user connection
                Socket s = ss.accept();
                
                // creating data input stream for reading the file name
                DataInputStream din = new DataInputStream(s.getInputStream());
                String str = din.readUTF();
                String perms = sendFilePerms(str);
                // sending the permissions using data output stream
                DataOutputStream dout = new DataOutputStream(s.getOutputStream());
                dout.writeUTF(perms);
                
                // closing the socket connections
                s.close();
		ss.close();
        } catch (IOException ex) {
            Logger.getLogger(MasterNode_reallocate.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
    }
    
}
