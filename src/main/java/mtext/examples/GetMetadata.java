package mtext.examples;

import de.kwsoft.mtext.api.AbstractFolder;
import de.kwsoft.mtext.api.DocumentInformation;
import de.kwsoft.mtext.api.Folder;
import de.kwsoft.mtext.api.MTextException;
import de.kwsoft.mtext.api.ReadOnlyListIterator;
import de.kwsoft.mtext.api.ResourceMetadata;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;
import mtext.examples.util.MUtil;

/**
 * M/Text client API example: Get the value of a specific (searchable) Metadata of a
 * document.
 **/
public class GetMetadata {
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
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {

		MUtil.checkArguments(args, 5, GetMetadata.class, "<username> <password> <folderPath> <docuentName> <metadataKey>");
		final String username = args[0];
		final String password = args[1];
		final String folderPath = args[2];
		final String documentName = args[3];
		final String metadataName = args[4];
		
		// initializations
		MTextClient client = null;
		AbstractFolder folder = null;
		ReadOnlyListIterator documentInfoIterator = null;
		DocumentInformation documentInformation = null;
		ResourceMetadata metadata = null;
		boolean isFound = false;

		try {
			// connect
			client = MTextFactory.connect(username, password, null);
			
			// get the specified folder
			folder = client.getRootFolder().getFolderByName(folderPath);
			
			if (folder instanceof Folder) {
				// get the text document information iterator
				documentInfoIterator = ((Folder) folder).getTextDocumentInformation();
				// search the document and fetch the user defined properties
				while (documentInfoIterator.hasNext()) {
					documentInformation = (DocumentInformation) documentInfoIterator.next();
					if (documentInformation.getDocumentName().equals(documentName)) {
						// get the properties
						metadata = documentInformation.getMetadata();
						isFound = true;
						break;
					}
				}
				// can't find the document
				if ((!isFound) || (metadata == null)) {
					throw new Exception("Can't find the document '" + documentName + "' !");
				}
				String propertyValue = metadata.getSingleValue(metadataName);
				// check, if property exists
				if (propertyValue == null) {
					throw new Exception("Property '" + metadataName + "' is not defined for document '" + documentName + "' !");
				}
				else {
					// print the property
					System.out.println(metadataName + " = " + propertyValue);
				}
			}
			else {
				System.out.println("Specified folder is root folder !" + "The root folder cannot contain documents !!");
			}
		}
		catch (MTextException me) {
			// M/Text exception occured
			System.out.println("Can't get the property '" + metadataName + "' from document '" + documentName + "'!");
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
