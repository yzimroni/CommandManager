package net.yzimroni.commandmanager.command.args.arguments;

import java.util.Arrays;
import java.util.List;

import net.yzimroni.commandmanager.command.args.ArgumentParseData;
import net.yzimroni.commandmanager.command.args.ArgumentValidCheck;
import net.yzimroni.commandmanager.command.args.CommandArgument;

public class BooleanArgument extends CommandArgument<Boolean> {

	public BooleanArgument(String name, boolean require) {
		super(name, require);
	}

	@Override
	public String getInputType() {
		return "Boolean";
	}

	@Override
	public String getValidInputs() {
		return "Boolean";
	}

	@Override
	public ArgumentValidCheck isValidInput(ArgumentParseData data) {
		if (Arrays.asList("yes", "true", "on").contains(data.getInput())) {
			return ArgumentValidCheck.create(true);
		}
		if (Arrays.asList("no", "false", "off").contains(data.getInput())) {
			return ArgumentValidCheck.create(true);
		}
		return ArgumentValidCheck.create(false, "Invalid boolean");
	}

	@Override
	public Boolean getInput(ArgumentParseData data) {
		if (Arrays.asList("yes", "true", "on").contains(data.getInput())) {
			return true;
		}
		if (Arrays.asList("no", "false", "off").contains(data.getInput())) {
			return false;
		}
		return null;
	}

	@Override
	public boolean isVarArgs() {
		return false;
	}

	@Override
	public List<String> getTabCompleteOptions(ArgumentParseData data) {
		if (data.getInput().trim().isEmpty()) {
			return Arrays.asList("true", "false");
		}
		return Arrays.asList("yes", "true", "on", "no", "false", "off");
	}
	
	

}
