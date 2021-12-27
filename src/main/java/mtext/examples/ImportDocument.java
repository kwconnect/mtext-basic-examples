package mtext.examples;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.kwsoft.mtext.api.Configuration;
import de.kwsoft.mtext.api.ConfigurationFactory;
import de.kwsoft.mtext.api.Job;
import de.kwsoft.mtext.api.MTextException;
import de.kwsoft.mtext.api.PersistentTextDocument;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;
import mtext.examples.util.MUtil;

/**
 * M/Text client API example: Import of a mtxz document.
 **/
public class ImportDocument {

	/**
	 * Set the value of a specific (searchable) Metadata of a document.
	 * 
	 * @param args Command line arguments<br>
	 *             args[0] = username<br>
	 *             args[1] = password<br>
	 *             args[2] = mtxz document<br>
	 **/
	public static void main(String[] args) throws IOException {

		MUtil.checkArguments(args, 4, ImportDocument.class, "<username> <password> <mtxzDocument> <targetDocumentName>");
		final String username = args[0];
		final String password = args[1];
		final String mtxzDocument = args[2];
		final String targetDocumentName = args[3];

		MTextClient client = null;
		InputStream stream = null;

		try {
			// connect
			client = MTextFactory.connect(username, password, null);

			// create the job
			Job job = client.createJob();

			// begin the job
			job.begin();

			// configure the job
			Configuration config = client.getConfigurationFactory().newImportDocumentConfiguration();
			config.put(ConfigurationFactory.IMPORT_DOCUMENT_NAME, targetDocumentName);

			// import the document
			stream = new FileInputStream(mtxzDocument);
			PersistentTextDocument textDocument = job.importDocument(stream, ConfigurationFactory.IMPORT_MIME_MTEXT_MTXZ, config);

			// finish
			textDocument.save();
			job.execute();

			// create a new job
			job = client.createJob();
			job.begin();
			
			// assign the document to the new job
			job.assignDocument(textDocument);
			
			System.out.println("Successfully imported document " + textDocument.getDocumentInformation().getQualifiedDocumentName());
			
			textDocument.close();
			job.execute();
		}
		catch (MTextException ex) {
			ex.printStackTrace();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			if (client != null && !client.isClosed()) {
				client.close();
			}
			if (stream != null) {
				stream.close();
			}
		}
	}
}