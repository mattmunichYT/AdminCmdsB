package fr.mattmunich.admincmdsb.commands;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;

public class ChatCommand implements CommandExecutor {

	private Main main;

	public ChatCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {

		if (s instanceof BlockCommandSender) {
			s.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		if (!(s instanceof Player)) {
			s.sendMessage(main.getPrefix() + "§4Vous devez etre un §6joueur§4 pour executer cette commande !");
			return true;
		}
		Player p = (Player) s;
		if (!main.staff.contains(p) || !main.superstaff.contains(p)) {
			p.sendMessage(main.getPrefix() + "§4Vous n'avez pas la permission d'utiliser cette commande !");
			return true;
		}

		if (args.length > 1) {
			p.sendMessage("§cSintax : /chat [on/off]");
			if (main.nochat.contains(p)) {
				p.sendMessage(main.getPrefix() + "§6Statut du chat : §4désactivé");
			} else {
				p.sendMessage(main.getPrefix() + "§6Statut du chat : §2activé");
			}
			return true;
		}
		if (args.length == 0) {
			if (main.nochat.contains(p)) {
				main.nochat.remove(p);
				p.sendMessage(main.getPrefix() + "§2Votre chat a été réactivé !");
				return true;
			} else if (!main.nochat.contains(p)) {
				main.nochat.add(p);
				p.sendMessage(main.getPrefix() + "§2Votre chat a été désactivé !");
				return true;
			}
		} else {
			if (args[0].equalsIgnoreCase("on")) {
				if (main.nochat.contains(p)) {
					main.nochat.remove(p);
					p.sendMessage(main.getPrefix() + "§2Votre chat a été réactivé !");
					return true;
				} else if (!main.nochat.contains(p)) {
					p.sendMessage(main.getPrefix() + "§4Votre chat est déjà activé !");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("off")) {
				if (main.nochat.contains(p)) {
					p.sendMessage(main.getPrefix() + "§4Votre chat est déjà désactivé !");
					return true;
				} else if (!main.nochat.contains(p)) {
					main.nochat.add(p);
					p.sendMessage(main.getPrefix() + "§2Votre chat a été désactivé !");
					return true;
				}
			} else {
				p.sendMessage("§cSintax : /chat [on/off]");
				if (main.nochat.contains(p)) {
					p.sendMessage(main.getPrefix() + "§6Statut du chat : §4désactivé");
				} else {
					p.sendMessage(main.getPrefix() + "§6Statut du chat : §2activé");
				}
			}
			return true;
		}
		p.sendMessage("§cSintax : /chat [on/off]");
		if (main.nochat.contains(p)) {
			p.sendMessage(main.getPrefix() + "§6Statut du chat : §4désactivé");
		} else {
			p.sendMessage(main.getPrefix() + "§6Statut du chat : §2activé");
		}
		return true;
	}
}
