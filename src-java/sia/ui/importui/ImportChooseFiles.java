package sia.ui.importui;

import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;

import sia.models.UserAccount;

public class ImportChooseFiles extends WizardPage {

	String[] extensions;
	String[][] descriptions;
	String[] files;
	Label[] labels;
	Label[] fileLabels;
	Label descriptionLabel;
	Button[] buttons;
	/**
	 * Create the wizard.
	 */
	public ImportChooseFiles() {
		super("chooseFiles");
		setTitle("Choose files");
		setDescription("Choose files:");
		extensions = new String[] {};
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		System.out.println("FFF");
		Composite container = null;
		if (getControl() == null) {
			container = new Composite(parent, SWT.NONE);
			container.setLayout(new GridLayout(1, false));	
		} else {
			container = (Composite)getControl();
		}
		for (Control c : container.getChildren())
			c.dispose();
		labels = new Label[extensions.length];
		fileLabels = new Label[extensions.length];
		buttons = new Button[extensions.length];
		
		for (int i = 0; i < extensions.length; i++) {
			labels[i] = new Label(container, SWT.NONE);
			labels[i].setText(descriptions[i][0]);
			labels[i].addMouseTrackListener(new MyMouseTrackAdapter(i));
			buttons[i] = new Button(container, SWT.NONE);
			buttons[i].setText("Load file");
			buttons[i].addSelectionListener(new MySelectionAdapter(i));
			buttons[i].addMouseTrackListener(new MyMouseTrackAdapter(i));
			fileLabels[i] = new Label(container, SWT.NONE);
			fileLabels[i].setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 2, 1));
			fileLabels[i].setText("Choose file");
		}
		
		descriptionLabel = new Label(container, SWT.NONE);
		descriptionLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		descriptionLabel.setText("Move your cursor to one of buttons to see little hint where you can find those files.");
		
		setControl(container);
	}
	
	protected void setFileExtensions(String[] extensions) {
		this.extensions = extensions;
		files = new String[extensions.length];
	}
	
	protected void setDescriptions(String[][] descriptions) {
		this.descriptions = descriptions;
	}
	
	protected String[] getFiles() {
		return files;
	}
	
	private class MyMouseTrackAdapter extends MouseTrackAdapter {
		int n; //number of file
		public MyMouseTrackAdapter(int n) { this.n = n; }
		@Override
		public void mouseEnter(MouseEvent e) {
			descriptionLabel.setText(descriptions[n][1]);
		    //TODO find wiser way to resize label after setText
			descriptionLabel.setSize(descriptions[n][1].length()*7, descriptions[n][1].length()*70/300);
		}
		
		@Override
		public void mouseExit(MouseEvent e) {
		}
	}
	
	private class MySelectionAdapter extends SelectionAdapter {
		int n; //number of file
		MySelectionAdapter(int n) {this.n = n;}

		@Override
		public void widgetSelected(SelectionEvent e) {
			FileDialog dlg = new FileDialog(getShell(), SWT.OPEN);
			dlg.setFilterNames(new String[] {extensions[n]});

			dlg.setFilterExtensions(new String[] { extensions[n] });
			String fileName = dlg.open();
		    if (fileName != null) {
		    	files[n]=fileName;
		    	fileLabels[n].setText(fileName);
		    	//TODO find wiser way to resize label after setText
		    	fileLabels[n].setSize(fileName.length()*7, fileLabels[n].getSize().y);
		    }
		}
	}
	
}
