/**
* Created and edited by: Sahil Pethe (ssp5329@g.rit.edu)
*                        Sukraat Ahluwalia (sxa4430@g.rit.edu)
*/
import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;


public class ChunkServerFileTransfer_receiving extends ChunkServer implements Runnable {


    // this is the default IP address of the master node
    String eventManagerIPAdress="127.21.34.67";
    // this is the filname which we have to save
    String fileName;


    @Override
    public void run()  {

        while (true) {

            try {
                // the below code is for receiving the files on the chunk server
                //Initialize sockets
                ServerSocket ssock = new ServerSocket(5000);
                Socket socket = ssock.accept();
                System.out.println("New incoming connection established on IP address:"+ Inet4Address.getLocalHost().getHostAddress()+"!");
                DataInputStream din = new DataInputStream(socket.getInputStream());
                fileName=din.readUTF();

                byte[] contents = new byte[10000];

                long availSpace = new File("/").getFreeSpace();
                
                
                FileOutputStream fos = new FileOutputStream(Paths.get("").toAbsolutePath().toString()+"/cached_data/"+fileName);
                File tempFile = new File(Paths.get("").toAbsolutePath().toString()+"/cached_data/"+fileName);
                long fSize = tempFile.length();
                
                // we check if the available space exceeds the file size
                if(availSpace <= fSize){
                    System.err.println("Not enough space exiting with error code 1");
                    System.exit(1);
                }
                
               
               
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                InputStream is = socket.getInputStream();

                //No of bytes read in one read() call
                int bytesRead = 0;

                while ((bytesRead = is.read(contents)) != -1)
                    bos.write(contents, 0, bytesRead);

                
                bos.flush();
                // we close the sockets of the connection
                socket.close();
                ssock.close();

                // we print out the file name that has been received by the chunk server
                System.out.println("File "+ fileName+" saved successfully!");
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

    }
}
