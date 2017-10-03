/**
* Created and edited by: Sahil Pethe (ssp5329@g.rit.edu)
*                        Sukraat Ahluwalia (sxa4430@g.rit.edu)
*/
import java.io.File;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by pethe on 4/29/2017.
 */
public class MasterNode_FileStorageThread extends MasterNode implements Runnable{
    // this counter is used to denote the index of the file in the directory 
    int counter=0;

    MasterNode_FileStorageThread()
    {
        // we create the necessary folder directories if they haven't been created 
        if (!Files.isDirectory(Paths.get("files_to_transfer"))) {
            File dir = new File("files_to_transfer");
            dir.mkdir();
        }
        if (!Files.isDirectory(Paths.get("files_retrieved"))) {
            File dir = new File("files_retrieved");
            dir.mkdir();
        }
    }


    @Override
    public void run() {

        while (true) {
            try {
                while (true) {
                    // we create the directory object of the file path we want 
                    File dir = new File(Paths.get("").toAbsolutePath().toString()+"/files_to_transfer");
                    
                    // the below if block will make sure that it will go in only if the directory in question has 1 or more files 
                    if (dir.list().length > 0) {
                        // we store all the contents of the folder in a file array
                        File[] dir_contents = dir.listFiles();
                        // we just ignore the files which are being sent to avoid unnecessary duplication
                        while(activeFileTransfers.contains(dir_contents[counter]))
                        {
                            // we increment the counter here
                            counter++;
                        }
                        
                        // we skip all the hidden files which are generated once any of the files is deleted
                        if (!dir_contents[counter].getName().startsWith(".")) {
                            try {
                                
                                String fileName = dir_contents[counter].getName();
                                Random rand = new Random();
                                // we add the filename to the active file transfers list
                                activeFileTransfers.add(fileName);
                                // we select a random IP address from the arraylist Ipaddress
                                InetAddress ipSelected = IpAddress.get(rand.nextInt(IpAddress.size()));

				// we check if the current entry in the hashmap of the file to array list of IP address is empty or not
                                // if its empty, we simply add a new arraylist object and add the selected IP address to it
				if(!fileNames.containsKey(fileName)){
					ArrayList<InetAddress> f = new ArrayList<InetAddress>();
					f.add(ipSelected);
					fileNames.put(fileName,f);	
				}else{	
                                    // else we just add the IP address to the arraylist like usual
                                	fileNames.get(fileName).add(ipSelected);
                                }
				
                                // we initialize the thread for sending files to any random chunk server
                                Thread t1 = new Thread(new MasterNodeFileTransfer_sending(fileName, ipSelected));
                                t1.start();
                                // we add the filename to the active transfers list
                                activeFileTransfers.add(fileName);
                                // we make a clone of the Ipaddress array list 
                                ArrayList<InetAddress> bar = (ArrayList<InetAddress>)IpAddress.clone();
                                // we remove the IP address of the chunk server we just sent the file to
                                bar.remove(ipSelected);
                                // we select another random IP address and make sure that it isn't the one which we have already sent the file to
                                InetAddress ipSelected1 = bar.get(rand.nextInt(IpAddress.size()-1));
                                
                                // we add the IP address of the second chunk server to which we have sent the file to
                                fileNames.get(fileName).add(ipSelected1);
                                // we initialize a new therad to send the file to the second chunk server
                                Thread t2 = new Thread(new MasterNodeFileTransfer_sending(fileName, ipSelected1));
                                t2.start();
                                
                            } catch (NullPointerException a) {
                                // if we catch a null pointer exception, we set the counter to 0 and sleep thread for 1 sec
                                counter = 0;
                                Thread.sleep(1000);
                                a.printStackTrace();
                            }
                            catch(ArrayIndexOutOfBoundsException ai)
                            {
                                // in case the counter value goes above the directory array size, we just set the counter to 0 and start again 
                                counter=0;
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            // we increment the counter so that it goes to the next file in the folder
                            counter++;
                        }

                    }
                    // we sleep the thread for 1 second to allow a buffer period for new files to enter and make the directory non-empty 
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                // in case some random exception occurs, we set the counter to 0 anyways
                counter=0;
                

            }

        }
    }
}
