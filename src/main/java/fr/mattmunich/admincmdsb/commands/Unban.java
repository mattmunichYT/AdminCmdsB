package fr.mattmunich.admincmdsb.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;
import fr.mattmunich.admincmdsb.Utility;
import fr.mattmunich.admincmdsb.commandhelper.PlayerData;

public class Unban implements CommandExecutor{

	private Main main;

	public Unban(Main main) {
		this.main = main;
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

		if(args.length == 0) {
			p.sendMessage("§cSintax : /unban <player>");
			return true;
		}

		if(args.length == 1) {

            PlayerData data = null;
            try {
                data = new PlayerData(Utility.getUUIDFromName(args[0]));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if(!data.exist()) {
				p.sendMessage(main.getPrefix() + "§4Le joueur est introuvable !");
				return true;
			}

			if(!data.isTempbanned() && !data.isBanned()){
				p.sendMessage(main.getPrefix()  + "§4Le joueur n'est pas banni !");
				return  true;
			}

			data.setUnTempbanned();
			data.setUnBanned();

			p.sendMessage(main.getPrefix() + "§2Le joueur §6" + args[0] + "§2 a été débanni avec succès !");

			Bukkit.broadcastMessage(main.getBanPrefix() + "§2Le joueur §6" + args[0] + "§2 a été débanni par §6" + p.getName() + " §2!");

			return true;
		}

		return true;
	}

}
