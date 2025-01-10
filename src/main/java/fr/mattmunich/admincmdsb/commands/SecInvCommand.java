package fr.mattmunich.admincmdsb.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;
import fr.mattmunich.admincmdsb.commandhelper.PlayerManager;

public class SecInvCommand implements CommandExecutor {

	private Main main;

	public SecInvCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {

		if(!(s instanceof Player)) {
			s.sendMessage(main.getPrefix() + "§4Vous devez êtres un joueur pour executer cette commande !");
			return true;
		}

		Player p = (Player) s;

		if(!main.staff.contains(p)) {
			p.sendMessage(main.noPermissionMsg);
			return true;
		}

		if(args.length > 1) {
			p.sendMessage("§cSintax : /secondaryinventory [on/off]");
			return true;
		}

		if(args.length == 1) {
			if(args[0] == "on") {
				if(main.secondInv.contains(p)) {
					p.sendMessage(main.getPrefix() + "§4Vous avez déjà votre inventaire secondaire !");
					return true;
				}else {
					PlayerManager pm = PlayerManager.getFromPlayer(p);
					pm.init();
					main.secondInv.add(p);
					p.sendMessage(main.getPrefix() + "§2Vous avez maintenant votre inventaire secondaire !");
					pm.saveInventory();
					return true;
				}
			}else {
				if(!main.secondInv.contains(p)) {
					p.sendMessage(main.getPrefix() + "§4Vous n'avez pas votre inventaire secondaire !");
					return true;
				}else {
					PlayerManager pm = PlayerManager.getFromPlayer(p);
					p.getInventory().clear();
					main.secondInv.remove(p);
					pm.giveInvetnory();
					pm.destroy();
					p.sendMessage(main.getPrefix() + "§2Vous n'avez maintenant plus votre inventaire secondaire !");
					return true;
				}
			}
		}else {
			if(main.secondInv.contains(p)) {
				PlayerManager pm = PlayerManager.getFromPlayer(p);
				p.getInventory().clear();
				main.secondInv.remove(p);
				pm.giveInvetnory();
				pm.destroy();
				p.sendMessage(main.getPrefix() + "§2Vous n'avez maintenant plus votre inventaire secondaire !");
			}else {
				PlayerManager pm = new PlayerManager(p);
				pm.init();
				main.secondInv.add(p);
				p.sendMessage(main.getPrefix() + "§2Vous avez maintenant votre inventaire secondaire !");
				pm.saveInventory();
			}
		}

		return true;


	}

}
