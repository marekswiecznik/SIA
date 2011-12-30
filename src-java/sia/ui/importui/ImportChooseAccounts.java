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
	private List<UserAccount> userAccounts;
	private Composite container;
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
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));	
		this.container = container;
		
		setControl(container);
	}
	
	public void setControls() {
		for (Control c : container.getChildren())
			c.dispose();
		for (int i = 0; i < userAccounts.size(); i++) {
			Button btnCheckButton = new Button(container, SWT.CHECK);
			btnCheckButton.setImage(SWTResourceManager.getImage(ImportChooseAccounts.class, "/sia/ui/resources/protocols/"+userAccounts.get(i).getProtocol().getIcon()));
			btnCheckButton.setText(userAccounts.get(i).getUid());
		}
		container.layout();
	}
	
	public boolean[] getSelectedAccounts() {
		return null;
	}
	
	public void setUserAccounts(List<UserAccount> uas) {
		userAccounts = uas;
	}
}
