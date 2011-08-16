package org.vagabond.rcp;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.vagabond.rcp.util.PluginLogProvider;
import org.vagabond.rcp.util.ResourceManager;
import org.vagabond.util.LogProviderHolder;

import com.quantum.QuantumPlugin;
import com.quantum.log.PluginLogManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "VagabondRCP"; //$NON-NLS-1$
	// log4j properties file name
	public static final String LOG_PROPERTIES_FILE = "log4j.properties";
	
	// The shared instance
	private static Activator plugin;
	
	// logger objects
	private PluginLogManager logManager = null;
    private Logger log = null;

	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		configureLogging(context.getBundle());
		log.debug("logging configured");
		ResourceManager.getInstance().setBundle(context.getBundle());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	/**
	 * Configure log4j framework
	 */
    private void configureLogging(Bundle bundle) {
        try {
            URL url = bundle.getEntry("/resource/"+  LOG_PROPERTIES_FILE);
            System.out.println(url);
            InputStream propertiesInputStream = url.openStream();
            if (propertiesInputStream != null) {
                Properties props = new Properties();
                props.load(propertiesInputStream);
                propertiesInputStream.close();
                PluginLogProvider.getInstance().setLogManager(
                		new PluginLogManager(this, props));
                this.log = PluginLogProvider.getInstance().getLogger(Activator.class
                        .getName());
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        log.debug("set logging for TrampEx");
        LogProviderHolder.getInstance().setLogProvider(
        		PluginLogProvider.getInstance());
    }
}
