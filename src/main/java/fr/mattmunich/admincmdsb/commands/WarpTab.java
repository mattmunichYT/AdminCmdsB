package fr.mattmunich.admincmdsb.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import fr.mattmunich.admincmdsb.commandhelper.Warp;

public class WarpTab implements TabCompleter {

	private Warp warp;

	public WarpTab(Warp warp) {
		this.warp = warp;
	}

	List<String> arguments = new ArrayList<>();

	@Override
	public List<String> onTabComplete(CommandSender s, Command c, String l, String[] args) {
		if (arguments.isEmpty()) {
			String warps = warp.getConfig().get("warp.list").toString();
			if (!warps.isEmpty()) {
				arguments = Arrays.asList(warps.split(","));
			}
		}

		List<String> result = new ArrayList<>();
		if (args.length == 1) {
			for (String a : arguments) {
				if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
					result.add(a);
				}
			}
			return result;
		}
		return null;
	}

}
