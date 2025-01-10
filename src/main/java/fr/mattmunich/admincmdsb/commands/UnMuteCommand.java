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

public class UnMuteCommand implements CommandExecutor {

	private Main main;

	public UnMuteCommand(Main main) {
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
			p.sendMessage("§cSintax : /unmute <player>");
			return true;
		}

		if(args.length == 1) {

			PlayerData data = new PlayerData(Utility.getUUIDFromName(args[0]));

			if(!data.exist()) {
				p.sendMessage(main.getPrefix() + "§4Le joueur est introuvable !");
				return true;
			}

			if(!data.isTempmuted() && !data.isMuted()){
				p.sendMessage(main.getPrefix()  + "§4Le joueur n'est pas mute !");
				return  true;
			}

			if(p instanceof Player ) {
				Player rp = (Player) p;
				if(rp.getName() == Bukkit.getPlayer(args[0]).getName() && ! main.admin.contains(rp)) {
					rp.sendMessage(main.getPrefix() + "§4Vous n'avez pas la permission de vous unmute vous-même !");
					return true;
				}
			}

			data.setUnTempmuted();
			data.setMuted(false);

			Player t = Bukkit.getPlayer(args[0]);

			if(t != null) {

				main.mute.remove(t);
			}


			p.sendMessage(main.getPrefix() + "§2Le joueur §6" + args[0] + "§2 a été démute avec succès !");

			Bukkit.broadcastMessage(main.getBanPrefix() + "§2Le joueur §6" + args[0] + "§2 a été unmute par §6" + p.getName() + " §2!");

			return true;
		}

			return true;


	}

}
