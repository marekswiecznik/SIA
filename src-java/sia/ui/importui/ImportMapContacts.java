package sia.ui.importui;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import sia.models.Contact;

/**
 * 
 * @author Agnieszka Glabala
 *
 */
public class ImportMapContacts extends WizardPage {

	/**
	 * Create the wizard.
	 */
	public ImportMapContacts() {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
	}

	public void setContacts(List<Contact> contacts) {
		
	}
	
	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
	}

}
