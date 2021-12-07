package mtext.examples;

import de.kwsoft.mtext.api.*;

import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;

import mtext.examples.util.MUtil;


/**
 * M/Text client API example: Create a new document in a folder
 * @author Timo Dreier
 **/
public class CreateDocument {
    /**
     * Creates a new document in a specified folder
     * @param args Command line arguments:<br>
     * args[0] = user name<br>
     * args[1] = password<br>
     * args[2] = folder path<br>
     * args[3] = document name
     **/
    public static void main(String[] args) {
        // initializations
        MTextClient client = null;
        Job job = null;
        PersistentTextDocument textDoc = null;
        ChangeableDocumentContent docContent = null;
        final Object[] modelArguments = {"test1", "test2", "test3"};
        // check whether we have correct number of arguments
        if (MUtil.checkArguments(args, 4)) {
            final String name = args[0];
            final String pwd = args[1];
            final String folderPath = args[2];
            final String documentName = args[3];
            final String fullQualifiedDocumentName =
                folderPath + "\\" + documentName;
            try {
                // connect
                client = MTextFactory.connect(name, pwd, null);
                // create a job
                job = client.createJob();
                // begin the job
                job.begin();
                // create text document
                textDoc = job.createPersistentTextDocument(
                    fullQualifiedDocumentName, null);
                // get the changeable document content
                docContent =
                    (ChangeableDocumentContent)textDoc.getDocumentContent();
                // add a text line to the content
                docContent.addTextLine("This is the new document with the name "
                    + documentName);
                // add a model line to the content
                docContent.addModelLine("ADR", modelArguments);
                // save the document
                textDoc.save();
                // close the document
                textDoc.close();
                // execute the job
                job.execute();
            }
            // M/Text exception
            catch (MTextException me) {
                System.out.println("Can't create the document '" +
                    documentName + "' !");
                me.printStackTrace();
            }
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
            System.out.println("M/Text client api example: CreateDocument");
            System.out.println();
            System.out.println("Usage: java mtext.examples.CreateDocument " +
                "<name> <password> <folder path> <document name>");
        }
    }
}
