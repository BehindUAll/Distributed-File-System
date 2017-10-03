/**
* Created and edited by: Sahil Pethe (ssp5329@g.rit.edu)
*                        Sukraat Ahluwalia (sxa4430@g.rit.edu)
*/
import java.util.Scanner;


public class MenuClass extends MasterNode implements Runnable {
  
    @Override
    public void run() {
        while(true){
            
            // we take input from the user for the option he wants to execute
            Scanner in_scan = new Scanner(System.in);
            
            // printing out all the list of options the user can execute
            System.out.println("Enter any of the commands below:");
            System.out.println("Enter 1 to get a file");
            System.out.println("Enter 2 to get a files permissions");
            System.out.println("Enter 3 to get a list of files in the system");
            System.out.println("Enter 4 to reallocate files");
           
                // taking the index name from the user
                int choice = in_scan.nextInt();
                in_scan.nextLine();
                switch(choice){
                    // we use a switch case block to get the execute code according to the index
                    case 1:
                        String name = in_scan.nextLine();
                        getFile(name);
                        break;
                        
                    case 2:
                        System.out.println("Enter the file name for permissions needed");
                        String fName = in_scan.nextLine();
                        getFilePerms(fName);
                        //showPerms(perms);
                        break;
                        
                    case 3:
                        
                        showFiles();
                        break;
                        
                    case 4:
                        new Thread(new MasterNode_reallocate()).start();
                        break;
                        
                    default:
                        System.out.println("Incorrect choice entered");
                        break;
                }
        }
    }
    
}
