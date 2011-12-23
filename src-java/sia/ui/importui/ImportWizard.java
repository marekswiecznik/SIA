package sia.ui.importui;

import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import sia.datasourses.DataSource;
import sia.datasourses.FMADataSource;
import sia.models.UserAccount;
import sia.utils.Dictionaries;

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

	public void repaint() {
		Point size = this.getShell().getSize();
		this.getShell().setSize(this.getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT));
		this.getShell().setSize(size);
	}
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == chooseIM) {
			if(((ImportChooseIM)chooseIM).getSelected()!=this.im) {			
				this.im = ((ImportChooseIM)chooseIM).getSelected();
				this.datasource = Dictionaries.getInstance().getDataSource("Float's Mobile Agent");
				((ImportChooseFiles)chooseFiles).setFileExtensions(datasource.getFileExtensions());
				((ImportChooseFiles)chooseFiles).setDescriptions(datasource.getFileDescriptions());
			}
			chooseFiles.createControl((Composite)this.getShell());
			repaint();
			return chooseFiles;
		}
		else if(page == chooseFiles && ((ImportChooseFiles)chooseFiles).getFiles()[0] != null) {	
			//TODO merge those two?
			String[] passwordDescriptions = datasource.getRequiredPassword();
			if(passwordDescriptions != null && passwordDescriptions.length > 0) {
				((ImportSetPasswords)setPasswords).setPasswordDescpriptions(passwordDescriptions);
				setPasswords.createControl((Composite)this.getShell());
				repaint();
				System.out.println("CCCCC");
				return setPasswords;
			}
			datasource.loadFiles(((ImportChooseFiles)chooseFiles).getFiles());
			List<UserAccount> uas  = datasource.getUserAccounts();
			if(uas == null) {
				return setAccounts;
			}
			else {
				((ImportChooseAccounts)chooseAccounts).setUserAccounts(uas);
				chooseAccounts.createControl((Composite)this.getShell());
				repaint();
				return chooseAccounts;
			}
		}
		if(page == setPasswords) {
			datasource.setPasswords(((ImportSetPasswords)setPasswords).getPasswords());
			datasource.loadFiles(((ImportChooseFiles)chooseFiles).getFiles());
			List<UserAccount> uas  = datasource.getUserAccounts();
			if(uas.size()>0 && uas.get(0).getUid()==null) {
				return setAccounts;
			}
			else {
				((ImportChooseAccounts)chooseAccounts).setUserAccounts(uas);
				chooseAccounts.createControl((Composite)this.getShell());
				repaint();
				return chooseAccounts;
			}
		}
		else if(page == chooseAccounts) {
			datasource.setUserAccounts(((ImportChooseAccounts)chooseAccounts).getSelectedAccounts());
			((ImportMapContacts)mapContacts).setContacts(datasource.getContacts());
			mapContacts.createControl((Composite)this.getShell());
			repaint();
			return mapContacts;
		}
		else if(page == setAccounts) {
			datasource.setUserAccounts(((ImportSetAccounts)setAccounts).getSelectedAccounts());
			((ImportMapContacts)mapContacts).setContacts(datasource.getContacts());
			mapContacts.createControl((Composite)this.getShell());
			repaint();
			return mapContacts;
		}
		return super.getNextPage(page);
	}
	
	@Override
	public boolean performFinish() {
		return false;
	}
	
	
}
