package fr.mattmunich.admincmdsb.commands;

import java.time.Duration;

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
import fr.mattmunich.admincmdsb.commandhelper.Settings;

public class Tempban implements CommandExecutor {

	private Main main;

	private Grades grades;

	private Settings settings;

	public Tempban(Main main, Grades grades, Settings settings) {
		this.main = main;
		this.grades = grades;
		this.settings = settings;
	}

	@Override
	public boolean onCommand(CommandSender p, Command cmd, String label, String[] args) {

		if (p instanceof BlockCommandSender) {
			p.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		if (p instanceof Player) {
			Player player = (Player) p;
			if (!main.superstaff.contains(player)) {
				player.sendMessage(main.getPrefix() + "§4Vous n'avez pas la permission d'utiliser cette commande !");
				return true;
			}
		}

		if (args.length < 3) {
			p.sendMessage("§cSintax : /tempban <player> <time> <reason>");
			return true;
		}

		String format = args[1].substring(args[1].length() - 1, args[1].length());
		// String format = args[1];
		long duration = Integer.valueOf(args[1].substring(0, args[1].length() - 1));

		Duration dur = Duration.ofMillis(duration);
		long before;

		switch (format) {

		case "s":
			duration = dur.plusSeconds(duration).toMillis();
			before = duration * 1000;
			// Bukkit.getConsoleSender().sendMessage("Duration : " + duration + " Time : " +
			// time + " Current Millis : " + System.currentTimeMillis());
			break;

		case "m":
			duration = dur.plusMinutes(duration).toMillis();
			before = duration * 1000 * 60;
			break;

		case "h":
			duration = dur.plusHours(duration).toMillis();
			before = duration * 1000 * 60 * 60;
			break;

		case "d":
			duration = dur.plusDays(duration).toMillis();
			before = duration * 1000 * 60 * 60 * 24;
			break;

		case "w":
			duration = dur.plusDays(duration).toMillis() * 7;
			before = duration * 1000 * 60 * 60 * 24 * 7;
			break;

		case "y":
			duration = dur.plusDays(duration).toMillis() * 365;
			before = duration * 1000 * 60 * 60 * 24 * 365;
			break;

		default:
			p.sendMessage(main.getPrefix()
					+ "§4Format de temps non reconnu !§a Utilisez 1m (=> minute), 1h (=> heure), 1d (=> jour), 1w (=> semaine) ou 1y (=> année)");
			return true;
		}

		Bukkit.getConsoleSender().sendMessage("Duration : " + duration + " ; before : " + before);

		if (args.length >= 3) {

			String reason = "";

			for (int i = 2; i < args.length; i++) {
				reason = reason + args[i] + " ";

			}

			reason = reason.trim();

			PlayerData data = new PlayerData(Utility.getUUIDFromName(args[0]));

			if (!data.exist()) {
				p.sendMessage(main.getPrefix() + "§4Le joueur est introuvable !");
				return true;
			}
			if (reason.isEmpty()) {
				reason = "§8§oAucune";
			}

			if (data.isTempbanned()) {
				p.sendMessage(main.getPrefix() + "§4Le joueur §6" + args[0] + "§4 est déjà banni !");
				return true;
			}

			if (p instanceof Player) {

				if (Bukkit.getPlayer(args[0]) != null) {
					Player target = Bukkit.getPlayer(args[0]);
					Player player = (Player) p;
					if (grades.hasPowerInf(player, grades.getPlayerGrade(target).getPower())) {
						player.sendMessage(main.getPrefix()
								+ "§4Vous n'avez pas la permission de §cban§4 un joueur plus haut gradé que vous !");
						return true;
					}
				}

			}

			if (p instanceof Player) {

				if (Bukkit.getPlayer(args[0]) != null) {
					Player target = Bukkit.getPlayer(args[0]);
					Player player = (Player) p;
					if (grades.hasPowerInf(player, grades.getPlayerGrade(target).getPower())) {
						player.sendMessage(main.getPrefix()
								+ "§4Vous n'avez pas la permission de §cban§4 un joueur plus haut gradé que vous !");
						return true;
					}
				}

			}

			// data.setTempbanned(p.getName(), reason.isEmpty() ? "§8§oAucune" : "§6" +
			// reason, time, args[1]);
			data.setTempbanned(p.getName(), "§6" + reason, duration, args[1]);

			Player t = Bukkit.getPlayer(args[0]);

			if (t != null) {

				t.kickPlayer((settings.getServerName() == null ? "§6§lServerBan" : settings.getServerName())
						+ "§r\n\n§4Vous avez été banni du serveur par §6" + data.getTempbannedFrom()
						+ "§4 !§r\n§4Fin du bannissement : §6" + data.getTempbanEnd() + "§r\n§4Raison : §6"
						+ data.getTempbannedReason() + "§r\n\n\n§4Contactez le staff en cas d'erreur \n");
			}

			p.sendMessage(main.getPrefix() + "§2Le joueur §6" + args[0] + "§2 a été banni avec succès !");

			Bukkit.broadcastMessage(main.getBanPrefix() + "§2Le joueur §6" + args[0]
					+ "§2 a été temporairement banni jusqu'au §6" + data.getTempbanEnd() + "§2, par §6" + p.getName()
					+ " §2" + (reason.isEmpty() ? "!" : ", raison : §6" + reason));

			return true;

		}
		return true;
	}

}
