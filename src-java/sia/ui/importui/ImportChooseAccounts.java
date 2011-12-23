package sia.ui.importui;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseEvent;

import sia.models.UserAccount;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

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
		super("wizardPage");
		setTitle("Choose Accounts");
		setDescription("Choose accounts you would like to import from.");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		Button btnCheckButton = new Button(container, SWT.CHECK);
		btnCheckButton.setImage(SWTResourceManager.getImage(ImportChooseAccounts.class, "/sia/ui/resources/protocols/gg.png"));
		btnCheckButton.setText("Check Button");

	}
	
	public boolean[] getSelectedAccounts() {
		return null;
	}
	
	public void setUserAccounts(List<UserAccount> uas) {
		userAccounts = uas;
	}
}
