package sia.ui.importui;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import sia.models.Contact;
import sia.models.ContactAccount;
import sia.utils.Dictionaries;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridLayout;

/**
 * 
 * @author Agnieszka Glabala
 *
 */
public class ImportMapContacts extends WizardPage {
	private List<Contact> contacts;
	private Composite container;
	private Combo[] combos;
	private ScrolledComposite scrolledComposite;
	private List<Contact> dbContacts;
	/**
	 * Create the wizard.
	 */
	public ImportMapContacts() {
		super("mapContacts");
		setTitle("Map contacts");
		setDescription("Map contacs");
		dbContacts = Dictionaries.getInstance().getContacts();
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	
	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		setControl(scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		Composite container =  new Composite(scrolledComposite, SWT.NONE);
		this.container = container;
		container.setLayout(new GridLayout(6, false));

		scrolledComposite.setContent(container);
		scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	public void setControls() {
		
		for (Control c : container.getChildren())
			c.dispose();
		Label lblFirstName = new Label(container, SWT.NONE);
		lblFirstName.setText("First name");
		
		Label lblLastName = new Label(container, SWT.NONE);
		lblLastName.setText("Last name");
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setText("Name");
		
		Label lblProtocol = new Label(container, SWT.NONE);
		lblProtocol.setText("Protocol");
		
		Label lblUid = new Label(container, SWT.NONE);
		lblUid.setText("UID");
		
		Label lblContact = new Label(container, SWT.NONE);
		lblContact.setText("Contact");
		
		List<ContactAccount> accounts;
		
		if(contacts!=null) {
			
			String[] contactsComboNames = new String[dbContacts.size()+1];
			contactsComboNames[0] = "";
			for (int i = 0; i < dbContacts.size(); i++) {
				if(dbContacts.get(i).getFirstname().length()>0 && dbContacts.get(i).getLastname().length()==0) {
					contactsComboNames[i+1] = dbContacts.get(i).getName() + " ("+dbContacts.get(i).getFirstname()+")";
				} else if(dbContacts.get(i).getFirstname().length()==0 && dbContacts.get(i).getLastname().length()>0) {
					contactsComboNames[i+1] = dbContacts.get(i).getName() + " ("+dbContacts.get(i).getLastname()+")";
				} else if(dbContacts.get(i).getFirstname().length()>0 && dbContacts.get(i).getLastname().length()>0) {
					contactsComboNames[i+1] = dbContacts.get(i).getName() + " ("+dbContacts.get(i).getFirstname()+" "+dbContacts.get(i).getLastname()+")";
				} else {
					contactsComboNames[i+1] = dbContacts.get(i).getName();
				}
			}
			combos = new Combo[contacts.size()];
			for (int i = 0; i < contacts.size(); i++) {
				accounts = contacts.get(i).getContactAccounts();
				Label fname = new Label(container, SWT.NONE);
				fname.setText(contacts.get(i).getFirstname());
				fname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, accounts.size()));
				
				Label lname = new Label(container, SWT.NONE);
				lname.setText(contacts.get(i).getLastname());
				lname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, accounts.size()));
				
				Label name = new Label(container, SWT.NONE);
				name.setText(contacts.get(i).getName());
				name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, accounts.size()));
				
				Label protocol0 = new Label(container, SWT.NONE);
				protocol0.setImage(sia.ui.org.eclipse.wb.swt.SWTResourceManager.getImage(ImportChooseIM.class, "/sia/ui/resources/protocols/"+accounts.get(0).getProtocol().getIcon()));
				
				Label uid0 = new Label(container, SWT.NONE);
				uid0.setText(accounts.get(0).getUid());
				
				combos[i] = new Combo(container, SWT.CHECK);
				combos[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, accounts.size()));
				combos[i].setItems(contactsComboNames);
				for (int j = 1; j < accounts.size(); j++) {
					Label protocol = new Label(container, SWT.NONE);
					protocol.setImage(sia.ui.org.eclipse.wb.swt.SWTResourceManager.getImage(ImportChooseIM.class, "/sia/ui/resources/protocols/"+accounts.get(j).getProtocol().getIcon()));
					
					Label uid = new Label(container, SWT.NONE);
					uid.setText(accounts.get(j).getUid());
					uid.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				}
				
				Label separator = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
				separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
			}
			container.layout();
			scrolledComposite.setContent(container);
			scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}
	}

	public List<Contact> getEmptyContacts() {
		List<Contact> contacts = new ArrayList<Contact>();
		for (int i = 0; i < combos.length; i++) {
			if(combos[i].getSelectionIndex()<1) {
				contacts.add(this.contacts.get(i));
			} else {
				dbContacts.get(combos[i].getSelectionIndex()-1).getContactAccounts().addAll(this.contacts.get(i).getContactAccounts());
			}
		}
		return contacts;
	}
	
}
