package fr.mattmunich.monplugin.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import fr.mattmunich.monplugin.MonPlugin;
import fr.mattmunich.monplugin.commandhelper.GradeList;
import fr.mattmunich.monplugin.commandhelper.Grades;

public class NickCommand implements CommandExecutor, TabCompleter {

	private MonPlugin main;

	private final Grades grades;

	public NickCommand(MonPlugin main, Grades grades) {
		this.main = main;
		this.grades = grades;
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] args) {

		if(s instanceof BlockCommandSender) {
			s.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}


		if(!(s instanceof Player)) {
			s.sendMessage(main.getPrefix() + "§cVous devez être un joueur pour utiliser cette commande !");
			return true;
		}
		Player p = (Player)s;

		if (!main.superstaff.contains(p)) {
			p.sendMessage(main.noPermissionMsg);
			return true;
		}

		if(args.length == 0|| args.length > 3) {
			p.sendMessage("§cSintax : /nick <NickName/Player> [NickName] [grade]");
			return true;
		}

		if(args.length == 2) {
			String targetName = args[0];

			if(Bukkit.getPlayer(targetName) == null) {
				p.sendMessage(main.getPrefix() + "§4Le joueur est hors-ligne ou n'éxiste pas !");
				return true;
			}

			Player target = Bukkit.getPlayer(targetName);

			target.setDisplayName(ChatColor.translateAlternateColorCodes('&', main.hex(args[1])));
			target.setPlayerListName(ChatColor.translateAlternateColorCodes('&', main.hex(args[1])));
			target.setCustomName(ChatColor.translateAlternateColorCodes('&', main.hex(args[1])));
			target.setCustomNameVisible(true);
			p.sendMessage(main.getPrefix() + "§2Le pseudo de §6" + target.getName() + "§2 a été changé en \"§6" + target.getDisplayName() + "§2\" !");
			return true;
		} else if (args.length == 3) {
			String targetName = args[0];

			if(Bukkit.getPlayer(targetName) == null) {
				p.sendMessage(main.getPrefix() + "§4Le joueur est hors-ligne ou n'éxiste pas !");
				return true;
			}

			Player target = Bukkit.getPlayer(targetName);
			GradeList gradeList = null;

			try {
				gradeList = grades.getGradeById(Integer.parseInt(args[2]));
			} catch(NumberFormatException nbe){
				try {
					gradeList = GradeList.valueOf(args[2].toUpperCase());
				}catch(Exception e) {
					target.sendMessage(main.getPrefix() + "§4Grade non trouvé !");
					return true;
				}
			}

			String gPrefix = gradeList.getPrefix();
			String gSuffix = gradeList.getSuffix();


			target.setDisplayName(ChatColor.translateAlternateColorCodes('&', main.hex(gPrefix + "" + main.hex(args[1]) + "" + gSuffix)));
			target.setPlayerListName(ChatColor.translateAlternateColorCodes('&',  main.hex(gPrefix + "" + main.hex(args[1]) + "" + gSuffix)));
			target.setCustomName(ChatColor.translateAlternateColorCodes('&',  main.hex(gPrefix + "" + main.hex(args[1]) + "" + gSuffix)));
			target.setCustomNameVisible(true);
			p.sendMessage(main.getPrefix() + "§2Le pseudo de §6" + target.getName() + "§2 a été changé en \"§6" + target.getDisplayName() + "§2\" !");
			return true;
		}

		p.setDisplayName(ChatColor.translateAlternateColorCodes('&', main.hex(args[0])));
		p.setCustomName(ChatColor.translateAlternateColorCodes('&', main.hex(args[0])));
		p.setPlayerListName(ChatColor.translateAlternateColorCodes('&', main.hex(args[0])));
		p.sendMessage(main.getPrefix() + "§2Votre pseudo a été changé en \"§6" + p.getDisplayName() + "§2\" !");

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tabComplete = Lists.newArrayList();

		if(args.length == 1) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				tabComplete.add(p.getName());
			}
		}

		if(args.length == 2) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				tabComplete.add(p.getName());
			}
		}

		if(args.length == 3) {
			for(GradeList gradeList : GradeList.values()) {
				if(gradeList.getName().toLowerCase().startsWith(args[2].toLowerCase())) {
					tabComplete.add(gradeList.getName().toLowerCase());
				}
			}
		}

		return tabComplete;
	}

}
