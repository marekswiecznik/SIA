package sia.ui.importui;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

import sia.ui.org.eclipse.wb.swt.SWTResourceManager;


public class ImportChooseIM extends WizardPage {
	private String[] imNames;
	private Button[] radioButtons;
	/**
	 * Create the wizard.
	 */
	public ImportChooseIM(String[] names) {
		super("wizardPage");
		setTitle("Choose IM");
		setDescription("Choose IM from which you would like to import data:");
		this.imNames = names;
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	@Override
	public void createControl(Composite parent) {	
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		setControl(scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Composite composite = new Composite(scrolledComposite, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		
		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		radioButtons = new Button[imNames.length];
		for (int i = 0; i < imNames.length; i++) {
			radioButtons[i] = new Button(composite, SWT.RADIO);
			radioButtons[i].setImage(SWTResourceManager.getImage(ImportChooseIM.class, "/sia/ui/resources/ims/"+imNames[i]+".png"));
			radioButtons[i].setText(imNames[i]); 
		}
		radioButtons[0].setSelection(true);
	}
	
	public int getSelected(){
		for (int i = 0; i < radioButtons.length; i++) {
			if(radioButtons[i].getSelection()) 
				return i;
		}
		return -1;
	}
	
	@Override
	public boolean canFlipToNextPage(){
		return getSelected()>-1;
	}
	
}
