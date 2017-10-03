/**
* Created and edited by: Sahil Pethe (ssp5329@g.rit.edu)
*                        Sukraat Ahluwalia (sxa4430@g.rit.edu)
*/
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;


public class MasterNode_Heartbeat extends MasterNode implements Runnable{
    
    // we create an object variable to store the ip address of the chunk server to which the master node is receiving a heartbeat from
    public InetAddress ip;
    
    public MasterNode_Heartbeat(InetAddress ip){
        // storing the ip address of the chunk server
        this.ip = ip;
    }
    
    @Override
    public void run() {
       while(true){
           try{
               // we sleep the thread for 20 seconds to make sure that the routing delays or messaging delays are accounted for 
               // after the 20 second period, we have made sure that the chunk server which isnt sending any heartbeat is dead 
               // and we can then start with reallocation of the files which were previously saved on the chunk server which failed
               Thread.sleep(20000);
               // we create a new socket 
               Socket s = new Socket(ip,10000);
               // we set the connection timeout to 20 seconds
               s.setSoTimeout(20000);
               // we create a new data input stream object
               DataInputStream hbeat_in = new DataInputStream(s.getInputStream());
               String msg = hbeat_in.readUTF();
                
               // we close the socket conection
               s.close();
           } catch(InterruptedException e){
		
	   }catch(IOException ex) {
		System.err.println("Hearbeat Failed beginning reallocation");
               
                // after the chunk server fails and this exception is thrown, we make a list of all the files 
                // which were on the chunk server which failed and then add it to the array list filesFailed
		ArrayList<String> filesFailed = new ArrayList<String>();
		
                // we add unique file names to the set called files
		Set<String> files = fileNames.keySet();
                // we iterate through the files arraylist and check if the IP address mathces with the IP 
                // address stored in the arraylist of the hashmap filenames
		for(String name:files){
			ArrayList<InetAddress> ips = fileNames.get(name);
			if(ips.contains(ip)){
                            // if there is a match, we know for sure that the filename in question was 
                            // on the chunk server which failed just now
				filesFailed.add(name);
                                // then we simply remove that entry from hte arraylist
				fileNames.get(name).remove(ip);
			}
		}
                // we remove the IP address from the arraylist IpAddress too
		IpAddress.remove(ip);
		
                // for all the files in the filesFailed arraylist, we are receiving the files from the other chunk server to the master node
		for(String fnames:filesFailed){
			InetAddress first_ip = fileNames.get(fnames).get(0);
                        // creating the thread to do the file transfer from the chunk server to the master node
			new Thread(new MasterNodeFileTransfer_receiving(fnames,first_ip)).start();
		}	
                
                // after all of the file transfering has done
		new Thread(new MasterNode_reallocate()).start();			
	  }catch(Exception err){

	 }
       }
    }
    
}
