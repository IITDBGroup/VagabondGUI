package org.vagabond.rcp.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.colorchooser.ColorSelectionModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class SWTResourceManager {

	private Map<String,Font> fonts;
	private Map<RGB,Color> colors;
	private Map<String, RGB> namedColors;
	private Map<String, Image> images;
	
	private static SWTResourceManager instance = new SWTResourceManager();
	
	private SWTResourceManager () {
		fonts = new HashMap<String,Font> ();
		colors = new HashMap<RGB,Color> ();
		namedColors = new HashMap<String,RGB> ();
		images = new HashMap<String, Image> ();
	}
	
	public static Color getColor(RGB rgb) {
		if (!instance.colors.containsKey(rgb)) {
			instance.colors.put(rgb, new Color(Display.getCurrent(),rgb));
		}
		
		return instance.colors.get(rgb);
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
			Font sysFont = Display.getCurrent().getSystemFont();
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
			instance.fonts.put(name, new Font(Display.getCurrent(), 
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
			newImage = new Image(Display.getCurrent(), 
			        ResourceManager.getInstance().getResource("icons/attribute.gif"));
			instance.images.put(name, newImage);
		}
		
		return instance.images.get(name);
	}
	
	public static void dispose () {
		for(Font f: instance.fonts.values())
			f.dispose();
		for(Color c: instance.colors.values())
			c.dispose();
		for(Image i: instance.images.values())
			i.dispose();
	}
	
}
