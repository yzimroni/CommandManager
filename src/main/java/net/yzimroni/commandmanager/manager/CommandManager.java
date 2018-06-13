package net.yzimroni.commandmanager.manager;

import java.util.ArrayList;
import java.util.List;

import net.yzimroni.commandmanager.command.Command;
import net.yzimroni.commandmanager.command.SubCommand;
import net.yzimroni.commandmanager.command.args.ArgumentData;
import net.yzimroni.commandmanager.command.args.ArgumentParseData;
import net.yzimroni.commandmanager.command.args.ArgumentValidCheck;
import net.yzimroni.commandmanager.command.args.ArgumentValue;
import net.yzimroni.commandmanager.command.args.CommandArgument;
import net.yzimroni.commandmanager.log.CommandManagerLog;
import net.yzimroni.commandmanager.utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;

public class CommandManager implements CommandExecutor, Listener {
		
	private List<Command> commands;
	private TabCompleteManager tabComplete;
	
	private static CommandManager manager;
	
	public CommandManager() {
		commands = new ArrayList<Command>();
		tabComplete = new TabCompleteManager(this);
		manager = this;
	}
	
	public static CommandManager get() {
		return manager;
	}
	
	public void registerCommand(Plugin plugin, Command command) {
		CommandManagerLog.debug("Registering command: %s, plugin: %s", command, plugin);
		command.setPlugin(plugin);
		commands.add(command);
		PluginCommand bukkitCommand = Bukkit.getPluginCommand(command.getName());
		if (bukkitCommand != null) {
			bukkitCommand.setExecutor(this);
			bukkitCommand.setTabCompleter(tabComplete);
		}
	}
		
	public Command getCommand(String name) {
		name = name.toLowerCase();
		for (Command command : commands) {
			if (command.getName().equalsIgnoreCase(name)) {
				return command;
			}
		}
		for (Command command : commands) {
			if (command.getAliases().contains(name)) {
				return command;
			}
		}
		return null;
	}
	
	public List<Command> getCommandsByPlugin(Plugin plugin) {
		List<Command> cmds = new ArrayList<Command>();
		for (Command command : commands) {
			if (command.getPlugin().equals(plugin)) {
				cmds.add(command);
			}
		}
		return cmds;
	}
	
	public void unregisterCommand(Command command) {
		CommandManagerLog.debug("Unregistering command: %s, plugin: %s", command, command.getPlugin());
		if (commands.contains(command)) {
			PluginCommand bukkitCommand = Bukkit.getPluginCommand(command.getName());
			if (bukkitCommand != null) {
				bukkitCommand.setExecutor(null);
				bukkitCommand.setTabCompleter(null);
			}
			commands.remove(command);
		}
	}
	
