package sia.ui.importui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.IPageChangingListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.sormula.SormulaException;

import sia.datasources.DataSource;
import sia.models.Contact;
import sia.ui.SIA;
import sia.utils.Dictionaries;

/**
 * 
 * @author Agnieszka Glabala
 */
public class ImportWizard extends Wizard implements IPageChangingListener, IPageChangedListener, SelectionListener,
		ModifyListener {
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
	ImportSummary summary;
	List<Contact> contacts;
	private Thread currentThread;
	private Loader loader;

	public ImportWizard() {
		setWindowTitle("Import messages");
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
		summary = new ImportSummary();
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
		addPage(summary);
		addPage(saveLoading);
		chooseIM.setPageComplete(false);
		saveLoading.setPageComplete(false);
	}

	public boolean validatePage(IWizardPage page) {
		try {
			((WizardPage) page).setErrorMessage(null);
			if (page == chooseIM) {
				if (chooseIM.getSelected() == -1) {
					chooseIM.setErrorMessage("Choose an application");
					return false;
				}
			} else if (page == chooseFiles) {
				String msg = datasource.validateFiles(chooseFiles.getFiles());
				if (msg != null) {
					chooseFiles.setErrorMessage(msg);
					return false;
				}
			} else if (page == setPasswords) {
				String msg = datasource.validatePasswords(setPasswords.getPasswords());
				if (msg != null) {
					setPasswords.setErrorMessage(msg);
					return false;
				}
			} else if (page == accountsLoading) {
				if (datasource.getProgress(DataSource.Progress.USER_ACCOUNTS_PROGRESS) != 100) {
					accountsLoading.setErrorMessage("You can't go to the next page until accounts are loaded.");
					return false;
				}
			} else if (page == setAccounts) {
				String msg = datasource.validateUid(setAccounts.getUserAccounts().get(0).getUid());
				if (msg != null) {
					setAccounts.setErrorMessage(msg);
					return false;
				}
			} else if (page == chooseAccounts) {
				if (chooseAccounts.getUserAccounts().isEmpty()) {
					chooseAccounts.setErrorMessage("You have to select at least one account.");
					return false;
				}
			} else if (page == messageLoading) {
				if (datasource.getProgress(DataSource.Progress.CONTACTS_PROGRESS) != 100) {
					messageLoading
							.setErrorMessage("You can't go to the next page until contacts and messages are loaded.");
					return false;
				}
			} else if (page == mapContacts) {
			} else if (page == setContacts) {
				List<ImportSetContacts.Controls> controls = setContacts.getControls();
				String uid;
				for (ImportSetContacts.Controls ctls : controls) {
					if (ctls.name.getVisible() && ctls.name.getEnabled()) {
						if (ctls.name.getText().trim().isEmpty()) {
							setContacts.setErrorMessage("Contact with uid "+ctls.uids[0].getText()+" has empty name. This field is required.");
							return false;
						}
						for (Contact c : contacts) {
							if (ctls.name.getText().toLowerCase().trim().equals(c.getName().toLowerCase())) {
								setContacts
										.setErrorMessage("A contact with name \""
												+ ctls.name.getText()
												+ "\" already exists. Connect one to another on previous page or change its name.");
								return false;
							}
						}
						for (ImportSetContacts.Controls ctls2 : controls) {
							if (ctls != ctls2 && ctls2.getVisible() && ctls2.name.getEnabled()
									&& ctls.name.getText().equals(ctls2.name.getText())) {
								setContacts.setErrorMessage("Two contacts can't have the same name ("
										+ ctls.name.getText() + "). Connect one to another or change name.");
								return false;
							}
						}
					}
					uid = ctls.getSelectedItem();
					if (ctls.name.isVisible() && uid != null) {
						for (int i = 0; i < datasource.getContacts().size(); i++) {
							if (datasource.getContacts().get(i).getContactAccounts().get(0).getUid().equals(uid)
									&& controls.get(i).getSelectedItem() != null) {
								setContacts.setErrorMessage("Chained contact merging disallowed. Contact with UID "
										+ ctls.uids[0].getText() + " can't be attached to contact with UID " + uid
										+ ", because it's already attached to another contact.");
								return false;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			SIA.getInstance().handleException("An unexpected abort occured when validating wizard.", e);
		} finally {
			((WizardPage) page).setPageComplete(((WizardPage) page).getErrorMessage() == null);
			page.canFlipToNextPage();
		}
		return true;
	}

	@Override
	public boolean performCancel() {
		cancelCurrentAction();
		return super.performCancel();
	}

	@Override
	public boolean performFinish() {
		if (datasource == null || datasource.getProgress(DataSource.Progress.SAVE_PROGRESS) != 100) {
			return false;
		}
		try {
			Dictionaries.getInstance().loadContacts();
		} catch (SormulaException e) {
			SIA.getInstance().handleException("An abort occured when reloading contacts.", e);
		}
		return true;
	}

	@Override
	public void handlePageChanging(PageChangingEvent event) {
		WizardDialog dialog = (WizardDialog) event.getSource();
		WizardPage current = (WizardPage) event.getCurrentPage();
		WizardPage target = (WizardPage) event.getTargetPage();
		target.setErrorMessage(null);
		if (current.getNextPage().equals(target) && !validatePage(current)) {
			event.doit = false;
			return;
		}
		if (current instanceof ImportLoading && current.getPreviousPage().equals(target)) {
			cancelCurrentAction();
		}
		try {
			if (target == chooseFiles) {
				int previousIm = im;
				im = chooseIM.getSelected();
				this.datasource = Dictionaries.getInstance().getDataSource(imNames[im]);
				//TODO: consider moving initParser into another thread or making a loader
				datasource.initParser();
				if (previousIm != im) {
					if (datasource.getRequiredPassword() != null && datasource.getRequiredPassword().length > 0) {
						setPasswords.setPasswordDescpriptions(datasource.getRequiredPassword());
						setPasswords.setControls();
					}
				}
				if (datasource.getFileExtensions() == null || datasource.getFileExtensions().length == 0) {
					dialog.showPage(setPasswords);
					event.doit = false;
				} else if (previousIm != im) {
					chooseFiles.setFileExtensions(datasource.getFileExtensions());
					chooseFiles.setDescriptions(datasource.getFileDescriptions());
					chooseFiles.setControls();
					chooseFiles.setPageComplete(false);
					chooseFiles.canFlipToNextPage();
					updateSize();
				}
			} else if (target == setPasswords) {
				if (datasource.getRequiredPassword() != null && datasource.getRequiredPassword().length > 0) {
					setPasswords.setPageComplete(false);
					setPasswords.canFlipToNextPage();
				} else {
					dialog.showPage(accountsLoading);
					event.doit = false;
				}
			} else if (target == accountsLoading) {
			} else if (target == setAccounts) {
				if (!wasSetAccounts) {
					if (datasource.getUserAccounts() != null && datasource.getUserAccounts().size() > 0
							&& datasource.getUserAccounts().get(0).getUid().length() > 0) {
						chooseAccounts.setUserAccounts(datasource.getUserAccounts());
						chooseAccounts.setControls();
						chooseAccounts.setPageComplete(false);
						chooseAccounts.canFlipToNextPage();
						dialog.showPage(chooseAccounts);
						event.doit = false;
					} else {
						setAccounts.setUserAccounts(datasource.getUserAccounts());
						setAccounts.setControls();
						setAccounts.setPageComplete(false);
						setAccounts.canFlipToNextPage();
						wasSetAccounts = true;
					}
				}
			} else if (target == chooseAccounts) {
				if (wasSetAccounts) {
					dialog.showPage(current == messageLoading ? setAccounts : messageLoading);
					event.doit = false;
				}
			} else if (target == messageLoading) {
			} else if (target == mapContacts) {
				if (datasource.getContacts().size() == 0) {
					dialog.showPage(saveLoading);
					event.doit = false;
				}
				updateSize();
			} else if (target == setContacts) {
				validatePage(setContacts);
				setContacts.layout();
			} else if (target == summary) {
				List<Contact> cloneContacts =new ArrayList<Contact>();
				for (Contact c : contacts) {
					cloneContacts.add(c.clone());
				}
				List<Contact> cloneParsedContacts =new ArrayList<Contact>();
				for (Contact c : datasource.getContacts()) {
					cloneParsedContacts.add(c.clone());
				}
				setContacts.setParsedContacts(cloneParsedContacts);
				mapContacts.setParsedContacts(cloneParsedContacts);
				setContacts.setContacts(cloneContacts);
				mapContacts.setContacts(cloneContacts);
				mapContacts.addContactAccounts();
				setContacts.addNewContacts();
				summary.setContacts(cloneContacts);
				summary.setControls();
				setContacts.setContacts(contacts);
				mapContacts.setContacts(contacts);
				setContacts.setParsedContacts(datasource.getContacts());
				mapContacts.setParsedContacts(datasource.getContacts());
			} else if (target == saveLoading) {
			}
		} catch (Exception e) {
			SIA.getInstance().handleException("An unexpected abort occured.", e);
		}
	}

	private void updateSize() {
		Point newSize = this.getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Rectangle r = this.getShell().getDisplay().getBounds();
		if (newSize.y > r.height - 32)
			newSize.y = r.height - 32;
		if (newSize.x > r.width - 32)
			newSize.x = r.width - 32;
		this.getShell().setSize(newSize);
	}

	@Override
	public void pageChanged(PageChangedEvent event) {
		try {
			if (event.getSelectedPage() == accountsLoading) {
				if (datasource.getRequiredPassword() != null && datasource.getRequiredPassword().length > 0) {
					datasource.setPasswords(setPasswords.getPasswords());
				}
				currentThread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							datasource.loadFiles(chooseFiles.getFiles());
							datasource.getUserAccounts();
						} catch (Exception e) {
							if (loader != null) {
								loader.cancel();
							}
							SIA.getInstance().handleException("Loading files and parsing user accounts failed.", e);
							getShell().getDisplay().asyncExec(new Runnable() {
								public void run() {
									accountsLoading.setErrorMessage("Loading files and parsing user accounts failed.");
								}
							});
						}
					}
				});
				currentThread.start();
				loader = new Loader(DataSource.Progress.USER_ACCOUNTS_PROGRESS, accountsLoading);
				new Thread(loader).start();
			} else if (event.getSelectedPage() == messageLoading) {
				if (wasSetAccounts) {
					datasource.setUserAccounts(setAccounts.getUserAccounts());
				} else {
					datasource.setUserAccounts(chooseAccounts.getUserAccounts());
				}
				currentThread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							if (datasource.getContacts() != null) {
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
						} catch (Exception e) {
							if (loader != null) {
								loader.cancel();
							}
							SIA.getInstance().handleException("Retrieving contacts and conversations failed.", e);
							getShell().getDisplay().asyncExec(new Runnable() {
								public void run() {
									messageLoading.setErrorMessage("Retrieving contacts and conversations failed.");
								}
							});
						}
					}
				});
				currentThread.start();
				loader = new Loader(DataSource.Progress.CONTACTS_PROGRESS, messageLoading);
				new Thread(loader).start();
			} else if (event.getSelectedPage() == saveLoading) {
				mapContacts.addContactAccounts(); // merge new ContactAccounts with existing Contacts
				setContacts.addNewContacts();
				currentThread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							datasource.save(contacts);
						} catch (Exception e) {
							if (loader != null) {
								loader.cancel();
							}
							try {
								SIA.getInstance().cleanup();
								SIA.getInstance().updateConversations(datasource.getUserAccounts());
							} catch (Exception e1) {
								SIA.getInstance().handleException("Unexpected abort on saving data into database and during clean-up. This is a critical abort, so application will be closed now.", e1);
								SIA.getInstance().close(null);
							}
							SIA.getInstance().handleException("Unexpected abort on saving data into database.", e);
							getShell().getDisplay().asyncExec(new Runnable() {
								public void run() {
									saveLoading.setErrorMessage("Unexpected abort on saving data into database.");
								}
							});
						} 
					}
				});
				currentThread.start();
				loader = new Loader(DataSource.Progress.SAVE_PROGRESS, saveLoading);
				new Thread(loader).start();
			}
		} catch (Exception e) {
			SIA.getInstance().handleException("An unexpected abort occured on page changing.", e);
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		validatePage(this.getContainer().getCurrentPage());
	}

	@Override
	public void modifyText(ModifyEvent e) {
		validatePage(this.getContainer().getCurrentPage());
	}

	/**
	 * Cancel current action
	 */
	private void cancelCurrentAction() {
		if (datasource != null) {
			datasource.stopParserCurrentAction();
			if (loader != null) {
				loader.cancel();
			}
			if (currentThread != null) {
				currentThread.interrupt();
			}
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
		private boolean cancel;

		/**
		 * Default and only constructor
		 * 
		 * @param progress
		 *            progress percent
		 * @param page
		 *            loading page
		 */
		public Loader(DataSource.Progress progress, ImportLoading page) {
			this.progress = progress;
			this.page = page;
			this.cancel = false;
		}
		
		public void cancel() {
			this.cancel = true;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
			int value = 0;
			getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					page.setPageComplete(false);
					page.canFlipToNextPage();
				}
			});
			while (value < 100 && !cancel) {
				value = datasource.getProgress(progress);
				final int valueToSet = value;
				getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						page.setProgress(valueToSet);
					}
				});
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// Silent like a ninja
				}
			}
			//TODO: fix.
			if (getShell() != null && !cancel) {
				getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						page.setPageComplete(true);
						page.canFlipToNextPage();
					}
				});
			}
			datasource.stopParserCurrentAction();
		}
	}
}
