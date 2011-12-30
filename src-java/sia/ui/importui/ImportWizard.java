package sia.ui.importui;

import java.util.List;

import org.eclipse.jface.dialogs.IPageChangingListener;
import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import sia.datasources.DataSource;
import sia.datasources.FMADataSource;
import sia.models.UserAccount;
import sia.utils.Dictionaries;

/**
 * 
 * @author Agnieszka Glabala
 *
 */
public class ImportWizard extends Wizard implements IPageChangingListener {
	private String[] imNames;
	private int im;
	private boolean wasSetAccounts = false;
	DataSource datasource;
	IWizardPage chooseIM;
	IWizardPage chooseFiles;
	IWizardPage setPasswords;
	IWizardPage chooseAccounts;
	IWizardPage setAccounts;
	IWizardPage mapContacts;
	IWizardPage accountsLoading;
	IWizardPage messageLoading;
	IWizardPage saveLoading;
	public ImportWizard() {
		setWindowTitle("Import messages");
		
//		imNames = new String[] {"applications-accessories",  "applications-games", "applications-multimedia", "applications-science", "empathy",  "applications-development",  
//				"applications-graphics", "applications-office", "applications-system", "kadu", "applications-engineering", "applications-internet", "applications-other", 
//				"applications-utilities", "phone"};
		imNames = Dictionaries.getInstance().getDataSources().keySet().toArray(new String[] {});
		chooseIM = new ImportChooseIM(imNames);
		chooseFiles = new ImportChooseFiles();
		setPasswords = new ImportSetPasswords();
		chooseAccounts = new ImportChooseAccounts();
		setAccounts = new ImportSetAccounts();
		mapContacts = new ImportMapContacts();
		accountsLoading = new ImportLoading();
		accountsLoading.setTitle("Accounts loading");
		messageLoading = new ImportLoading();
		messageLoading.setTitle("Messages loading");
		saveLoading = new ImportLoading();
		saveLoading.setTitle("Saving");
		im=-1;
	}

	@Override
	public void addPages() {
		addPage(chooseIM);
		addPage(chooseFiles);
		addPage(setPasswords);
		addPage(accountsLoading);
		addPage(setAccounts);
		addPage(chooseAccounts);
		addPage(messageLoading);
		addPage(mapContacts);
		addPage(saveLoading);
	}
	
	@Override
	public boolean performFinish() {
		return false;
	}

	@Override
	public void handlePageChanging(PageChangingEvent event) {
		WizardDialog dialog = (WizardDialog) event.getSource();		
		if(event.getTargetPage() == chooseFiles) {
			//from chooseIM
			System.out.println("chooseFiles");
			this.datasource = Dictionaries.getInstance().getDataSource(imNames[((ImportChooseIM)chooseIM).getSelected()]);
			((ImportChooseFiles)chooseFiles).setFileExtensions(datasource.getFileExtensions());
			((ImportChooseFiles)chooseFiles).setDescriptions(datasource.getFileDescriptions());
			((ImportChooseFiles)chooseFiles).setControls();
//			Point size = this.getShell().getSize();
			this.getShell().setSize(this.getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT));
//			this.getShell().setSize(size);
//			this.getShell().layout(true, true);
		}
		else if(event.getTargetPage() == setPasswords) {
			//from chooseFiles
			System.out.println("setPasswords");
			
			((ImportSetPasswords)setPasswords).setPasswordDescpriptions(datasource.getRequiredPassword());
			if(datasource.getRequiredPassword()!=null && datasource.getRequiredPassword().length >0) {
				((ImportSetPasswords)setPasswords).setPasswordDescpriptions(datasource.getRequiredPassword());
				((ImportSetPasswords)setPasswords).setControls();
			}
			else {
				dialog.showPage(accountsLoading);
				event.doit = false;
			}
		}
		else if(event.getTargetPage() == accountsLoading) {
			System.out.println("accountsLoading");
			datasource.getUserAccounts();
		}
		else if(event.getTargetPage() == setAccounts) {
			//from chooseFiles or from setPasswords
			System.out.println("setAccounts");
			
			datasource.loadFiles(((ImportChooseFiles)chooseFiles).getFiles());
			if(datasource.getUserAccounts()!=null && datasource.getUserAccounts().size()>0 && datasource.getUserAccounts().get(0).getUid().length()>0) {
				wasSetAccounts = false;
				((ImportChooseAccounts)chooseAccounts).setUserAccounts(datasource.getUserAccounts());
				((ImportChooseAccounts)chooseAccounts).setControls();
				dialog.showPage(chooseAccounts);
				event.doit = false;
			}
			else {
				wasSetAccounts = true;
			}
		}
		else if(event.getTargetPage() == chooseAccounts) {
			//from setAccounts?
			System.out.println("chooseAccounts");
			if(wasSetAccounts) {
				dialog.showPage(mapContacts);
				event.doit = false;
			}
		}
		else if(event.getTargetPage() == messageLoading) {
			System.out.println("messageLoading");
			datasource.getContacts();
		}
		else if(event.getTargetPage() == mapContacts) {
			
			System.out.println("mapContacts");
			
		}
	}
}
