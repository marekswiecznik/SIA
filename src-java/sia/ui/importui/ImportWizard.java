package sia.ui.importui;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import sia.datasourses.DataSource;


public class ImportWizard extends Wizard {
	private String[] imNames;
	private int im;
	DataSource datasource;
	IWizardPage chooseIM;
	IWizardPage chooseFiles;
	IWizardPage setPasswords;
	IWizardPage chooseAccounts;
	IWizardPage setAccounts;
	IWizardPage mapContacts;
	public ImportWizard() {
		setWindowTitle("Import messages");
		
		imNames = new String[] {"applications-accessories",  "applications-games", "applications-multimedia", "applications-science", "empathy",  "applications-development",  
				"applications-graphics", "applications-office", "applications-system", "kadu", "applications-engineering", "applications-internet", "applications-other", 
				"applications-utilities", "phone"};
		chooseIM = new ImportChooseIM(imNames);
		chooseFiles = new ImportChooseFiles();
		setPasswords = new ImportSetPasswords();
		chooseAccounts = new ImportChooseAccounts();
		setAccounts = new ImportSetAccounts();
		mapContacts = new ImportMapContacts();
		//datasource = new DataSource();
	}

	@Override
	public void addPages() {
		addPage(chooseIM);
		addPage(chooseFiles);
		addPage(setPasswords);
		addPage(chooseAccounts);
		addPage(setAccounts);
		addPage(mapContacts);
	}

	protected void setIM(int im) {
		if(im!=this.im) {
			//delete all buttons from files page
			Button[] bts = ((ImportChooseFiles)chooseFiles).buttons;
			Label[] lbs = ((ImportChooseFiles)chooseFiles).labels;
			Label[] flbs = ((ImportChooseFiles)chooseFiles).filesLabels;
			for (int i = 0; i < lbs.length; i++) {
				bts[i].dispose();
				lbs[i].dispose();
				flbs[i].dispose();
			}
			this.im = im;
			
			((ImportChooseFiles)chooseFiles).setFileExtensions(datasource.getFileExtensions());
			((ImportChooseFiles)chooseFiles).setDescriptions(datasource.getDescriptions());
			chooseFiles.createControl((Composite)this.getShell());
			Point size = this.getShell().getSize();
			this.getShell().setSize(this.getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT));
			this.getShell().setSize(size);
		}
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == chooseIM)
			setIM(((ImportChooseIM)chooseIM).getSelected());
		return super.getNextPage(page);
	}
	
	@Override
	public boolean performFinish() {
		return false;
	}
	
	
}
