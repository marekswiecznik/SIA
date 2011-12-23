package sia.ui.importui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

/**
 * 
 * @author Agnieszka Glabala
 *
 */
public class ImportSetPasswords extends WizardPage {
	private String[] passwordDescriptions;
	/**
	 * Create the wizard.
	 */
	public ImportSetPasswords() {
		super("wizardPage");
		setTitle("Set passwords");
		setDescription("Set passwords required to read database.");
	}

	public void setPasswordDescpriptions(String[] pds) {
		passwordDescriptions = pds;
	}
	
	public String[] getPasswords() {
		return null;
	}
	
	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		
		Button btnNewButton = new Button(container, SWT.NONE);
		
		btnNewButton.setBounds(23, 10, 79, 27);
		btnNewButton.setText("New Button");
		//TODO Password page

	}
}
