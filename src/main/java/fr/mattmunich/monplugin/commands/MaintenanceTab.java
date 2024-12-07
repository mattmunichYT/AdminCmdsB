package fr.mattmunich.monplugin.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class MaintenanceTab implements TabCompleter {

	List<String> arguments = new ArrayList<>();

	@Override
	public List<String> onTabComplete(CommandSender s, Command c, String l, String[] args) {
		if(arguments.isEmpty()) {
			arguments.add("on");
			arguments.add("off");

		}
		return arguments;
	}

}
