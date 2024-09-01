package org.eclipse.contribution.spider.navigator;

import java.net.URL;
import java.util.Map;

import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IPluginRegistry;
import org.eclipse.core.runtime.Plugin;


public class Platform {

	public IAdapterManager getAdapterManager() {
		return org.eclipse.core.runtime.Platform.getAdapterManager();
	}
	
	public Map getAuthorizationInfo(URL serverUrl, String realm, String authScheme) {
		return org.eclipse.core.runtime.Platform.getAuthorizationInfo(serverUrl, realm, authScheme);
	}
	public String[] getCommandLineArgs() {
		return org.eclipse.core.runtime.Platform.getCommandLineArgs();
	}
	
	public String getDebugOption(String option) {
		return org.eclipse.core.runtime.Platform.getDebugOption(option);
	}
	
	public IPath getLocation() {
		return org.eclipse.core.runtime.Platform.getLocation();
	}

	public IPath getLogFileLocation() {
		return org.eclipse.core.runtime.Platform.getLogFileLocation();
	}

	public Plugin getPlugin(String id) {
		return org.eclipse.core.runtime.Platform.getPlugin(id);
	}

	public IPluginRegistry getPluginRegistry() {
		return org.eclipse.core.runtime.Platform.getPluginRegistry();
	}
}
