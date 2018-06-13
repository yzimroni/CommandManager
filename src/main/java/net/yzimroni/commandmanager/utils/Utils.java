package net.yzimroni.commandmanager.utils;

public class Utils {

	private Utils() {
		
	}
	
	public static String[] cutArgs(String[] temp, int index) {
		if ((temp.length - index) <= 0) {
			return new String[0];
		}
		String[] args = new String[temp.length - index];
		for (int i = 0; i < args.length; i++) {
			args[i] = temp[i + index];
		}
		return args;
	}
	
	public static String[] cutArgs(String[] temp, int start, int end) {
		int totalLength = end - start;
		if ((temp.length - totalLength) < 0 || start >= end || start < 0 || end > temp.length) {
			return new String[0];
		}
		String[] args = new String[totalLength];
		for (int i = 0; i < totalLength; i++) {
			args[i] = temp[i + start];
		}
		return args;
	}
	
	public static boolean isInt(String s) {
		try {
			Integer.valueOf(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static int getInt(String s) {
		try {
			return Integer.valueOf(s);
		} catch (Exception e) {}
		return 0;
	}
	
}
