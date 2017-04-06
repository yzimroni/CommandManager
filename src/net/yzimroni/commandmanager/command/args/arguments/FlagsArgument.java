package net.yzimroni.commandmanager.command.args.arguments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Joiner;

import net.yzimroni.commandmanager.command.args.ArgumentParseData;
import net.yzimroni.commandmanager.command.args.ArgumentValidCheck;
import net.yzimroni.commandmanager.command.args.CommandArgument;
import net.yzimroni.commandmanager.utils.FlagsData;

public class FlagsArgument extends CommandArgument<FlagsData> {
	
	private List<String> flags = new ArrayList<String>();


	public FlagsArgument(String name, boolean require) {
		super(name, require);
	}
	
	public boolean addFlag(String name) {
		name = name.toLowerCase();
		if (!hasFlag(name)) {
			flags.add(name);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean hasFlag(String name) {
		name = name.toLowerCase();
		return flags.contains(name);
	}
	
	public boolean removeFlag(String name) {
		name = name.toLowerCase();
		if (hasFlag(name)) {
			flags.remove(name);
			return true;
		}
		return false;
	}

	@Override
	public String getInputType() {
		return "Flags";
	}

	@Override
	public String getValidInputs() {
		return "Flags: " + Joiner.on(", ").join(flags);
	}

	@Override
	public ArgumentValidCheck isValidInput(ArgumentParseData data) {
		if (data.getArgs() == null || data.getArgs().length == 0) {
			return ArgumentValidCheck.create(false);
		}
		for (String arg : data.getArgs()) {
			if (arg.startsWith("-")) {
				String flag = arg.substring(1).toLowerCase();
				if (!hasFlag(flag)) {
					return ArgumentValidCheck.create(false, "Unknown flag: " + arg);
				}
			} else {
				return ArgumentValidCheck.create(false, "Invalid flag: " + arg);
			}
		}
		
		return ArgumentValidCheck.create(true);
	}

	@Override
	public FlagsData getInput(ArgumentParseData data) {
		List<String> dataflag = new ArrayList<String>();
		for (String arg : data.getArgs()) {
			String flag = arg.substring(1).toLowerCase();
			dataflag.add(flag);
		}
		
		return new FlagsData(dataflag);
	}

	@Override
	public List<String> getTabCompleteOptions(ArgumentParseData data) {
		List<String> options = new ArrayList<String>();
		List<String> used = Arrays.asList(data.getArgs());
		String input = data.getInput().toLowerCase();
		if (input.startsWith("-")) {
			input = input.substring(1);
		}
		for (String flag : flags) {
			if (used.contains("-" + flag)) {
				continue;
			}
			if (input.isEmpty() || flag.startsWith(input)) {
				options.add("-" + flag);
			}
		}
		if (options.isEmpty()) {
			options.add("");
		}
		return options;
	}

	@Override
	public boolean isVarArgs() {
		return true;
	}
	
	

}
