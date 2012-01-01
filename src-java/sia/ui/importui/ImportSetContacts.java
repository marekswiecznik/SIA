package sia.ui.importui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
	private Map<String, Contact> mapContact;
	/**
	 * Create the wizard.
	 */
	public ImportSetContacts() {
		super("setContacts");
		setTitle("Set contacts");
		setDescription("");
		mapContact = new HashMap<String, Contact>();
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
		
		fnames = new Text[contacts.size()];
		lnames = new Text[contacts.size()];
		names = new Text[contacts.size()];
		combos = new Combo[contacts.size()];
		for (int i = 0; i < contacts.size(); i++) {
			mapContact.put(contacts.get(i).getContactAccounts().get(0).getUid(), contacts.get(i));
		}
		
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
			combos[i].addSelectionListener(new MySelectionAdapter(i));
			String[] contacts4combo = new String[contacts.size()];
			contacts4combo[0] = "";
			int ind=1;
			for (int j = 0; j < contacts.size(); j++) {
				if(i!=j) {
					contacts4combo[ind] = contacts.get(j).getContactAccounts().get(0).getUid(); 
					
					ind++;
				}
			}
			combos[i].setItems(contacts4combo);
			
			for (int j = 1; j < accounts.size(); j++) {
				Label protocol = new Label(container, SWT.NONE);
				protocol.setImage(sia.ui.org.eclipse.wb.swt.SWTResourceManager.getImage(ImportChooseIM.class, "/sia/ui/resources/protocols/"+accounts.get(j).getProtocol().getIcon()));
				Label uid = new Label(container, SWT.FILL);
				uid.setText(accounts.get(j).getUid());
			}
			Label separator = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
			separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 6, 1));
		}
		
		container.layout();
		scrolledComposite.setContent(container);
		scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}
	
	public List<Contact> getContacts() {
		List<Contact> contacts = new ArrayList<Contact>();
		for (int i = 0; i < names.length; i++) {
			if(combos[i].getSelectionIndex()<1) {
				Contact c = this.contacts.get(i);
				c.setFirstname(fnames[i].getText());
				c.setLastname(lnames[i].getText());
				c.setName(names[i].getText());
				contacts.add(c);
			}
			else {
				Contact c = mapContact.get(combos[i].getItems()[combos[i].getSelectionIndex()]);
				c.getContactAccounts().addAll(this.contacts.get(i).getContactAccounts());
			}
		}
		
		return contacts;
	}
	
	private class MySelectionAdapter extends SelectionAdapter {
		int n; //number of file
		MySelectionAdapter(int n) {this.n = n;}

		@Override
		public void widgetSelected(SelectionEvent e) {
			if(((Combo)e.getSource()).getSelectionIndex()>0) {
				fnames[n].setEnabled(false);
				lnames[n].setEnabled(false);
				 names[n].setEnabled(false);
			} else {
				fnames[n].setEnabled(true);
				lnames[n].setEnabled(true);
				 names[n].setEnabled(true);
			}
		}
	}
	
}
