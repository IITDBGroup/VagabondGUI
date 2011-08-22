package org.vagabond.rcp.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Bundle;

public class ResourceManager {

	private static ResourceManager inst = new ResourceManager ();
	
	private Bundle bundle = null;
	
	public static ResourceManager getInstance() {
		return inst;
	}
	
	public void setBundle (Bundle bundle) {
		this.bundle = bundle;
	}
	
	public InputStream getResource(String name) throws IOException  {
		if (bundle != null)
			return bundle.getEntry(name).openStream();
		else
			return ClassLoader.getSystemResourceAsStream(name);
	}
	
	public Enumeration<String> getResources (String dir) {
		return bundle.getEntryPaths(dir);
	}
	
	public Map<String,URL> getResourcesAsStreams (String dir, String fileSuffix) {
		Enumeration<String> fileNames;
		HashMap<String,URL> ins;
		String key;
		
		ins = new HashMap<String,URL> ();
		fileNames = bundle.getEntryPaths(dir);
		
		while(fileNames.hasMoreElements()) {
			String file = fileNames.nextElement();
			if (file.endsWith(fileSuffix)) {
				key = file.substring(dir.length() + 1,file.indexOf(fileSuffix));
				ins.put(key, bundle.getEntry(file));
			}
		}
		
		return ins;
	}
}
