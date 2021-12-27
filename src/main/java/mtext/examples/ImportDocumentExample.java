package mtext.examples;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.kwsoft.mtext.api.Configuration;
import de.kwsoft.mtext.api.ConfigurationFactory;
import de.kwsoft.mtext.api.MTextException;
import de.kwsoft.mtext.api.PersistentTextDocument;
import de.kwsoft.mtext.api.server.MTextFactory;
import de.kwsoft.mtext.api.server.MTextServer;
import de.kwsoft.mtext.api.server.ServerJob;

public class ImportDocumentExample {

	public static final String MTEXT_USER = "churavy";
	public static final String MTEXT_PWD = "churavy";

	public static final String SOURCE_DOCUMENT = "d:\\Work\\Kw\\Issues\\Deserialization failed Error - Parsing error 102\\811300026444.sendToOMS.mtxz";
	public static final String DOCUMENT_NAME = "ImportDocumentName";

	public static void main(String[] args) throws IOException {
		MTextServer server = null;
		InputStream stream = null;

		try {
			server = MTextFactory.connect(MTEXT_USER, MTEXT_PWD, null);
			ServerJob job = server.createJob();
			job.begin();
			stream = new FileInputStream(SOURCE_DOCUMENT);

			ConfigurationFactory configFactory = server.getConfigurationFactory();
			Configuration config = configFactory.newImportDocumentConfiguration();
			config.put(ConfigurationFactory.IMPORT_DOCUMENT_NAME, DOCUMENT_NAME);

			PersistentTextDocument textDocument = job.importDocument(stream, ConfigurationFactory.IMPORT_MIME_MTEXT_MTXZ, config);

			textDocument.save();
			textDocument.close();

			job.execute();
		} catch (MTextException ex) {
			ex.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(server != null && !server.isClosed()) {
				server.close();
			}
			if(stream != null) {
				stream.close();
			}
		}
	}
}