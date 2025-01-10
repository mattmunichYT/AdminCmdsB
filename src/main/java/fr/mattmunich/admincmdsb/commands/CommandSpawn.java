package fr.mattmunich.admincmdsb.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpawn implements CommandExecutor {

	//EVENT
//	private final MonPlugin main;
//
//	public CommandSpawn(MonPlugin main) {
//		this.main = main;
//	}
	//END

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof BlockCommandSender) {
			sender.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		//EVENT
//		if(sender instanceof Player) {
//			Player p = (Player)sender;
//			if(!main.superstaff.contains(p)) {
//				p.sendMessage(main.getErrorPrefix() + "Cette commande est désactivée durant l'Évent d'Halloween !");
//				return true;
//			}
//		}
		//END

		if(sender instanceof Player) {
			Player player = (Player) sender;

			if(args.length == 1 && (args[0].equalsIgnoreCase("MJEP-Normal") || args[0].equalsIgnoreCase("normal") || args[0].equalsIgnoreCase("main"))) {
				Location spawn = Bukkit.getWorld("world").getSpawnLocation();
				player.sendMessage("§e(§6!§e) §2Vous avez été téléporté au spawn !");
				player.teleport(spawn);
				return true;
			}
//			else if(args.length == 1 && args[0].equalsIgnoreCase("MJEP-Noel")) {
//				player.chat("/noel");
//				Location spawn = Bukkit.getWorld("noel").getSpawnLocation();
//				player.sendMessage("§e(§6!§e) §2Vous avez été téléporté au spawn du monde §4N§co§fë§bl !");
//				player.teleport(spawn);
//				return true;
//			}

			Location spawn = player.getWorld().getSpawnLocation();
			player.sendMessage("§e(§6!§e) §2Vous avez été téléporté au spawn !");
			player.teleport(spawn);

			return true;

		} else {
			sender.sendMessage("§cVous devez etre un joueur pour utiliser cette commande !");
		}


		return true;
	}

}
