/**
* Created and edited by: Sahil Pethe (ssp5329@g.rit.edu)
*                        Sukraat Ahluwalia (sxa4430@g.rit.edu)
*/
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MasterNode_reallocate implements Runnable{
    // this class is basically used to reallocate the files from the failed chunk server 
    // to some other random chunk server to maintain the number of replication
    @Override
    public void run() {
        // we get the path for the files to transfer
        String src_path = Paths.get("").toAbsolutePath().toString()+"/files_retrieved/";
        
        // we create a file list to move all the files inside the folder
        File tempDir = new File(src_path);
        File[] list = tempDir.listFiles();
        
        System.out.println("Moving");
        
        // we move each and every file from the foler files_retrieved to files_to_transfer
        for(File foo:list){
            String name = foo.getName();
            
            String send_path = src_path + name;
            String dest_path = Paths.get("").toAbsolutePath().toString()+"/files_to_transfer/";
            
            // after moving the files to files_to_trnasfer folder, the reallocation takes place automatically 
            // since the master node is always checking for new files to transfer to the chunk servers
            foo.renameTo(new File(dest_path+name));
        }
        
        // we print out that the reallocation is complete
        System.out.println("Reallocation complete");
    }
    
}
