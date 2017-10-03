import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;

/**
 * Created by pethe on 4/30/2017.
 */
public class ChunkServerFileTransfer_sending extends ChunkServer implements Runnable{


    String eventManagerIPAdress="127.21.34.67";
    String fileName;


    @Override
    public void run()  {

        while (true) {

            try {
                // the below block of code is to send files over the network to the master node
                //Initialize socket
                ServerSocket ssock = new ServerSocket(6000);
                Socket socket = ssock.accept();
                
                // we read the file name in order to save it
                DataInputStream din = new DataInputStream(socket.getInputStream());
                fileName=din.readUTF();
                File file = new File(Paths.get("").toAbsolutePath().toString()+"/cached_data/"+fileName);

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
               
                // closing the socket connections
                socket.close();
                ssock.close();

            } catch (Exception e) {
                e.printStackTrace();

            }
        }

    }
}
