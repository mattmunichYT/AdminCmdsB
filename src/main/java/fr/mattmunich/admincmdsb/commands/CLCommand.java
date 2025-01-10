package fr.mattmunich.admincmdsb.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;

public class CLCommand implements CommandExecutor {

	private Main main;

	public CLCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender player, Command cmd, String label, String[] args) {

		if(player instanceof Player) {
			Player p = (Player)player;

			if(!(main.superstaff.contains(p))) {
				p.sendMessage(main.noPermissionMsg);
				return true;
			}

		}

		if(args.length > 0) {
			player.sendMessage(main.getPrefix() + "§4Cette commande ne comporte pas d'arguments !");
			return true;
		}

		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[type=item]");
		Bukkit.broadcastMessage(main.getClearLagMessage());

		return true;
	}

}
