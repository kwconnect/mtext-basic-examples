package mtext.examples;

import mtext.examples.util.MUtil;
import de.kwsoft.mtext.api.DocumentAccessMode;
import de.kwsoft.mtext.api.MTextException;
import de.kwsoft.mtext.api.PersistentTextDocument;
import de.kwsoft.mtext.api.client.ClientJob;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;

/**
 * M/Text client API example: Opens the Classic Editor (Java Swing Client) with
 * an existing M/TEXT document.
 **/
public class OpenClassicEditor {
	/**
	 * Opens the Classic Editor (Java Swing Client) with an existing M/TEXT
	 * document.
	 * 
	 * @param args Command line arguments<br>
	 *             args[0] = username<br>
	 *             args[1] = password<br>
	 *             args[2] = fullQualifiedDocumentName<br>
	 **/
	public static void main(String[] args) {
		MUtil.checkArguments(args, 3, OpenClassicEditor.class, "<name> <password> <fullQualifiedDocumentName>");
		final String username = args[0];
		final String password = args[1];
		final String fullQualifiedDocumentName = args[2];

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
			textDocument = job.openTextDocument(fullQualifiedDocumentName, DocumentAccessMode.READ_WRITE, null);
			// call the editor
			job.editDocument(textDocument, null);
			// save the document
			textDocument.save();
			// close the document
			textDocument.close();
			// execute job
			job.execute();

		}
		catch (MTextException mte) {
			System.out.println("Cannot call the editor for this document!");
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