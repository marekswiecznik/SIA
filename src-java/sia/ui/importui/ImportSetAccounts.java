package sia.ui.importui;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import sia.models.UserAccount;

/**
 * 
 * @author Agnieszka Glabala
 *
 */
public class ImportSetAccounts extends WizardPage {
	List<UserAccount> userAccounts;
	/**
	 * Create the wizard.
	 */
	public ImportSetAccounts() {
		super("setAccounts");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
	}

	public List<UserAccount> getSelectedAccounts() {
		return null;
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
