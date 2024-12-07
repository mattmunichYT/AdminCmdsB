package fr.mattmunich.monplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.monplugin.MonPlugin;

public class NoMineCommand implements CommandExecutor {

	private MonPlugin main;

	public NoMineCommand(MonPlugin main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender p, Command cmd, String l, String[] args) {

		if(p instanceof BlockCommandSender) {
			p.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}


		if(p instanceof Player) {
			Player rp = (Player) p;

			if(!(main.superstaff.contains(rp))) {
				rp.sendMessage(main.noPermissionMsg);
				return true;
			}
		}

		if(!(args.length == 1)) {
			p.sendMessage("§cSintax : /nomine <Player>");
			return true;
		}

		String tname = args[0];

		if(Bukkit.getPlayer(tname) == null) {
			p.sendMessage(main.getPrefix() + "§4Le joueur est hors ligne ou n'existe pas !");
			return true;
		}

		Player t = Bukkit.getPlayer(tname);

		if(main.nomine.contains(t)) {
			main.nomine.remove(t);
			p.sendMessage(main.getPrefix() + "§2Le joueur §6" + t.getName() + "§2 peut maintenant miner des blocs !");
			return true;
		}else if(!main.nomine.contains(t)) {
			main.nomine.add(t);
			p.sendMessage(main.getPrefix() + "§2Le joueur §6" + t.getName() + "§2 ne peut maintenant plus miner de blocs !");
			return true;
		}else {
			System.err.println(main.errorMsg);
			p.sendMessage(main.errorMsg);
		}

		return true;
	}

}
