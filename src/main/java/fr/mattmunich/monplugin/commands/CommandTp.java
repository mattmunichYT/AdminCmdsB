package fr.mattmunich.monplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.monplugin.MonPlugin;

public class CommandTp implements CommandExecutor {

	private MonPlugin main;

	public CommandTp(MonPlugin main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof BlockCommandSender) {
			sender.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage(main.getPrefix() + "§4Vous devez etre un §6joueur§4 pour executer cette commande !");
			return true;
		}
		Player player = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("tphere")) {

			if (!(main.staff.contains(player))) {
				player.sendMessage(main.noPermissionMsg);
				return true;
			}

			if (args.length == 0 || args.length > 1) {
				sender.sendMessage("§cSintax : /tphere <player>");
				return true;
			}

			if (args[0].equalsIgnoreCase("@a")) {

				if (!(main.superstaff.contains(player))) {
					player.sendMessage(main.getPrefix() + "§4Vous n'avez pas la permission d'utiliser cet argument !");
					return true;
				}

				for (Player all : Bukkit.getOnlinePlayers()) {
					if (all.getName() != player.getName()) {
						all.teleport(player);
						all.sendMessage(
								main.getPrefix() + "§2Vous avez été TpHere comme tous les joueurs du serveur !");
					}
				}
				player.sendMessage(main.getPrefix() + "§6" + (Bukkit.getOnlinePlayers().size() - 1)
						+ "§2 joueurs ont été téléportés à vous !");
				return true;
			}

			String targetName = args[0];

			if (Bukkit.getPlayer(targetName) == null) {
				player.sendMessage(main.getPrefix() + "§4Le joueur est hors-ligne ou n'éxiste pas !");
				return true;
			}

			Player target = Bukkit.getPlayer(targetName);

			target.teleport(player);
			player.sendMessage(main.getPrefix() + "§6" + target.getName() + " §2a été téléporté à votre position !");

			return true;

		}
		return true;
	}

}
