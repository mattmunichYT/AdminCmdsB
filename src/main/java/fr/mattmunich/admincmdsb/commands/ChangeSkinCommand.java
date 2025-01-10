package fr.mattmunich.admincmdsb.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;
import fr.mattmunich.admincmdsb.commandhelper.SkinManager;

public class ChangeSkinCommand implements CommandExecutor {

	private Main main;

	private SkinManager sm;

	public ChangeSkinCommand(Main main, SkinManager sm) {
		this.main = main;
		this.sm = sm;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {
		if(s instanceof BlockCommandSender) {
			s.sendMessage("§4Utilisation de Command Blocks désactivée !");
			BlockCommandSender cmds = (BlockCommandSender) s;
			cmds.sendMessage("Nope");
			return true;
		}

		if(!(s instanceof Player)) {
			s.sendMessage(main.requirePlayerToExcMsg);
			return true;
		}

		Player p = (Player) s;

		if(!(main.admin.contains(p))) {
			p.sendMessage(main.noPermissionMsg);
			return true;
		}

		if(args.length == 1) {
			String skinName = args[0];
			p.sendMessage(main.getPrefix() + "§eDemande de changement du skin... §7§o(seulement sur ce serveur)§r");

			sm.changeSkin(p, skinName, true);
			return true;
		} else if (args.length == 2) {
			String skinName = args[0];
			String tName = args[1];

			if(Bukkit.getPlayer(tName) == null) {
				p.sendMessage(main.playerNotFoundMsg);
				return true;
			}
			Player t = Bukkit.getPlayer(tName);
			p.sendMessage(main.getPrefix() + "§eDemande de changement du skin de " + t.getName() + "... §7§o(seulement sur ce serveur)§r");
			sm.changeSkin(t, skinName, true);
			return true;
		} else {
			p.sendMessage(main.getPrefix() + "§cSintax : /changeskin <skinName> [target]");
			return true;
		}
	}

}
