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
package org.eclipse.contribution.junit.internal.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.contribution.junit.internal.core.JUnitPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class TestReportLabelProvider extends LabelProvider implements ITableLabelProvider, IColorProvider {
	Image images[];
	
	public TestReportLabelProvider() {
		images= new Image[2];
		images[TestResult.OK]= createImage("icons/testok.gif"); //$NON-NLS-1$
		images[TestResult.FAILED]= createImage("icons/testerr.gif"); //$NON-NLS-1$
	}
	
	private static Image createImage(String path) {
		URL url= JUnitPlugin.getPlugin().getDescriptor().getInstallURL();
		ImageDescriptor descriptor= null;
		try {
			descriptor= ImageDescriptor.createFromURL(new URL(url, path));
		} catch (MalformedURLException e) {
			descriptor= ImageDescriptor.getMissingImageDescriptor();
		}
		return descriptor.createImage();  
	}

	public String getColumnText(Object element, int columnIndex) {
		TestResult result= (TestResult)element;
		switch (columnIndex) {
		case 0:
			return result.method+" - "+result.klass; //$NON-NLS-1$
		case 1:
			return Long.toString(result.testDuration());
		}
		return null;
	}

	public Image getColumnImage(Object element, int columnIndex) { 
		if (columnIndex == 0) 
			return images[((TestResult)element).status];
		return null;
	}

	public void dispose() {
		for (int i= 0; i < images.length; i++) 
			images[i].dispose();
	}

	public Color getForeground(Object element) {
		if (((TestResult)element).isFailure())		
			return Display.getDefault().getSystemColor(SWT.COLOR_RED);
		return null;
	}

	public Color getBackground(Object element) {
		return null;
	}
}