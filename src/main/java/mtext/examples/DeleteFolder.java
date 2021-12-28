package mtext.examples;

import de.kwsoft.mtext.api.*;

import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;

import mtext.examples.util.MUtil;

/**
 * M/Text client API example: Delete a folder by its full qualified name.
 **/
public class DeleteFolder {
	/**
	 * Deletes a folder by the path
	 * 
	 * @param args Command line arguments<br>
	 *             args[0] = user name<br>
	 *             args[1] = password<br>
	 *             args[2] = the full qualified folder name
	 **/
	public static void main(String[] args) {

		MUtil.checkArguments(args, 3, CreateFolder.class, "<name> <password> <folderName>");
		final String name = args[0];
		final String pwd = args[1];
		final String folderName = args[2];

		// initializations
		MTextClient client = null;
		AbstractFolder folder = null;

		try {
			// connect
			client = MTextFactory.connect(name, pwd, null);
			
			// get the folder
			folder = client.getRootFolder().getFolderByName(folderName);
			
			if (folder instanceof Folder) {
				// delete the folder
				((Folder) folder).delete();
				System.out.println("Successfully deleted folder " + folderName);
			}
			else {
				System.err.println("Root folder cannot be deleted");
			}
		}
		// folder exception
		catch (FolderNotEmptyException fnee) {
			System.out.println("Can't delete folder '" + folderName + "' (Folder is not empty) !");
			fnee.printStackTrace();
		}
		// M/Text exception
		catch (MTextException me) {
			System.out.println("Can't delete folder '" + folderName + "'!");
			me.printStackTrace();
		}
		catch (Exception e) {
			// e.g. the path is wrong -> the folder is null -> Exception
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
