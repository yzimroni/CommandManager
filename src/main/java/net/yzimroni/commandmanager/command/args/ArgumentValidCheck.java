package net.yzimroni.commandmanager.command.args;

public class ArgumentValidCheck {

	private boolean valid;
	private String message;

	private ArgumentValidCheck(boolean valid, String message) {
		this.valid = valid;
		this.message = message;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	public boolean hasMessage() {
		return message != null && !message.isEmpty();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static ArgumentValidCheck create(boolean valid) {
		ArgumentValidCheck a = new ArgumentValidCheck(valid, null);
		return a;
	}

	public static ArgumentValidCheck create(boolean valid, String message) {
		ArgumentValidCheck a = new ArgumentValidCheck(valid, message);
		return a;
	}

	@Override
	public String toString() {
		return "ArgumentValidCheck [valid=" + valid + ", message=" + message + ", hasMessage()=" + hasMessage() + "]";
	}



}
