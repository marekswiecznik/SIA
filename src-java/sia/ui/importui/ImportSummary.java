package sia.ui.importui;

import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import sia.models.Contact;
import sia.models.ContactAccount;
import sia.models.Conversation;
import sia.models.Message;

public class ImportSummary extends WizardPage {
	private List<Contact> contacts;
	Composite container;
	Label lCAccounts;
	Label lContacts;
	Label lConv;
	Label lMessages;
	/**
	 * Create the wizard.
	 */
	public ImportSummary() {
		super("importSummary");
		setTitle("Summary");
		setDescription("Click next button to save imported data");
	}

	public void setContacts(List<Contact> contacts) {
		this.contacts = contacts;
	}
	
	
	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		Label l = new Label(container, SWT.NULL);
		l.setText("New contacts: ");
		lContacts = new Label(container, SWT.NONE);
		
		l = new Label(container, SWT.NULL);
		l.setText("New contact accounts: ");
		lCAccounts = new Label(container, SWT.NONE);
		
		l = new Label(container, SWT.NULL);
		l.setText("New conversations: ");
		lConv = new Label(container, SWT.NONE);
		
		l = new Label(container, SWT.NULL);
		l.setText("New messages: ");
		lMessages= new Label(container, SWT.NONE);
		
	}

	public void setControls() {
		int newCAccounts = 0;
		int newContacts= 0 ;
		int newConv = 0;
		int newMessages = 0;
		
		for (Contact c : contacts) {
			if(c.getId()==0) {
				newContacts++;
			}
			for(ContactAccount ca : c.getContactAccounts()) {
				if(ca.getId()==0) {
					newCAccounts++;
				}
				for(Conversation conv : ca.getConversations()) {
					if(conv.getId()==0) {
						newConv++;
					}
					for(Message m : conv.getMessages()) {
						if(m.getId()==0) {
							newMessages++;
						}
					}
				}
			}
		}
		lContacts.setText(newContacts+"");
		lCAccounts.setText(newCAccounts+"");
		lConv.setText(newConv+"");
		lMessages.setText(newMessages+"");
		container.layout();
	}
	
}
