package net.yzimroni.commandmanager.command.args.arguments;

import java.util.List;

import net.yzimroni.commandmanager.command.args.ArgumentParseData;
import net.yzimroni.commandmanager.command.args.ArgumentValidCheck;
import net.yzimroni.commandmanager.command.args.CommandArgument;

public class StringArgument extends CommandArgument<String> {

	private boolean multiString = false;

	public StringArgument(String name, boolean require) {
		this(name, require, false);
	}
	
	public StringArgument(String name, boolean require, boolean multiString) {
		super(name, require);
		this.multiString = multiString;
	}

	@Override
	public String getInputType() {
		return "String";
	}

	@Override
	public String getValidInputs() {
		return "Any Text";
	}

	@Override
	public ArgumentValidCheck isValidInput(ArgumentParseData data) {
		return ArgumentValidCheck.create(true);
	}

	@Override
	public String getInput(ArgumentParseData data) {
		return data.getInput();
	}

	@Override
	public boolean isVarArgs() {
		return multiString;
	}

	@Override
	public List<String> getTabCompleteOptions(ArgumentParseData data) {
		return null;
	}

}
