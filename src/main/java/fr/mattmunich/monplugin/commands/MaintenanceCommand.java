package fr.mattmunich.monplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.monplugin.MonPlugin;
import fr.mattmunich.monplugin.commandhelper.Grades;
import fr.mattmunich.monplugin.commandhelper.Settings;

public class MaintenanceCommand implements CommandExecutor {

	private MonPlugin main;

	private Grades grades;

	private Settings settings;

	public MaintenanceCommand(MonPlugin main, Grades grades, Settings settings) {
		this.main = main;
		this.grades = grades;
		this.settings = settings;
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] args) {

		if (s instanceof BlockCommandSender) {
			s.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		if (s instanceof Player) {
			Player p = (Player) s;
			if (!(main.superstaff.contains(p))) {
				p.sendMessage(main.noPermissionMsg);
				return true;
			}
		}

		if (args.length == 0) {
			s.sendMessage(main.getPrefix() + "§6Staut du mode §oMaintenance §r§6: " + settings.getMaintenance());
			return true;
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("on")) {
				s.sendMessage(main.getPrefix() + "§2Le mode §6§oMaintenance§r§2 est maintenant défini sur true");
				settings.maintenanceTrue();
				Bukkit.broadcastMessage(main.getPrefix() + "§6§lLe mode maintenace a été activé !");
				for (Player all : Bukkit.getOnlinePlayers()) {
					if (grades.hasPowerInf(all, 60)) {
						all.kickPlayer(
								"§6Le serveur es en maintenance\nVous pouvez seulement rejoindre le serveur en ayant le grade \n§bBuildeur§6, §2Modérateur §6ou §4Administrateur");
					}
				}

				return true;
			}
			if (args[0].equalsIgnoreCase("off")) {
				s.sendMessage(main.getPrefix() + "§2Le mode §6§oMaintenance§r§2 est maintenant défini sur false");
				settings.maintenanceFalse();
				Bukkit.broadcastMessage(main.getPrefix() + "§6§lLe mode maintenace a été désactivé !");
				return true;
			} else {
				s.sendMessage("§cSintax : /maintenance [on/off]");
				return true;
			}
		} else {
			s.sendMessage("§cSintax : /maintenance [on/off]");
			return true;
		}
	}

}
