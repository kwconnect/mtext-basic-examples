package mtext.examples;

import mtext.examples.util.MUtil;
import de.kwsoft.mtext.api.Configuration;
import de.kwsoft.mtext.api.DocumentAccessMode;
import de.kwsoft.mtext.api.MTextException;
import de.kwsoft.mtext.api.PersistentTextDocument;
import de.kwsoft.mtext.api.PrintResult;
import de.kwsoft.mtext.api.client.ClientJob;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;

/**
 * M/Text client API example: Prints an existing M/TEXT document to M/OMS.
 **/
public class PrintDocument {
	/**
	 * Prints an existing M/TEXT document to M/OMS.
	 * 
	 * @param args Command line arguments<br>
	 *             args[0] = username<br>
	 *             args[1] = password<br>
	 *             args[2] = fully qualified document name<br>
	 **/
	public static void main(String[] args) {

		MUtil.checkArguments(args, 3, PrintDocument.class, "<username> <password> <documentName>");
		
		final String username = args[0];
		final String password = args[1];
		final String documentName = args[2];
		final String destination = "OMS";

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
			textDocument = job.openTextDocument(documentName, DocumentAccessMode.SHARED_READ_ONLY, null);
			
			// print document
			Configuration printConfiguration = client.getConfigurationFactory().newPrintConfiguration();
			PrintResult printResult = client.getConfigurationFactory().newPrintResult();
			job.printDocument(textDocument, destination, printConfiguration, printResult);
			
			// close the document
			textDocument.close();
			
			// execute job
			job.execute();

			for (String printResultKey: printResult.getKeys()) {
				Object entry = printResult.get(printResultKey);
				System.out.println(printResultKey + ": " + entry);
			}
			
		}
		catch (MTextException mte) {
			System.out.println("Failed to print the document!");
			mte.printStackTrace();
		}
		finally {

			// close the client
			if (client != null) {
				client.close();
			}
		}

	}
}