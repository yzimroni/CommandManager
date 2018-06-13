package net.yzimroni.commandmanager.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.yzimroni.commandmanager.command.Command;
import net.yzimroni.commandmanager.command.SubCommand;
import net.yzimroni.commandmanager.command.args.ArgumentParseData;
import net.yzimroni.commandmanager.command.args.CommandArgument;
import net.yzimroni.commandmanager.log.CommandManagerLog;
import net.yzimroni.commandmanager.utils.Utils;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabCompleteManager implements TabCompleter {
	
	private CommandManager manager = null;

	public TabCompleteManager(CommandManager manager) {
		this.manager = manager;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String alias, String[] args) {
		Command command = manager.getCommand(alias);
		if (command != null) {
			CommandManagerLog.debug("onTabComplete(%s, %s)", sender, command);
			List<String> l = handleCommand(sender, command, args);
			CommandManagerLog.debug("Result for tab complete for tab command for command '%s' for sender '%s' with args %s: %s", command, sender, args, l);
			return l;
		} else {
			CommandManagerLog.debug("unknown command (tab complete): %s, sender %s", cmd, sender);
		}
		return null;
	}
	
	private List<String> handleCommand(CommandSender sender, Command command, String[] args) {
		if (args.length == 0) {
			CommandManagerLog.debug("args.length is 0 at handle tab complete for '%s' for sender %s", command, sender);
			return Arrays.asList("");
		}
		
		if (!command.canRun(sender, true)) {
			CommandManagerLog.debug("Sender %s can't run command '%s', send empty list", sender, command);
			return Arrays.asList("");
		}
		
		SubCommand subt = command.getSubCommand(args[0]);
		if (subt != null) {
			return handleCommand(sender, subt, Utils.cutArgs(args, 1));
		} else {
			return autoCompleteCommand(sender, command, args);
		}
	}
	
	private List<String> autoCompleteCommand(CommandSender sender, Command command, String[] args) {
		if (args.length == 0) {
			CommandManagerLog.debug("args.length is 0 at handle tab complete for '%s' for sender %s", command, sender);
			return Arrays.asList("");
		}
		
		if (!command.canRun(sender, true)) {
			CommandManagerLog.debug("Sender %s can't run command '%s', send empty list (1)", sender, command);
			return Arrays.asList("");
		}

		CommandArgument<?> previousArg = null, arg = null;
		if (command.getArguments() != null && !command.getArguments().isEmpty()) {
			CommandManagerLog.debug("Find arg for auto complete command %s for sender %s with args: %s", command, sender, args);
			if (command.getArguments().size() >= args.length) {
				int target = args.length - 1;
				int index = -1;
				for (CommandArgument<?> argtemp : command.getArguments()) {
					if (argtemp.hasPermission() && !sender.hasPermission(argtemp.getPermission())) {
						continue;
					}
					index++;
					if (target == index) {
						arg = argtemp;
						break;
					}
					previousArg = argtemp;
				}
			} else if (command.getArguments().get(command.getArguments().size() - 1).isVarArgs()) {
				List<CommandArgument<?>> arglist = command.getArguments();
				arg = arglist.get(arglist.size() - 1);
				if (arglist.size() >= 2) {
					for (int i = arglist.size() - 1; i > 0; i--) {
						CommandArgument<?> argtemp = arglist.get(i);
						if (argtemp.hasPermission() && !sender.hasPermission(argtemp.getPermission())) {
							continue;
						}
						previousArg = argtemp;
						break;
					}
				}
			}
		}
		CommandManagerLog.debug("Argument for auto complete command %s for sender %s with args: %s: %s, previousArg: %s", command, sender, args, arg, previousArg);
		try {
			List<String> list = new ArrayList<String>();
			String input = args[args.length - 1];
			if (arg != null) {
				List<String> temp = arg.getTabCompleteOptions(new ArgumentParseData(input, sender, command, Utils.cutArgs(args, arg.isVarArgs() ? command.getArguments().size() - 1 : args.length - 1, args.length), null /* TODO We dont have a previous args here */));
				CommandManagerLog.debug("Tab complete option recivied from argument %s %s for sender %s with args: %s: %s", arg, command, sender, args, temp);
				if (temp != null && !temp.isEmpty()) {
					list.addAll(temp);
				}
			}
			
			if (args.length == 1) {
				if (command.isAutoHelpCommand()) {
					if ("help".startsWith(args[0])) {
						CommandManagerLog.debug("Add 'help' (auto-help command) for auto complete command %s for sender %s", command, sender);
						list.add("help");
					}
				}
				
				if (!command.getSubCommands().isEmpty()) {
					for (SubCommand sub : command.getSubCommands()) {
						if (sub.getName().toLowerCase().startsWith(input.toLowerCase()) && sub.canRun(sender, true)) {
							CommandManagerLog.debug("Add sub-command for auto complete command %s for sender %s with args: %s", command, sender, sub);
							list.add(sub.getName());
						}
					}
				}
			}
			
			if (arg != null && list.isEmpty()) {
				return null;
			}
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
		return Arrays.asList("");
	}



}