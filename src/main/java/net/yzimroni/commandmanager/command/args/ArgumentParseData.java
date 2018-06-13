package net.yzimroni.commandmanager.command.args;

import java.util.Arrays;
import java.util.List;

import net.yzimroni.commandmanager.command.Command;

import org.bukkit.command.CommandSender;

public class ArgumentParseData {

	private String input;
	private CommandSender commandSender;
	private Command command;
	private String[] args;
	private List<ArgumentValue<?>> previousArgs;

	public ArgumentParseData(String input, CommandSender commandSender, Command command, String[] args, List<ArgumentValue<?>> previousArgs) {
		this.input = input;
		this.commandSender = commandSender;
		this.command = command;
		this.args = args;
		this.previousArgs = previousArgs;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public CommandSender getCommandSender() {
		return commandSender;
	}

	public void setCommandSender(CommandSender commandSender) {
		this.commandSender = commandSender;
	}

	public Command getCommand() {
		return command;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String[] args) {
		this.args = args;
	}

	public List<ArgumentValue<?>> getPreviousArgs() {
		return previousArgs;
	}

	public void setPreviousArgs(List<ArgumentValue<?>> previousArgs) {
		this.previousArgs = previousArgs;
	}

	@Override
	public String toString() {
		return "ArgumentParseData [input=" + input + ", commandSender=" + commandSender + ", command=" + command + ", args=" + Arrays.toString(args)
			+ ", previousArgs=" + previousArgs + "]";
	}

}
