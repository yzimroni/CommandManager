package net.yzimroni.commandmanager;

import net.yzimroni.commandmanager.command.Command;
import net.yzimroni.commandmanager.command.CommandExecutor;
import net.yzimroni.commandmanager.command.SubCommand;
import net.yzimroni.commandmanager.command.args.ArgumentData;
import net.yzimroni.commandmanager.command.args.arguments.BooleanArgument;
import net.yzimroni.commandmanager.log.CommandManagerLog;
import net.yzimroni.commandmanager.manager.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandManagerPlugin extends JavaPlugin {

	private CommandManager manager;
	
	@Override
	public void onEnable() {
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		CommandManagerLog.setDebug(getConfig().getBoolean("debug.enabled", false));
		CommandManagerLog.setFullMessages(getConfig().getBoolean("debug.fullMessages", false));
		
		manager = new CommandManager();
		Bukkit.getPluginManager().registerEvents(manager, this);
		
		createPluginCommand();
		
		CommandManagerLog.log("CommandManager enabled");
	}
	
	@Override
	public void onDisable() {
		getConfig().set("debug.enabled", CommandManagerLog.isDebug());
		getConfig().set("debug.fullMessages", CommandManagerLog.isFullMessages());
		saveConfig();
		
		if (manager != null) {
			manager = null;
		}
		
		CommandManagerLog.log("CommandManger disabled");
	}
	
	private void createPluginCommand() {
		Command command = new Command("commandmanager", "CommandManager plugin command", new CommandExecutor() {
			
			@Override
			public void executeCommand(CommandSender sender, Command command, ArgumentData args) {
				sender.sendMessage("CommandManager v0.1 by yzimroni");
			}
		});
		command.setAliases("cm");
		command.setPermission("commandmanager.admin");
		
		SubCommand debug = new SubCommand("debug", "CommandManager debug", new CommandExecutor() {
			
			@Override
			public void executeCommand(CommandSender sender, Command command, ArgumentData args) {
				sender.sendMessage("Debug mode: " + CommandManagerLog.isDebug());
				if (args.has("debugging")) {
					boolean debug = args.get("debugging", Boolean.class).booleanValue();
					CommandManagerLog.setDebug(debug);
					sender.sendMessage("New debug mode: " + CommandManagerLog.isDebug());
				}
			}
		});
		
		debug.setPermission("commandmanager.debug");
		debug.addArgument(new BooleanArgument("debugging", false));
		
		SubCommand fullMessages = new SubCommand("fullmessages", "Debug full messages mode", new CommandExecutor() {
			
			@Override
			public void executeCommand(CommandSender sender, Command command, ArgumentData args) {
				sender.sendMessage("Debug full message mode: " + CommandManagerLog.isDebug());
				if (args.has("fullMessages")) {
					boolean fullMessages = args.get("fullMessages", Boolean.class).booleanValue();
					CommandManagerLog.setFullMessages(fullMessages);
					sender.sendMessage("New debug full message mode: " + CommandManagerLog.isFullMessages());
				}
			}
		});
		
		fullMessages.addArgument(new BooleanArgument("fullMessages", false));
		
		debug.addSubCommand(fullMessages);
		
		command.addSubCommand(debug);
		manager.registerCommand(this, command);
	}
		
}
