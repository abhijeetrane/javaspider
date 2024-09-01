/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Erich Gamma (erich_gamma@ch.ibm.com) and
 * 	   Kent Beck (kent@threeriversinstitute.org)
 *******************************************************************************/
package org.eclipse.contribution.junit.internal.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;

public class AutoTestNature implements IProjectNature {
	IProject project;
	
	public AutoTestNature() {
	}

	public void configure() throws CoreException {
		IProjectDescription description= getProject().getDescription();
		ICommand[] commands= description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i)
			if (commands[i].getBuilderName().equals(JUnitPlugin.AUTO_TEST_BUILDER)) 
				return;
		
		ICommand command= description.newCommand();
		command.setBuilderName(JUnitPlugin.AUTO_TEST_BUILDER);
		ICommand[] newCommands= new ICommand[commands.length + 1];
		System.arraycopy(commands, 0, newCommands, 0, commands.length);
		newCommands[newCommands.length-1]= command;
		description.setBuildSpec(newCommands);
		getProject().setDescription(description, null);
	}

	public void deconfigure() throws CoreException {
		IProjectDescription description = getProject().getDescription();
		ICommand[] commands = description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i) {
			if (commands[i].getBuilderName().equals(JUnitPlugin.AUTO_TEST_BUILDER)) {
				ICommand[] newCommands = new ICommand[commands.length - 1];
				System.arraycopy(commands, 0, newCommands, 0, i);
				System.arraycopy(commands, i + 1, newCommands, i, commands.length - i - 1);
				description.setBuildSpec(newCommands);
				getProject().setDescription(description, null);
				return;
			}
		}
	}

	public IProject getProject()  {
		return project;
	}

	public void setProject(IProject project)  {
		this.project= project;
	}
	
}
