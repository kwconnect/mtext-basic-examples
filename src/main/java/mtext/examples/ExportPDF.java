package mtext.examples;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Iterator;

import de.kwsoft.mtext.api.DataModelNodeValidationFailedException;
import de.kwsoft.mtext.api.MTextException;
import de.kwsoft.mtext.api.TemporaryTextDocument;
import de.kwsoft.mtext.api.client.ClientJob;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;
import de.kwsoft.mtext.api.databinding.DataModelNode;
import de.kwsoft.mtext.api.databinding.DataModelNodeValidation.ValidationValue;
import de.kwsoft.mtext.api.databinding.DocumentDataBinding;
import de.kwsoft.mtext.api.databinding.XMLDataSource;
import mtext.examples.util.MUtil;

/**
 * Creates a document from data binding and shows it in preview.
 **/
public class CreateDocumentFromXML {

	/**
	 * Shows a print preview of a document created using specified data binding,
	 * getting the needed data sets from XML files.
	 * 
	 * @param args Command line arguments<br>
	 *             args[0] = username<br>
	 *             args[1] = password<br>
	 *             args[2] = project name<br>
	 *             args[3] = data binding name<br>
	 **/
	public static void main(String[] args) {
		
		MUtil.checkArguments(args, 4, CreateDocumentFromXML.class, "<username> <password> <projectName> <dataBindingName>");
		
		final String username = args[0];
		final String password = args[1];
		final String projectName = args[2];
		final String dataBindingName = args[3];
		
		// initializations
		MTextClient client = null;
		ClientJob job = null;
		TemporaryTextDocument textDocument = null;

		try {
			// connect to server
			client = MTextFactory.connect(username, password, null);

			// create job
			job = client.createJob();
			// begin job
			job.begin();
			// open text document
			textDocument = job.createTemporaryTextDocument("testDocument", projectName, null);
			final DocumentDataBinding binding = client.createDocumentDataBinding(projectName, dataBindingName);
			fillDataSources(binding);
			// execute the data binding to fill the document instance
			textDocument.executeDocumentDataBinding(binding);
			// export to pdf
			job.exportDocument(textDocument, "application/pdf", new ByteArrayOutputStream(), client.getConfigurationFactory().newExportDocumentConfiguration(), null);
			// close the document
			textDocument.close();
			// execute job
			job.execute();

		}
		catch (Exception mte) {
			// M/Text exception occurred
			if (mte instanceof MTextException) {
				printMTextException((MTextException) mte);
			}
			else {
				mte.printStackTrace();
			}
		}
		// close the client
		finally {
			if (client != null)
				client.close();
		}
	}

	private static void printMTextException(MTextException mte) {
		printError(mte);
		Iterator reasons = mte.getReasons();
		while (reasons.hasNext()) {
			Object reason = reasons.next();
			if (reason instanceof MTextException) {
				printMTextException((MTextException) reason);
			}
			else if (reason instanceof Throwable) {
				printError((Throwable) reason);
			}
			else {
				System.out.println(reason);
			}
		}
	}

	private static void printError(Throwable error) {
		String message = error.getLocalizedMessage();
		if (message == null) {
			message = error.getMessage();
		}
		System.out.println(error.getClass() + " - " + message);
		System.out.println("---");
		if (error instanceof DataModelNodeValidationFailedException) {
			DataModelNode node = ((DataModelNodeValidationFailedException) error).getNode();
			System.out.println(node.getName());
			System.out.println(node.getDataType());
			System.out.println(node.getDialogLabel());
			System.out.println(node.getValidation().getValidationType());
			System.out.println(node.getValidation().getValidationOperator());
			for (ValidationValue value : node.getValidation().getValdationValues()) {
				System.out.println(value.getValue());
				System.out.println(value.getDescription());
			}
			System.out.println("---");
			System.out.println(node.getResourceName());
			System.out.println(node.getReferenceResourceName());
			System.out.println(node.isFromReference());
			System.out.println(node.isLeaf());
		}
	}

	private static void fillDataSources(final DocumentDataBinding binding) throws Exception {
		for (String dataSourceName : new Iterable<String>() {
			public Iterator<String> iterator() {
				return binding.getDataSourceNames();
			}
		}) {
			try {
				binding.setDataSource(dataSourceName, new XMLDataSource(new InputStreamReader(new FileInputStream(dataSourceName + ".xml"), "utf-8")));
			}
			catch (FileNotFoundException e) {
				System.out.println("Warning: cannot find the data source file, leaving unset");
				e.printStackTrace(System.out);
			}
		}
	}

}