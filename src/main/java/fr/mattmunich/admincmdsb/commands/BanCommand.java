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

public class BanCommand implements CommandExecutor {

	private Main main;

	private Grades grades;

	public BanCommand(Main main, Grades grades) {
		this.main = main;
		this.grades = grades;
	}

	@Override
	public boolean onCommand(CommandSender player, Command cmd, String label, String[] args) {

		if(player instanceof BlockCommandSender) {
			player.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}


		if(!main.superstaff.contains(player)) {

			if(player instanceof Player) {
				player.sendMessage(main.noPermissionMsg);
				return true;
			}

		}

		if(args.length < 2) {
			player.sendMessage("§cSintax : /ban <player> <reason>");
			return true;
		}

		String reason = "";

		for(int i = 1; i < args.length; i++) {
			reason = reason + args[i] + " ";
		}
		reason = reason.trim();

        PlayerData data = null;
        try {
            data = new PlayerData(Utility.getUUIDFromName(args[0]));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if(!data.exist()) {
			player.sendMessage(main.getPrefix() + "§4Le joueur est introuvable !");
			return true;
		}
		if(reason.isEmpty()) {
			reason = "§8§oAucune";
		}
		if(data.isBanned()) {
			player.sendMessage(main.getPrefix() + "§4Le joueur §6" + args[0] + "§4 est déjà banni !");
			return true;
		}

		if(player instanceof Player) {

			if(Bukkit.getPlayer(args[0]) != null) {
				Player target = Bukkit.getPlayer(args[0]);
				Player p = (Player) player;
				if(grades.hasPowerInf(p, grades.getPlayerGrade(target).getPower())) {
					player.sendMessage(main.getPrefix() + "§4Vous n'avez pas la permission de §cbannir§4 un joueur plus haut gradé que vous !");
					return true;
				}
			}

		}

		data.setBanned(player.getName(), "§6" + reason);
		player.sendMessage(main.getPrefix() + "§2Le joueur §6" + args[0] + "§2 a été banni avec succès !");

		Player t = Bukkit.getPlayer(args[0]);

		if(t != null) {


			t.kickPlayer("§6§lServerBan§r\n\n§4Vous avez été banni du serveur par §6" + data.getBannedFrom() + "§r\n§4Raison : §6" + data.getBannedReason() + "§r\n\n\n§4Contactez le staff en cas d'erreur");
		}


		player.sendMessage(main.getPrefix() + "§2Le joueur §6" + args[0] + "§2 a été banni avec succès !");

		Bukkit.broadcastMessage(main.getBanPrefix() + "§2Le joueur §6" + args[0] + "§2 a été banni par §6" + player.getName() + " §2, raison : §6" + reason);


	return true;
	}

}
