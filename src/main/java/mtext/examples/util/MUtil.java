package mtext.examples.util;

import java.io.File;

/**
 * This class contains some static utility methods.
 **/
public final class MUtil {

	/**
	 * Hidden constructor to avoid creation of class
	 **/
	private MUtil() {
	}

	/**
	 * Checks the minimum argument count. The method throws an
	 * {@link IllegalArgumentException} if too less arguments were provided.
	 * 
	 * @param args
	 * @param argumentCount
	 * @param theClass
	 * @param usageMessage
	 */
	public static void checkArguments(String[] args, int argumentCount, Class<?> theClass, String usageMessage) {

		if (args.length >= argumentCount) {
			return;
		}
		else {
			if (argumentCount < 2) {
				System.err.println("You have to enter " + argumentCount + " argument");
			}
			else {
				System.err.println("You have to enter " + argumentCount + " arguments");
			}

			throw new IllegalArgumentException("Usage: " + theClass.getName() + " " + usageMessage);
		}
	}

	/**
	 * Returns a {@link File} object with the given fileName that is located in the
	 * system temp directory specified by the SystemProperty java.io.tmpdir.
	 * 
	 * @param fileName
	 * @return
	 */
	public static File getFileInTempDirectory(String fileName) {
		String tmpdir = System.getProperty("java.io.tmpdir");
		return new File(tmpdir, fileName);
	}

}
