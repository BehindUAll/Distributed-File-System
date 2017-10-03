/**
* Created and edited by: Sahil Pethe (ssp5329@g.rit.edu)
*                        Sukraat Ahluwalia (sxa4430@g.rit.edu)
*/
import java.io.*;
import java.net.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MasterNode 

{
    
    // master node is the server which is connected to all the chunk servers
    // each and every request is made on the master node by the user
    // we can make use of the chunk servers to store files, retrieve files and get permission information
    // initializing the sockets required
    ServerSocket ss;
    Socket s;
    
    // we initialize all the data structures needed in this whole project
    public static ArrayList<InetAddress> IpAddress=new ArrayList<>();
    public static ArrayList<String> activeFileTransfers=new ArrayList<>();
    public static HashMap<String,ArrayList<InetAddress>> fileNames=new HashMap<>();
    // this flag is used to run the thread of the menu system only once
    public static int flag = 0;	

    public static void main (String as[])
    {
        // we created a new master node object
        MasterNode mys1=new MasterNode();
        mys1.start_running();
    }

    public void getFile(String fileName,Socket s){
        
    }
    
   
    
     
    public static void showFiles(){
        // we store all the files in a set 
        Set<String> names = fileNames.keySet();
        
        System.out.println("Listing files");
        
        // we print the names of the files in this for block
        for(String name:names){
            System.out.println(name);
        }
    }
    
    public static void getFile(String fName){
         // we store all the files in a set 
         Set<String> names = fileNames.keySet();
         
         // we iteratively initialize threads to receive files on the master node from the chunk servers
         for(String name:names){
            if(fName.equals(name)){
                InetAddress ip = fileNames.get(name).get(0);
                // creating threads for all the transfers that are happening
                new MasterNodeFileTransfer_receiving(name, ip).run();
            }
        }
    }
    
    public void getFilePerms(String fName){
        try {
            // we get the IP address from the hashmap fileNames 
            InetAddress ip = fileNames.get(fName).get(0);
            
            // initializing a new socket
            s = new Socket(ip, 9000);
            // initializing data output stream
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
            dout.writeUTF(fName);
            
            // initializing data input stream
            DataInputStream din = new DataInputStream(s.getInputStream());
            String perms = din.readUTF();
            
            s.close();
            System.out.println("Permissions for "+fName+":"+" "+perms);
            
        } catch (IOException ex) {
            Logger.getLogger(ChunkServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    
    
    public void start_running()
    {
        
        try
        {
            
            System.out.println("Master node Started");
            while(true) {
                // master node starts listening on port number 8000
                System.out.println("Server listening on port number 8000");
                ss = new ServerSocket(8000);
                //accepting incoming user connection
                s = ss.accept();
                
                //we store the IP address of the users in an arraylist for future refernce
                IpAddress.add(s.getInetAddress());
                
                System.out.println(s);
                System.out.println("CLIENT CONNECTED");
                
                // we create new threads for handling file storage and master node's heartbeat
                new Thread(new MasterNode_FileStorageThread()).start();
                new Thread(new MasterNode_Heartbeat(s.getInetAddress())).start();
                // prints the user's IP address each time a user connects to the server
                System.out.println(IpAddress);
                
                // we set the flag so that the thread for the menu system doesn't get initialized more than once
		if(flag==0){
			flag = 1;
                        // creating the menu system thread
			new Thread(new MenuClass()).start();                
               } 
            
                // closing the socket connections
                ss.close();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

    }

}
