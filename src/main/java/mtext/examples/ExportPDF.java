package mtext.examples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import de.kwsoft.mtext.api.Configuration;
import de.kwsoft.mtext.api.TemporaryTextDocument;
import de.kwsoft.mtext.api.client.ClientJob;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;
import de.kwsoft.mtext.api.databinding.DataSource;
import de.kwsoft.mtext.api.databinding.DocumentDataBinding;
import de.kwsoft.mtext.api.databinding.JSONDataSource;
import de.kwsoft.mtext.api.databinding.XMLDataSource;
import mtext.examples.util.MUtil;

/**
 * M/Text client API example: Creates a temporary document from data binding and
 * exports a PDF file.
 **/
public class ExportPDF {

	/**
	 * Creates a temporary document from data binding and exports a PDF file.
	 * 
	 * @param args Command line arguments<br>
	 *             args[0] = username<br>
	 *             args[1] = password<br>
	 *             args[2] = project name<br>
	 *             args[3] = data binding name<br>
	 *             args[4] = data source name<br>
	 *             args[5] = data source file<br>
	 **/
	public static void main(String[] args) {

		MUtil.checkArguments(args, 6, ExportPDF.class, "<username> <password> <projectName> <dataBindingName> <dataSourceName> <dataSourceFile>");

		final String username = args[0];
		final String password = args[1];
		final String projectName = args[2];
		final String dataBindingName = args[3];
		final String dataSourceName = args[4];
		final String dataSourceFile = args[5];

		// initializations
		MTextClient client = null;
		ClientJob job = null;
		TemporaryTextDocument textDocument = null;
		FileOutputStream fileOutputStream = null;

		try {
			// connect to client
			client = MTextFactory.connect(username, password, null);

			// create job
			job = client.createJob();

			// begin job
			job.begin();
			// open text document
			Configuration createDocumentConfiguration = client.getConfigurationFactory().newCreateTextDocumentConfiguration();
			textDocument = job.createTemporaryTextDocument("temporary_document_*", projectName, createDocumentConfiguration);

			// create Data Binding
			final DocumentDataBinding binding = client.createDocumentDataBinding(projectName, dataBindingName);

			// Set the Data Source
			binding.setDataSource(dataSourceName, getDataSource(dataSourceFile));

			// execute the data binding to fill the document instance
			Configuration executeDataBindingConfiguration = client.getConfigurationFactory().newExecuteDocumentDataBindingConfiguration();
			textDocument.executeDocumentDataBinding(binding, executeDataBindingConfiguration);

			// export to pdf
			File pdfFile = MUtil.getFileInTempDirectory("Api_Export_Example.pdf");
			fileOutputStream = new FileOutputStream(pdfFile);
			job.exportDocument(textDocument, "application/pdf", fileOutputStream, client.getConfigurationFactory().newExportDocumentConfiguration(), null);
			
			// close the document
			textDocument.close();
			
			// execute job
			job.execute();

			System.out.println("Successfully exported document to PDF file: " + pdfFile.getAbsolutePath());

		}
		catch (Exception mte) {
			// M/Text exception occurred
			mte.printStackTrace();
		}
		// close the client
		finally {

			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (client != null) {
				client.close();
			}
		}
	}

	private static DataSource getDataSource(String fileName) throws IOException {

		if (fileName.toLowerCase().endsWith(".xml")) {
			return new XMLDataSource(new File(fileName));
		}
		else if (fileName.toLowerCase().endsWith(".json")) {
			return new JSONDataSource(FileUtils.readFileToString(new File(fileName)));
		}
		else {
			throw new IllegalArgumentException("The type of file is not supported as a data source: " + fileName);
		}
	}

}