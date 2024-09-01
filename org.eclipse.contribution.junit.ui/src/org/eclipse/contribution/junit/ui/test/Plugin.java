package org.eclipse.contribution.junit.ui.test;

import org.eclipse.core.runtime.IPluginDescriptor;

public class Plugin extends org.eclipse.core.runtime.Plugin {
	private static Plugin plugin;

	public Plugin(IPluginDescriptor descriptor) {
		super(descriptor);
		plugin= this;
	}
	
	public static Plugin getDefault() {
		return plugin;
	}
}
