package fr.mattmunich.monplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.monplugin.MonPlugin;

public class AntiASTPCommand implements CommandExecutor {

	private MonPlugin main;

	public AntiASTPCommand(MonPlugin main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {
		if(!(s instanceof Player)) {
			s.sendMessage(main.getPrefix() + "§4Vous devez etre un §6joueur§4 pour executer cette commande !");
			return true;
		}
		Player p = (Player) s;
		if (!main.staff.contains(p)) {
			p.sendMessage(main.getPrefix() + "§4Vous n'avez pas la permission d'utiliser cette commande !");
			return true;
		}

		if(args.length < 0 || args.length > 1) {
			p.sendMessage("§cSintax : /bypassastp [on/off]");
			if(main.bypassastp.contains(p)) {
				p.sendMessage(main.getPrefix() + "§6Statut du anti-téléportation aux mini-jeux : §2actif");
			} else if(!main.bypassastp.contains(p)){
				p.sendMessage(main.getPrefix() + "§6Statut du anti-téléportation aux mini-jeux : §4inactif");
			} else {
				p.sendMessage(main.errorMsg);
			}
			return true;
		}
		if(args.length == 0) {
			if(main.bypassastp.contains(p)) {
				main.bypassastp.remove(p);
				p.sendMessage(main.getPrefix() + "§2Votre anti-téléportation aux mini-jeux a été désactivée !");
				return true;
			} else if(!main.bypassastp.contains(p)) {
				main.bypassastp.add(p);
				p.sendMessage(main.getPrefix() + "§2Votre anti-téléportation aux mini-jeux a été activé !");
				return true;
			}
		}else if(args.length == 1) {
			if(args[0].equalsIgnoreCase("on")) {
				if(main.bypassastp.contains(p)) {
					main.bypassastp.remove(p);
					p.sendMessage(main.getPrefix() + "§2Votre anti-téléportation aux mini-jeux a été désactivée !");
					return true;
				}else if(!main.bypassastp.contains(p)) {
					p.sendMessage(main.getPrefix() + "§4Votre anti-téléportation aux mini-jeux est déjà désactivée !");
					return true;
				}
			}else if(args[0].equalsIgnoreCase("off")) {
				if(main.bypassastp.contains(p)) {
					p.sendMessage(main.getPrefix() + "§4Votre anti-téléportation aux mini-jeux est déjà activée !");
					return true;
				}else if(!main.bypassastp.contains(p)) {
					main.bypassastp.add(p);
					p.sendMessage(main.getPrefix() + "§2Votre anti-téléportation aux mini-jeux a été activée !");
					return true;
				}
			}else {
				p.sendMessage("§cSintax : /bypassastp [on/off]");
				if(main.bypassastp.contains(p)) {
					p.sendMessage(main.getPrefix() + "§6Statut du anti-téléportation aux mini-jeux : §2actif");
				} else if(!main.bypassastp.contains(p)){
					p.sendMessage(main.getPrefix() + "§6Statut du anti-téléportation aux mini-jeux : §4inactif");
				} else {
					p.sendMessage(main.errorMsg);
				}
			}
				return true;
		} else {
			p.sendMessage("§cSintax : /bypassastp [on/off]");
			if(main.bypassastp.contains(p)) {
				p.sendMessage(main.getPrefix() + "§6Statut du anti-téléportation aux mini-jeux : §2actif");
			} else if(!main.bypassastp.contains(p)){
				p.sendMessage(main.getPrefix() + "§6Statut du anti-téléportation aux mini-jeux : §4inactif");
			} else {
				p.sendMessage(main.errorMsg);
			}
			return true;
		}
		p.sendMessage("§cSintax : /bypassastp [on/off]");
		if(main.bypassastp.contains(p)) {
			p.sendMessage(main.getPrefix() + "§6Statut du anti-téléportation aux mini-jeux : §2actif");
		} else if(!main.bypassastp.contains(p)){
			p.sendMessage(main.getPrefix() + "§6Statut du anti-téléportation aux mini-jeux : §4inactif");
		} else {
			p.sendMessage(main.errorMsg);
		}
		return true;
	}
}
