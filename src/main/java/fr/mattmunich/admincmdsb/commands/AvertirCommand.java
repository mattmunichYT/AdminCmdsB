package fr.mattmunich.admincmdsb.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;

public class AvertirCommand implements CommandExecutor {

	private Main main;

	public AvertirCommand(Main main) {
		this.main = main;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof BlockCommandSender) {
			sender.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage(main.getPrefix() + "§4Vous devez etre un joueur pour executer cette commande !");
			return true;
		}

		Player player = (Player) sender;

		if (!(main.superstaff.contains(player))) {
			player.sendMessage(main.noPermissionMsg);
			return true;
		}

		if (args.length == 0 || args.length == 1 || args.length > 2) {
			sender.sendMessage("§cSintax : /avertir <public/discret> <player>");
			return true;
		}

		if (args[0].equalsIgnoreCase("public")) {

			String targetName = args[1];

			if (Bukkit.getPlayer(targetName) == null) {
				player.sendMessage(main.getPrefix() + "§4Le joueur est hors-ligne ou n'éxiste pas !");
				return true;
			}

			Player target = Bukkit.getPlayer(targetName);

			player.sendMessage(target.getPlayerListName() + " §2à été averti §6publiquement §2!");
			target.sendMessage(
					main.getPrefix() + "§4Vous avez été averti par un §lAdministrateur§r§4 \n §6Passe Discord !");
			target.sendTitle("", "§4§lVous avez été averti !", 20, 20, 20);
			target.attack(target);
			main.freeze.add(target);
			target.setGameMode(GameMode.ADVENTURE);
			target.setInvulnerable(true);
			target.getWorld().spawnEntity(target.getLocation(), EntityType.FIREWORK_ROCKET);
			target.getWorld().spawnEntity(target.getLocation(), EntityType.FIREWORK_ROCKET);
			target.getWorld().spawnEntity(target.getLocation(), EntityType.FIREWORK_ROCKET);
			target.getWorld().spawnEntity(target.getLocation(), EntityType.FIREWORK_ROCKET);
			target.getWorld().spawnEntity(target.getLocation(), EntityType.FIREWORK_ROCKET);
			target.getWorld().spawnEntity(target.getLocation(), EntityType.FIREWORK_ROCKET);
			target.setInvulnerable(false);
			Bukkit.broadcastMessage("§e[§6Bans§e] §6" + target.getName() + " §2à été averti §6publiquement §2!");

			return true;
		}

		if (args[0].equalsIgnoreCase("discret")) {

			String targetName = args[1];

			if (Bukkit.getPlayer(targetName) == null) {
				player.sendMessage(main.getPrefix() + "§4Le joueur est hors-ligne ou n'éxiste pas !");
				return true;
			}

			Player target = Bukkit.getPlayer(targetName);

			player.sendMessage(target.getPlayerListName() + " §2à été averti §6discrètement §2!");
			target.sendMessage(main.getPrefix()
					+ "§4Vous avez été averti par un §lAdministrateur§r§4 \n §6Viens Discord : https://discord.gg/ChxuabKHpQ !");
			target.sendTitle("", "§4§lVous avez été averti !");
			target.attack(target);
			main.freeze.add(target);
			target.setGameMode(GameMode.ADVENTURE);
			target.setInvulnerable(true);
			target.getWorld().spawnEntity(target.getLocation(), EntityType.FIREWORK_ROCKET);
			target.getWorld().spawnEntity(target.getLocation(), EntityType.FIREWORK_ROCKET);
			target.getWorld().spawnEntity(target.getLocation(), EntityType.FIREWORK_ROCKET);
			target.setInvulnerable(false);

			return true;
		}

		return true;
	}
}
