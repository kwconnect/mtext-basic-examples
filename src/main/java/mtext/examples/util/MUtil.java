package mtext.examples.util;

/**
 * This class contains some static utility methods.
 **/
public final class MUtil {
	/**
	 * Hidden constructor to avoid creation of class
	 **/
	private MUtil() {
	}

	public static void checkArguments(String[] args, int argumentCount, Class<?> theClass, String usageMessage) {

		if (args.length == argumentCount) {
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
	 * Checks, if there are too less or too much arguments
	 * 
	 * @param args The arguments, which should be checked
	 * @param cnt  The number of expected arguments
	 * @return boolean <code>true</code> if the specified arguments contains the
	 *         expected number of arguments
	 **/
	@Deprecated
	public static boolean checkArguments(String[] args, int cnt) {
		if (args.length == cnt) {
			return true;
		}
		else {
			if (cnt < 2) {
				System.out.println("You have to enter " + cnt + " argument");
			}
			else {
				System.out.println("You have to enter " + cnt + " arguments");
			}
			return false;
		}
	}
}
