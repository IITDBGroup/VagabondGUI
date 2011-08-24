package org.vagabond.rcp.util;

import java.math.*;

public class MathHelper {

	public static int max (int ... ints) {
		int result = Integer.MIN_VALUE;
		
		for(int i = 0; i < ints.length; result = Math.max(result, ints[i++]))
			;
		return result;
	}
	
}
