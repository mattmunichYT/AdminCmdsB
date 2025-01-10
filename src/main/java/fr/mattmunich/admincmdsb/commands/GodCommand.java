package fr.mattmunich.admincmdsb.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;

public class GodCommand implements CommandExecutor {

	private Main main;

	public GodCommand(Main main) {
		this.main = main;
	}



	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof BlockCommandSender) {
			sender.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}



		if(!(sender instanceof Player)) {
			sender.sendMessage(main.getPrefix() + "§4Vous devez etre un joueur pour executer cette commande !");
			return true;
		}

		Player player = (Player) sender;

		if (!main.staff.contains(player)) {
			player.sendMessage(main.getPrefix() + "§4Vous n'avez pas la permission d'utiliser cette commande !");
			return true;
		}

		if(args.length == 0) {
			if(!player.isInvulnerable()) {
				if(!(main.superstaff.contains(player))) {
					player.sendMessage(main.noPermissionMsg);
					return true;
				}
				player.setInvulnerable(true);
				player.setFoodLevel(20);
				player.sendMessage(main.getPrefix() + "§2Le mode §6God§2 a été activé !");
				return true;
			}
			if(player.isInvulnerable()) {
				player.setInvulnerable(false);
				player.sendMessage(main.getPrefix() + "§4Le mode §6God§4 a été désactivé !");
				return true;
			}
			return true;
		}

		if(args.length == 1) {

			if(!(main.superstaff.contains(player))) {
				player.sendMessage(main.noPermissionMsg);
				return true;
			}

			String targetName = args[0];

			if(Bukkit.getPlayer(targetName) == null) {
				player.sendMessage(main.getPrefix() + "§4Le joueur est hors-ligne ou n'éxiste pas !");
				return true;
			}

			Player target = Bukkit.getPlayer(targetName);

			if(!main.god.contains(target)) {
				main.god.add(target);
				target.setInvulnerable(true);
				target.setFoodLevel(20);
				target.sendMessage(main.getPrefix() + "§2Le mode \"§6God§2\" a été activé par §6 " + player.getName());
				player.sendMessage(main.getPrefix() + "§2Le mode \"§6God§2\" a été activé pour " + target.getName());
				return true;
			}

			if(main.god.contains(target)) {
				main.god.remove(target);
				target.setInvulnerable(true);
				target.setHealth(target.getHealthScale());
				target.sendMessage(main.getPrefix() + "§4Le mode \"§6God§4\" a été déactivé par §6 " + player.getName());
				player.sendMessage(main.getPrefix() + "§2Le mode \"§6God§2\" a été désactivé pour " + target.getName());
				return true;
			}

			return true;
		} else {
			player.sendMessage("§cSintax : /god [player]");
			return true;
		}



	}

}
