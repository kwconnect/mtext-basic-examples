package mtext.examples;

import java.util.Iterator;

import de.kwsoft.mtext.api.AbstractFolder;
import de.kwsoft.mtext.api.DocumentInformation;
import de.kwsoft.mtext.api.Folder;
import de.kwsoft.mtext.api.MTextException;
import de.kwsoft.mtext.api.ReadOnlyListIterator;
import de.kwsoft.mtext.api.TextDocumentInformation;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;
import mtext.examples.util.MUtil;

/**
 * M/Text client API example: Get all document information of a specified folder
 **/
public class GetDocumentInformation {
	/**
	 * Get information of all documents, which belongs to a specified folder
	 * 
	 * @param args Command line arguments<br>
	 *             args[0] = username<br>
	 *             args[1] = password<br>
	 *             arsg[2] = folder path
	 **/
	public static void main(String[] args) {

		MUtil.checkArguments(args, 3, GetDocumentInformation.class, "<username> <password> <folder path>");
		final String username = args[0];
		final String password = args[1];
		final String folderPath = args[2];

		// initializations
		MTextClient client = null;
		
		@SuppressWarnings("rawtypes")
		ReadOnlyListIterator listIterator = null;
		AbstractFolder folder = null;
		try {
			// connect
			client = MTextFactory.connect(username, password, null);
			// get the folder
			folder = client.getRootFolder().getFolderByName(folderPath);
			
			System.out.println("Dumping DocumentInformation for folder " + folderPath);
			if (folder instanceof Folder) {

				// get the iterator with the text document informations
				listIterator = ((Folder) folder).getTextDocumentInformation();
				// show the information
				showDocumentInformation(listIterator);

				// get the iterator with the model document informations
				listIterator = ((Folder) folder).getModelDocumentInformation();
				// show the information
				showDocumentInformation(listIterator);
			}
			else {
				System.out.println("Root folder does not contain document information !");
			}
		}
		catch (MTextException me) {
			// M/Text exception occured
			System.out.println("Error during showing document information !");
			me.printStackTrace();
		}
		// general exception e.g. path of folder is wrong -> folder = null
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Iterate of the specified iterator and show all DocumentInformation
	 * 
	 * @param iterator Contains DocumentInformation objects
	 **/
	@SuppressWarnings("rawtypes")
	private static void showDocumentInformation(ReadOnlyListIterator iterator) {
		
		final String indent = "         ";
		DocumentInformation documentInformation = null;
		
		while (iterator.hasNext()) {
			documentInformation = (DocumentInformation) iterator.next();
			if (documentInformation instanceof TextDocumentInformation) {
				System.out.print("Document: ");
			}
			else {
				System.out.print("Model: ");
			}
			System.out.println(documentInformation.getDocumentName());
			System.out.println(indent + "Qualified Name : " + documentInformation.getQualifiedDocumentName());
			System.out.println(indent + "Description    : " + documentInformation.getDocumentDescription());
			System.out.println(indent + "Created        :" + documentInformation.getCreationTimestamp());
			System.out.println(indent + "Created by     :" + documentInformation.getCreationPrincipal().getName());
			System.out.println(indent + "Modified       :" + documentInformation.getLastModifiedTimestamp());
			System.out.println(indent + "Modified by    :" + documentInformation.getLastModifiedPrincipal().getName());
			System.out.println(indent + "Printed        :" + documentInformation.getLastPrintedTimestamp());
			System.out.println(indent + "Printed by     :" + documentInformation.getLastPrintedPrincipal().getName());
			System.out.println(indent + "Metadata       :");
			
			Iterator<String> metadataIterator = documentInformation.getMetadata().getKeyNames();
			
			if (!metadataIterator.hasNext()) {
				System.out.println(indent + indent + "No Metadata found!");
			}
			
			while (metadataIterator.hasNext()) {
				String metadataKey = metadataIterator.next();
				String metadataValue = documentInformation.getMetadata().getSingleValue(metadataKey);
				System.out.println(indent + indent + metadataKey + " = " + metadataValue);
			}
		}
	}
}
