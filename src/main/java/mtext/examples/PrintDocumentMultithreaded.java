package mtext.examples;

import mtext.examples.util.MUtil;
import de.kwsoft.mtext.api.DocumentAccessMode;
import de.kwsoft.mtext.api.JobExecutionException;
import de.kwsoft.mtext.api.JobInProgressException;
import de.kwsoft.mtext.api.MTextException;
import de.kwsoft.mtext.api.PersistentTextDocument;
import de.kwsoft.mtext.api.client.ClientJob;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;

/**
 * Print a document, which is specified by the folder path, multiple times in a multiple threads
 * @author Timo Dreier
 **/
public class PrintDocumentMultithreaded
{
  /**
   * print a document on the standard mtext printer
   * @param args Command line arguments<br>
   * args[0]  = name<br>
   * args[1]  = password<br>
   * args[2]  = folder path<br>
   * args[3]  = document name<br>
   * args[4]  = destination name<br>
   * args[5]  = jobs per thread count<br>
   * args[6]  = thread count
   **/
  public static void main(String[] args)
  {

    // Check whether we have correct number of arguments
    if(MUtil.checkArguments(args, 7))
    {
      final String name = args[0];
      final String pwd = args[1];
      final String folderPath = args[2];
      final String documentName = args[3];
      final String destinationName = args[4];
      final int jobsPerThreadCount = Integer.valueOf(args[5]);
      final int threadCount = Integer.valueOf(args[6]);
      final String fullQualifiedDocumentName;

      if (".".equals(folderPath))
      {
        // Retreive document from default folder of the user if no folder name is given
        fullQualifiedDocumentName = documentName;
      }
      else
      {
        fullQualifiedDocumentName = folderPath + "\\" + documentName;
      }

      for (int threadIndex = 0; threadIndex < threadCount; threadIndex++) {
          new Thread(new Runnable() {
            public void run() {
                MTextClient client = null;
                try
                {
                    //connect to server
                    client = MTextFactory.connect(name, pwd, null);
                    
                    for (int jobIndex = 0; jobIndex < jobsPerThreadCount; jobIndex++) {  
                        System.out.println("Thread: " + Thread.currentThread().getId() + ", job: " + jobIndex);
                        runJob(client);
                    }
                    
                }catch(MTextException mte)
                {
                    System.out.println("Cannot print the document!");
                    mte.printStackTrace();
                }
                //close the client
                finally
                {
                    if(client != null)
                        client.close();
                }
            }

            private void runJob(MTextClient client)
                    throws JobInProgressException, JobExecutionException {
                try
                {
                    //create job
                    ClientJob job = client.createJob();
                    //begin job
                    job.begin();
                    //open text document
                    PersistentTextDocument textDocument = job.openTextDocument(
                            fullQualifiedDocumentName,
                            DocumentAccessMode.SHARED_READ_ONLY, null);
                    //print document
                    job.printDocument(textDocument, destinationName, null);
                    //close the document
                    textDocument.close();
                    //execute job
                    job.execute();
                }catch(MTextException mte)
                {
                    System.out.println("Cannot print the document!");
                    mte.printStackTrace();
                }
            }
              
          }).start();
      }
    }
    else
    {
      System.out.println("M/TEXT client api example: PrintDocumentMultithreaded");
            System.out.println();
            System.out.println("Usage: java mtext.examples.PrintDocumentMultithreaded " +
                "<name> <password> <folder path> <document name>" +
                "<destination name> <jobs per thread count> <thread count> "
                );
    }
  }
}