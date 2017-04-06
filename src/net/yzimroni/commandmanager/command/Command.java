package net.yzimroni.commandmanager.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import net.yzimroni.commandmanager.command.args.CommandArgument;
import net.yzimroni.commandmanager.log.CommandManagerLog;

/**
 * Represents a CommandManager Command
 */
public class Command {
	protected String name;
	protected String description;
	protected CommandExecutor executer;
	protected String permission;
	protected String permissionMessage;
	protected List<String> aliases;
	protected List<SubCommand> subCommands;
	protected List<CommandArgument<?>> arguments;
	protected boolean onlyPlayer;
	protected boolean autoHelpCommand;
	protected CommandValidator validator;
	protected Plugin plugin;
	
	//For cache
	private boolean allArgumentUnrequired = false;
	
	public Command(String name, String description, String permission, CommandExecutor executer) {
		this.name = name.toLowerCase();
		this.description = description;
		this.executer = executer;
		this.permission = permission;
		
		permissionMessage = "" + ChatColor.RED + "You are not allowed to do this";
		subCommands = new ArrayList<SubCommand>();
		arguments = new ArrayList<CommandArgument<?>>();
		aliases = new ArrayList<String>();
		onlyPlayer = false;
	}
	
	public Command(String name, String description, CommandExecutor executer) {
		this(name, description, null, executer);
	}
	
	/**
	 * Return the name of the command
	 * @return the name of the command
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Return the full name of the command
	 * @return The full name of the command
	 */
	public String getFullName() {
		return getName();
	}

	/**
	 * Returns the description of the command, which displayed in help messages
	 * @return the description of the command
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Set the description of the command, which displayed in help messages
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the {@link CommandExecutor} of the command
	 * @return the {@link CommandExecutor} of the command
	 */
	public CommandExecutor getExecuter() {
		return executer;
	}
	
	/**
	 * Set the {@link CommandExecutor} of the command
	 * @param executer the {@link CommandExecutor} to set
	 */
	public void setExecuter(CommandExecutor executer) {
		this.executer = executer;
	}
	
	/**
	 * Returns the required permission to execute the command
	 * @return the permission of the command
	 */
	public String getPermission() {
		return permission;
	}
	
	/**
	 * Set the required permission to execute the command
	 * The permission can be null or empty, in this case anyone can execute the command
	 * @param permission the permission to set
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * Returns The message sent to a CommandSender that try to execute the command without have the permission TODO
	 * @return the permission message of the command
	 */
	public String getPermissionMessage() {
		return permissionMessage;
	}
	
	/**
	 * TODO
	 * @param permissionMessage the permission message to set
	 */

	public void setPermissionMessage(String permissionMessage) {
		this.permissionMessage = permissionMessage;
	}

	/**
	 * Returns the aliases of this command (without the command name)
	 * @return the aliases of this command
	 */
	public List<String> getAliases() {
		return aliases;
	}

	/**
	 * Set the aliases of this command
	 * @param aliases the aliases to set
	 */
	public void setAliases(List<String> aliases) {
		this.aliases.clear();
		for (String a : aliases) {
			this.aliases.add(a.toLowerCase());
		}
	}
	
	/**
	 * Set the aliases of this command
	 * @param aliases the aliases to set
	 */
	public void setAliases(String... aliases) {
		setAliases(Arrays.asList(aliases));
	}

	/**
	 * Returns the {@link SubCommand}s of this command
	 * @return the SubCommands of this command
	 */
	public List<SubCommand> getSubCommands() {
		return subCommands;
	}
	
	/**
	 * Returns the {@link CommandValidator} of this command
	 * @return the {@link CommandValidator} of this command
	 */
	public CommandValidator getValidator() {
		return validator;
	}

	/**
	 * Set the {@link CommandValidator} of this command
	 * @param validator the {@link CommandValidator} to set, can be null
	 */
	public void setValidator(CommandValidator validator) {
		this.validator = validator;
	}

