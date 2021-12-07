package mtext.examples;

import de.kwsoft.mtext.api.*;

import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;

import mtext.examples.util.MUtil;

/**
 * M/Text client API example: Create a new folder in a specified parent folder
 * @author Timo Dreier
 **/
public class CreateFolder {
    /**
     * Creates a new folder in the specified parent folder
     * @param args Command line arguments<br>
     * args[0] = username<br>
     * args[1] = password<br>
     * args[2] = folder path<br>
     * args[3] = name of the new folder
     **/
    public static void main(String[] args) {
        // initializations
        MTextClient client = null;
        AbstractFolder folder = null;
        // if there are two less or to much arguments
        if (MUtil.checkArguments(args, 4)) {
            final String name = args[0];
            final String pwd = args[1];
            final String folderPath = args[2];
            final String folderName = args[3];
            try {
                // connect
                client = MTextFactory.connect(name, pwd, null);

                // get the specified folder by the client and the path
                if(folderPath.equals("\\")) {
                  folder = client.getRootFolder();
                }
                else {
                  folder = client.getRootFolder().getFolderByName(folderPath);
                }
                // create a new folder in the specified parent folder
                folder.createSubFolder(folderName);
            }
            // M/Text exception
            catch (MTextException me) {
                System.out.println("Can't create the folder '" +
                    folderName + "'!");
                me.printStackTrace();
            }
            // e.g. the path is wrong -> the folder is null -> Exception
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (client != null) {
                    // close client
                    client.close();
                }
            }
        }
        else {
            System.out.println("M/Text client api example: CreateFolder");
            System.out.println();
            System.out.println("Usage: java mtext.examples.CreateFolder " +
                "<name> <password> <folder path> <folder name>");
        }
    }
}
