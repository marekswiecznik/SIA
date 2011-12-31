package sia.ui.importui;

import java.util.List;

import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.IPageChangingListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import sia.datasources.DataSource;
import sia.models.Contact;
import sia.utils.Dictionaries;

/**
 * 
 * @author Agnieszka Glabala
 * 
 */
public class ImportWizard extends Wizard implements IPageChangingListener, IPageChangedListener {
	private String[] imNames;
	private int im;
	private boolean wasSetAccounts = false;
	DataSource datasource;
	ImportChooseIM chooseIM;
	ImportChooseFiles chooseFiles;
	ImportSetPasswords setPasswords;
	ImportChooseAccounts chooseAccounts;
	ImportSetAccounts setAccounts;
	ImportMapContacts mapContacts;
	ImportLoading accountsLoading;
	ImportLoading messageLoading;
	ImportLoading saveLoading;
	ImportSetContacts setContacts;
	public ImportWizard() {
		setWindowTitle("Import messages");

		// imNames = new String[] {"applications-accessories",
		// "applications-games", "applications-multimedia",
		// "applications-science", "empathy", "applications-development",
		// "applications-graphics", "applications-office",
		// "applications-system", "kadu", "applications-engineering",
		// "applications-internet", "applications-other",
		// "applications-utilities", "phone"};
		imNames = Dictionaries.getInstance().getDataSources().keySet().toArray(new String[] {});
		chooseIM = new ImportChooseIM(imNames);
		chooseFiles = new ImportChooseFiles();
		setPasswords = new ImportSetPasswords();
		chooseAccounts = new ImportChooseAccounts();
		setAccounts = new ImportSetAccounts();
		mapContacts = new ImportMapContacts();
		accountsLoading = new ImportLoading("accountsLoading");
		accountsLoading.setTitle("Accounts loading");
		messageLoading = new ImportLoading("messagesLoading");
		messageLoading.setTitle("Messages loading");
		saveLoading = new ImportLoading("saveLoading");
		saveLoading.setTitle("Saving");
		setContacts = new ImportSetContacts();
		im = -1;
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
		addPage(setContacts);
		addPage(saveLoading);
	}

	@Override
	public boolean performFinish() {
		return false;
	}

	@Override
	public void handlePageChanging(PageChangingEvent event) {
		WizardDialog dialog = (WizardDialog) event.getSource();
		if (event.getTargetPage() == chooseFiles) {
			// from chooseIM
			System.out.println("chooseFiles");
			this.datasource = Dictionaries.getInstance().getDataSource(
					imNames[chooseIM.getSelected()]);
			chooseFiles.setFileExtensions(datasource.getFileExtensions());
			chooseFiles.setDescriptions(datasource.getFileDescriptions());
			chooseFiles.setControls();
			this.getShell().setSize(this.getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT));
		} else if (event.getTargetPage() == setPasswords) {
			// from chooseFiles
			System.out.println("setPasswords");
			setPasswords.setPasswordDescpriptions(datasource.getRequiredPassword());
			if (datasource.getRequiredPassword() != null && datasource.getRequiredPassword().length > 0) {
				setPasswords.setPasswordDescpriptions(datasource.getRequiredPassword());
				setPasswords.setControls();
			} else {
				dialog.showPage(accountsLoading);
				event.doit = false;
			}
		} else if (event.getTargetPage() == accountsLoading) {
			System.out.println("accountsLoading");
		} else if (event.getTargetPage() == setAccounts) {
			// from chooseFiles or from setPasswords
			System.out.println("setAccounts");
			datasource.loadFiles(chooseFiles.getFiles());
			if (datasource.getUserAccounts() != null && datasource.getUserAccounts().size() > 0
					&& datasource.getUserAccounts().get(0).getUid().length() > 0) {
				wasSetAccounts = false;
				chooseAccounts.setUserAccounts(datasource.getUserAccounts());
				chooseAccounts.setControls();
				dialog.showPage(chooseAccounts);
				event.doit = false;
			} else {
				wasSetAccounts = true;
			}
		} else if (event.getTargetPage() == chooseAccounts) {
			// from setAccounts?
			System.out.println("chooseAccounts");
			if (wasSetAccounts) {
				dialog.showPage(messageLoading);
				event.doit = false;
			}
		} else if (event.getTargetPage() == messageLoading) {
			System.out.println("messageLoading");
			mapContacts.setContacts(datasource.getContacts());
		} else if (event.getTargetPage() == mapContacts) {
			mapContacts.setControls();
			System.out.println("mapContacts");
		} else if (event.getTargetPage() == setContacts) {
			List<Contact> empty = mapContacts.getEmptyContacts();
			System.out.println("setContacts");
		} else if (event.getTargetPage() == saveLoading) {
			System.out.println("saveLoading");
		}
	}

	@Override
	public void pageChanged(PageChangedEvent event) {
		if (event.getSelectedPage() == accountsLoading) {
			datasource.getUserAccounts();
		} else if (event.getSelectedPage() == messageLoading) {
			datasource.getContacts();
		} else if (event.getSelectedPage() == saveLoading) {
			datasource.save();
		}
	}
}
