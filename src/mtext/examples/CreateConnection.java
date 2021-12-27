package mtext.examples;

import de.kwsoft.mtext.api.MTextException;

import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;

import mtext.examples.util.MUtil;

/**
 * M/Text client API example: Create a connection and close it
 **/
public class CreateConnection {
	/**
	 * Create a connection and close it
	 * 
	 * @param args Command line arguments<br>
	 *             args[0] = username<br>
	 *             args[1] = password
	 **/
	public static void main(String[] args) {

		// initializations
		MTextClient client = null;

		// check whether we have correct number of arguments
		if (MUtil.checkArguments(args, 2)) {

			try {
				// connect to the server with the arguments name and password
				client = MTextFactory.connect(args[0], args[1], null);
				System.out.println("Created the connection succesfully!");
				client.close();
				System.out.println("Closed the Connection!");
			}
			// the method connect(...) throws a MTextException
			catch (MTextException me) {
				System.out.println("Failed to connect to M/TEXT Client!");
				me.printStackTrace();
			}
		}
		else {
			System.out.println("M/Text client api example: CreateConnection");
			System.out.println();
			System.out.println("Usage: java mtext.examples.CreateConnection <name> <password>");
		}
	}
}
