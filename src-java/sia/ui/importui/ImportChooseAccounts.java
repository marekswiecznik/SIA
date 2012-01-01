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
	private UserAccount[] userAccountsTmp;
	private Composite container;
	private Button[] buttons;
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
		buttons = new Button[userAccounts.size()];
		for (int i = 0; i < userAccounts.size(); i++) {
			buttons[i] = new Button(container, SWT.CHECK);
			buttons[i].setImage(SWTResourceManager.getImage(ImportChooseAccounts.class, "/sia/ui/resources/protocols/"+userAccounts.get(i).getProtocol().getIcon()));
			buttons[i].setText(userAccounts.get(i).getUid());
		}
		container.layout();
	}
	
	public List<UserAccount> getUserAccounts() {
		if(userAccountsTmp==null) {
			userAccountsTmp = userAccounts.toArray(new UserAccount[] {});
		}
		for (int i = 0; i < userAccountsTmp.length; i++) {
			if(!buttons[i].getSelection()) {
				userAccounts.remove(userAccountsTmp[i]);
			}
		}
		System.out.println("TTTTTT "+userAccounts.size());
		return userAccounts;
	}
	
	public void setUserAccounts(List<UserAccount> uas) {
		userAccounts = uas;
	}
}
