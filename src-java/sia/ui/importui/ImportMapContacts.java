package sia.ui.importui;

import java.util.List;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import sia.models.Contact;
import sia.models.ContactAccount;
import sia.ui.importui.ImportSetContacts.Controls;

/**
 * 
 * @author Agnieszka Glabala
 *
 */
public class ImportMapContacts extends WizardPage {
	private List<Contact> parsedContacts;
	private Composite container;
	private Combo[] combos;
	private ScrolledComposite scrolledComposite;
	private List<Contact> contacts;
	private ImportSetContacts nextPage;
	
	/**
	 * Create the wizard.
	 */
	public ImportMapContacts() {
		super("mapContacts");
		setTitle("Map contacts");
		setDescription("Map contacs");
	}
	
	public void setNextPage(ImportSetContacts np) {
		this.nextPage = np;
	}
	
	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	
	public void setParsedContacts(List<Contact> parsedContacts) {
		this.parsedContacts = parsedContacts;
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
		if(parsedContacts!=null) {
			String[] contactsComboNames = new String[contacts.size()+1];
			contactsComboNames[0] = "";
			for (int i = 0; i < contacts.size(); i++) {
				if(contacts.get(i).getFirstname().length()>0 && contacts.get(i).getLastname().length()==0) {
					contactsComboNames[i+1] = contacts.get(i).getName() + " ("+contacts.get(i).getFirstname()+")";
				} else if(contacts.get(i).getFirstname().length()==0 && contacts.get(i).getLastname().length()>0) {
					contactsComboNames[i+1] = contacts.get(i).getName() + " ("+contacts.get(i).getLastname()+")";
				} else if(contacts.get(i).getFirstname().length()>0 && contacts.get(i).getLastname().length()>0) {
					contactsComboNames[i+1] = contacts.get(i).getName() + " ("+contacts.get(i).getFirstname()+" "+contacts.get(i).getLastname()+")";
				} else {
					contactsComboNames[i+1] = contacts.get(i).getName();
				}
			}
			combos = new Combo[parsedContacts.size()];
			for (int i = 0; i < parsedContacts.size(); i++) {
				accounts = parsedContacts.get(i).getContactAccounts();
				Label fname = new Label(container, SWT.NONE);
				fname.setText(parsedContacts.get(i).getFirstname());
				fname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, accounts.size()));
				
				Label lname = new Label(container, SWT.NONE);
				lname.setText(parsedContacts.get(i).getLastname());
				lname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, accounts.size()));
				
				Label name = new Label(container, SWT.NONE);
				name.setText(parsedContacts.get(i).getName());
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

				combos[i].addSelectionListener(new MySelectionAdapter(i));
				Label separator = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
				separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
			}
			container.layout();
			scrolledComposite.setContent(container);
			scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}
	}

	private class MySelectionAdapter extends SelectionAdapter {
		int n; //number of file
		MySelectionAdapter(int n) {this.n=n;}

		@Override
		public void widgetSelected(SelectionEvent e) {
			if(((Combo)e.getSource()).getSelectionIndex()>0) {
				Controls c = nextPage.getControls().get(n);
				c.remove();
			} else {
				Controls c = nextPage.getControls().get(n);
				c.add();
			}
		}
	}
	
	public void addContactAccounts() {
		for (int i = 0; i < combos.length; i++) {
			if(combos[i].getSelectionIndex()>0) {
				for (ContactAccount ca : this.parsedContacts.get(i).getContactAccounts()) {
					if(!this.contacts.get(combos[i].getSelectionIndex()-1).getContactAccounts().contains(ca)) {
						this.contacts.get(combos[i].getSelectionIndex()-1).addContactAccount(ca);
					}
				}
			}
		}
	}
	
}
