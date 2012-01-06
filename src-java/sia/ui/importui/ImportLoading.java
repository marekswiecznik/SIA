package sia.ui.importui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;

public class ImportLoading extends WizardPage {
	private ProgressBar progressBar;
	
	/**
	 * Create the wizard.
	 */
	public ImportLoading(String name) {
		super(name);
		setTitle("Accounts loading");
		setDescription("Please wait...");
	}
	
	/**
	 * Set progress
	 * @param value progress value
	 */
	public void setProgress(int value) {
		progressBar.setSelection(value);
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(1, false));
		
		progressBar = new ProgressBar(container, SWT.SMOOTH);
		progressBar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		setControl(container);
	}
}
