package fr.mattmunich.admincmdsb.commands;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;

public class MutedChatCommand implements CommandExecutor {

	private Main main;

	public MutedChatCommand(Main main) {
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
		if (!(main.superstaff.contains(p))) {
			p.sendMessage(main.noPermissionMsg);
			return true;
		}

		if (args.length < 0 || args.length > 1) {
			p.sendMessage("§cSintax : /mutedchat [on/off]");
			if (main.mutedChat.contains(p)) {
				p.sendMessage(main.getPrefix() + "§6Statut du MutedChat : §4désactivé");
			} else if (!main.mutedChat.contains(p)) {
				p.sendMessage(main.getPrefix() + "§6Statut du mutedMutedChat : §2activé");
			} else {
				p.sendMessage(main.errorMsg);
			}
			return true;
		}
		if (args.length == 0) {
			if (main.mutedChat.contains(p)) {
				main.mutedChat.remove(p);
				p.sendMessage(main.getPrefix() + "§2Votre MutedChat a été réactivé !");
				return true;
			} else if (!main.mutedChat.contains(p)) {
				main.mutedChat.add(p);
				p.sendMessage(main.getPrefix() + "§2Votre MutedChat a été désactivé !");
				return true;
			}
		} else if (args.length == 1) {
			if (args[0].equalsIgnoreCase("on")) {
				if (main.mutedChat.contains(p)) {
					main.mutedChat.remove(p);
					p.sendMessage(main.getPrefix() + "§2Votre MutedChat a été réactivé !");
					return true;
				} else if (!main.mutedChat.contains(p)) {
					p.sendMessage(main.getPrefix() + "§4Votre MutedChat est déjà activé !");
					return true;
				}
			} else if (args[0].equalsIgnoreCase("off")) {
				if (main.mutedChat.contains(p)) {
					p.sendMessage(main.getPrefix() + "§4Votre MutedChat est déjà désactivé !");
					return true;
				} else if (!main.mutedChat.contains(p)) {
					main.mutedChat.add(p);
					p.sendMessage(main.getPrefix() + "§2Votre MutedChat a été désactivé !");
					return true;
				}
			} else {
				p.sendMessage("§cSintax : /mutedchat [on/off]");
				if (main.mutedChat.contains(p)) {
					p.sendMessage(main.getPrefix() + "§6Statut du MutedChat : §4désactivé");
				} else if (!main.mutedChat.contains(p)) {
					p.sendMessage(main.getPrefix() + "§6Statut du MutedChat : §2activé");
				} else {
					p.sendMessage(main.errorMsg);
				}
			}
			return true;
		} else {
			p.sendMessage("§cSintax : /mutedchat [on/off]");
			if (main.mutedChat.contains(p)) {
				p.sendMessage(main.getPrefix() + "§6Statut du MutedChat : §4désactivé");
			} else if (!main.mutedChat.contains(p)) {
				p.sendMessage(main.getPrefix() + "§6Statut du MutedChat : §2activé");
			} else {
				p.sendMessage(main.errorMsg);
			}
			return true;
		}
		p.sendMessage("§cSintax : /mutedchat [on/off]");
		if (main.mutedChat.contains(p)) {
			p.sendMessage(main.getPrefix() + "§6Statut du MutedChat : §4désactivé");
		} else if (!main.mutedChat.contains(p)) {
			p.sendMessage(main.getPrefix() + "§6Statut du MutedChat : §2activé");
		} else {
			p.sendMessage(main.errorMsg);
		}
		return true;
	}
}
