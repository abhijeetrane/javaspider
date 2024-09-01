package org.eclipse.contribution.junit.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.PropertyPage;

public class AutoTestPropertyPage extends PropertyPage {

	public AutoTestPropertyPage() {
		super();
	}


	Button fAutoTest;
	protected Control createContents(Composite parent) {
		noDefaultAndApplyButton();
		fAutoTest= new Button(parent, SWT.CHECK);
		fAutoTest.setText("Auto-test");
		try {
			fAutoTest.setSelection(getIsAutoTesting());
		} catch (CoreException e) {
		}
		return fAutoTest;
	}
	
	private static final String BUILDER_ID= "org.eclipse.contribution.junit.ui.autotest";

	private ICommand getAutoTestingBuilder() throws CoreException {
		IProjectDescription description= getProject().getDescription();
		ICommand command= description.newCommand();
		command.setBuilderName(BUILDER_ID);
		return command;
	}
	
	private boolean getIsAutoTesting() throws CoreException {
		IProjectDescription description= getProject().getDescription();
		List commands= Arrays.asList(description.getBuildSpec());
		return commands.contains(getAutoTestingBuilder());
	}

	private IResource getResource() {
		return (IResource) getElement().getAdapter(IResource.class);
	}

	public boolean performOk() {
		setIsAutoTesting(fAutoTest.getSelection());
		return true;
	}
	
	private void setIsAutoTesting(boolean on) {
		try {
			if (on)
				addBuilder();
			else
				removeBuilder();
		} catch (CoreException e) {
		}
	}

	private void addBuilder() throws CoreException {
		IProjectDescription desc= getProject().getDescription();
		List fixed= Arrays.asList(desc.getBuildSpec());
		List commands= new ArrayList(fixed);
		if (! getIsAutoTesting()) {
			commands.add(commands.size(), getAutoTestingBuilder());
			desc.setBuildSpec((ICommand[]) commands.toArray(new ICommand[0]));
			getProject().setDescription(desc, null);
		}
	}
	
	private void removeBuilder() throws CoreException {
		IProjectDescription desc= getProject().getDescription();
		List fixed= Arrays.asList(desc.getBuildSpec());
		List commands= new ArrayList(fixed);
		if (getIsAutoTesting()) {
			commands.remove(getAutoTestingBuilder());
			desc.setBuildSpec((ICommand[]) commands.toArray(new ICommand[0]));
			getProject().setDescription(desc, null);
		}
	}
	
	private IProject getProject() {
		return (IProject) getElement().getAdapter(IProject.class);
	}

}
