package fr.mattmunich.monplugin.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class PlTab implements TabCompleter {

	List<String> arguments = new ArrayList<>();
	List<String> arguments2 = new ArrayList<>();
	List<String> arguments3 = new ArrayList<>();

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

		if (arguments.isEmpty()) {
			arguments.add("settings");
			arguments.add("grades");
			arguments.add("credits");
			arguments.add("version");
			arguments.add("reload");

		}

		if (arguments2.isEmpty()) {
			arguments2.add("coMsg");
			arguments2.add("serverName");
			arguments2.add("customTabList");
			arguments2.add("seeVanished");
			arguments2.add("oldPVP");
			arguments2.add("TNTsEnabled");
			arguments2.add("advancedNameTags");
		}

		if (arguments3.isEmpty()) {
			arguments3.add("true");
			arguments3.add("false");
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
		if (args.length == 2) {
			for (String a : arguments2) {
				if (a.toLowerCase().startsWith(args[1].toLowerCase())) {
					result.add(a);
				}
			}
			return result;
		}
		if (args.length == 3) {
			for (String a : arguments3) {
				if (a.toLowerCase().startsWith(args[2].toLowerCase())) {
					result.add(a);
				}
			}
			return result;
		}

		return null;
	}

}
