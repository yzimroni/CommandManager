package net.yzimroni.commandmanager.command;

import net.yzimroni.commandmanager.command.args.ArgumentData;

import org.bukkit.command.CommandSender;

public abstract class CommandExecutor {

	/**
	 * Called to execute the command
	 * @param sender The CommandSender of this command
	 * @param command The command
	 * @param args The arguments the sender typed
	 */
	public abstract void executeCommand(CommandSender sender, Command command, ArgumentData args);
	
}
