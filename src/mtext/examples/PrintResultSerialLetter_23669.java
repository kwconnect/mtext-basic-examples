package mtext.examples;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import de.kwsoft.mtext.api.Configuration;
import de.kwsoft.mtext.api.DataModelNodeValidationFailedException;
import de.kwsoft.mtext.api.MTextException;
import de.kwsoft.mtext.api.PrintResult;
import de.kwsoft.mtext.api.TemporaryTextDocument;
import de.kwsoft.mtext.api.client.ClientJob;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;
import de.kwsoft.mtext.api.databinding.CSVDataSource;
import de.kwsoft.mtext.api.databinding.DataModelNode;
import de.kwsoft.mtext.api.databinding.DataModelNodeValidation.ValidationValue;
import de.kwsoft.mtext.api.databinding.DocumentDataBinding;

/**
 * Creates a document from data binding and shows it in preview.
 **/
public class PrintResultSerialLetter_23669 {

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
     **/
    public static void main(String[] args) {
        // initializations
        MTextClient client = null;
        ClientJob job = null;
        TemporaryTextDocument textDocument = null;

        // Check whether we have correct number of arguments
        if (args.length != 4) {
            System.out.println("Usage: Client <login> <pwd> <project> <binding>");
            return;
        }
        final String login = args[0];
        final String pwd = args[1];
        final String projectName = args[2];
        final String bindingName = args[3];
        
        System.out.println("login: " + login);
        System.out.println("pass: " + pwd);
        System.out.println("projectName: " + projectName);
        System.out.println("bindingName: " + bindingName);

        try {
            // connect to server
            client = MTextFactory.connect(login, pwd, null);

            // create job
            job = client.createJob();
            // begin job
            job.begin();
            // open text document
            textDocument = job.createTemporaryTextDocument("testDocument", projectName, null);
            final DocumentDataBinding binding = client.createDocumentDataBinding(projectName, bindingName);
            fillDataSources(binding);
            // execute the data binding to fill the document instance
            textDocument.executeDocumentDataBinding(binding);
            PrintResult newPrintResult = client.getConfigurationFactory().newPrintResult();
            Configuration newPrintConfiguration = client.getConfigurationFactory().newPrintConfiguration();
			job.printDocument(textDocument, "pdf", newPrintConfiguration, newPrintResult);
//			job.printDocument(textDocument, "OMS", newPrintConfiguration, newPrintResult);
					
            // close the document
            textDocument.close();
            // execute job
            job.execute();

			String[] keys2 = newPrintResult.getKeys();
			Object res = newPrintResult.get("FileName");
			if (res instanceof List) {
				List list = (List) res;
				for (int i = 0, sizei = list.size(); i < sizei; i++) {
					Object resItem = list.get(i);
					System.out.println("FileName("+ i + ") = " + resItem);
				}
			}
			else {
				System.out.println("FileName = " + res);
			}			
			int numberOfPages2 = newPrintResult.getNumberOfPages();
			System.out.println("numberOfPages: " + numberOfPages2);
			
        } catch (Exception mte) {
            // M/Text exception occurred
        	System.out.println("Exception");
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
            } else
            if (reason instanceof Throwable) {
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
        }
    }

    private static void fillDataSources(final DocumentDataBinding binding) throws Exception {
//        for (String dataSourceName : new Iterable<String>() {
//            public Iterator<String> iterator() {
//                return binding.getDataSourceNames();
//            }
//        }) {
//            try {
//            	DataSource dataSource = binding.getDataSource(dataSourceName);
//            	Source source = dataSource.getSource();
//            	// TODO: use CSV instead
//                binding.setDataSource(dataSourceName, new XMLDataSource(new InputStreamReader(new FileInputStream(dataSourceName + ".xml"), "utf-8")));                
//            } catch (FileNotFoundException e) {
//                System.out.println("Warning: cannot find the data source file, leaving unset");
//                e.printStackTrace(System.out);
//            }
//        }
    	FileInputStream fileInputStream = new FileInputStream("d:\\mExpresses\\mExpress7_full_de\\workspace\\bsp_Common\\Testdaten\\Daten_07.csv");
    	binding.setDataSource("DATA", new CSVDataSource("", new InputStreamReader(fileInputStream, "utf-8"), ','));                
    	
    }

}