package net.yzimroni.commandmanager.command.args.arguments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.yzimroni.commandmanager.command.args.ArgumentParseData;
import net.yzimroni.commandmanager.command.args.ArgumentValidCheck;
import net.yzimroni.commandmanager.command.args.CommandArgument;

public class EnumArgument<T extends Enum<?>> extends CommandArgument<T> {
	
	private String name;
	private HashMap<String, T> values = new HashMap<String, T>();
	private String validInputs = "";

	public EnumArgument(String name, T[] values) {
		super(name);
		for (int i = 0; i < values.length; i++) {
			this.values.put(values[i].name().toUpperCase(), values[i]);
			
			if (name == null) {
				name = values[i].getClass().getName();
			}
			
			if (!validInputs.isEmpty()) {
				validInputs += ", ";
			}
			validInputs += values[i].name();
		}
	}

	@Override
	public String getInputType() {
		return name;
	}

	@Override
	public String getValidInputs() {
		return validInputs;
	}

	@Override
	public ArgumentValidCheck isValidInput(ArgumentParseData data) {
		return ArgumentValidCheck.create(values.containsKey(data.getInput().toUpperCase()), "Value not found");
	}

	@Override
	public T getInput(ArgumentParseData data) {
		return values.get(data.getInput().toUpperCase());
	}

	@Override
	public List<String> getTabCompleteOptions(ArgumentParseData data) {
		List<String> options = new ArrayList<String>();
		String input = data.getInput().toLowerCase();
		for (String s : values.keySet()) {
			if (s.toLowerCase().startsWith(input)) {
				options.add(s);
			}
		}
		
		return options;
	}

	@Override
	public boolean isVarArgs() {
		return false;
	}

}
