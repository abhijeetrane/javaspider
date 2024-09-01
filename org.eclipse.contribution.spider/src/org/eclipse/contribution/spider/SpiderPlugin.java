package org.eclipse.contribution.spider;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * The plug-in runtime class for the JUnit plug-in.
 */
public class SpiderPlugin extends AbstractUIPlugin {
	/**
	 * The single instance of this plug-in runtime class.
	 */
	private static SpiderPlugin fgPlugin= null;

	private static URL fgIconBaseURL;

	public SpiderPlugin(IPluginDescriptor desc) {
		super(desc);
		fgPlugin= this;
		String pathSuffix= "icons/"; 
		try {
			fgIconBaseURL= new URL(getDescriptor().getInstallURL(), pathSuffix);
		} catch (MalformedURLException e) {
			// do nothing
		}
	}

	public static SpiderPlugin getDefault() {
		return fgPlugin;
	}

	public static ImageDescriptor getImageDescriptor(String relativePath) {
		try {
			return ImageDescriptor.createFromURL(new URL(SpiderPlugin.fgIconBaseURL, relativePath));
		} catch (MalformedURLException e) {
			// should not happen
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}
}
