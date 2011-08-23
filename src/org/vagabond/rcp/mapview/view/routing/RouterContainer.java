package org.vagabond.rcp.mapview.view.routing;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.ConnectionRouter;

public class RouterContainer {

	private static RouterContainer instance = new RouterContainer ();
	
	private Map<String, ConnectionRouter> routers;
	
	private RouterContainer () {
		routers = new HashMap<String, ConnectionRouter> ();
	}
	
	public static RouterContainer getInstance() {
		return instance;
	}
	
	public ConnectionRouter getRouter (String name) {
		return routers.get(name);
	}
	
	public void registerRouter (String name, ConnectionRouter conn) {
		routers.put(name, conn);
	}
	
	public void dropRouters() {
		routers.clear();
	}
	
	
}
