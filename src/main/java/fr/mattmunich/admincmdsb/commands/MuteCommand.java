package fr.mattmunich.admincmdsb.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;
import fr.mattmunich.admincmdsb.commandhelper.PlayerData;

public class MuteCommand implements CommandExecutor {

	private Main main;

	public MuteCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender player, Command cmd, String label, String[] args) {

		if (player instanceof BlockCommandSender) {
			player.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		if (label.equalsIgnoreCase("mute")) {
			if (player instanceof Player) {
				Player p = (Player) player;

				if (!(main.superstaff.contains(p))) {
					p.sendMessage(main.noPermissionMsg);
					return true;
				}
			}
			if (args.length < 1) {
				player.sendMessage("§cSinax : /mute <player> [reason]");
				return true;
			}

			String targetName = args[0].toString();

			if (Bukkit.getPlayer(targetName) == null) {
				player.sendMessage(main.getPrefix() + "§4Le joueur est hors-ligne ou n'existe pas !");
				return true;
			}

			Player target = Bukkit.getPlayer(targetName);

			PlayerData data = new PlayerData(target);

            data.setMuted(true);
			String reason = " ";
			for (int i = 1; i < args.length; i++) {
				reason = reason + " " + args[i];
			}

			reason = reason.trim();

			player.sendMessage(main.getPrefix() + "§2Le joueur §6" + target.getName() + "§2 a été mute avec succès !");
			target.sendMessage(main.getPrefix() + "§4Tu as été mute par §6" + player.getName() + " §4!"
					+ (reason.isEmpty() ? "" : " §4Raison : §6" + reason.toString()));

			/*
			 * String uuid = target.getUniqueId().toString();
			 * if(!grades.getConfig().contains(uuid)) { player.sendMessage(main.getPrefix()
			 * + "§4Le joueur ne s'est jamais connecté !"); return true; }
			 * 
			 * GradeList gradeList = null;
			 * 
			 * gradeList = GradeList.MUTED;
			 * 
			 * grades.chageRank(uuid, gradeList);
			 */

			Bukkit.broadcastMessage(
					main.getBanPrefix() + "§6" + target.getName() + "§e a été mute par un memebre du §2§lStaff§e !"
							+ (reason.isEmpty() ? "" : " §eRaison : §6" + reason.toString()));

		}

		return true;

	}

}
