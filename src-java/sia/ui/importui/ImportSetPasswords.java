package sia.ui.importui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;


public class ImportSetPasswords extends WizardPage {

	/**
	 * Create the wizard.
	 */
	public ImportSetPasswords() {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
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
		

	}
}
