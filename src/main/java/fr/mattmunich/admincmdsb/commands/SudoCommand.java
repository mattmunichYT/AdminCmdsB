package fr.mattmunich.admincmdsb.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;

public class SudoCommand implements CommandExecutor {

	private Main main;

	public SudoCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender p, Command cmd, String l, String[] args) {

		if (p instanceof BlockCommandSender) {
			p.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		if (p instanceof Player) {
			Player rp = (Player) p;

			if (!main.superstaff.contains(rp)) {
				rp.sendMessage(main.noPermissionMsg);
				return true;
			}
		}

		if (args.length < 1) {
			p.sendMessage("§cSintax : /sudo <Player> <Command/Message>");
			return true;
		}

		String tname = args[0];

		if (Bukkit.getPlayer(tname) == null) {
			p.sendMessage(main.getPrefix() + "§4Le joueur est hors ligne ou n'existe pas !");
			return true;
		}

		Player t = Bukkit.getPlayer(tname);

		String command = "";

		for (int i = 1; i < args.length; i++) {
			command = command + args[i] + " ";

		}

		command = command.trim();

		if (command.startsWith("c:")) {
			command.replaceFirst("c:", "");
		}

		t.chat(command);
		p.sendMessage(
				main.getPrefix() + "§2Le joueur §6" + tname + "§2 a dit/utilisé la commande §6" + command + "§2 !");

		return true;
	}

}
