package mtext.examples;

import de.kwsoft.mtext.api.Configuration;
import de.kwsoft.mtext.api.DataBindingTemplate;
import de.kwsoft.mtext.api.FilterComparison;
import de.kwsoft.mtext.api.PropertySimpleFilter;
import de.kwsoft.mtext.api.client.ClientConfigurationFactory;
import de.kwsoft.mtext.api.client.MTextClient;
import de.kwsoft.mtext.api.client.MTextFactory;
import mtext.examples.util.MUtil;

/**
 * M/Text client API example: Opens the Template Selection Dialog (Java Swing
 * Client).
 **/
public class OpenTemplateSelectionDialog {

	/**
	 * Opens the Template Selection Dialog (Java Swing Client).
	 * 
	 * @param args Command line arguments<br>
	 *             args[0] = username<br>
	 *             args[1] = password<br>
	 *             args[2] = sparte (optional) - filter criteria for the template
	 *             selection by Metadata.Sparte<br>
	 * 
	 **/
	public static void main(String[] args) {

		MUtil.checkArguments(args, 2, OpenClassicEditor.class, "<name> <password> [sparte]");

		final String username = args[0];
		final String password = args[1];
		String sparte = null;

		if (args.length > 2) {
			sparte = args[2];
		}

		try {

			// initializations
			MTextClient client = MTextFactory.connect(username, password, null);

			ClientConfigurationFactory configurationFactory = (ClientConfigurationFactory) client.getConfigurationFactory();

			Configuration configuration = configurationFactory.newTemplateSelectionDialogConfiguration();

			if (sparte != null) {
				configuration.put(ClientConfigurationFactory.TEMPLATE_SELECTION_FILTER, new PropertySimpleFilter("Metadata.Sparte", FilterComparison.EQUAL, sparte));
			}

			// Show the template selection dialog
			DataBindingTemplate template = client.showTemplateSelectionDialog(configuration);
			System.out.println(template == null ? "Dialog canceled" : "Selected " + template.getFullName());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
