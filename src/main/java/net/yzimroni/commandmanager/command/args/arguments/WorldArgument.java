package net.yzimroni.commandmanager.command.args.arguments;

import java.util.ArrayList;
import java.util.List;

import net.yzimroni.commandmanager.command.args.ArgumentParseData;
import net.yzimroni.commandmanager.command.args.ArgumentValidCheck;
import net.yzimroni.commandmanager.command.args.CommandArgument;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldArgument extends CommandArgument<World> {

	public WorldArgument(String name, boolean require) {
		super(name, require);
	}

	@Override
	public String getInputType() {
		return "World";
	}

	@Override
	public String getValidInputs() {
		return "World name";
	}

	@Override
	public ArgumentValidCheck isValidInput(ArgumentParseData data) {
		if (Bukkit.getWorld(data.getInput()) != null) {
			return ArgumentValidCheck.create(true);
		}
		return ArgumentValidCheck.create(false, "World not found");
	}

	@Override
	public World getInput(ArgumentParseData data) {
		return Bukkit.getWorld(data.getInput());
	}

	@Override
	public boolean isVarArgs() {
		return false;
	}

	@Override
	public List<String> getTabCompleteOptions(ArgumentParseData data) {
		List<String> names = new ArrayList<String>();
		String input = data.getInput().trim();
		for (World w : Bukkit.getWorlds()) {
			if (input.isEmpty() || w.getName().toLowerCase().startsWith(input.toLowerCase())) {
				names.add(w.getName());
			}
		}
		return names;
	}
	
	

}
