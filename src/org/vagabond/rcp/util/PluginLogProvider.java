package org.vagabond.rcp.util;

import org.apache.log4j.Logger;
import org.vagabond.util.LogProvider;

import com.quantum.log.PluginLogManager;

public class PluginLogProvider implements LogProvider {

	private PluginLogManager logManager;
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
		return logManager.getLogger(name);
	}

	@Override
	public Logger getLogger(Class clazz) {
		return logManager.getLogger(clazz.getName());
	}
}
