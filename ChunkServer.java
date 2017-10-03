/**
* Created and edited by: Sahil Pethe (ssp5329@g.rit.edu)
*                        Sukraat Ahluwalia (sxa4430@g.rit.edu)
*/
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChunkServer {
    // the chunk server is the server connected to the master node and will get offloaded with 
    // files that the chunk server sends

    // we use socket s to connect to a server
    Socket s;
    // datastream is used to stream data
    DataInputStream din;
    DataOutputStream dout;
    PrintWriter pw;
    ObjectInputStream ob_in;
    static String eventManIP="129.21.22.196";

    public ChunkServer() {
            
    }

    public static void main(String args[]) {
        
        // we use this in case the initial IP address of the master node is different than the default one
        if(args.length == 0){
          //do nothing
        }else{
            eventManIP = args[0];
        }
        ChunkServer myc1 = new ChunkServer();
        myc1.start_running();
    }
    
   
   
       
    
    
    public void start_running() {
        try {
            // this method initializes all the threads that need to run parallely in order for the different chunk server functions to work
	    new Thread(new ChunkServer_Heartbeat()).start();

            s = new Socket(eventManIP, 8000);

            //we print the user PC information
            System.out.println(s);
            
            //we close the connection in order to listen to it again
            s.close();
            
            // we create directories on the chunk server
            if (!Files.isDirectory(Paths.get("cached_data"))) {
                File dir = new File("cached_data");
                dir.mkdir();
            }
            
            // all the necessary threads are initialized here
            new Thread(new ChunkServerFileTransfer_receiving()).start();
            new Thread(new ChunkServerFileTransfer_sending()).start();
	    new Thread(new ChunkServer_permissionListener()).start();
         
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
