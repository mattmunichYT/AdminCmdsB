package fr.mattmunich.monplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.monplugin.MonPlugin;
import fr.mattmunich.monplugin.commandhelper.GradeList;
import fr.mattmunich.monplugin.commandhelper.Grades;

import net.md_5.bungee.api.ChatColor;

public class StaffChatCommand implements CommandExecutor {

	private MonPlugin main;

	private Grades grades;

	public StaffChatCommand(MonPlugin main, Grades grades) {
		this.main = main;
		this.grades = grades;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof BlockCommandSender) {
			sender.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}


		if(label.equalsIgnoreCase("staffchat")) {
			if(!(sender instanceof Player)) {
				sender.sendMessage(main.getPrefix() + "§4Vous devez etre un joueur pour utiliser cette commande !");
				return true;
			}

			Player player = (Player)sender;

			GradeList gradeList = grades.getPlayerGrade(player);

			if(!(main.superstaff.contains(player))) {
				player.sendMessage(main.noPermissionMsg);
				return true;
			}

//			if(args.length < 1) {
//				player.sendMessage("§cSintax : /staffchat [message] ");
//				return true;
//			}
			if(args.length > 1) {
				String msg = "§e[§2Staff§aChat§e] §2--> §r" + player.getDisplayName() + gradeList.getChatSeparator();
				for(String s : args) {
					msg = msg + s + " ";
				}

				for(Player p : Bukkit.getOnlinePlayers()) {
					if(main.mod.contains(p) || main.admin.contains(p)) {
						player.sendMessage(Color(main.hex(msg)));
					}
				}

				return true;
			}else {
				if(main.schat.contains(player)) {
					main.schat.remove(player);
					player.sendMessage(main.getPrefix() + "§4StaffChat désactivé !");
					return true;
				}else {
					player.sendMessage(main.getPrefix() + "§2StaffChat activé !");
					main.schat.add(player);
					return true;
				}
			}

		}

		return true;
	}
	private String Color(String s) {
		s = ChatColor.translateAlternateColorCodes('&', s);
		return s;
	}

}
