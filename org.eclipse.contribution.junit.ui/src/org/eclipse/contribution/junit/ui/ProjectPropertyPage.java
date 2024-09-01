package org.eclipse.contribution.junit.ui;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;

public class ProjectPropertyPage extends PropertyPage implements IWorkbenchPropertyPage {
					
	private String fFilterString;
	private Table fTable;
	private Button fAddButton;
	private Button fRemoveButton;
	private Button fIsAutoTest;
	
	public ProjectPropertyPage() {
		super();
	}

	protected Control createContents(Composite parent) {
		noDefaultAndApplyButton();
		
		Composite composite= new Composite(parent, SWT.NULL);
		GridLayout layout= new GridLayout();
		layout.numColumns= 3;
		layout.marginWidth= 0;
		layout.marginHeight= 0;
		composite.setLayout(layout);
		GridData data= new GridData();
		data.verticalAlignment= GridData.FILL;
		data.horizontalAlignment= GridData.FILL;
		composite.setLayoutData(data);
		
		createShowCheck(composite);

		return composite;
	}

	private void createShowCheck(Composite composite) {
		boolean isAutoTest= false;
		GridData data;
		fIsAutoTest= new Button(composite, SWT.CHECK);
		fIsAutoTest.setText("Auto-test");
		data= new GridData();
		data.horizontalAlignment= GridData.FILL;
		data.horizontalSpan= 2;
		fIsAutoTest.setLayoutData(data);		
		setWidgetValues(isAutoTest);
	}

	private void setWidgetValues(boolean isAutoTest) {
		try {
			String persistentProperty= getProject().getPersistentProperty(getIsAutoTestName());
			isAutoTest= Boolean.valueOf(persistentProperty).booleanValue();
		} catch (CoreException e) {
		}
		fIsAutoTest.setSelection(isAutoTest);
	}

	public void init(IWorkbench workbench) {
	}
	
	public boolean performOk() {
		try {
			getProject().setPersistentProperty(getIsAutoTestName(), Boolean.toString(fIsAutoTest.getSelection()));
		} catch (CoreException e) {
			ErrorDialog.openError(getShell(), "Problem Setting Property", "", e.getStatus());
			return true; // Otherwise the user would be stuck. I hate that.
		}
		return true;
	}

	private QualifiedName getIsAutoTestName() {
		return new QualifiedName("org.eclipse.contribution.junit", "isAutoTest");
	}

	public IProject getProject() {
		return (IProject) getElement().getAdapter(IResource.class);
	}

}
