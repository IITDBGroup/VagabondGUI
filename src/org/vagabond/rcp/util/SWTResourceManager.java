package org.vagabond.rcp.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class SWTResourceManager {

	static Logger log = PluginLogProvider.getInstance().getLogger(SWTResourceManager.class);
	
	private Map<String,Font> fonts;
	private Map<RGB,Color> colors;
	private Map<String, RGB> namedColors;
	private Map<String, Image> images;
	private Map<String, ImageDescriptor> iDescs;
	
	private static SWTResourceManager instance = new SWTResourceManager();
	
	private SWTResourceManager () {
		fonts = new HashMap<String,Font> ();
		colors = new HashMap<RGB,Color> ();
		namedColors = new HashMap<String,RGB> ();
		images = new HashMap<String, Image> ();
		iDescs = new HashMap<String, ImageDescriptor> ();
	}
	
	public static Color getColor(RGB rgb) {
		if (!instance.colors.containsKey(rgb)) {
			instance.colors.put(rgb, new Color(null,rgb));
		}
		
		return instance.colors.get(rgb);
	}
	
	public static Color getColor(int r, int g, int b) {
		return getColor(new RGB(r,g,b));
	}
	
	public static Color getSystemColor (int id) {
		return getDisplay().getSystemColor(id);
	}
	
	public static Color nameColor (String name, RGB rgb) {
		if (instance.namedColors.containsKey(name))
			return getColor(name);
		Color named = getColor(rgb);
		instance.namedColors.put(name, rgb);
		return named;
	}
	
	public static Color getColor(String name) {
		return getColor(instance.namedColors.get(name));
	}
	
	public static Font getSystemFont (int size, boolean bold) {
		String fontName = (bold ? "BOLD_" : "") + "SYSTEM_FONT_" + size;
		
		if (!instance.fonts.containsKey(fontName)) {
			Font sysFont = getDisplay().getSystemFont();
			Font font = new Font(null, 
					sysFont.getFontData()[0].getName(), size, 
					bold ? SWT.BOLD : SWT.NONE);
			instance.fonts.put(fontName, font);
		}
		
		return instance.fonts.get(fontName);
	}
	
	public static Font getBoldSystemFont (int size) {
		return getSystemFont(size, true);
	}
	
	public static Font getFont(String name, String font, int size, boolean bold) {
		if (!instance.fonts.containsKey(name)) {
			instance.fonts.put(name, new Font(null, 
					font, size, bold ? SWT.BOLD : SWT.NONE));	
		}
		return instance.fonts.get(name);		
	}
	
	public static Font getFont(String name) throws Exception {
		if (instance.fonts.containsKey(name))
			return instance.fonts.get(name);
		throw new Exception ("No font named " + name + " registered");
	}
	
	public static Image getImage (String name) throws Exception {
		if (!instance.images.containsKey(name)) {
			Image newImage;
			newImage = new Image(null, 
			        ResourceManager.getInstance().getResource("icons/" + name));
			instance.images.put(name, newImage);
		}
		
		return instance.images.get(name);
	}
	
	public static ImageDescriptor getImageDescriptor (String name) throws IOException {
		if (!instance.iDescs.containsKey(name)) {
			Image newImage;
			newImage = new Image(null, 
			        ResourceManager.getInstance().getResource("icons/" + name));
			
			instance.iDescs.put(name, ImageDescriptor.createFromImage(newImage));
		}
		
		return instance.iDescs.get(name);
	}
	
	private static Display getDisplay () {
		if (Display.getCurrent() != null)
			return Display.getCurrent();
		return Display.getDefault();
	}
	
	public static void dispose () {
		log.debug("dispose fonts");
		for(Font f: instance.fonts.values())
			f.dispose();
		log.debug("dispose colors");
		for(Color c: instance.colors.values())
			c.dispose();
		log.debug("dispose images");
		for(Image i: instance.images.values())
			i.dispose();
		log.debug("dispose done");
	}
	
}
