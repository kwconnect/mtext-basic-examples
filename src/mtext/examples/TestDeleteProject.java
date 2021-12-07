package mtext.examples;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Scanner;

import de.kwsoft.mtext.api.DataModelNodeValidationFailedException;
import de.kwsoft.mtext.api.DocumentNotFoundException;
import de.kwsoft.mtext.api.JobExecutionException;
import de.kwsoft.mtext.api.JobInProgressException;
import de.kwsoft.mtext.api.MTextException;
import de.kwsoft.mtext.api.TemporaryTextDocument;
import de.kwsoft.mtext.api.client.ClientJob;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.databinding.DataBindingNotFoundException;
import de.kwsoft.mtext.api.databinding.DataMappingParameterNotSetException;
import de.kwsoft.mtext.api.databinding.DataModelNode;
import de.kwsoft.mtext.api.databinding.DataModelNodeValidation.ValidationValue;
import de.kwsoft.mtext.api.databinding.DataSourceNotSetException;
import de.kwsoft.mtext.api.databinding.DocumentDataBinding;
import de.kwsoft.mtext.api.databinding.ScriptEvalException;
import de.kwsoft.mtext.api.databinding.XMLDataSource;
import de.kwsoft.mtext.api.server.MTextServer;
import de.kwsoft.mtext.api.server.ServerJob;

/**
 * Creates a document from data binding and shows it in preview.
 **/
public class TestDeleteProject {

    private static TemporaryTextDocument textDocumentServer;
	private static ServerJob jobServer;
	private static MTextServer server;

	/**
     * Shows a print preview of a document created using specified data binding,
     * getting the needed data sets from XML files.
     * 
     * @param args
     *            Command line arguments<br>
     *            args[0] = login name<br>
     *            args[1] = password<br>
     *            args[2] = project name<br>
     *            args[3] = data binding name<br>
     *            args[4] = path with data binding data<br>
     **/
    public static void main(String[] args) {
        // initializations
        MTextClient client = null;
        ClientJob job = null;
        TemporaryTextDocument textDocument = null;

        // Check whether we have correct number of arguments
        if (args.length < 4 || args.length > 5) {
            System.out.println("Usage: Client <login> <pwd> <project> <binding>");
            return;
        }
        final String login = args[0];
        final String pwd = args[1];
        final String projectName = args[2];
        final String bindingName = args[3];
        final String dataPathName = args[4];
        
        System.out.println(MessageFormat.format("Start: login:{0}, password:{1}, project:{2}, databinding:{3}, data-path:{4})", login, pwd, projectName, bindingName, dataPathName));

        try {
            // connect to server
            server = de.kwsoft.mtext.api.server.MTextFactory.connect(login, pwd, null);

            createDocument(projectName, bindingName, dataPathName);
            
            Scanner in = new Scanner(System.in);
            System.out.println("Press any key to continue...");
//            if (!in.next()) {
//            	
//            }
            System.in.read();
            
            createDocument(projectName, bindingName, dataPathName);
            
            System.out.println("*** Finish ***");

        } 
        catch (Exception mte) {
            // M/Text exception occurred
            if (mte instanceof MTextException) {
                printMTextException((MTextException) mte);
            }
            else {  
            	printError(mte);
                mte.printStackTrace();
            }
        }
        // close the client
        finally {
            if (client != null) {
                client.close();
            }
            if (server != null) {
            	server.close();
            }
        }
    }

	/**
	 * @param projectName
	 * @param bindingName
	 * @param dataPathName
	 * @throws JobInProgressException
	 * @throws DataBindingNotFoundException
	 * @throws MTextException
	 * @throws Exception
	 * @throws IOException
	 * @throws DocumentNotFoundException
	 * @throws DataSourceNotSetException
	 * @throws DataMappingParameterNotSetException
	 * @throws ScriptEvalException
	 * @throws JobExecutionException
	 */
	private static void createDocument(final String projectName, final String bindingName, final String dataPathName) 
			throws JobInProgressException, DataBindingNotFoundException,
			MTextException, Exception, IOException, DocumentNotFoundException,
			DataSourceNotSetException, DataMappingParameterNotSetException,
			ScriptEvalException, JobExecutionException {
		
		// create job
		jobServer = server.createJob();
		
		// begin job
		jobServer.begin();
		
		// open text document
		textDocumentServer = jobServer.createTemporaryTextDocument("testDocument", projectName, null);
		
		final DocumentDataBinding bindingServer = server.createDocumentDataBinding(projectName, bindingName);
		
		fillDataSources(bindingServer, dataPathName);
		
		// execute the data binding to fill the document instance
		textDocumentServer.executeDocumentDataBinding(bindingServer);

		// export to pdf
		System.out.println("export to pdf");
		jobServer.exportDocument(textDocumentServer, "application/pdf", new ByteArrayOutputStream(), server.getConfigurationFactory().newExportDocumentConfiguration(), null);
		
		// close the document
		System.out.println("close the document");
		textDocumentServer.close();
		
		// execute job
		System.out.println("Execute job");
		jobServer.execute();
	}

    private static void printMTextException(MTextException mte) {
        printError(mte);
        Iterator reasons = mte.getReasons();
        while (reasons.hasNext()) {
            Object reason = reasons.next();
            if (reason instanceof MTextException) {
                printMTextException((MTextException) reason);
            } else
            if (reason instanceof Throwable) {
                printError((Throwable) reason);
            }
            else {
                System.out.println(reason);
            }
        }
        mte.printStackTrace();
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

    private static void fillDataSources(final DocumentDataBinding binding, String dataPathName) throws Exception {
        for (String dataSourceName : new Iterable<String>() {
            public Iterator<String> iterator() {
                return binding.getDataSourceNames();
            }
        }) {
            try {
            	String filenameDATA = dataSourceName + ".xml";
            	if (dataPathName != null) {
            		filenameDATA = dataPathName + filenameDATA;  
            	}
            	XMLDataSource xmlDataSource = new XMLDataSource(new InputStreamReader(new FileInputStream(filenameDATA), "utf-8"));
                binding.setDataSource(dataSourceName, xmlDataSource);                
            } catch (FileNotFoundException e) {
                System.out.println("Warning: cannot find the data source file, leaving unset");
                e.printStackTrace(System.out);
            }
        }
    }

}