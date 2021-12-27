package mtext.examples.util;

/**
 * This class contains some static utility methods.
 * @author Timo Dreier
 **/
public final class MUtil {
    /** 
     * Hidden constructor to avoid creation of class 
     **/
    private MUtil() {
        // not intended to be instantiated
    }

    /**
     * Checks, if there are too less or too much arguments
     * @param args The arguments, which should be checked
     * @param cnt The number of expected arguments
     * @return boolean <code>true</code> if the specified arguments contains
     * the expected number of arguments
     **/
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
