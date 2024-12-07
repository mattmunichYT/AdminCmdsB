package fr.mattmunich.monplugin.commands;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.monplugin.MonPlugin;

public class SpawnCommand implements CommandExecutor {

	private MonPlugin main;

	public SpawnCommand(MonPlugin main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof BlockCommandSender) {
			sender.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}


		if (label.equalsIgnoreCase("setspawn")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(main.getPrefix() + "§4Vous devez etre un joueur pour utiliser cette commande !");
				return true;
			}

			Player player = (Player) sender;

			if (!(main.superstaff.contains(player))) {
				player.sendMessage(main.noPermissionMsg);
				return true;
			}

			if (args.length > 0) {
				player.sendMessage(
						main.getPrefix() + "§4Cette commande ne contient pas d'arguments ! \n§cSintax : /setspawn");
				return true;
			}

			player.getWorld().setSpawnLocation(player.getLocation().getBlockX(), player.getLocation().getBlockY(),
					player.getLocation().getBlockZ());
			player.sendMessage(main.getPrefix() + "§2Le spawn du serveur a été défini à vorte position !");
			return true;
		}

		return true;
	}

}
