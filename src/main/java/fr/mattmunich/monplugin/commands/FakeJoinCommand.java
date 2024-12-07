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
import fr.mattmunich.monplugin.commandhelper.Settings;

public class FakeJoinCommand implements CommandExecutor, TabCompleter {

	private MonPlugin main;

	private Grades grades;

	private Settings settings;

	public FakeJoinCommand(MonPlugin main, Grades grades, Settings settings) {
		this.main = main;
		this.grades = grades;
		this.settings = settings;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {

		if (!(s instanceof Player) || s instanceof BlockCommandSender) {
			s.sendMessage(main.requirePlayerToExcMsg);
			return true;
		}

		Player p = (Player) s;

		if (!main.admin.contains(p)) {
			p.sendMessage(main.noPermissionMsg);
			return true;
		}

		if (!settings.getCoMsg()) {
			p.sendMessage(main.getPrefix()
					+ "§4Les messages de §cconnection§4/§cdéconnection§4 sont désactivés sur le serveur !");
			return true;
		}

		if (args.length != 2) {
			p.sendMessage(main.getPrefix() + "§cSintax : /fakejoin <Player> <Grade>");
			return true;
		}

		String grade = args[1];

		GradeList gradeList = null;

		try {
			gradeList = grades.getGradeById(Integer.parseInt(grade));
		} catch (NumberFormatException nbe) {
			try {
				gradeList = GradeList.valueOf(grade.toUpperCase());
			} catch (Exception e) {
				p.sendMessage(main.getPrefix() + "§4Grade non trouvé !");
				return true;
			}
		}

		String gPrefix = gradeList.getPrefix();
		String gSuffix = gradeList.getSuffix();

		String name = (ChatColor.translateAlternateColorCodes('&', main.hex(gPrefix + main.hex(args[0]) + gSuffix)));

		Bukkit.broadcastMessage(name + "§e s'est connecté");

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> tabComplete = Lists.newArrayList();

		if (args.length == 1) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				tabComplete.add(p.getName());
			}
		}

		if (args.length == 2) {
			for (GradeList gradeList : GradeList.values()) {
				if (gradeList.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
					tabComplete.add(gradeList.getName().toLowerCase());
				}
			}
		}

		return tabComplete;
	}

}
