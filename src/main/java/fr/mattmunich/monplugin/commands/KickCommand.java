package fr.mattmunich.monplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.monplugin.MonPlugin;
import fr.mattmunich.monplugin.commandhelper.Ban;
import fr.mattmunich.monplugin.commandhelper.Grades;

public class KickCommand implements CommandExecutor {

	private MonPlugin main;

	private final Ban ban;

	private Grades grades;


	public KickCommand(MonPlugin main, Ban ban, Grades grades) {
		this.main = main;
		this.ban = ban;
		this.grades = grades;

	}

	@Override
	public boolean onCommand(CommandSender player, Command cmd, String label, String[] args) {

		if(player instanceof BlockCommandSender) {
			player.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}


		if(player instanceof Player) {
			Player p = (Player) player;

			if(!(main.superstaff.contains(p))) {
				p.sendMessage(main.noPermissionMsg);
				return true;
			}
		}

		if(args.length < 1) {
			player.sendMessage("§cSintax : /kick <player> [reason]");
			return true;
		}

		String targetName = args[0].toString();

		if(Bukkit.getPlayer(targetName) == null) {
			player.sendMessage(main.getPrefix() + "§4Ce joueur est hors-ligne ou n'existe pas !");
			return true;
		}

		Player target = Bukkit.getPlayer(targetName);

		if(player instanceof Player) {
			Player p = (Player) player;
			if(grades.hasPowerInf(p, grades.getPlayerGrade(target).getPower())) {
				player.sendMessage(main.getPrefix() + "§4Vous n'avez pas la permission de§ckick§4 un joueur plus haut gradé que vous !");
				return true;
			}
		}
		String reason = " ";
		for(int i = 1 ; i<args.length; i++) {
			reason = reason + " " + args[i];
		}

		reason = reason.trim();

		reason = ChatColor.translateAlternateColorCodes('&', main.hex(reason));
		if(reason == null) {
			ban.kickPlayer(target, "");
			return true;
		}
		ban.kickPlayer(target, reason);
		return true;

	}

}
