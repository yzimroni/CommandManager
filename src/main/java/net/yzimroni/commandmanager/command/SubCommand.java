package net.yzimroni.commandmanager.command;

/**
 * Represents a CommandManager Sub-{@link Command}
 */
public class SubCommand extends Command {
	
	private Command mainCommand;

	public SubCommand(String name, String description, String permission, CommandExecutor executer) {
		super(name, description, permission, executer);
	}
	
	public SubCommand(String name, String description, CommandExecutor executer) {
		this(name, description, null, executer);
	}
	
	/**
	 * Returns the the name of the main command and then the name of the command
	 * @return The full name of the command
	 */
	@Override
	public String getFullName() {
		if (mainCommand instanceof SubCommand) {
			return ((SubCommand) mainCommand).getFullName() + " " + getName();
		} else {
			return mainCommand.getName() + " " + getName();
		}
	}

	/**
	 * Returns the main command of this sub-command
	 * @return the main command of this sub-command
	 */
	public Command getMainCommand() {
		return mainCommand;
	}
	
	/**
	 * Set the main command of this sub-command
	 * @param mainCommand the Command to set
	 */
	public void setMainCommand(Command mainCommand) {
		this.mainCommand = mainCommand;
	}

	@Override
	public String toString() {
		return "SubCommand [name=" + name + ", description=" + description + ", executer=" + executer + ", permission="
			+ permission + ", permissionMessage=" + permissionMessage + ", aliases=" + aliases + ", subCommands=" + subCommands + ", arguments="
			+ arguments + ", onlyPlayer=" + onlyPlayer + ", autoHelpCommand=" + autoHelpCommand + ", plugin=" + plugin + ", getFullName()="
			+ getFullName() + "]";
	}
	
	
	
}
