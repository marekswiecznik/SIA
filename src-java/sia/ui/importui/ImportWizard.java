package sia.ui.importui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.IPageChangingListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.sormula.SormulaException;

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
	List<Contact> contacts;
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
		List<Contact> tmpContacts = Dictionaries.getInstance().getContacts();
		contacts = new ArrayList<Contact>();
		for (int i = 0; i < tmpContacts.size(); i++) {
			contacts.add(tmpContacts.get(i).clone());
		}
		mapContacts.setNextPage(setContacts);
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
			im = chooseIM.getSelected();
			this.datasource = Dictionaries.getInstance().getDataSource(imNames[im]);
			chooseFiles.setFileExtensions(datasource.getFileExtensions());
			chooseFiles.setDescriptions(datasource.getFileDescriptions());
			chooseFiles.setControls();
			this.getShell().setSize(this.getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT));
		} else if (event.getTargetPage() == setPasswords) {
			setPasswords.setPasswordDescpriptions(datasource.getRequiredPassword());
			if (datasource.getRequiredPassword() != null && datasource.getRequiredPassword().length > 0) {
				setPasswords.setPasswordDescpriptions(datasource.getRequiredPassword());
				setPasswords.setControls();
			} else {
				dialog.showPage(accountsLoading);
				event.doit = false;
			}
		} else if (event.getTargetPage() == accountsLoading) {
		} else if (event.getTargetPage() == setAccounts) {
			if (datasource.getUserAccounts() != null && datasource.getUserAccounts().size() > 0
					&& datasource.getUserAccounts().get(0).getUid().length() > 0) {
				wasSetAccounts = false;
				chooseAccounts.setUserAccounts(datasource.getUserAccounts());
				chooseAccounts.setControls();
				dialog.showPage(chooseAccounts);
				event.doit = false;
			} else {
				setAccounts.setUserAccounts(datasource.getUserAccounts());
				setAccounts.setControls();
				wasSetAccounts = true;
			}
		} else if (event.getTargetPage() == chooseAccounts) {
			if (wasSetAccounts) {
				dialog.showPage(messageLoading);
				event.doit = false;
			}
		} else if (event.getTargetPage() == messageLoading) {
		} else if (event.getTargetPage() == mapContacts) {
			if(datasource.getContacts().size()==0) {
				dialog.showPage(saveLoading);
				event.doit = false;
			}
		} else if (event.getTargetPage() == setContacts) {
			setContacts.layout();
		} else if (event.getTargetPage() == saveLoading) {
		}
	}

	@Override
	public void pageChanged(PageChangedEvent event) {
		if (event.getSelectedPage() == accountsLoading) {
<<<<<<< HEAD
			new Thread(new Runnable() {
				@Override
				public void run() {
					datasource.loadFiles(chooseFiles.getFiles());
					datasource.getUserAccounts();
				}
			}).start();
			new Thread(new Loader(DataSource.Progress.USER_ACCOUNTS_PROGRESS, accountsLoading)).start();
=======
			datasource.loadFiles(chooseFiles.getFiles());
			datasource.getUserAccounts();
			if(datasource.getRequiredPassword()!=null && datasource.getRequiredPassword().length>0) {
				datasource.setPasswords(setPasswords.getPasswords());
			}
>>>>>>> bfbcb9a794fc190fddd49fbb9ba8974720b6ceee
		} else if (event.getSelectedPage() == messageLoading) {
			if (wasSetAccounts) {
				datasource.setUserAccounts(setAccounts.getUserAccounts());
			} else {
				datasource.setUserAccounts(chooseAccounts.getUserAccounts());
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					datasource.getContacts();
					datasource.mapContacts(contacts);
					mapContacts.setParsedContacts(datasource.getContacts());
					mapContacts.setContacts(contacts);
					setContacts.setParsedContacts(datasource.getContacts());
					setContacts.setContacts(contacts);
					getShell().getDisplay().asyncExec(new Runnable() {
						public void run() {
							mapContacts.setControls();
							setContacts.setControls();
						}
					});
				}
			}).start();
			new Thread(new Loader(DataSource.Progress.CONTACTS_PROGRESS, messageLoading)).start();
		} else if (event.getSelectedPage() == saveLoading) {
			mapContacts.addContactAccounts(); //merge new ContactAccounts with existing Contacts
			setContacts.addNewContacts();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						datasource.save(contacts);
					} catch (SQLException e) {
						// TODO catch block
						MessageDialog.openError(getShell(), "SQLException", e.getMessage());
						e.printStackTrace();
					} catch (SormulaException e) {
						// TODO catch block
						MessageDialog.openError(getShell(), "SormulaException", e.getMessage());
						e.printStackTrace();
					}
				}
			}).start();
			new Thread(new Loader(DataSource.Progress.SAVE_PROGRESS, saveLoading)).start();
		}
	}
	
	/**
	 * Loader. 
	 * 
	 * Updates specified operation progress
	 * 
	 * @author jumper
	 */
	class Loader implements Runnable {
		private DataSource.Progress progress;
		private ImportLoading page; 
		
		/**
		 * Default and only constructor
		 * @param progress progress percent
		 * @param page loading page
		 */
		public Loader(DataSource.Progress progress, ImportLoading page) {
			this.progress = progress;
			this.page = page;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			int value = 0;
			while (value < 100) {
				value = datasource.getProgress(progress);
				final int valueToSet = value;
				getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						page.setProgress(valueToSet);
					}
				});
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {/*TODO: logger*/}
			}
		} 
	}
}
