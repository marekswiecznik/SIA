package sia.ui.importui;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
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
		super("mapContacts");
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
		Composite container = null;
		if (getControl() == null) {
			container = new Composite(parent, SWT.NONE);
			container.setLayout(new GridLayout(1, false));	
		} else {
			container = (Composite)getControl();
		}
		setControl(container);
	}

}
