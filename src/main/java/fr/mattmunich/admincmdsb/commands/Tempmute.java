package fr.mattmunich.admincmdsb.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;
import fr.mattmunich.admincmdsb.Utility;
import fr.mattmunich.admincmdsb.commandhelper.Grades;
import fr.mattmunich.admincmdsb.commandhelper.PlayerData;

public class Tempmute implements CommandExecutor{

	private Main main;

	private Grades grades;

	public Tempmute(Main main, Grades grades) {
		this.main = main;
		this.grades = grades;
	}

	@Override
	public boolean onCommand(CommandSender p, Command cmd, String label, String[] args) {

		if(p instanceof BlockCommandSender) {
			p.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		if(p instanceof Player) {
			Player player = (Player) p;
			if (!main.superstaff.contains(player)) {
				player.sendMessage(main.getPrefix() + "§4Vous n'avez pas la permission d'utiliser cette commande !");
				return true;
			}
		}

		if(args.length < 3) {
			p.sendMessage("§cSintax : /tempmute <player> <time> <reason>");
			return true;
		}

		String format = args[1].substring(args[1].length() - 1, args[1].length());
		//String format = args[1];
		int duration = Integer.valueOf(args[1].substring(0, args[1].length() - 1));


		switch(format) {

		case "s":
			duration = duration * 1000;
			//Bukkit.getConsoleSender().sendMessage("Duration : " + duration  + " Time : " + time + " Current Millis : " + System.currentTimeMillis());

			break;

		case "m":
			duration = duration * 1000 * 60;
			break;

		case "h":
			duration = duration * 1000 * 60 * 60;
			break;

		case "d":
			duration = duration * 1000 * 60 * 60 * 24;
			break;

		case "w":
			duration = duration * 1000 * 60 * 60 * 24 * 7;
			break;

			default:
				p.sendMessage(main.getPrefix() + "§4Format de temps non reconnu !");
				return true;
		}

		if(args.length >= 3) {

			String reason = "";

			for(int i = 2; i < args.length; i++) {
				reason = reason + args[i] + " ";

			}

			reason = reason.trim();


			PlayerData data = new PlayerData(Utility.getUUIDFromName(args[0]));

			if(!data.exist()) {
				p.sendMessage(main.getPrefix() + "§4Le joueur est introuvable !");
				return true;
			}
			if(reason.isEmpty()) {
				reason = "§8§oAucune";
			}

			if(data.isTempmuted()) {
				p.sendMessage(main.getPrefix() + "§4Le joueur §6" + args[0] + "§4 est déjà mute !");
				return true;
			}

			if(p instanceof Player) {

				if(Bukkit.getPlayer(args[0]) != null) {
					Player target = Bukkit.getPlayer(args[0]);
					Player player = (Player) p;
					if(grades.hasPowerInf(player, grades.getPlayerGrade(target).getPower())) {
						player.sendMessage(main.getPrefix() + "§4Vous n'avez pas la permission de §cmute§4 un joueur plus haut gradé que vous !");
						return true;
					}
				}

			}

			//data.setTempbanned(p.getName(), reason.isEmpty() ? "§8§oAucune" : "§6" + reason, time, args[1]);
			data.setTempmuted(p.getName(), "§6" + reason, duration, args[1]);



			Player t = Bukkit.getPlayer(args[0]);

			if(t != null) {
				main.mute.add(t);
			}

			p.sendMessage(main.getPrefix() + "§2Le joueur §6" + t.getName() + "§2 a été mute avec succès !");

			Bukkit.broadcastMessage(main.getBanPrefix() + "§2Le joueur §6" + t.getName() + "§2 a été temporairement mute jusqu'au §6" + data.getTempmuteEnd() + "§2, par §6" + p.getName() + " §2" + (reason.isEmpty() ? "!" : ", raison : §6" + reason));


		return true;

		}
		return true;
	}

}
