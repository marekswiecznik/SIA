package sia.ui.importui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import sia.models.UserAccount;
import sia.utils.Dictionaries;

/**
 * 
 * @author Agnieszka Glabala
 *
 */
public class ImportSetAccounts extends WizardPage {
	List<UserAccount> userAccounts;
	private Text text;
	private Combo combo;
	private List<UserAccount> uas4combo2;
	Composite container;
	/**
	 * Create the wizard.
	 */
	public ImportSetAccounts() {
		super("setAccounts");
		setTitle("Set Account");
		setDescription("Set your user ID:");
	}

	public List<UserAccount> getUserAccounts() {
		if(combo.getSelectionIndex()>0) {
			userAccounts.set(0, uas4combo2.get(combo.getSelectionIndex()-1));
		} else {
			userAccounts.get(0).setUid(text.getText());
		}
		return userAccounts;
	}
	
	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(2, false));
		

	}

	public void setControls() {
		if(userAccounts!=null) {
			for (Control c : container.getChildren())
				c.dispose();
			Label lblOrSelectFrom = new Label(container, SWT.NONE);
			lblOrSelectFrom.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
			lblOrSelectFrom.setText("Select user account from existed:");
			
			combo = new Combo(container, SWT.NONE);
			combo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if(combo.getSelectionIndex()>0) {
						text.setEnabled(false);
					} else {
						text.setEnabled(true);
					}
				}
			});
			combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			List<UserAccount> uas = Dictionaries.getInstance().getUserAccounts();
			ArrayList<String> uas4combo = new ArrayList<String>();
			uas4combo2 = new ArrayList<UserAccount>();
			uas4combo.add("");
			for (int i = 0; i < uas.size(); i++) {
				if(uas.get(i).getProtocol().equals(userAccounts.get(0).getProtocol())) {
					uas4combo.add(uas.get(i).getProtocol().getName()+" "+uas.get(i).getUid());
					uas4combo2.add(uas.get(i));
				}
			}
			combo.setItems(uas4combo.toArray(new String[] {}));
			Label lblCreateNewUser = new Label(container, SWT.NONE);
			lblCreateNewUser.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
			lblCreateNewUser.setText("or create new one:");
			
			Label lblUserId = new Label(container, SWT.NONE);
			lblUserId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblUserId.setText("User ID:");
			
			text = new Text(container, SWT.BORDER);
			text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			container.layout();

			text.addModifyListener((ImportWizard)getWizard());
			combo.addSelectionListener((ImportWizard)getWizard());
		}
	}
	
	public void setUserAccounts(List<UserAccount> userAccounts2) {
		userAccounts = userAccounts2;
		
	}
}
