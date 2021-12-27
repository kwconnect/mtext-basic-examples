package mtext.examples;

import java.util.Iterator;
import java.util.Map;
import de.kwsoft.mtext.api.*;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;
import mtext.examples.util.MUtil;

/**
 * M/Text client API example: Get all document information of a
 * specified folder
 * @author Timo Dreier
 **/
public class GetDocumentInformation {
    /**
     * Get information of all documents, which belongs to a specified folder
     * @param args Command line arguments<br>
     * args[0] = username<br>
     * args[1] = password<br>
     * arsg[2] = folder path
     **/
    public static void main(String[] args) {
        // initializations
        MTextClient client = null;
        ReadOnlyListIterator listIter = null;
        AbstractFolder folder = null;
        // Check whether we have correct number of arguments
        if (MUtil.checkArguments(args, 3)) {
            final String name = args[0];
            final String pwd = args[1];
            final String folderPath = args[2];
            try {
                // connect
                client = MTextFactory.connect(name, pwd, null);
                // get the folder
                folder = client.getRootFolder().getFolderByName(folderPath);
                if (folder instanceof Folder) {
                    // show text document information
                    System.out.println("Text document information");
                    // get the iterator with the text document informations
                    listIter = ((Folder)folder).getTextDocumentInformation();
                    // show the information
                    showDocumentInformation(listIter);
                    // show model document information
                    System.out.println("Model document information");
                    // get the iterator with the model document informations
                    listIter = ((Folder)folder).getModelDocumentInformation();
                    // show the information
                    showDocumentInformation(listIter);
                }
                else {
                    System.out.println("Root folder does not contain document" +
                        "information !");
                }
            }
            // M/Text exception occured
            catch (MTextException me) {
                System.out.println(
                    "Error during showing document information !");
                me.printStackTrace();
            }
            // general exception  e.g. path of folder is wrong -> folder =  null
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println(
                "M/Text client api example: GetDocumentInformation");
            System.out.println();
            System.out.println(
                "Usage: java mtext.examples.GetDocumentInformation " +
                "<name> <password> <folder path>");
        }
    }

    /**
     * Iterate of the specified iterator and show all DocumentInformation
     * @param iterator Contains DocumentInformation objects
     **/
    private static void showDocumentInformation(ReadOnlyListIterator
        iterator) {
            final String indent = "         ";
            final String propIndent = indent + indent;
            Iterator propertyIterator = null;
            DocumentInformation docInf = null;
            Map.Entry property = null;
            while (iterator.hasNext()) {
                docInf = (DocumentInformation)iterator.next();
                if (docInf instanceof TextDocumentInformation) {
                    System.out.print("Text  : ");
                }
                else {
                    System.out.print("Model : ");
                }
                System.out.println(docInf.getDocumentName());
                System.out.println(indent + "Qualified Name : " +
                    docInf.getQualifiedDocumentName());
                System.out.println(indent + "Description    : " +
                    docInf.getDocumentDescription());
                System.out.println(indent + "Created        :" +
                    docInf.getCreationTimestamp());
                System.out.println(indent + "Created by     :" +
                    docInf.getCreationPrincipal().getName());
                System.out.println(indent + "Modified       :" +
                    docInf.getLastModifiedTimestamp());
                System.out.println(indent + "Modified by    :" +
                    docInf.getLastModifiedPrincipal().getName());
                System.out.println(indent + "Printed        :" +
                    docInf.getLastPrintedTimestamp());
                System.out.println(indent + "Printed by     :" +
                    docInf.getLastPrintedPrincipal().getName());
                System.out.println(indent + "User def. properties  :");
                propertyIterator =
                    docInf.getUserDefinedProperties().entrySet().iterator();
                while (propertyIterator.hasNext()) {
                    property = (Map.Entry) propertyIterator.next();
                    System.out.println(propIndent + property.getKey() + " = " +
                        property.getValue());
                }
            }
    }
}
