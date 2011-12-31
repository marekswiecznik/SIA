package sia.ui.importui;

import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;

import sia.models.UserAccount;

public class ImportChooseFiles extends WizardPage {

	private String[] extensions;
	private String[][] descriptions;
	private String[] files;
	private Label[] labels;
	private Label[] fileLabels;
	private Label descriptionLabel;
	private Button[] buttons;
	private Label[] separators;
	private Composite container;
	private ScrolledComposite scrolledComposite;
	private int width, height;
	/**
	 * Create the wizard.
	 */
	public ImportChooseFiles() {
		super("chooseFiles");
		setMessage("");
		setTitle("Choose files");
		setDescription("Choose files:");
		extensions = new String[] {};
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.V_SCROLL);
		setControl(scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		Composite container = new Composite(scrolledComposite, SWT.NONE);
		container.setLayout(new GridLayout(2, false));	
		scrolledComposite.setContent(container);
		scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		this.container = container;
	}
	
	public void setControls() {
		for (Control c : container.getChildren())
			c.dispose();
		labels = new Label[extensions.length];
		fileLabels = new Label[extensions.length];
		buttons = new Button[extensions.length];
		separators = new Label[extensions.length];
		
		for (int i = 0; i < extensions.length; i++) {
			labels[i] = new Label(container, SWT.NONE);
			labels[i].setText(descriptions[i][0]);
			labels[i].addMouseTrackListener(new MyMouseTrackAdapter(i));
			buttons[i] = new Button(container, SWT.NONE);
			buttons[i].setText("Load file");
			buttons[i].addSelectionListener(new MySelectionAdapter(i));
			buttons[i].addMouseTrackListener(new MyMouseTrackAdapter(i));
			buttons[i].setImage(sia.ui.org.eclipse.wb.swt.SWTResourceManager.getImage(ImportChooseIM.class, "/sia/ui/resources/open.png"));
			fileLabels[i] = new Label(container, SWT.NONE);
			fileLabels[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
			fileLabels[i].setText("Choose file");
			separators[i] = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
			separators[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		}
		
		descriptionLabel = new Label(container, SWT.WRAP);
		descriptionLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		descriptionLabel.setText("Move your cursor.");
		
		container.layout();
		scrolledComposite.setContent(container);
		scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
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
			//container.update();
			//scrolledComposite.update();
			//scrolledComposite.setContent(container);
			//scrolledComposite.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			//width = Math.max(width, getShell().computeSize(getShell().getSize().x, SWT.DEFAULT).x);
			Point size = getShell().computeSize(getShell().getSize().x, SWT.DEFAULT);
			size.x = getShell().getSize().x;
			if (size.y > height)
				getShell().setSize(size);
			//width = getShell().getSize().x;
			height = getShell().getSize().y;
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
		    }
		}
	}
	
}
