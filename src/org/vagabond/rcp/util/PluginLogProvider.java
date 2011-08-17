package org.vagabond.rcp.util;

import org.apache.log4j.Logger;
import org.vagabond.util.LogProvider;

import com.quantum.log.PluginLogManager;

public class PluginLogProvider implements LogProvider {

	private PluginLogManager logManager = null;
	private static PluginLogProvider instance = new PluginLogProvider();
	
	private PluginLogProvider () {
	}
	
	public void setLogManager (PluginLogManager logManager) {
		this.logManager = logManager;
	}
	
	public static PluginLogProvider getInstance () {
		return instance;
	}
	
	@Override
	public Logger getLogger(String name) {
		if (logManager == null)
			return Logger.getLogger(name);
		return logManager.getLogger(name);
	}

	@Override
	public Logger getLogger(Class clazz) {
		if (logManager == null)
			return Logger.getLogger(clazz);
		return logManager.getLogger(clazz.getName());
	}
}
