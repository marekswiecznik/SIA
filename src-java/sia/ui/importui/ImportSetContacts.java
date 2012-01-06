package sia.ui.importui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.eclipse.swt.widgets.Text;

import sia.models.Contact;
import sia.models.ContactAccount;

public class ImportSetContacts extends WizardPage {
	List<Contact> contacts;
	List<Contact> parsedContacts;
	private Composite container;
	private ScrolledComposite scrolledComposite;
	private Map<Contact, Controls> mapControl;
	/**
	 * Create the wizard.
	 */
	public ImportSetContacts() {
		super("setContacts");
		setTitle("Set contacts");
		setDescription("");
		mapControl = new HashMap<Contact, Controls>();
	}

	public Map<Contact, Controls> getControls() {
		return mapControl;
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
		scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.V_SCROLL);
		setControl(scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite container = new Composite(scrolledComposite, SWT.NULL);

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
		
		for (int i = 0; i < parsedContacts.size(); i++) {
			//new Controls lays controls to container
			mapControl.put(parsedContacts.get(i), new Controls(parsedContacts.get(i)));
		}
		
		container.layout();
		scrolledComposite.setContent(container);
		scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public void layout() {
		container.layout(false);
	}
	
	public void addNewContacts() {
		for (Contact c : mapControl.keySet()) {
			Controls ctrls = mapControl.get(c);
			if(ctrls.getVisible()) {
				Contact newC = new Contact(0, ctrls.fname.getText(), ctrls.lname.getText(), ctrls.name.getText());
				for (ContactAccount ca : c.getContactAccounts()) {
					newC.addContactAccount(ca.clone());
				}
				contacts.add(newC);
			}
		}
	}
	
	private class MySelectionAdapter extends SelectionAdapter {
		Controls c; //number of file
		MySelectionAdapter(Controls c) {this.c=c;}

		@Override
		public void widgetSelected(SelectionEvent e) {
			if(((Combo)e.getSource()).getSelectionIndex()>0) {
				c.fname.setEnabled(false);
				c.lname.setEnabled(false);
				c.name.setEnabled(false);
			} else {
				c.fname.setEnabled(true);
				c.lname.setEnabled(true);
				c.name.setEnabled(true);
			}
		}
	}
	
	class Controls {
		Text fname;
		Text lname;
		Text name;
		Combo combo;
		Label[] protocols;
		Label[] uids;
		Label separator;
		
		public Controls(Contact c) {
			List<ContactAccount> accounts = c.getContactAccounts();

			this.protocols = new Label[accounts.size()];
			this.uids = new Label[accounts.size()];
			
			this.protocols[0] = new Label(container, SWT.NONE);
			this.protocols[0].setImage(sia.ui.org.eclipse.wb.swt.SWTResourceManager.getImage(ImportChooseIM.class, "/sia/ui/resources/protocols/"+accounts.get(0).getProtocol().getIcon()));
			
			this.uids[0] = new Label(container, SWT.NONE);
			this.uids[0].setText(accounts.get(0).getUid());
			
			this.fname = new Text(container, SWT.BORDER);
			this.fname.setText(c.getFirstname());
			this.fname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, accounts.size()));
			
			this.lname = new Text(container, SWT.BORDER);
			this.lname.setText(c.getLastname());
			this.lname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, accounts.size()));
			
			this.name = new Text(container, SWT.BORDER);
			this.name.setText(c.getName());
			this.name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, accounts.size()));

			this.combo = new Combo(container, SWT.CHECK);
			this.combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, accounts.size()));
			List<String> contacts4combo = new ArrayList<String>();
			contacts4combo.add("");
			for (int j = 0; j < contacts.size(); j++) {
				if(!c.equals(parsedContacts.get(j))) {
					contacts4combo.add(parsedContacts.get(j).getContactAccounts().get(0).getUid()); 	
				}
			}
			this.combo.setItems(contacts4combo.toArray(new String[] {}));
			this.combo.addSelectionListener(new MySelectionAdapter(this));
			
			for (int i = 1; i < accounts.size(); i++) {
				this.protocols[i] = new Label(container, SWT.NONE);
				this.protocols[i].setImage(sia.ui.org.eclipse.wb.swt.SWTResourceManager.getImage(ImportChooseIM.class, "/sia/ui/resources/protocols/"+accounts.get(i).getProtocol().getIcon()));
				
				this.uids[i] = new Label(container, SWT.NONE);
				this.uids[i].setText(accounts.get(i).getUid());
				this.uids[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			}
			
			
			this.separator = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
			this.separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		}

		boolean getVisible() {
			return name.getVisible();
		}
		
		void remove() {
			GridData ob = (GridData) fname.getLayoutData();
			ob.exclude = true;
			fname.setVisible(false);
			ob = (GridData) lname.getLayoutData();
			ob.exclude = true;
			lname.setVisible(false);
			ob = (GridData) name.getLayoutData();
			ob.exclude = true;
			name.setVisible(false);
			ob = (GridData) combo.getLayoutData();
			ob.exclude = true;
			combo.setVisible(false);
			for (int i = 0; i < uids.length; i++) {
				ob = (GridData) uids[i].getLayoutData();
				ob.exclude = true;
				uids[i].setVisible(false);
				ob = (GridData) protocols[i].getLayoutData();
				ob.exclude = true;
				protocols[i].setVisible(false);
			}
			ob = (GridData) separator.getLayoutData();
			ob.exclude = true;
			separator.setVisible(false);
		}
		void add() {
			GridData ob = (GridData) fname.getLayoutData();
			ob.exclude = false;
			fname.setVisible(true);
			ob = (GridData) lname.getLayoutData();
			ob.exclude = false;
			lname.setVisible(true);
			ob = (GridData) name.getLayoutData();
			ob.exclude = false;
			name.setVisible(true);
			ob = (GridData) combo.getLayoutData();
			ob.exclude = false;
			combo.setVisible(true);
			for (int i = 0; i < uids.length; i++) {
				ob = (GridData) uids[i].getLayoutData();
				ob.exclude = false;
				uids[i].setVisible(true);
				ob = (GridData) protocols[i].getLayoutData();
				ob.exclude = false;
				protocols[i].setVisible(true);
			}
			ob = (GridData) separator.getLayoutData();
			ob.exclude = false;
			separator.setVisible(true);
		}
	}
}
