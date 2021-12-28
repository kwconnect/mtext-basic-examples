package mtext.examples;

import mtext.examples.util.MUtil;
import de.kwsoft.mtext.api.Configuration;
import de.kwsoft.mtext.api.ConfigurationFactory;
import de.kwsoft.mtext.api.DocumentAccessMode;
import de.kwsoft.mtext.api.MTextException;
import de.kwsoft.mtext.api.PersistentTextDocument;
import de.kwsoft.mtext.api.PrintResult;
import de.kwsoft.mtext.api.client.ClientJob;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;

/**
 * M/Text client API example: Prints a document several times using one print
 * job.
 * 
 **/
public class PrintDocumentJobPart {

	/**
	 * Prints a document several times using one print job.
	 * 
	 * @param args Command line arguments<br>
	 *             args[0] = username<br>
	 *             args[1] = password<br>
	 *             args[3] = document name<br>
	 *             args[4] = printer name<br>
	 **/
	public static void main(String[] args) {

		MUtil.checkArguments(args, 4, PrintDocumentJobPart.class, "<username> <password> <document name> <destination name>");
		final String username = args[0];
		final String password = args[1];
		final String fullQualifiedDocumentName = args[2];
		final String printerName = args[3];

		// initializations
		MTextClient client = null;
		ClientJob job = null;
		PersistentTextDocument textDocument = null;

		try {
			// connect to server
			client = MTextFactory.connect(username, password, null);

			// create job
			job = client.createJob();
			
			// begin job
			job.begin();
			
			// open text document
			textDocument = job.openTextDocument(fullQualifiedDocumentName, DocumentAccessMode.SHARED_READ_ONLY, null);
			
			// print document 5 times in a print job
			Configuration config = client.getConfigurationFactory().newPrintConfiguration();
			config.put(ConfigurationFactory.PRINT_JOB_PART, Boolean.valueOf(true));
			for (int i = 0; i < 5; i++) {
				PrintResult printResult = client.getConfigurationFactory().newPrintResult();
				job.printDocument(textDocument, printerName, config, printResult);
				System.out.println("Added document to job " + fullQualifiedDocumentName);
			}
			
			// close the document
			textDocument.close();
			
			// execute job
			job.execute();
			
			System.out.println("Successfully executed print job.");

		}
		catch (MTextException mte) {
			System.out.println("Cannot print the document!");
			mte.printStackTrace();
		}
		finally {
			// close the client
			if (client != null && !client.isClosed()) {
				client.close();
			}
		}
	}
}