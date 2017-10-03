/**
* Created and edited by: Sahil Pethe (ssp5329@g.rit.edu)
*                        Sukraat Ahluwalia (sxa4430@g.rit.edu)
*/
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;

/**
 * Created by pethe on 4/30/2017.
 */
public class MasterNodeFileTransfer_receiving extends MasterNode implements Runnable{
    String fileName;
    InetAddress ipClientPC;

    MasterNodeFileTransfer_receiving(String fName, InetAddress ip)
    {
        // we get the input file name and ip address using this constructor
        fileName=fName;
        ipClientPC=ip;
    }

   
    public void run() {
        try {
            // the below block of code will handle the file transfer part from chunk server to master node
            //Initialize Sockets
            Socket socket = new Socket(ipClientPC, 6000);
            // initializing data output stream
            DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
            dout.writeUTF(fileName);


            byte[] contents = new byte[10000];

            //Initialize the FileOutputStream to the output file's full path.
            FileOutputStream fos = new FileOutputStream(Paths.get("").toAbsolutePath().toString()+"/files_retrieved/"+fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            InputStream is = socket.getInputStream();

            //No of bytes read in one read() call
            int bytesRead = 0;

            while ((bytesRead = is.read(contents)) != -1)
                bos.write(contents, 0, bytesRead);

            bos.flush();
            // closing the socket connection 
            socket.close();
            // we print out the file name that has been saved on the master node
            System.out.println("File "+ fileName+" saved successfully!");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
