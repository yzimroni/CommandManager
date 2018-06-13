package net.yzimroni.commandmanager.utils;

import java.util.List;

public class FlagsData {

	private List<String> flags;
	
	public FlagsData(List<String> flags) {
		this.flags = flags;
	}
	
	public boolean hasFlag(String name) {
		return flags.contains(name.toLowerCase());
	}
	
	public List<String> getFlags() {
		return flags;
	}

	@Override
	public String toString() {
		return "FlagsData [flags=" + flags + "]";
	}
	
}
