package fr.mattmunich.monplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.monplugin.MonPlugin;
import fr.mattmunich.monplugin.commandhelper.Grades;

public class FreezeCommand implements CommandExecutor {

	private MonPlugin main;

	private Grades grades;

	public FreezeCommand(MonPlugin main, Grades grades) {
		this.main = main;
		this.grades = grades;
	}

	@Override
	public boolean onCommand(CommandSender player, Command cmd, String label, String[] args) {

		if (player instanceof BlockCommandSender) {
			player.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		if (player instanceof Player) {
			Player p = (Player) player;

			if (!(main.superstaff.contains(p))) {
				p.sendMessage(main.noPermissionMsg);
				return true;
			}
		}

		if (args.length == 0 || args.length > 1) {
			player.sendMessage("§cSintax : /freeze <player>");
			return true;
		}

		String targetName = args[0];

		if (Bukkit.getPlayer(targetName) == null) {
			player.sendMessage(main.getPrefix() + "§4Le joueur est hors-ligne ou n'éxiste pas !");
			return true;
		}

		Player target = Bukkit.getPlayer(targetName);

		if (player instanceof Player) {
			Player p = (Player) player;
			if (grades.hasPowerInf(p, grades.getPlayerGrade(target).getPower())) {
				player.sendMessage(main.getPrefix()
						+ "§4Vous n'avez pas la permission de§cfreeze§4 un joueur plus haut gradé que vous !");
				return true;
			}
		}

		if (!main.freeze.contains(target)) {
			main.freeze.add(target);

			player.sendMessage(main.getPrefix() + "§2Vous avez freeze §6" + target.getName() + "§2 !");
			target.sendMessage(main.getPrefix() + "§4Vous avez été freeze par §6" + player.getName() + "§4 !");
			Bukkit.broadcastMessage("§1[§bFreeze§r§1] §6" + target.getName() + " §3à été freeze §3!");
			return true;
		} else {
			main.freeze.remove(target);
			player.sendMessage(main.getPrefix() + "§4Vous avez unfreeze §6" + target.getName() + "§4 !");
			target.sendMessage(main.getPrefix() + "§2Vous avez été unfreeze par §6" + player.getName() + "§2 !");
			Bukkit.broadcastMessage("§1[§bFreeze§r§1] §6" + target.getName() + " §3à été unfreeze §3!");
			return true;
		}
	}

}
