package org.eclipse.contribution.spider.navigator;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

class NavigatorLabelProvider extends LabelProvider {

	public String getText(Object object) {
		if (object instanceof String) 
			return (String) object;
		if (object instanceof MenuItem) {
			MenuItem mi= (MenuItem)object;
			String text= mi.getText();
			if ((mi.getStyle() & SWT.SEPARATOR) != 0)
				return "---";
			return text;
		}
		return object.getClass().getName();
	}
	
	public Image getImage(Object obj) {
		String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
		if (obj instanceof String)
		   imageKey = ISharedImages.IMG_OBJ_FOLDER;
		return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
	}
}