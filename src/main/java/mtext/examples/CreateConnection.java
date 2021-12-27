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

		// check whether we have correct number of arguments
		MUtil.checkArguments(args, 2, CreateConnection.class, "<username> <password>");

		// initializations
		MTextClient client = null;

		try {
			// connect to the client with the arguments name and password
			client = MTextFactory.connect(args[0], args[1], null);
			System.out.println("Successfully connected to M/TEXT Client!");
			
			client.close();
			System.out.println("The connection is closed!");
		}
		catch (MTextException me) {
			System.out.println("Failed to connect to M/TEXT Client!");
			me.printStackTrace();
		}
	}
}
