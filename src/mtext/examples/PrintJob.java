package mtext.examples;

import mtext.examples.util.MUtil;
import de.kwsoft.mtext.api.Configuration;
import de.kwsoft.mtext.api.ConfigurationFactory;
import de.kwsoft.mtext.api.DocumentAccessMode;
import de.kwsoft.mtext.api.MTextException;
import de.kwsoft.mtext.api.PersistentTextDocument;
import de.kwsoft.mtext.api.client.ClientJob;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;

/**
 * Print the document, which is specified by the folder path,
 * several times using a print job.
 * 
 * @author David Churavy
 **/
public class PrintJob
{
  /**
   * print a document on the standard mtext printer
   * @param args Command line arguments<br>
   * args[0]  = name<br>
   * args[1]  = password<br>
   * args[2]  = folder path<br>
   * args[3]  = document name<br>
   * args[4]  = printer name<br>
   **/
  public static void main(String[] args)
  {
    //initializations
    MTextClient client = null;
    ClientJob job = null;
    PersistentTextDocument textDocument = null;

    // Check whether we have correct number of arguments
    if(MUtil.checkArguments(args, 5))
    {
      final String name = args[0];
      final String pwd = args[1];
      final String folderPath = args[2];
      final String documentName = args[3];
      final String printerName = args[4];
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

      try
      {
        //connect to server
        client = MTextFactory.connect(name, pwd, null);

        //create job
        job = client.createJob();
        //begin job
        job.begin();
        //open text document
        textDocument = job.openTextDocument(
                       fullQualifiedDocumentName,
                       DocumentAccessMode.SHARED_READ_ONLY, null);
        //print document 5 times in a print job
        Configuration config = client.getConfigurationFactory().newPrintConfiguration();
        config.put(ConfigurationFactory.PRINT_JOB_PART, Boolean.valueOf(true));
        for (int i = 0; i < 5; i++) {            
            job.printDocument(textDocument, printerName, config);
        }
        //close the document
        textDocument.close();
        //execute job
        job.execute();

      //M/Text exception occured
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
    else
    {
      System.out.println("M/Text client api example: PrintJob");
            System.out.println();
            System.out.println("Usage: java mtext.examples.PrintJob " +
                "<name> <password> <folder path> <document name>" +
                "<destination name>"
                );
    }
  }
}