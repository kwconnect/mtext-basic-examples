package mtext.examples;

import de.kwsoft.mtext.api.*;

import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;

import mtext.examples.util.MUtil;

/**
 * M/Text client API example: Get the whole folder structure
 * @author Timo Dreier
 **/
public class GetAllFolders {
    /**
     * Gets all folders of the system and show them
     * @param args Command line arguments<br>
     * args[0] = user name<br>
     * args[1] = password
     **/
    public static void main(String[] args) {
        MTextClient client = null;
        if (MUtil.checkArguments(args, 2)) {
            try {
                // connect
                client = MTextFactory.connect(args[0], args[1], null);
                // get all folders
                getAllFolders(client.getRootFolder(), "");
            }
            // M/Text exception during getting folders
            catch (MTextException me) {
                System.out.println("Error during getting all folders !");
                me.printStackTrace();
            }
            finally {
                if (client != null) {
                    // close client
                    client.close();
                }
            }
        }
        else {
            System.out.println("M/Text client api example: GetAllFolders");
            System.out.println();
            System.out.println("Usage: java mtext.examples.GetAllFolders " +
                "<name> <password>");
        }
    }

    /**
     * Get all subfolders of a specified folder
     * @param root The folder for which all sub folders should be retrieved
     * @param oldIndent The indent used before
     **/
    private static void getAllFolders(AbstractFolder root, String oldIndent) {
        // get the list Iterator with the sub folders
        final ReadOnlyListIterator listIter = root.getSubFolders();
        final String indent = oldIndent + "   ";
        Folder folder = null;
        // if there are more folders
        while (listIter.hasNext()) {
            // get the next folder
            folder = (Folder)listIter.next();
            // print the name of the folder
            System.out.println(indent + folder.getName());
            // get sub folders of this folder
            getAllFolders(folder, indent);
        }
    }
}
