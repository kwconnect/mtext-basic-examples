package mtext.examples;

import java.util.Properties;

import de.kwsoft.mtext.api.*;

import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;

import mtext.examples.util.MUtil;

/**
 * M/Text client API example: Delete a property of a document by key
 * @author Timo Dreier
 **/
public class DeleteProperty {
    /**
     * Deletes a specified user defined property
     * @param args Command line arguments<br> 
     * args[0] = user name<br> 
     * args[1] = password<br> 
     * args[2] = full qulified folder path<br> 
     * args[3] = document name<br> 
     * args[4] = property name
     **/
    public static void main(String[] args) {
        // initializations
        MTextClient client = null;
        DocumentInformation docInf = null;
        Job job = null;
        PersistentTextDocument textDoc = null;
        Properties prop = null;
        if (MUtil.checkArguments(args, 5)) {
            final String name = args[0];
            final String pwd = args[1];
            final String documentName = args[2] + "\\" + args[3];
            final String propertyName = args[4];
            try {
                // connect
                client = MTextFactory.connect(name, pwd, null);
                // create first job to open the document
                job = client.createJob();
                // begin this job
                job.begin();
                // open the document
                textDoc = job.openTextDocument(documentName,
                    DocumentAccessMode.READ_WRITE, null);
                // execute the job
                job.execute();
                // create the second job, to get the document information and
                // delete the property
                job = client.createJob();
                //begin this job
                job.begin();
                //assign document to the job
                job.assignDocument(textDoc);
                //get the document information
                docInf = textDoc.getDocumentInformation();
                prop = docInf.getUserDefinedProperties();
                if (prop.remove(propertyName) == null) {
                    throw new Exception("Can't find a property with the name: "
                        + propertyName);
                }
                // set the new properties
                textDoc.setUserDefinedProperties(prop);
                // save the document
                textDoc.save();
                // close the document
                textDoc.close();
                // execute the job
                job.execute();
            }
            catch (MTextException me) {
                System.out.println("Can't delete property '" + propertyName +
                    "' from document '" + documentName + "' !");
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
            System.out.println("M/Text client api example: DeleteProperty");
            System.out.println();
            System.out.println("Usage: java mtext.examples.DeleteProperty " +
                "<name> <password> <folder path> <document name> "+
                "<property name>");
        }
    }
}
