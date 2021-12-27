package mtext.examples;

import de.kwsoft.mtext.api.Configuration;
import de.kwsoft.mtext.api.DataBindingTemplate;
import de.kwsoft.mtext.api.client.ClientConfigurationFactory;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;

/**
 * @author Vladislav Krycha
 *
 */
public class CallTemplateTreeFolder {

	/**
	   * call the template dialog
	   * @param args Command line arguments<br>
	   * args[0]  = name<br>
	   * args[1]  = password<br>

	   **/
	  public static void main(String[] args)
	  {
		  try {
		    //initializations
		    MTextClient client = MTextFactory.connect(args[0], args[1], null);
		    ClientConfigurationFactory configurationFactory = (ClientConfigurationFactory) client.getConfigurationFactory();
	        Configuration configuration = configurationFactory.newTemplateSelectionDialogConfiguration();
	        // Set a filter on the template tree
	        //configuration.put(ClientConfigurationFactory.TEMPLATE_SELECTION_FILTER, new PropertySimpleFilter("Metadata.bindingCategory", FilterComparison.EQUAL, "dataBindingA"));
	        //configuration.put(ClientConfigurationFactory.TEMPLATE_SELECTION_FILTER, new PropertySimpleFilter("Metadata.State", FilterComparison.EQUAL, "Undefined - undefiniert"));
	        //configuration.put(ClientConfigurationFactory.TEMPLATE_SELECTION_FILTER, new PropertySimpleFilter("Metadata.Sparte", FilterComparison.EQUAL, "Girokonto"));
	        // Show the template selection dialog
			DataBindingTemplate template = client.showTemplateSelectionDialog(configuration);
			System.out.println(template == null ? "Dialog canceled" : "Selected " + template.getFullName());
		} catch (Exception e) {			
			e.printStackTrace();
		}
        // template is null if the user canceled, otherwise it points to the selected resource


	  }

	  
}
