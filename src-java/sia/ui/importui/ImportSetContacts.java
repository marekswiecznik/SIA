package sia.ui.importui;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

import sia.models.Contact;
import sia.models.ContactAccount;

import org.eclipse.swt.widgets.Combo;

public class ImportSetContacts extends WizardPage {
	List<Contact> contacts;
	private Text[] fnames;
	private Text[] lnames;
	private Text[] names;
	private Combo[] combos;
	private Composite container;
	private ScrolledComposite scrolledComposite;
	/**
	 * Create the wizard.
	 */
	public ImportSetContacts() {
		super("setContacts");
		setTitle("Set contacts");
		setDescription("");
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	
	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.V_SCROLL);
		setControl(scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(6, false));
		
		scrolledComposite.setContent(container);
		scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		this.container = container;
	}

	public void setControls() {
		for (Control c : container.getChildren())
			c.dispose();
		Label lblProtocol = new Label(container, SWT.FILL);
		lblProtocol.setText("Protocol");
		
		Label lblNewLabel = new Label(container, SWT.FILL);
		lblNewLabel.setText("UID");
		
		Label lblFirstname = new Label(container, SWT.FILL);
		lblFirstname.setText("First name");
		
		Label lblSecondName = new Label(container, SWT.FILL);
		lblSecondName.setText("Second name");
		
		Label lblName = new Label(container, SWT.FILL);
		lblName.setText("Name");
		
		Label lblConnectWith = new Label(container, SWT.FILL);
		lblConnectWith.setText("Connect with...");
		
		fnames = new Text[contacts.size()];
		lnames = new Text[contacts.size()];
		names = new Text[contacts.size()];
		combos = new Combo[contacts.size()];
		for (int i = 0; i < contacts.size(); i++) {
			List<ContactAccount> accounts = contacts.get(i).getContactAccounts();
			
			Label protocol0 = new Label(container, SWT.NONE);
			protocol0.setImage(sia.ui.org.eclipse.wb.swt.SWTResourceManager.getImage(ImportChooseIM.class, "/sia/ui/resources/protocols/"+accounts.get(0).getProtocol().getIcon()));
			Label uid0 = new Label(container, SWT.FILL);
			uid0.setText(accounts.get(0).getUid());
			
			fnames[i] = new Text(container, SWT.BORDER);
			fnames[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, accounts.size()));
			
			lnames[i] = new Text(container, SWT.BORDER);
			lnames[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, accounts.size()));
			
			names[i] = new Text(container, SWT.BORDER);
			names[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, accounts.size()));
			
			combos[i] = new Combo(container, SWT.NONE);
			combos[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, accounts.size()));
			
			for (int j = 1; j < accounts.size(); j++) {
				Label protocol = new Label(container, SWT.NONE);
				protocol.setImage(sia.ui.org.eclipse.wb.swt.SWTResourceManager.getImage(ImportChooseIM.class, "/sia/ui/resources/protocols/"+accounts.get(j).getProtocol().getIcon()));
				Label uid = new Label(container, SWT.FILL);
				uid.setText(accounts.get(j).getUid());
			}
		}
		
		container.layout();
		scrolledComposite.setContent(container);
		scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
}
