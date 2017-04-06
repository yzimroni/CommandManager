package net.yzimroni.commandmanager.command.args;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.yzimroni.commandmanager.utils.FlagsData;

public class ArgumentData {

	private List<ArgumentValue<?>> args;
	private HashMap<String, Object> extra = null;
	
	private ArgumentData() {
		
	}

	public List<ArgumentValue<?>> getArgs() {
		return args;
	}

	private void setArgs(List<ArgumentValue<?>> args) {
		this.args = args;
	}
	
	public boolean has(String name) {
		for (ArgumentValue<?> v : args) {
			if (v.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}
	
	public <T> boolean has(String name, Class<T> clazz) {
		for (ArgumentValue<?> v : args) {
			if (v.getName().equalsIgnoreCase(name)) {
				if (clazz.isAssignableFrom(v.getValue().getClass())) {
					return true;
				}
				return false;
			}
		}
		return false;
	}
	
	public Object get(String name) {
		ArgumentValue<?> value = getValueHolder(name);
		if (value != null) {
			return value.getValue();
		}
		return null;
	}
	
	public ArgumentValue<?> getValueHolder(String name) {
		for (ArgumentValue<?> v : args) {
			if (v.getName().equalsIgnoreCase(name)) {
				return v;
			}
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> T get(String name, Class<T> clazz) {
		ArgumentValue<?> value = getValueHolder(name, clazz);
		if (value != null) {
			return (T) value.getValue();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> ArgumentValue<T> getValueHolder(String name, Class<T> clazz) {
		for (ArgumentValue<?> v : args) {
			if (v.getName().equalsIgnoreCase(name)) {
				if (clazz.isAssignableFrom(v.getValue().getClass())) {
					return (ArgumentValue<T>) v;
				}
				return null;
			}
		}
		return null;
	}
	
	public boolean hasFlag(String argName, String flagName) {
		if (has(argName, FlagsData.class)) {
			FlagsData flags = get(argName, FlagsData.class);
			return flags.hasFlag(flagName);
		}
		return false;
	}

	/*
	 * Extra methods
	 */
	
	public boolean addExtra(String name, Object object) {
		if (extra == null) {
			extra = new HashMap<String, Object>();
		}
		if (hasExtra(name)) {
			return false;
		}
		extra.put(name, object);
		return true;
	}
	
	public boolean hasExtra(String name) {
		return extra != null && extra.containsKey(name);
	}
	
	public <T> boolean hasExtra(String name, Class<T> clazz) {
		return getExtra(name, clazz) != null;
	}
	
	public Object getExtra(String name) {
		return extra != null ? extra.get(name) : null;
	}
		
	@SuppressWarnings("unchecked")
	public <T> T getExtra(String name, Class<T> clazz) {
		Object v = getExtra(name);
		if (v != null && clazz.isAssignableFrom(v.getClass())) {
			return (T) v;
		}
		return null;
	}
	
	/*
	 * End of extra methods
	 */

	public static ArgumentData create(ArgumentValue<?>... args) {
		ArgumentData data = new ArgumentData();
		List<ArgumentValue<?>> arguments = new ArrayList<ArgumentValue<?>>();
		for (int i = 0; i < args.length; i++) {
			arguments.add(args[i]);
		}
		data.setArgs(arguments);
		return data;
	}
	
	public static ArgumentData create(List<ArgumentValue<?>> arguments) {
		ArgumentData data = new ArgumentData();
		data.setArgs(arguments);
		return data;
	}

	@Override
	public String toString() {
		return "ArgumentData [args=" + args + "]";
	}
	
	
	
}
