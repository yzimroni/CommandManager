package net.yzimroni.commandmanager.log;

import java.util.Arrays;

import net.yzimroni.commandmanager.command.Command;
import net.yzimroni.commandmanager.command.args.ArgumentValue;
import net.yzimroni.commandmanager.command.args.CommandArgument;

public class CommandManagerLog {

	private static boolean debug = false;
	private static boolean fullMessages = false;
	
	public static void log(String s) {
		System.out.println("[CommandManager] " + s);
	}
	
	public static void log(String s, Object... objects) {
		for (int i = 0; i < objects.length; i++) {
			Object o = objects[i];
			if (o != null) {
				objects[i] = formatObject(o, false);
			}
		}
		log(String.format(s, objects));
	}
	
	public static void debug(String s) {
		if (debug) {
			System.out.println(" ");
			log("[DEBUG] " + s);
			System.out.println(" ");
		}
	}
	
	public static void debug(String s, Object... objects) {
		if (debug) {
			for (int i = 0; i < objects.length; i++) {
				Object o = objects[i];
				if (o != null) {
					objects[i] = formatObject(o, fullMessages);
				}
			}
			debug(String.format(s, objects));
		}
	}
	
	private static Object formatObject(Object o, boolean fullObjects) {
		if (o != null) {
			if (o instanceof Object[]) {
				return Arrays.toString((Object[]) o);
			} else if (o instanceof ArgumentValue<?>) {
				ArgumentValue<?> value = (ArgumentValue<?>) o;
				return "ArgumentValue [name=" + value.getName() + ", argument=" + formatObject(value.getArgument(), fullObjects) + ", value=" + formatObject(value.getValue(), fullObjects) + "]";
			}
			if (!fullObjects) {
				//Make the debug message shorter, instead of print the full toString() of some object,
				//print something else (like the name of the object)
				
				if (o instanceof Command) {
					return ((Command) o).getFullName();
				} else if (o instanceof CommandArgument<?>) {
					CommandArgument<?> arg = (CommandArgument<?>) o;
					return "CommandArgument [" + arg.getClass().getSimpleName() + "] [name=" + arg.getName() + ", displayName=" + arg.getDisplayName() + ", require=" + arg.isRequire() + "]";
				}
			}
		}
		return o;
	}

	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		CommandManagerLog.debug = debug;
	}

	public static boolean isFullMessages() {
		return fullMessages;
	}

	public static void setFullMessages(boolean fullMessages) {
		CommandManagerLog.fullMessages = fullMessages;
	}
	
}
