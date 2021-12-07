package mtext.examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mtext.examples.util.MUtil;
import de.kwsoft.mtext.api.DataModelResourceInformation;
import de.kwsoft.mtext.api.MTextException;
import de.kwsoft.mtext.api.ResourceInformation;
import de.kwsoft.mtext.api.ResourceProvider;
import de.kwsoft.mtext.api.ResourceProvider.ResourceType;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;
import de.kwsoft.mtext.api.server.MTextServer;

/**
 * M/Text client API example: Create a new folder in a specified parent folder
 * 
 * @author Michal Michal
 **/
public class ListResourcesTest {
    /**
     * Creates a new folder in the specified parent folder
     * @param args Command line arguments<br>
     * args[0] = username<br>
     * args[1] = password<br>
     * args[2] = baseProject or "null" for all projects<br>
     * args[3] = resource type or "null" for all resources<br>
     **/
    public static void main(String[] args) {
        // initializations
        MTextClient client = null;
        MTextServer server = null;

        // if there are two less or to much arguments
        if (MUtil.checkArguments(args, 4)) {
            final String name = args[0];
            final String pwd = args[1];
            final String baseProject = "null".equals(args[2]) ? null : args[2];
            ResourceType resourceType = "null".equals(args[3]) ? null : ResourceType.valueOf(args[3]);
            
            Collection<ResourceInformation> clientRess = Collections.EMPTY_LIST;
            Collection<ResourceInformation> serverRess = Collections.EMPTY_LIST;
            
            try {

                // connect - client
                client = MTextFactory.connect(name, pwd, null);
                
                {
	                System.out.println("");
	                System.out.println("*** CLIENT ***");
	                System.out.flush();

	                ResourceProvider resourceProvider = client.getResourceProvider();
	                Collection<ResourceInformation> resources = resourceProvider.listResources(baseProject, null, resourceType);
	
	                System.out.println("found " + resources.size() + " files");
	                for (ResourceInformation resourceInformation : resources) {
						System.out.print(" > " + resourceInformation.getFullName());
						System.out.print(" (" + resourceInformation.getProjectName() + ")");
						if (resourceType == ResourceType.DATA_MODEL) {
							System.out.print(", DATA_MODEL: " + ((DataModelResourceInformation) resourceInformation).loadDataModel().getName());
						}
						System.out.println("");
					}
	                
	                clientRess = resources;
                }
                
            	// connect - server
            	server = de.kwsoft.mtext.api.server.MTextFactory.connect(name, pwd, null);
            	{
	                System.out.println("");
	                System.out.println("*** SERVER ***");
	                System.out.flush();

	                ResourceProvider resourceProvider = server.getResourceProvider();
	                Collection<ResourceInformation> resources = resourceProvider.listResources(baseProject, null, resourceType);
	
	                System.out.println("found " + resources.size() + " files");
	                for (ResourceInformation resourceInformation : resources) {
						System.out.print(" > " + resourceInformation.getFullName());
						System.out.print(" (" + resourceInformation.getProjectName() + ")");
						if (resourceType == ResourceType.DATA_MODEL) {
							System.out.print(", DATA_MODEL: " + ((DataModelResourceInformation) resourceInformation).loadDataModel().getName());
						}
						System.out.println("");
					}
	                
	                serverRess = resources;
            	}
            	
            }
            
            // M/Text exception
            catch (MTextException me) {
                System.out.println("Invalid search " + args[3] + " in " + baseProject + " project!");
                me.printStackTrace();
            }
            // e.g. the path is wrong -> the folder is null -> Exception
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                if (client != null) {
                    // close client
                    client.close();
                }
                if (server != null) {
                    // close server
                    server.close();
                }
            }
            
            boolean resourceCountEquals = clientRess.size() == serverRess.size();
            List<String> clientFullNames = new ArrayList();
            List<String> serverFullNames = new ArrayList();
            Set<String> clientDoubles = new HashSet();
            Set<String> serverDoubles = new HashSet();
            
            for (ResourceInformation resourceInformation : serverRess) {
				String fullName = resourceInformation.getFullName();
				if (serverFullNames.contains(fullName)) {
					serverDoubles.add(fullName);
				}
				serverFullNames.add(fullName);
			}
            
            for (ResourceInformation resourceInformation : clientRess) {
				String fullName = resourceInformation.getFullName();
				if (clientFullNames.contains(fullName)) {
					clientDoubles.add(fullName);
				}
				clientFullNames.add(fullName);
				if (serverFullNames.contains(fullName)) {
					serverFullNames.remove(fullName);
				}
			}
            
            for (ResourceInformation resourceInformation : serverRess) {
				String fullName = resourceInformation.getFullName();
				if (clientFullNames.contains(fullName)) {
					clientFullNames.remove(fullName);
				}
			}
            
            System.out.println("************************************************");
            System.out.println("client and server resource count equals: " + resourceCountEquals);
            System.out.println("client+: " + clientFullNames.size() + ", " + clientFullNames);
            System.out.println("server+: " + serverFullNames.size() + ", " + serverFullNames);
            System.out.println("client duplicity: " + clientDoubles);
            System.out.println("server duplicity: " + serverDoubles);
            
        }
        else {
            System.out.println("M/Text client api example: ListResources");
            System.out.println();
            System.out.println("Usage: java mtext.examples.ListResources " +
                "<name> <password> <base project> <resource type>");
        }
    }
}