	/**
	 * Add a {@link SubCommand} to this command
	 * @param sub The {@link SubCommand} to add
	 * @return <code>true</code> if the {@link SubCommand} added to this command
	 */
	public boolean hasSubCommand(SubCommand sub) {
		for (SubCommand s : subCommands) {
			if (sub.equals(s)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check if a CommandSender can run this command
	 * @param sender The CommandSender to check
	 * @param silent If false, a message will send to the sender in case they can't run the command
	 * @return <code>true</code> if the sender can run this command
	 */
	public boolean canRun(CommandSender sender, boolean silent) {
		String perm = getPermission();
		if (perm != null && !perm.isEmpty()) {
			if (!sender.hasPermission(perm)) {
				if (!silent) {
					String message = getPermissionMessage();
					if (message != null && !message.isEmpty()) {
						sender.sendMessage(message);
					}
				}
				return false;
			}
		}
		if (isOnlyPlayer() && !(sender instanceof Player)) {
			if (!silent) {
				sender.sendMessage("This command can run only as a player");
			}
			return false;
		}
		return true;
	}

	/**
	 * Check if this command contains a {@link SubCommand} with the name
	 * @param name the name of the {@link SubCommand}
	 * @return <code>true</code> if this command has a {@link SubCommand} with the name
	 */
	public boolean hasSubCommand(String name) {
		for (SubCommand s : subCommands) {
			if (s.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		
		for (SubCommand s : subCommands) {
			if (s.getAliases().contains(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a {@link SubCommand} of this command with the name, if any
	 * @param name The name of the {@link SubCommand} you want
	 * @return SubCommand with the name or null
	 */
	public SubCommand getSubCommand(String name) {
		for (SubCommand s : subCommands) {
			if (s.getName().equalsIgnoreCase(name)) {
				return s;
			}
		}

		for (SubCommand s : subCommands) {
			if (s.getAliases().contains(name)) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Removes the specific {@link SubCommand} from this command
	 * @param sub The {@link SubCommand} to remove
	 * @return <code>true</code> if the {@link SubCommand} was a sub-command of this command
	 */
	public boolean removeSubCommand(SubCommand sub) {
		return subCommands.remove(sub);
	}

	/**
	 * Removes a {@link SubCommand} with the name from this command
	 * @param name The name of the {@link SubCommand} you want to remove
	 * @return <code>true</code> if the {@link SubCommand} was a sub-command of this command
	 */
	public boolean removeSubCommand(String name) {
		SubCommand sub = getSubCommand(name.toLowerCase());
		if (sub != null) {
			return removeSubCommand(sub);
		}
		return false;
	}
	
	/**
	 * Add a {@link SubCommand} to this command
	 * @param sub The {@link SubCommand} you want to add
	 * @return <code>true</code> if the {@link SubCommand} added to this command
	 */
	public boolean addSubCommand(SubCommand sub) {
		SubCommand temp = getSubCommand(sub.getName());
		if (temp == null) {
			sub.setMainCommand(this);
			return subCommands.add(sub);
		}
		return false;
	}
	
	/**
	 * Returns the CommandArguments of this command
	 * @return the CommandArguments of this command
	 */
	public List<CommandArgument<?>> getArguments() {
		return arguments;
	}
	
	/**
	 * Check if there is argument(s) for this command
	 * @return <code>true</code> if there is arguments(s) in this command
	 */
	public boolean hasArguments() {
		return !arguments.isEmpty();
	}
	
	
	/**
	 * Check if all the arguments in this command (if any) are not required
	 * The result is cached in allArgumentUnrequired
	 * This method should only be called when the arguments in the commands changes
	 */
	protected void checkAllArgumentRequired() {
		for (CommandArgument<?> arg : arguments) {
			if (arg.isRequire()) {
				allArgumentUnrequired = false;
				return;
			}
		}
		allArgumentUnrequired = true;
	}
	
	/**
	 * Returns if all the arguments in this command (if any) are not required
	 * @return <code>true</code> if all the arguments in this command (if any) are not required
	 */
	public boolean isAllArgumentsUnrequired() {
		return allArgumentUnrequired;
	}
	
	/**
	 * Adds a CommandArgument to this command
	 * @param arg The argument you want to add
	 */
	public void addArgument(CommandArgument<?> arg) {
		for (CommandArgument<?> other : arguments) {
			if (arg.isVarArgs()) {
				if (other.isVarArgs()) {
					try {
						throw new Exception("You can't add more than one varargs arguments");
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
			}
			if (other.getName().equalsIgnoreCase(arg.getName())) {
				try {
					throw new Exception("Can't add argument '" + arg.getName() + "', already have one by this name");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		CommandArgument<?> oneBeforeLast = !arguments.isEmpty() ? arguments.get(arguments.size() - 1) : null;
		if (oneBeforeLast != null) {
			if (oneBeforeLast.isVarArgs()) {
				try {
					throw new Exception("A varargs argument must be the last argument in a command");
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
			if (arg.isRequire()) {
				if (!oneBeforeLast.isRequire()) {
					try {
						throw new Exception("Canno't add required argument after un-required argument");
					} catch (Exception e) {
						e.printStackTrace();
					}
					return;
				}
			}
		}
		arguments.add(arg);
		checkAllArgumentRequired();
	}
	
	public boolean isOnlyPlayer() {
		return onlyPlayer;
	}

	public void setOnlyPlayer(boolean onlyPlayer) {
		this.onlyPlayer = onlyPlayer;
	}

	/**
	 * Returns if auto-help is enabled for this command
	 * @return <code>true</code> if auto-help is enabled for this command
	 */
	public boolean isAutoHelpCommand() {
		return autoHelpCommand;
	}
	
	/**
	 * Set if auto-help is enabled for this command
	 * @param autoHelpCommand the boolean to set
	 */

	public void setAutoHelpCommand(boolean autoHelpCommand) {
		this.autoHelpCommand = autoHelpCommand;
	}

	/**
	 * Send help messages for this command (and {@link SubCommand}s) to the sender
	 * @param sender The CommandSender you want to send help messages to
	 * @param deep How much {@link SubCommand} deep send, for example, 0 will be only the help for this command,
	 * 1 will send to this command and all the sub-commands, 2 will send to this message, to all the sub-commands
	 * and all the sub-commands of all the sub-commands of this command etc
	 * -1 for no deep limit
	 */
	public void printHelp(CommandSender sender, int deep) {
		if (!canRun(sender, true)) {
			CommandManagerLog.debug("printHelp() called at '%s' for sender %s, sender cant run", this, sender);
			return;
		}
		CommandManagerLog.debug("Sending printHelp about command %s for sender %s with deep %s", this, sender, deep);
		String cmd = ChatColor.YELLOW + "/" + getFullName();
		if (!arguments.isEmpty()) {
			for (CommandArgument<?> arg : arguments) {
				if (arg.hasPermission() && !sender.hasPermission(arg.getPermission())) {
					continue;
				}
				boolean require = arg.isRequire();
				cmd += " " + (require ? "<" : "[") + arg.getNameToDisplay() + (arg.isVarArgs() ? "..." : "") + (require ? ">" : "]");
			}
		}
		cmd += ChatColor.RESET + " - " + ChatColor.AQUA + getDescription();
		sender.sendMessage(cmd);
		if ((deep > 0 || deep == -1) && !subCommands.isEmpty()) {
			for (SubCommand sub : subCommands) {
				sub.printHelp(sender, deep == -1 ? -1 : deep - 1);
			}
		}
	}

	/**
	 * Returns the plugin that registred this command
	 * @return the plugin that registred this command
	 */
	public Plugin getPlugin() {
		return plugin;
	}

	/**
	 * Set the command plugin
	 * @param plugin The plugin to set
	 */
	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public String toString() {
		return "Command [name=" + name + ", description=" + description + ", executer=" + executer + ", permission="
			+ permission + ", permissionMessage=" + permissionMessage + ", aliases=" + aliases + ", subCommands=" + subCommands + ", arguments="
			+ arguments + ", onlyPlayer=" + onlyPlayer + ", autoHelpCommand=" + autoHelpCommand + ", validator=" + validator + ", plugin=" + plugin
			+ ", allArgumentUnrequired=" + allArgumentUnrequired + ", getFullName()=" + getFullName() + "]";
	}

}
