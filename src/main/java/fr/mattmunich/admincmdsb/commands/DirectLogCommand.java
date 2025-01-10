package fr.mattmunich.admincmdsb.commands;

import java.util.List;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import fr.mattmunich.admincmdsb.Main;

public class DirectLogCommand implements CommandExecutor, TabCompleter {


	private Main main;

	public DirectLogCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {

		if(s instanceof BlockCommandSender) {
			s.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		if(!(s instanceof Player)) {
			s.sendMessage(main.getPrefix() + "Vous devez être un joueur pour pouvoir utiliser cette commande !");
			return true;
		}

		Player p = (Player) s;

		if(!(main.superstaff.contains(p))) {
			p.sendMessage(main.noPermissionMsg);
			return true;
		}

		if(args.length > 1) {
			p.sendMessage("§cSintax : /directlog [on/off]");
			return true;
		}

		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("on")) {
				if(main.directlog.contains(p)) {
					p.sendMessage(main.getPrefix() + "§4Vous avez déjà activé le DirectLog !");
					return true;
				} else {
					main.directlog.add(p);
					p.sendMessage(main.getPrefix() + "§2Vous avez activé le DirectLog avec succès !");
					return true;
				}
			} else if(args[0].equalsIgnoreCase("off")) {
				if(!main.directlog.contains(p)) {
					p.sendMessage(main.getPrefix() + "§4Vous n'avez pas activé le DirectLog !");
					return true;
				} else {
					main.directlog.remove(p);
					p.sendMessage(main.getPrefix() + "§2Vous avez désactivé le DirectLog avec succès !");
					return true;
				}
			} else {
				p.sendMessage("§cSintax : /directlog [on/off]");
				return true;
			}
		} else {
			if(!main.directlog.contains(p)) {
				main.directlog.add(p);
				p.sendMessage(main.getPrefix() + "§2Vous avez activé le DirectLog avec succès!");
				return true;
			} else {
				main.directlog.remove(p);
				p.sendMessage(main.getPrefix() + "§2Vous avez désactivé le DirectLog avec succès !");
				return true;
			}
		}
	}


	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tabComplete = Lists.newArrayList();

		if(args.length == 1) {
			tabComplete.add("on");
			tabComplete.add("off");
		}

		return tabComplete;
	}

}
