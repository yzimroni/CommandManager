package net.yzimroni.commandmanager.command.args.arguments;

import java.util.Arrays;
import java.util.List;

import net.yzimroni.commandmanager.command.args.ArgumentParseData;
import net.yzimroni.commandmanager.command.args.ArgumentValidCheck;
import net.yzimroni.commandmanager.command.args.CommandArgument;
import net.yzimroni.commandmanager.log.CommandManagerLog;
import net.yzimroni.commandmanager.utils.Utils;

public class DurationArgument extends CommandArgument<Long> {

	public DurationArgument(String name) {
		super(name);
	}

	@Override
	public String getInputType() {
		return "Duration";
	}

	@Override
	public String getValidInputs() {
		return "<number><s/m/h/d/M/y>";
	}

	@Override
	public ArgumentValidCheck isValidInput(ArgumentParseData data) {
		String input = data.getInput();
		if (input == null || input.isEmpty()) {
			return ArgumentValidCheck.create(false, "Number can't be null/empty");
		}
		String it = input.substring(0, input.length() - 1);
		if (Utils.isInt(it)) {
			int i = Utils.getInt(it);
			if (i > 0) {
				String t = input.substring(input.length() - 1, input.length());
				if (Arrays.asList("s", "m", "h", "d", "w", "M", "y").contains(t)) {
					return ArgumentValidCheck.create(true);
				} else {
					return ArgumentValidCheck.create(false, "Unknown time colum");
				}
			} else {
				return ArgumentValidCheck.create(false, "Number must be positive");
			}

		} else {
			return ArgumentValidCheck.create(false, "Invalid number");
		}
	}

	@Override
	public Long getInput(ArgumentParseData data) {
		String input = data.getInput();
		String it = input.substring(0, input.length() - 1);
		int i = Utils.getInt(it);
		int multiplayer = 0;
		String t = input.substring(input.length() - 1, input.length());
		switch (t) {
		case "s":
			multiplayer = 1;
			break;
		case "m":
			multiplayer = 60;
			break;
		case "h":
			multiplayer = 60 * 60;
			break;
		case "d":
			multiplayer = 60 * 60 * 24;
			break;
		case "w":
			multiplayer = 60 * 60 * 24 * 7;
			break;
		case "M":
			multiplayer = 60 * 60 * 24 * 30;
			break;
		case "y":
			multiplayer = 60 * 60 * 24 * 365;
			break;
		default:
			CommandManagerLog.debug("input: " + input + ", it: " + it + ", i: " + i + ", t: " + t);
			break;
		}
		return (long) (i * multiplayer);

	}

	@Override
	public List<String> getTabCompleteOptions(ArgumentParseData data) {
		return Arrays.asList("");
	}

	@Override
	public boolean isVarArgs() {
		return false;
	}

}
