package net.yzimroni.commandmanager.command.args.arguments;

import java.util.Arrays;
import java.util.List;

import net.yzimroni.commandmanager.command.args.ArgumentParseData;
import net.yzimroni.commandmanager.command.args.ArgumentValidCheck;
import net.yzimroni.commandmanager.command.args.CommandArgument;

public class IntegerArgument extends CommandArgument<Integer> {

	private Integer minimum = null;
	private Integer maximum = null;
	
	private boolean fixTabComplete = false;

	public IntegerArgument(String name, boolean require) {
		super(name, require);
	}
	
	public IntegerArgument(String name, boolean require, Integer minimum, Integer maximum) {
		this(name, require);
		this.minimum = minimum;
		this.maximum = maximum;
	}
	
	public IntegerArgument(String name, boolean require, Integer minimum, Integer maximum, boolean fixTabComplete) {
		this(name, require, minimum, maximum);
		this.fixTabComplete = fixTabComplete;
	}

	@Override
	public String getInputType() {
		return "Integer";
	}

	@Override
	public String getValidInputs() {
		return "Number";
	}

	@Override
	public ArgumentValidCheck isValidInput(ArgumentParseData data) {
		try {
			int i = Integer.valueOf(data.getInput());
			if (minimum != null) {
				if (i < minimum) {
					return ArgumentValidCheck.create(false, "Number out of range (number too small)");
				}
			}
			if (maximum != null) {
				if (i > maximum) {
					return ArgumentValidCheck.create(false, "Number out of range (number too big)");
				}
			}
			return ArgumentValidCheck.create(true);
		} catch (Exception e) {
			return ArgumentValidCheck.create(false, "Invalid number");
		}
	}

	@Override
	public Integer getInput(ArgumentParseData data) {
		return Integer.valueOf(data.getInput());
	}

	@Override
	public boolean isVarArgs() {
		return false;
	}

	@Override
	public List<String> getTabCompleteOptions(ArgumentParseData data) {
		if (fixTabComplete && (minimum != null || maximum != null)) {
			try {
				int i = Integer.valueOf(data.getInput());
				Integer set = null;
				if (minimum != null) {
					if (i < minimum) {
						set = minimum;
					}
				}
				if (maximum != null) {
					if (i > maximum) {
						set = maximum;
					}
				}
				if (set != null) {
					return Arrays.asList("" + set.intValue());
				}
			} catch (Exception e) {}
		}
		return Arrays.asList("");
	}

	public Integer getMinimum() {
		return minimum;
	}

	public void setMinimum(Integer minimum) {
		this.minimum = minimum;
	}

	public Integer getMaximum() {
		return maximum;
	}

	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
	}

	public boolean isFixTabComplete() {
		return fixTabComplete;
	}

	public void setFixTabComplete(boolean fixTabComplete) {
		this.fixTabComplete = fixTabComplete;
	}

}
