package net.yzimroni.commandmanager.command.args.arguments;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Joiner;

import net.yzimroni.commandmanager.command.args.ArgumentParseData;
import net.yzimroni.commandmanager.command.args.ArgumentValidCheck;
import net.yzimroni.commandmanager.command.args.CommandArgument;

public class ValuesArgument extends CommandArgument<String> {
	
	private Set<String> values = new HashSet<String>();

	public ValuesArgument(String name, String... values) {
		super(name);
		addValues(values);
	}
	
	public boolean hasValue(String value) {
		return values.contains(value);
	}
	
	public void addValues(String... values) {
		for (int i = 0; i < values.length; i++) {
			addValue(values[i]);
		}
	}
	
	public void addValue(String value) {
		if (!hasValue(value)) {
			values.add(value);
		}
	}

	@Override
	public String getInputType() {
		return "Value";
	}

	@Override
	public String getValidInputs() {
		return Joiner.on(", ").join(values);
	}

	@Override
	public ArgumentValidCheck isValidInput(ArgumentParseData data) {
		return ArgumentValidCheck.create(hasValue(data.getInput()), "Invalid value");
	}

	@Override
	public String getInput(ArgumentParseData data) {
		//We already checked the input in isValidInput
		return data.getInput();
	}

	@Override
	public List<String> getTabCompleteOptions(ArgumentParseData data) {
		List<String> options = new ArrayList<String>();
		String input = data.getInput();
		for (String s : values) {
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
