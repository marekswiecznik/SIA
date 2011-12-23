package sia.ui.importui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;

/**
 * 
 * @author Agnieszka Glabala
 *
 */
public class ImportSetPasswords extends WizardPage {
	private String[] passwordDescriptions;
	private Label[] labels;
	private Text[] textfields;
	
	/**
	 * Create the wizard.
	 */
	public ImportSetPasswords() {
		super("wizardPage");
		setTitle("Set passwords");
		setDescription("Set passwords required to read database.");
	}

	public void setPasswordDescpriptions(String[] pds) {
		passwordDescriptions = pds;
	}
	
	public String[] getPasswords() {
		return null;
	}
	
	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		System.out.println("PPP");
		Composite container = null;
		if (getControl() == null) {
			container = new Composite(parent, SWT.NULL);
			container.setLayout(new GridLayout(2, false));	
		} else {
			container = (Composite)getControl();
		}

		if(passwordDescriptions!=null && passwordDescriptions.length>0) {
			labels = new Label[passwordDescriptions.length];
			textfields = new Text[passwordDescriptions.length];
			for (int i = 0; i < passwordDescriptions.length; i++) {
				labels[i] = new Label(container, SWT.NONE);
				labels[i].setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				labels[i].setText(passwordDescriptions[i]);
				
				textfields[i] = new Text(container, SWT.BORDER | SWT.PASSWORD);
				textfields[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			}
		}
		
		setControl(container);
	}
}
