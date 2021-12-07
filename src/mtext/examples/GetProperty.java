package mtext.examples;

import java.util.Properties;

import mtext.examples.util.MUtil;
import de.kwsoft.mtext.api.AbstractFolder;
import de.kwsoft.mtext.api.DocumentInformation;
import de.kwsoft.mtext.api.Folder;
import de.kwsoft.mtext.api.MTextException;
import de.kwsoft.mtext.api.ReadOnlyListIterator;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;

/**
 * Get the property of a document by the property key
 * @author Timo Dreier
 **/
public class GetProperty {
    /**
     * Gets the property of a document by the property key
     * @param args Command line arguments<br>
     * args[0] = user name<br>
     * args[1] = password<br>
     * args[2] = folder path<br>
     * args[3] = document name<br>
     * args[4] = property name
     **/
    public static void main(String[] args) {
        // initializations
        MTextClient client = null;
        AbstractFolder folder = null;
        ReadOnlyListIterator docIter = null;
        DocumentInformation docInf = null;
        Properties prop = null;
        boolean isFound = false;
        if (MUtil.checkArguments(args, 5)) {
            final String name = args[0];
            final String pwd = args[1];
            final String folderPath = args[2];
            final String documentName = args[3];
            final String propertyName = args[4];

            try {
                // connect
                client = MTextFactory.connect(name, pwd, null);
                //get the specified folder
                folder = client.getRootFolder().getFolderByName(folderPath);
                if (folder instanceof Folder) {
                    //get the text document information iterator
                    docIter = ((Folder)folder).getTextDocumentInformation();
                    //search the document and fetch the user defined properties
                    while (docIter.hasNext()) {
                        docInf = (DocumentInformation)docIter.next();
                        if (docInf.getDocumentName().equals(documentName)) {
                            //get the properties
                            prop = docInf.getUserDefinedProperties();
                            isFound = true;
                            break;
                        }
                    }
                    //can't find the document
                    if ((!isFound) || (prop == null)) {
                        throw new Exception("Can't find the document '" +
                            documentName + "' !");
                    }
                    Object propertyValue = prop.getProperty(propertyName);
                    //check, if property exists
                    if (propertyValue == null) {
                        throw new Exception("Property '" + propertyName +
                            "' is not defined for document '" +
                            documentName + "' !");
                    }
                    else {
                        //print the property
                        System.out.println(propertyValue);
                    }
                }
                else {
                    System.out.println("Specified folder is root folder !" +
                        "The root folder cannot contain documents !!");
                }
            }
            // M/Text exception occured
            catch (MTextException me) {
                System.out.println("Can't get the property '" + propertyName +
                    "' from document '" + documentName + "'!");
                me.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (client != null) {
                    client.close();
                }
            }
        }
        else {
            System.out.println("M/Text client api example: GetProperty");
            System.out.println();
            System.out.println("Usage: java mtext.examples.GetProperty " +
                "<name> <password> <folder path> <document name> "+
                "<property name>");
        }
    }
}
