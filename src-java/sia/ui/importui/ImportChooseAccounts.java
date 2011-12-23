package sia.ui.importui;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import sia.models.UserAccount;
import sia.ui.org.eclipse.wb.swt.SWTResourceManager;

/**
 * 
 * @author Agnieszka Glabala
 *
 */
public class ImportChooseAccounts extends WizardPage {
	List<UserAccount> userAccounts;
	/**
	 * Create the wizard.
	 */
	public ImportChooseAccounts() {
		super("chooseAccounts");
		setTitle("Choose Accounts");
		setDescription("Choose accounts you would like to import from.");
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
		for (Control c : container.getChildren())
			c.dispose();
		
		Button btnCheckButton = new Button(container, SWT.CHECK);
		btnCheckButton.setImage(SWTResourceManager.getImage(ImportChooseAccounts.class, "/sia/ui/resources/protocols/gg.png"));
		btnCheckButton.setText("Check Button");

		setControl(container);
	}
	
	public boolean[] getSelectedAccounts() {
		return null;
	}
	
	public void setUserAccounts(List<UserAccount> uas) {
		userAccounts = uas;
	}
}
