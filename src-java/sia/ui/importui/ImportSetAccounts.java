package sia.ui.importui;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import sia.models.UserAccount;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

/**
 * 
 * @author Agnieszka Glabala
 *
 */
public class ImportSetAccounts extends WizardPage {
	List<UserAccount> userAccounts;
	private Text text;
	/**
	 * Create the wizard.
	 */
	public ImportSetAccounts() {
		super("setAccounts");
		setTitle("Set Account");
		setDescription("Set your user ID:");
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
		container.setLayout(new GridLayout(2, false));
		
		Label lblUserId = new Label(container, SWT.NONE);
		lblUserId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUserId.setText("User ID:");
		
		text = new Text(container, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}
}
