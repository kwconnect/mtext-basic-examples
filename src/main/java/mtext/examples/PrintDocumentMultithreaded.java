package mtext.examples;

import de.kwsoft.mtext.api.Configuration;
import de.kwsoft.mtext.api.DocumentAccessMode;
import de.kwsoft.mtext.api.JobExecutionException;
import de.kwsoft.mtext.api.JobInProgressException;
import de.kwsoft.mtext.api.MTextException;
import de.kwsoft.mtext.api.PersistentTextDocument;
import de.kwsoft.mtext.api.PrintResult;
import de.kwsoft.mtext.api.client.ClientJob;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;
import mtext.examples.util.MUtil;

/**
 * M/Text client API example: Prints a document multiple times in multiple
 * threads.
 **/
public class PrintDocumentMultithreaded {

	/**
	 * Prints a document multiple times in multiple threads.
	 * 
	 * @param args Command line arguments<br>
	 *             args[0] = username<br>
	 *             args[1] = password<br>
	 *             args[3] = document name<br>
	 *             args[4] = destination name<br>
	 *             args[5] = jobs per thread count<br>
	 *             args[6] = thread count
	 **/
	public static void main(String[] args) {

		MUtil.checkArguments(args, 6, PrintDocumentMultithreaded.class, "<username> <password> <document name> <destination name> <jobs per thread count> <thread count> ");

		final String username = args[0];
		final String password = args[1];
		final String fullQualifiedDocumentName = args[2];
		final String destinationName = args[3];
		final int jobsPerThreadCount = Integer.valueOf(args[4]);
		final int threadCount = Integer.valueOf(args[5]);

		for (int threadIndex = 0; threadIndex < threadCount; threadIndex++) {
			new Thread(new Runnable() {
				public void run() {
					MTextClient client = null;
					try {
						// connect to server
						client = MTextFactory.connect(username, password, null);

						for (int jobIndex = 0; jobIndex < jobsPerThreadCount; jobIndex++) {
							System.out.println("Thread: " + Thread.currentThread().getId() + ", job: " + jobIndex);
							runJob(client);
						}

					}
					catch (MTextException mte) {
						System.out.println("Cannot print the document!");
						mte.printStackTrace();
					}
					finally {
						// close the client
						if (client != null && !client.isClosed()) {
							client.close();
						}
					}
				}

				private void runJob(MTextClient client) throws JobInProgressException, JobExecutionException {

					// create job
					ClientJob job = client.createJob();

					// begin job
					job.begin();

					// open text document
					PersistentTextDocument textDocument = job.openTextDocument(fullQualifiedDocumentName, DocumentAccessMode.SHARED_READ_ONLY, null);

					// print document
					Configuration configuration = client.getConfigurationFactory().newPrintConfiguration();
					PrintResult printResult = client.getConfigurationFactory().newPrintResult();
					job.printDocument(textDocument, destinationName, configuration, printResult);

					// close the document
					textDocument.close();

					// execute job
					job.execute();

					System.out.println("Successfully printed document " + fullQualifiedDocumentName);

				}

			}).start();
		}

	}
}