	public void unregisterCommandsByPlugin(Plugin plugin) {
		List<Command> pluginCommands = getCommandsByPlugin(plugin);
		if (pluginCommands != null && !pluginCommands.isEmpty()) {
			CommandManagerLog.debug("Unregistering all commands from the plugin %s", plugin);
			for (Command command : pluginCommands) {
				unregisterCommand(command);
			}
			CommandManagerLog.debug("Unregistered %s commands from the plugin %s", pluginCommands.size(), plugin);
		}
	}


	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String commandLabel, String[] args) {
		Command command = getCommand(commandLabel);
		if (command != null) {
			handleCommand(sender, command, args);
			return true;
		} else {
			CommandManagerLog.debug("unknown command: %s by %s", commandLabel, sender);
			return false;
		}
	}
	
	private void handleCommand(CommandSender sender, Command command, String[] args) {
		CommandManagerLog.debug("Handling command '%s' for the sender %s", command, sender);
		if (!command.canRun(sender, false)) {
			CommandManagerLog.debug("%s can't run the command %s", sender, command);
			return;
		}
		
		//Auto help-command
		if (command.isAutoHelpCommand() && args.length == 1 && (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))) {
			CommandManagerLog.debug("Sending auto-help for command '%s' for the sender %s", command, sender);
			command.printHelp(sender, -1);
			return;
		}
		
		if (args.length == 0 || command.getSubCommands().isEmpty()) {
			callCommand(sender, command, args);
		} else {
			if (args.length >= 1) {
				SubCommand sub = command.getSubCommand(args[0]);
				if (sub != null) {
					handleCommand(sender, sub, Utils.cutArgs(args, 1));
					return;
				} else {
					callCommand(sender, command, args);
				}
			}
		}
	}
	
	private void callCommand(CommandSender sender, Command command, String[] args) {
		CommandManagerLog.debug("Calling command '%s' for the sender %s", command, sender);
		CommandManagerLog.debug("Command call Info: %s: %s, %s", command, sender, args);
		if (!command.canRun(sender, false)) {
			CommandManagerLog.debug("%s can't run the command %s", sender, command);
			return;
		}
		if (args.length == 0) {
			//If the sender dont send args and the command have no arguments OR the command have only un-required arguments
			ArgumentData argumentdata = ArgumentData.create();
			if (!command.hasArguments() || (command.hasArguments() && command.isAllArgumentsUnrequired())) {
				if (command.getValidator() != null) {
					CommandManagerLog.debug("Check command %s validation for sender %s without args", command, sender);
					ArgumentValidCheck check = command.getValidator().validate(sender, command, argumentdata);
					CommandManagerLog.debug("CommandValidator for command %s for sender %s result: %s", command, sender, check);
					if (!check.isValid()) {
						sender.sendMessage(ChatColor.RED + "Error: " + check.getMessage());
						return;
					}
				}
				CommandManagerLog.debug("Run the command %s for sender %s as no-args command", command, sender);
				command.getExecuter().executeCommand(sender, command, argumentdata);
			} else {
				command.printHelp(sender, 1);
			}
			return;
		} else {
			List<ArgumentValue<?>> arguments = parseArguments(sender, command, args);
			if (arguments != null) {
				ArgumentData argumentdata = ArgumentData.create(arguments);
				if (command.getValidator() != null) {
					CommandManagerLog.debug("Check command %s validation for sender %s with arguments:", command, sender, arguments);
					ArgumentValidCheck check = command.getValidator().validate(sender, command, argumentdata);
					CommandManagerLog.debug("CommandValidator for command %s for sender %s result: %s", command, sender, check);
					if (!check.isValid()) {
						sender.sendMessage(ChatColor.RED + "Error: " + check.getMessage());
						return;
					}
				}
				CommandManagerLog.debug("Executing command '%s' for sender '%s' with args : %s", command, sender, arguments);
				command.getExecuter().executeCommand(sender, command, argumentdata);
			} else {
				CommandManagerLog.debug("Received null argument list for command '%s' for sender '%s'", command, sender);
			}
			
		}
	}
	
	private List<ArgumentValue<?>> parseArguments(CommandSender sender, Command command, String[] args) {
		//For the max argument check (if there is more args then needed but lastTakeAll is true, its ok as the extra args will send to the take all argument)
		boolean lastVarargs = !command.getArguments().isEmpty() && command.getArguments().get(command.getArguments().size() - 1).isVarArgs();
		List<ArgumentValue<?>> arguments = new ArrayList<ArgumentValue<?>>();
		int index = 0, lastIndex = 0;
		for (int i = 0; i < command.getArguments().size(); i++) {
			CommandArgument<?> arg = command.getArguments().get(i);
			if ((index + 1) > args.length) {
				if (arg.isRequire()) {
					CommandManagerLog.debug("Command '%s' for sender '%s' need more args", command, sender);
					sender.sendMessage("Need more args");
					command.printHelp(sender, 1);
					return null;
				}
				break;
			}
			
			if (arg.hasPermission()) {
				if (!sender.hasPermission(arg.getPermission())) {
					continue;
				}
			}
			
			String input = "";
			if (arg.isVarArgs()) {
				for (int n = index; n < args.length; n++) {
					if (!input.isEmpty()) {
						input += " ";
					}
					input += args[n];
					index++;
				}
			} else {
				 input = args[index];
				 index++;
			}
			try {
				ArgumentParseData data = new ArgumentParseData(input, sender, command, Utils.cutArgs(args, lastIndex, index), arguments);
				ArgumentValidCheck check = arg.isValidInput(data);
				CommandManagerLog.debug("Parsing data for argument %s for command %s for sender %s: %s", arg, command, sender, check);
				if (check.isValid()) {
					ArgumentValue<?> value = arg.getValue(data);
					CommandManagerLog.debug("arg %s for command %s for sender %s for input '%s': %s", arg, command, sender, input, value);
					arguments.add(value);
				} else {
					CommandManagerLog.debug("arg %s for command %s for sender %s for input '%s' is not valid: %s", arg, command, sender, input, check);
					sender.sendMessage(ChatColor.RED + "Invalid input to argument '" + arg.getNameToDisplay() + "'" + (check.hasMessage() ? ": " + check.getMessage() : ""));
					return null;
					//Invalid arguments
				}
			} catch (Exception e) {
				sender.sendMessage(ChatColor.RED + "Error while parsing argument '" + arg.getNameToDisplay() + "'");
				CommandManagerLog.log("Error while parsing argument '%s' in command '%s' for sender '%s': %s %s", arg, command, sender, e.getClass().getName(), e.getMessage());
				e.printStackTrace();
				return null;
			}
			lastIndex = index;
		}

		if ((index + 1) <= args.length && !lastVarargs) {
			CommandManagerLog.debug("Command '%s' for sender '%s' have too many args", command, sender);
			sender.sendMessage("Too many args");
			command.printHelp(sender, 1);
			return null;
		}
		
		return arguments;

	}
	
	@EventHandler
	public void onPluginDisable(PluginDisableEvent e) {
		unregisterCommandsByPlugin(e.getPlugin());
	}
}
