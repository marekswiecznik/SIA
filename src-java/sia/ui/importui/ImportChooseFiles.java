package sia.ui.importui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;


public class ImportChooseFiles extends WizardPage {

	String[] extensions;
	String[][] descriptions;
	String[] files;
	Label[] labels;
	Label[] filesLabels;
	Button[] buttons;
	Composite parent;
	/**
	 * Create the wizard.
	 */
	public ImportChooseFiles() {
		super("wizardPage");
		setTitle("Choose files");
		setDescription("Choose files:");
		extensions = new String[] {};
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		this.parent = parent;
		Composite container = null;
		if (getControl() == null) {
			container = new Composite(parent, SWT.NULL);
			container.setLayout(new GridLayout(2, false));
			
			Label lblNewLabel_2 = new Label(container, SWT.NONE);
			lblNewLabel_2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
			lblNewLabel_2.setText("To implement the canFlipToNextPage method for the first page of our wizard, \n" +
								  "we first prevent the user from moving to the next page when the page has any \n" +
								  "errors. When there are no errors, the destination and departure fields are \n" +
								  "filled, the return date is set and a mode of transport is selected, the user \n" +
								  "can move to the next page.");
		} else {
			container = (Composite)getControl();
		}
		labels = new Label[extensions.length];
		filesLabels = new Label[extensions.length];
		buttons = new Button[extensions.length];
		
		for (int i = 0; i < extensions.length; i++) {
			labels[i] = new Label(container, SWT.NONE);
			labels[i].setText(descriptions[i][0]);
			buttons[i] = new Button(container, SWT.NONE);
			buttons[i].setText("Load file");
			buttons[i].addMouseListener(new MyMouseAdapter(i));
			filesLabels[i] = new Label(container, SWT.NONE);
			filesLabels[i].setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
			filesLabels[i].setText("plik");
		}
		setControl(container);
	}
	
	protected void recreateControl() {
		//parent.;
//		Composite container = new Composite(parent, SWT.NULL);
//		setControl(container);
//		container.setLayout(new GridLayout(2, false));
//		labels = new Label[extensions.length];
//		for (int i = 0; i < extensions.length; i++) {
//			Label lblNewLabel = new Label(container, SWT.NONE);
//			lblNewLabel.setText(descriptions[i][0]);
//			
//			Button btnNewButton = new Button(container, SWT.NONE);
//			btnNewButton.setText("Load file");
//			btnNewButton.addMouseListener(new MyMouseAdapter(i));
//			labels[i] = new Label(container, SWT.NONE);
//			labels[i].setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
//			labels[i].setText("plik");
//		}
//		
//		Label lblNewLabel_2 = new Label(container, SWT.NONE);
//		lblNewLabel_2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
//		lblNewLabel_2.setText("To implement the canFlipToNextPage method for the first page of our wizard, \n" +
//							  "we first prevent the user from moving to the next page when the page has any \n" +
//							  "errors. When there are no errors, the destination and departure fields are \n" +
//							  "filled, the return date is set and a mode of transport is selected, the user \n" +
//							  "can move to the next page.");
	}
	protected void setFileExtensions(String[] extensions) {
		this.extensions = extensions;
		files = new String[extensions.length];
	}
	protected void setDescriptions(String[][] descriptions) {
		this.descriptions = descriptions;
	}
	private class MyMouseAdapter extends MouseAdapter {
		int n; //number of file
		MyMouseAdapter(int n) {this.n = n;}

		@Override
		public void mouseDown(MouseEvent arg0) {
//			FileDialog dlg = new FileDialog(getShell(), SWT.OPEN);

//			dlg.setFilterNames(new String[] { "OpenOffice.org Spreadsheet Files (*.sxc)",
//		        "Microsoft Excel Spreadsheet Files (*.xls)", "Comma Separated Values Files (*.csv)",
//		        "All Files (*.*)" });

//			dlg.setFilterExtensions(new String[] { extensions[n] });
//			String fileName = dlg.open();
//		    if (fileName != null) {
//		      System.out.println(fileName);
//		      labels[n].setText(fileName);
//		    }
		}
	}
	
}
