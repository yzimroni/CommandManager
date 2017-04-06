package net.yzimroni.commandmanager.command;

import net.yzimroni.commandmanager.command.args.ArgumentData;
import net.yzimroni.commandmanager.command.args.ArgumentValidCheck;

import org.bukkit.command.CommandSender;

public interface CommandValidator {

	/**
	 * Called before a Command execute, to check if the command can be execute
	 * @param sender The CommandSender of this command
	 * @param command The command
	 * @param args The arguments the sender typed
	 * @return The result of the validation
	 */
	public ArgumentValidCheck validate(CommandSender sender, Command command, ArgumentData args);
	
}
