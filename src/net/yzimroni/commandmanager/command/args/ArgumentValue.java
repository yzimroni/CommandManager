package net.yzimroni.commandmanager.command.args;

/**
 * Represents a {@link CommandArgument} output, to used in {@link ArgumentData}
 */
public class ArgumentValue<T> {

	private String name;
	private CommandArgument<T> argument;
	private T value;

	public ArgumentValue(String name, CommandArgument<T> argument, T value) {
		super();
		this.name = name;
		this.argument = argument;
		this.value = value;
	}

	/**
	 * Returns the name of the {@link CommandArgument} created the output of this object
	 * @return the name of the {@link CommandArgument} created the output of this object
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the {@link CommandArgument} created the output of this object
	 * @return
	 */
	public CommandArgument<T> getArgument() {
		return argument;
	}

	/**
	 * Returns the output from {@link CommandArgument} this object hold TODO
	 * @return Returns the output from {@link CommandArgument} this object hold
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Set the output from {@link CommandArgument} this object hold
	 * @param value The value to set
	 */
	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ArgumentValue [name=" + name + ", argument=" + argument + ", value=" + value + "]";
	}

}
