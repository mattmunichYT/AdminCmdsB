package fr.mattmunich.admincmdsb.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;
import fr.mattmunich.admincmdsb.Utility;
import fr.mattmunich.admincmdsb.commandhelper.PlayerData;

public class HomeTab implements TabCompleter {

	@SuppressWarnings("unused")
	private Main main;

	public HomeTab(Main main) {
		this.main = main;
	}

	List<String> arguments = new ArrayList<>();
	@SuppressWarnings("unused")
	@Override
	public List<String> onTabComplete(CommandSender s, Command c, String l, String[] args) {
		if(arguments.isEmpty()) {

			Player p = (Player) s;

			PlayerData data = new PlayerData(Utility.getUUIDFromName(p.getName()));

			try {
				//p.sendMessage(main.getPrefix() + " HOMES LIST: " + data.getHomes();
				String homeList = data.getHomes();
				arguments = Arrays.asList(homeList.split(","));
			} catch (Exception e) {
				System.err.println(main.errorMsg);
			}

		}

		List<String> result = new ArrayList<>();
		if(args.length == 1) {
			for (String a : arguments) {
				if(a.toLowerCase().startsWith(args[0].toLowerCase())) {
					result.add(a);
				}
			}
			return result;
		}
		return null;
	}

}
