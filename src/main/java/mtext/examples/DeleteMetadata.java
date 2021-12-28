package mtext.examples;

import de.kwsoft.mtext.api.DocumentAccessMode;
import de.kwsoft.mtext.api.DocumentInformation;
import de.kwsoft.mtext.api.Job;
import de.kwsoft.mtext.api.MTextException;
import de.kwsoft.mtext.api.PersistentTextDocument;
import de.kwsoft.mtext.api.ResourceMetadata;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;
import mtext.examples.util.MUtil;

/**
 * M/Text client API example: Deletes a specific Metadata from a document.
 **/
public class DeleteMetadata {
	/**
	 * Deletes a specific Metadata from a document.
	 * 
	 * @param args Command line arguments<br>
	 *             args[0] = username<br>
	 *             args[1] = password<br>
	 *             args[2] = folder path<br>
	 *             args[3] = document name<br>
	 *             args[4] = metadata key<br>
	 *             args[5] = metadata value
	 **/
	public static void main(String[] args) {

		MUtil.checkArguments(args, 5, DeleteMetadata.class, "<username> <password> <folderPath> <docuentName> <metadataKey>");
		final String username = args[0];
		final String password = args[1];
		final String folderPath = args[2];
		final String documentName = args[3];
		final String metadataKey = args[4];

		// initializations
		MTextClient client = null;
		DocumentInformation documentInformation = null;
		PersistentTextDocument textDocument = null;
		Job job = null;
		ResourceMetadata metadata = null;
		// Check whether we have correct number of arguments
		final String fullQualifiedDocumentName = folderPath + "\\" + documentName;
		
		try {
			// connect
			client = MTextFactory.connect(username, password, null);
			
			// create first job to open the textDocument
			job = client.createJob();
			
			// begin this job
			job.begin();
			
			// open text document
			textDocument = job.openTextDocument(fullQualifiedDocumentName, DocumentAccessMode.READ_WRITE, null);
			
			// execute first job
			job.execute();
			
			// create second job to get the document information and set the metadata
			job = client.createJob();
			
			// begin this job
			job.begin();
			
			// assign document to this job
			job.assignDocument(textDocument);
			
			// get document information
			documentInformation = textDocument.getDocumentInformation();
			
			// get the user defined properties of the document
			metadata = documentInformation.getMetadata();
			
			// add the new property to the properties
			metadata.remove(metadataKey);
			
			// set the new properties
			textDocument.setMetadata(metadata);
			// save the document
			textDocument.save();
			// close the document
			textDocument.close();
			// execute the job
			job.execute();
			
			System.out.println("Successfully removed Metadata " + metadataKey);
		}
		// M/Text exception occured
		catch (MTextException me) {
			System.out.println("Can't set the property '" + metadataKey + "' for document '" + documentName + "' !");
			me.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			// close the client
			if (client != null && !client.isClosed()) {
				client.close();
			}
		}

	}
}
