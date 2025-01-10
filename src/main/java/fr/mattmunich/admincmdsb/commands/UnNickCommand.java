package fr.mattmunich.admincmdsb.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;
import fr.mattmunich.admincmdsb.commandhelper.GradeList;
import fr.mattmunich.admincmdsb.commandhelper.Grades;




public class UnNickCommand implements CommandExecutor {

	private Main main;

	private Grades grades;

	public UnNickCommand(Main main, Grades grades) {
		this.main = main;
		this.grades = grades;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if(s instanceof BlockCommandSender) {
			s.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		if(!(s instanceof Player)) {
			s.sendMessage(main.getPrefix() + "§cVous devez être un joueur pour utiliser cette commande !");
			return true;
		}

		Player p = (Player)s;
		GradeList gradeList = grades.getPlayerGrade(p);

		if(!(main.superstaff.contains(p))) {
			p.sendMessage(main.noPermissionMsg);
			return true;
		}

		if(args.length > 2) {
			p.sendMessage(main.getPrefix() + "Sintax : /unnick [player]");
			return true;
		}

		if(args.length == 1) {
			String targetName = args[0];

			if(Bukkit.getPlayer(targetName) == null) {
				p.sendMessage(main.getPrefix() + "§4Le joueur est hors-ligne ou n'éxiste pas !");
				return true;
			}

			Player t = Bukkit.getPlayer(targetName);

			GradeList gradeListT = grades.getPlayerGrade(t);

			t.setDisplayName(main.hex(gradeListT.getPrefix()) + t.getName() + main.hex(gradeListT.getSuffix()));
			t.setPlayerListName(t.getDisplayName());
			t.setCustomNameVisible(false);
			p.sendMessage(main.getPrefix() + "§2Le pseudo de " + t.getName() + " est redevenu§6 " + t.getDisplayName() + "§2 !");
			return true;
		}

		p.setDisplayName(main.hex(gradeList.getPrefix()) + p.getName() + main.hex(gradeList.getSuffix()));
		p.setCustomNameVisible(false);
		p.setPlayerListName(p.getDisplayName());
		p.sendMessage(main.getPrefix() + "§2Votre pseudo est redevenu§6 " + p.getDisplayName() + "§2 !");

		return true;
	}


}
