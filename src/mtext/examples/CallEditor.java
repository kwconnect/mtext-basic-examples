package mtext.examples;

import mtext.examples.util.MUtil;
import de.kwsoft.mtext.api.DocumentAccessMode;
import de.kwsoft.mtext.api.MTextException;
import de.kwsoft.mtext.api.PersistentTextDocument;
import de.kwsoft.mtext.api.client.ClientJob;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;

/**
 * open a document and call the editor
 * @author Timo Dreier
 **/
public class CallEditor
{
  /**
   * call the editor for a specified text document
   * @param args Command line arguments<br>
   * args[0]  = name<br>
   * args[1]  = password<br>
   * args[2]  = folder path<br>
   * args[3]  = document name<br>
   **/
  public static void main(String[] args)
  {
    //initializations
    MTextClient client = null;
    ClientJob job = null;
    PersistentTextDocument textDocument = null;

    // Check whether we have correct number of arguments
    if(MUtil.checkArguments(args, 4))
    {
      final String name = args[0];
      final String pwd = args[1];
      final String folderPath = args[2];
      final String documentName = args[3];
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
                       DocumentAccessMode.READ_WRITE, null);
        //call the editor
        job.editDocument(textDocument, null);
        //save the document
        textDocument.save();
        //close the document
        textDocument.close();
        //execute job
        job.execute();

      //M/Text exception occured
      }catch(MTextException mte)
      {
        System.out.println("Cannot call the editor for this document!");
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
      System.out.println("M/TEXT client api example: CallEditor");
            System.out.println();
            System.out.println("Usage: java mtext.examples.CallEditor " +
                "<name> <password> <folder path> <document name>"
                );
    }
  }
}