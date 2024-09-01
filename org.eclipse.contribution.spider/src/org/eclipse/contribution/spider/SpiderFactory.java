package org.eclipse.contribution.spider;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class SpiderFactory implements IPerspectiveFactory {
	public SpiderFactory() {
	}

	public void createInitialLayout(IPageLayout layout)  {
		String editorArea = layout.getEditorArea();
		
		IFolderLayout folder= layout.createFolder("left", IPageLayout.LEFT, (float)0.25, editorArea); //$NON-NLS-1$
		folder.addView("org.eclipse.contribution.spider.navigator.SpiderNavigatorView");
		folder.addView(IPageLayout.ID_OUTLINE);
		folder.addPlaceholder(IPageLayout.ID_RES_NAV);
		
		IFolderLayout outputfolder= layout.createFolder("top", IPageLayout.TOP, (float)0.75, editorArea); //$NON-NLS-1$
		outputfolder.addView("org.eclipse.contribution.spider.view");
		outputfolder.addPlaceholder("org.eclipse.search.SearchResultView");
		outputfolder.addPlaceholder("org.eclipse.debug.ui.ConsoleView");
	}
}
