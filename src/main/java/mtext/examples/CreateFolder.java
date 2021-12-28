package mtext.examples;

import de.kwsoft.mtext.api.*;

import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;

import mtext.examples.util.MUtil;

/**
 * M/Text client API example: Create a new folder in a specified parent folder
 **/
public class CreateFolder {
	/**
	 * Creates a new folder in the specified parent folder
	 * 
	 * @param args Command line arguments<br>
	 *             args[0] = username<br>
	 *             args[1] = password<br>
	 *             args[2] = folder path<br>
	 *             args[3] = name of the new folder
	 **/
	public static void main(String[] args) {

		MUtil.checkArguments(args, 4, CreateFolder.class, "<name> <password> <folder path> <folder name>");
		final String name = args[0];
		final String pwd = args[1];
		final String folderPath = args[2];
		final String folderName = args[3];

		// initializations
		MTextClient client = null;
		AbstractFolder parentFolder = null;
		
		try {
			// connect
			client = MTextFactory.connect(name, pwd, null);

			// get the specified folder by the client and the path
			if (folderPath.equals("\\")) {
				parentFolder = client.getRootFolder();
			}
			else {
				parentFolder = client.getRootFolder().getFolderByName(folderPath);
			}
			
			// create a new folder in the specified parent folder
			parentFolder.createSubFolder(folderName);
			System.out.println("Successfully created new folder " + folderPath + "\\" + folderName);
		}
		catch (MTextException me) {
			System.err.println("Failed to create the folder " + folderName);
			me.printStackTrace();
		}
		// e.g. the path is wrong -> the folder is null -> Exception
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
