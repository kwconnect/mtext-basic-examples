package mtext.examples;

import de.kwsoft.mtext.api.DocumentAccessMode;
import de.kwsoft.mtext.api.Job;
import de.kwsoft.mtext.api.MTextException;
import de.kwsoft.mtext.api.PersistentTextDocument;
import de.kwsoft.mtext.api.ResourceMetadata;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;
import mtext.examples.util.MUtil;

/**
 * M/Text client API example: Get the value of a specific (searchable and not searchable) Metadata of a
 * document.
 **/
public class GetMetadataNotSearchable {
	/**
	 * Get the value of a specific (searchable) Metadata of a document.
	 * 
	 * @param args Command line arguments<br>
	 *             args[0] = username<br>
	 *             args[1] = password<br>
	 *             args[2] = folderPath<br>
	 *             args[3] = document name<br>
	 *             args[4] = Metadata name
	 **/
	public static void main(String[] args) {

		MUtil.checkArguments(args, 4, GetMetadataNotSearchable.class, "<username> <password> <docuentName> <metadataKey>");
		final String username = args[0];
		final String password = args[1];
		final String fullQualifiedDocumentName = args[2];
		final String metadataName = args[3];
		
		// initializations
		MTextClient client = null;
		ResourceMetadata metadata = null;

		try {
			// connect
			client = MTextFactory.connect(username, password, null);
			
			// create first job to open the textDocument
			Job job = client.createJob();
			
			// begin this job
			job.begin();
			
			// open text document
			PersistentTextDocument textDocument = job.openTextDocument(fullQualifiedDocumentName, DocumentAccessMode.SHARED_READ_ONLY, null);
			
			// execute first job
			job.execute();
			
			// create second job to get the document information and set the metadata
			job = client.createJob();
			
			// begin this job
			job.begin();
			
			// assign document to this job
			job.assignDocument(textDocument);
			
			// get Metadata (incl. not searchable Metadata)
			metadata = textDocument.getMetadata();
			String metadataValue = metadata.getSingleValue(metadataName);
			System.out.println(metadataName + " = " + metadataValue);

			textDocument.close();
			job.execute();
			
		}
		catch (MTextException me) {
			// M/Text exception occured
			System.out.println("Can't get the property '" + metadataName + "' from document '" + fullQualifiedDocumentName + "'!");
			me.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if (client != null) {
				client.close();
			}
		}

	}
}
