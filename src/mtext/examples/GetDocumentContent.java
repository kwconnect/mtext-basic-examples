package mtext.examples;

import java.util.Iterator;

import mtext.examples.util.MUtil;
import de.kwsoft.mtext.api.DocumentAccessMode;
import de.kwsoft.mtext.api.DocumentContent;
import de.kwsoft.mtext.api.DocumentLine;
import de.kwsoft.mtext.api.DocumentLineIterator;
import de.kwsoft.mtext.api.DocumentOpenException;
import de.kwsoft.mtext.api.Job;
import de.kwsoft.mtext.api.JobException;
import de.kwsoft.mtext.api.JobExecutionException;
import de.kwsoft.mtext.api.MTextException;
import de.kwsoft.mtext.api.ModelLine;
import de.kwsoft.mtext.api.PersistentTextDocument;
import de.kwsoft.mtext.api.TextLine;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;

/**
 * M/Text client API example: Get the content of a specified document and
 * show its content.
 * @author Timo Dreier
 **/
public class GetDocumentContent {
    /**
     * If the specified document is available -> get the document content
     * and show it
     * @param args Command line arguments<br>
     * args[0] = user name<br>
     * args[1] = password<br>
     * args[2] = folder path<br>
     * args[3] = document name
     **/
    public static void main(String[] args) {
        // initializations
        MTextClient client = null;
        Job job = null;
        DocumentLineIterator lineIter = null;
        PersistentTextDocument persTextDoc = null;
        DocumentContent docContent = null;
        ModelLine modelLine = null;
        DocumentLine docLine = null;
        Object[] modelParams = null;
        // Check whether we have correct number of arguments
        if (MUtil.checkArguments(args, 4)) {
            final String name = args[0];
            final String pwd = args[1];
            final String folderPath = args[2];
            final String documentName = args[3];
            final String fullQualifiedDocumentName;

            if (".".equals(folderPath)) {
                // Retreive document from default folder of the user if no folder name is given
                fullQualifiedDocumentName = documentName;
            }
            else {
                fullQualifiedDocumentName = folderPath + "\\" + documentName;
            }
            // connect
            try {
                client = MTextFactory.connect(name, pwd, null);

                // create first job to open and retrieve the document
                // content
                job = client.createJob();
                // begin the job
                try {
                    job.begin();
                    // open text document
                    persTextDoc = job.openTextDocument(
                        fullQualifiedDocumentName,
                        DocumentAccessMode.SHARED_READ_ONLY, null);
                    // get the content of this text document
                    docContent = persTextDoc.getDocumentContent();
                    // execute the first job to transfer content from server to the
                    // client
                    job.execute();
                }
                catch (JobExecutionException e) {
                    Iterator it = e.getReasons();
                    // Analyse the reasons of the exception
                    while (it.hasNext()) {
                        Exception reason = (Exception) it.next();
                        if (reason instanceof DocumentOpenException) {
                            System.out.println("Error opening document " +
                                               documentName);
                            System.out.println(reason.toString());
                            reason.printStackTrace();
                        }
                        else {
                            reason.printStackTrace();
                        }
                    }
                    e.printStackTrace();
                    return;
                }
                catch (JobException e) {
                    // We do not really expect this to happen - in most cases
                    // it just means we have an error in our code (like calling
                    // job.openTextDocument without previously calling job.begin
                    System.out.println("Unexpected exception");
                    e.printStackTrace();
                    return;
                }

                try {
                    // create second job to iterate over document content
                    job = client.createJob();
                    // begin the job
                    job.begin();
                    // assign document to this job
                    job.assignDocument(persTextDoc);
                    // get the iterator of the content
                    lineIter = docContent.getDocumentLineIterator();
                    // show the document content
                    while (lineIter.hasNext()) {
                        // get next document line
                        docLine = (DocumentLine) lineIter.next();
                        // if next line a text line
                        if (docLine instanceof TextLine) {
                            // cast to TextLine and show the line
                            System.out.println("TextLine: " +
                                               ( (TextLine) docLine).getText());
                        }
                        else {
                            modelLine = (ModelLine) docLine;
                            // get all arguments from the ModelLine
                            modelParams = modelLine.getArguments();
                            // get the model name
                            System.out.println("ModelLine: " +
                                               modelLine.getModelName());
                            // apppend the arguments to the string remindModelLine
                            for (int i = 0; i < modelParams.length; i++) {
                                System.out.println("        Parameter" + (i + 1) +
                                    ": " + modelParams[i]);
                            }
                        }
                    }
                    // close the document
                    persTextDoc.close();
                    // execute the job
                    job.execute();
                }
                // M/Text exception occured
                catch (MTextException me) {
                    System.out.println(
                        "Can't read the document content from document '" +
                        documentName + "'!");
                    me.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            catch (MTextException e) {
                System.out.println(
                    "Cannot connect to m/text server '");
                e.printStackTrace();
                return;
            }
            finally {
                if (client != null) {
                    // close client
                    client.close();
                }
            }
        }
        else {
            System.out.println("M/Text client api example: GetDocumentContent");
            System.out.println();
            System.out.println("Usage: java mtext.examples.GetDocumentContent "
                               +
                "<name> <password> <folder path> <document name>");
        }
    }
}