package sia.ui.importui;

import java.util.List;

import javax.sound.sampled.SourceDataLine;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import sia.datasourses.DataSource;
import sia.models.UserAccount;

/**
 * 
 * @author Agnieszka Glabala
 *
 */
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
		im=-1;
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
			((ImportChooseFiles)chooseFiles).disposeControls();
			this.im = im;
			((ImportChooseFiles)chooseFiles).setFileExtensions(datasource.getFileExtensions());
			((ImportChooseFiles)chooseFiles).setDescriptions(datasource.getFileDescriptions());
			chooseFiles.createControl((Composite)this.getShell());
			Point size = this.getShell().getSize();
			this.getShell().setSize(this.getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT));
			this.getShell().setSize(size);
		}
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == chooseIM) {
			setIM(((ImportChooseIM)chooseIM).getSelected()); 
			return super.getNextPage(page);
		}
		else if(page == chooseFiles) {	
			//TODO merge those two?
			String[] passwordDescriptions = datasource.getRequiredPassword();
			if(passwordDescriptions != null && passwordDescriptions.length > 0) {
				((ImportSetPasswords)setPasswords).setPasswordDescpriptions(passwordDescriptions);
				return setPasswords;
			}
			datasource.loadFiles(((ImportChooseFiles)chooseFiles).getFiles());
			datasource.parse();
			List<UserAccount> uas  = datasource.getUserAccouts();
			if(uas == null) {
				return setAccounts;
			}
			else {
				((ImportChooseAccounts)chooseAccounts).setUserAccounts(uas);
				return chooseAccounts;
			}
		}
		else if(page == setPasswords) {
			datasource.setPasswords(((ImportSetPasswords)setPasswords).getPasswords());
			datasource.loadFiles(((ImportChooseFiles)chooseFiles).getFiles());
			datasource.parse();
			List<UserAccount> uas  = datasource.getUserAccouts();
			if(uas.size()>0 && uas.get(0).getUid()==null) {
				return setAccounts;
			}
			else {
				((ImportChooseAccounts)chooseAccounts).setUserAccounts(uas);
				return chooseAccounts;
			}
		}
		else if(page == chooseAccounts) {
			datasource.setUserAccounts(((ImportChooseAccounts)chooseAccounts).getSelectedAccounts());
			((ImportMapContacts)mapContacts).setContacts(datasource.getContacts());
			return mapContacts;
		}
		else if(page == setAccounts) {
			datasource.setUserAccounts(((ImportSetAccounts)setAccounts).getSelectedAccounts());
			((ImportMapContacts)mapContacts).setContacts(datasource.getContacts());
			return mapContacts;
		}
		return super.getNextPage(page);
	}
	
	@Override
	public boolean performFinish() {
		return false;
	}
	
	
}
