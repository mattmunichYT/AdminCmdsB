package fr.mattmunich.monplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.monplugin.MonPlugin;
import fr.mattmunich.monplugin.commandhelper.Grades;

public class EcSeeCommand implements CommandExecutor {

	private MonPlugin main;
	private Grades grades;

	public EcSeeCommand(MonPlugin main, Grades grades) {
		this.main = main;
		this.grades = grades;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(main.getPrefix() + "§4Vous devez etre un joueur pour utiliser cette commande !");
			return true;
		}

		Player player = (Player) sender;

		if (!main.superstaff.contains(player)) {
			player.sendMessage(main.noPermissionMsg);
			return true;
		}

		if (!(args.length == 1)) {
			player.sendMessage("§cSintax : /ecsee <Player>");
			return true;
		}

		String targetName = args[0];

		if (Bukkit.getPlayer(targetName) == null) {
			player.sendMessage(main.getPrefix() + "§4Le joueur est hors-ligne ou n'éxiste pas !");
//			OfflinePlayer ofp = Bukkit.getOfflinePlayer(targetName);
//			Player t = ofp.getPlayer();
//			player.openInventory(t.getEnderChest());
//			player.updateInventory();
//			t.updateInventory();
//
			return true;
		}

		Player target = Bukkit.getPlayer(targetName);

		if (grades.hasPowerInf(player, grades.getPlayerGrade(target).getPower())) {
			player.sendMessage(main.getPrefix()
					+ "§4Vous n'avez pas la permission de voir §cl'Ender Chest§4 de joueurs plus haut gradés que vous !");
			return true;
		}

		player.openInventory(target.getEnderChest());
		target.updateInventory();
		player.updateInventory();

		return true;
	}
}
