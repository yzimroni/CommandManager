package net.yzimroni.commandmanager.command.args.arguments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.yzimroni.commandmanager.command.args.ArgumentParseData;
import net.yzimroni.commandmanager.command.args.ArgumentValidCheck;
import net.yzimroni.commandmanager.command.args.CommandArgument;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class PlayerArgument extends CommandArgument<Player> {
	
	private boolean returnSource = false;

	public PlayerArgument(String name, boolean require, boolean returnSource) {
		super(name, require);
		this.returnSource = returnSource;
	}
	
	public PlayerArgument(String name, boolean require) {
		this(name, require, false);
	}

	
	
	@Override
	public String getInputType() {
		return "Player";
	}

	@Override
	public String getValidInputs() {
		return "Player name";
	}

	@Override
	public ArgumentValidCheck isValidInput(ArgumentParseData data) {
        Player senderPlayer = data.getCommandSender() instanceof Player ? (Player) data.getCommandSender() : null;
        Player target = Bukkit.getPlayer(data.getInput());
		return ArgumentValidCheck.create((target != null && (senderPlayer == null || senderPlayer.canSee(target))) || canReturnSource(data.getCommandSender()), "Player not found");
	}
	
	private boolean canReturnSource(CommandSender sender) {
		if (sender instanceof Player) {
			if (!isRequire() && returnSource) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Player getInput(ArgumentParseData data) {
		Player p = Bukkit.getPlayer(data.getInput());
		if (p == null && canReturnSource(data.getCommandSender()) && data.getCommandSender() instanceof Player) {
			p = (Player) data.getCommandSender();
		}
		return p;
	}

	@Override
	public boolean isVarArgs() {
		return false;
	}

	@Override
	public List<String> getTabCompleteOptions(ArgumentParseData data) {
        String lastWord = data.getInput();

        Player senderPlayer = data.getCommandSender() instanceof Player ? (Player) data.getCommandSender() : null;

        ArrayList<String> matchedPlayers = new ArrayList<String>();
        for (Player player : data.getCommandSender().getServer().getOnlinePlayers()) {
            String name = player.getName();
            if ((senderPlayer == null || senderPlayer.canSee(player)) && StringUtil.startsWithIgnoreCase(name, lastWord)) {
                matchedPlayers.add(name);
            }
        }

        Collections.sort(matchedPlayers, String.CASE_INSENSITIVE_ORDER);
		return matchedPlayers;
	}

	public boolean isReturnSource() {
		return returnSource;
	}

	public void setReturnSource(boolean returnSource) {
		this.returnSource = returnSource;
	}
	
}
