package net.yzimroni.commandmanager.command.args;

import java.util.List;

import net.yzimroni.commandmanager.command.Command;

public abstract class CommandArgument<T> {

	private String name;
	private String displayName;
	private boolean require;
	private String permission;
	
	public CommandArgument(String name, String displayName, boolean require, String permission) {
		this.name = name;
		this.displayName = displayName;
		this.require = require;
		this.permission = permission;
	}
		
	public CommandArgument(String name, String displayName, boolean require) {
		this(name, displayName, require, null);
	}
	
	public CommandArgument(String name, boolean require) {
		this(name, null, require);
	}
	
	public CommandArgument(String name) {
		this(name, true);
	}
	
	/**
	 * Returns the name of this argument
	 * @return the name of this argument
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the name display of this argument
	 * @return the name display of this argument, can be null
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Set the name display of this argument
	 * @param displayName the name set to
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	
	/**
	 * Returns the agument name to display this argument to command senders, if the argument display name is not null
	 * the {@link #getDisplayName()} returns, otherwise the {@link #getName()} returns
	 * @return The name to display for this argument
	 */
	public String getNameToDisplay() {
		if (displayName != null && !displayName.isEmpty()) {
			return displayName;
		}
		return name;
	}

	public abstract String getInputType();
	
	public abstract String getValidInputs();
	
	/**
	 * Checks if the <code>data</code> is valid and can be parsed
	 * @param data The data to check
	 * @return The result of the validation
	 */
	public abstract ArgumentValidCheck isValidInput(ArgumentParseData data);
	
	/**
	 * Get an input from this argument based on the <code>data</code>
	 * @param data The data to get the input from
	 * @return The raw input
	 */
	public abstract T getInput(ArgumentParseData data);
	
	/**
	 * Returns A List of possible completions for this argument
	 * 
	 * @param data The data of the command
	 * @return A List of possible completions for this argument
	 */
	public abstract List<String> getTabCompleteOptions(ArgumentParseData data);
	
	/**
	 * Get an input from this argument based on the <code>data</code>
	 * @param data The data to get the input from
	 * @return An {@link ArgumentValue} with the input of the argument for the <code>data</code>
	 */
	public ArgumentValue<T> getValue(ArgumentParseData data) {
		return new ArgumentValue<T>(name, this, getInput(data));
	}

	/**
	 * Returns if this argument takes a variable number of args
	 * Used to create an argument that take more then one args, for example a message argument
	 * @return <code>true</code> if this arguments takes a variable number of args 
	 */
	public abstract boolean isVarArgs();

	/**
	 * Returns if this argument is require in a {@link Command}
	 * @return if this argument is require in a {@link Command}
	 */
	public boolean isRequire() {
		return require;
	}
	
	/**
	 * Returns the permission of this CommandArgument
	 * 
	 * Argument permission will be checked and used ONLY if the argument is not required
	 * @return the permission of this CommandArgument
	 */
	public String getPermission() {
		return permission;
	}
	
	/**
	 * Check if this argument have a permission
	 * @return <code>true</code> if the argument have permission and the argument is not required
	 */
	public boolean hasPermission() {
		return !isRequire() && permission != null && !permission.isEmpty();
	}

	/**
	 * Set the permission of this CommandArgument
	 * 
	 * Argument permission will be checked and used ONLY if the argument is not required
	 * @param permission The permission to set
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * Set if this argument is require in a {@link Command}
	 * @param require the boolean to set
	 */
	public void setRequire(boolean require) {
		this.require = require;
	}

	@Override
	public String toString() {
		return "CommandArgument [" + getClass().getCanonicalName() + "] [name=" + name + ", displayName=" + displayName + ", require=" + require + ", permission=" + permission + "]";
	}
	
}
