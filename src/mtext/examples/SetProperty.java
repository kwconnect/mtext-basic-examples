package mtext.examples;

import java.util.Properties;

import de.kwsoft.mtext.api.*;

import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;

import mtext.examples.util.MUtil;

/**
 * M/Text client API example: Set a user defined property in a document
 * @author Timo Dreier
 **/
public class SetProperty {
    /**
     * Sets a user defined property in a document
     * @param args Command line arguments<br>
     * args[0] = user name<br>
     * args[1] = password<br>
     * args[2] = folder  path<br>
     * args[3] = document name<br>
     * args[4] = properties key<br>
     * args[5] = properties value
     **/
    public static void main(String[] args) {
        // initializations
        MTextClient client = null;
        DocumentInformation docInf = null;
        PersistentTextDocument textDocument = null;
        Job job = null;
        Properties prop = null;
        // Check whether we have correct number of arguments
        if (MUtil.checkArguments(args, 6)) {
            final String name = args[0];
            final String pwd = args[1];
            final String folderPath = args[2];
            final String documentName = args[3];
            final String propertyName = args[4];
            final String propertyValue = args[5];
            final String fullQualifiedDocumentName =
                folderPath + "\\" + documentName;
            try {
                // connect
                client = MTextFactory.connect(name, pwd, null);
                // create first job to open the textDocument
                job = client.createJob();
                // begin this job
                job.begin();
                // open text document
                textDocument = job.openTextDocument(fullQualifiedDocumentName,
                    DocumentAccessMode.READ_WRITE, null);
                // execute first job
                job.execute();
                // create second job to get the document information and set
                // the property
                job = client.createJob();
                // begin this job
                job.begin();
                // assign document to this job
                job.assignDocument(textDocument);
                // get document information
                docInf = textDocument.getDocumentInformation();
                // get the user defined properties of the document
                prop = docInf.getUserDefinedProperties();
                //add the new property to the properties
                prop.store(System.out,"");
                prop.setProperty(propertyName, propertyValue);
                System.out.print("Trying  to set this: " + propertyName + "   " + propertyValue + "\n");
                prop.store(System.out,"");
                //set the new properties
                textDocument.setUserDefinedProperties(prop);
                //save the document
                textDocument.save();
                //close the document
                textDocument.close();
                //execute the job
                job.execute();
            }
            // M/Text exception occured
            catch (MTextException me) {
                System.out.println("Can't set the property '" + propertyName +
                    "' for document '" + documentName + "' !");
                me.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                // close client
                if (client != null) {
                    client.close();
                }
            }
        }
        else {
            System.out.println("M/Text client api example: SetProperty");
            System.out.println();
            System.out.println("Usage: java mtext.examples.SetProperty " +
                "<name> <password> <folder path> <document name> "+
                "<property name> <property value>");
        }
    }
}
