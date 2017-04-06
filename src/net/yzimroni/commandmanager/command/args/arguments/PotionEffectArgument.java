package net.yzimroni.commandmanager.command.args.arguments;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.potion.PotionEffectType;

import net.yzimroni.commandmanager.command.args.ArgumentParseData;
import net.yzimroni.commandmanager.command.args.ArgumentValidCheck;
import net.yzimroni.commandmanager.command.args.CommandArgument;

public class PotionEffectArgument extends CommandArgument<PotionEffectType> {

	public PotionEffectArgument(String name) {
		super(name);
	}

	public PotionEffectArgument(String name, boolean require) {
		super(name, require);
	}

	public PotionEffectArgument(String name, String displayName, boolean require, String permission) {
		super(name, displayName, require, permission);
	}

	public PotionEffectArgument(String name, String displayName, boolean require) {
		super(name, displayName, require);
	}

	@Override
	public String getInputType() {
		return "Potion effect type";
	}

	@Override
	public String getValidInputs() {
		return "Potion effect type";
	}

	@Override
	public ArgumentValidCheck isValidInput(ArgumentParseData data) {
		if (PotionEffectType.getByName(data.getInput()) != null) {
			return ArgumentValidCheck.create(true);
		}
		//TODO add support for more name styles
		return ArgumentValidCheck.create(false, "Potion effect type not found");
	}

	@Override
	public PotionEffectType getInput(ArgumentParseData data) {
		return PotionEffectType.getByName(data.getInput());
	}

	@Override
	public List<String> getTabCompleteOptions(ArgumentParseData data) {
		PotionEffectType[] temp = PotionEffectType.values();
		List<String> options = new ArrayList<String>();
		String input = data.getInput().toLowerCase();
		for (PotionEffectType type : temp) {
			if (type != null) {
				if (type.getName().toLowerCase().startsWith(input)) {
					options.add(type.getName());
				}
			}
		}
		return options;
	}

	@Override
	public boolean isVarArgs() {
		return false;
	}
	
	

}
