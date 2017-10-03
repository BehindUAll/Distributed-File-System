/**
* Created and edited by: Sahil Pethe (ssp5329@g.rit.edu)
*                        Sukraat Ahluwalia (sxa4430@g.rit.edu)
*/
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class MasterNodeFileTransfer_sending extends MasterNode implements Runnable{

    String fileName;
    InetAddress ipClientPC;
    int defaultPort;
    
    MasterNodeFileTransfer_sending(String fName, InetAddress ip)
    {
        // we input the file name and ip address from the constructor 
        fileName=fName;
        ipClientPC=ip;
        
    }

    @Override
    public void run(){
        try {
            // the below code is used to transfer files from master node to the chunk server
            //Initialize Sockets
            Socket socket = new Socket(ipClientPC, 5000);
            // initializing the data output stream
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            dout.writeUTF(fileName);
            //Specify the file
            File file = new File(Paths.get("").toAbsolutePath().toString()+"/files_to_transfer/"+fileName);
            //File file = new File("files_to_transfer\\"+fileName);
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);

            //Get socket's output stream
            OutputStream os = socket.getOutputStream();

            //Read File Contents into contents array
            byte[] contents;
            long fileLength = file.length();
            long current = 0;

            long start = System.nanoTime();
            while (current != fileLength) {
                int size = 10000;
                if (fileLength - current >= size)
                    current += size;
                else {
                    size = (int) (fileLength - current);
                    current = fileLength;
                }
                contents = new byte[size];
                bis.read(contents, 0, size);
                os.write(contents);
          
            }

            os.flush();
            
            // closing the socket connection
            socket.close();
            // we delete the files from the master node after the transfer is completed
            file.delete();
            // we remove the active file transfers filename from the arraylist
            activeFileTransfers.remove(fileName);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
