package fr.mattmunich.admincmdsb.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;

public class FlySpeedCommand implements CommandExecutor {

private Main main;

	public FlySpeedCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {

		if(s instanceof BlockCommandSender) {
			s.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		if(!(s instanceof Player)) {
			s.sendMessage(main.getPrefix() + "§4Vous devez etre un joueur pour utiliser cette commande !");
			return true;
		}
		Player p = (Player) s;



		if(l.equalsIgnoreCase("flyspeed")) {

			if(!main.staff.contains(p)) {
				p.sendMessage(main.noPermissionMsg);
				return true;
			}

			if(args.length > 2 || args.length < 1) {
				p.sendMessage("§cSintax : /flyspeed <nombre/get> [player]");
				return true;
			}


			if(args[0].equalsIgnoreCase("get")) {
				if(args.length == 1) {
					p.sendMessage(main.getPrefix() + "§6La vitesse de vol pour §2" + p.getName() + "§6 est de §2" + p.getFlySpeed()*10 + "§6 !");
					return true;
				} else {
					String targetName = args[1];

					if(Bukkit.getPlayer(targetName) == null) {
						p.sendMessage(main.getPrefix() + "§4Le joueur est hors-ligne ou n'éxiste pas !");
						return true;
					}

					Player t = Bukkit.getPlayer(targetName);

					p.sendMessage(main.getPrefix() + "§6La vitesse de vol pour §2" + t.getName() + "§6 est de §2" + t.getFlySpeed()*10 + "§6 !");
					return true;
				}
			} else {
				if(!args[0].matches("-?\\d+")) {
					p.sendMessage(main.getPrefix() + "§4Veuillez entrer un nombre !");
					return true;
				}

				float n = Float.parseFloat(args[0]);
				float speed = n/10;
				if(n > 10 || n < 1) {
					p.sendMessage(main.getPrefix() + "§4Veuillez entrer un nombre de 1 à 10 !");
					return true;
				}
				if(args.length == 2) {

					String targetName = args[1];

					if(Bukkit.getPlayer(targetName) == null) {
						p.sendMessage(main.getPrefix() + "§4Le joueur est hors-ligne ou n'éxiste pas !");
						return true;
					}

					Player t = Bukkit.getPlayer(targetName);

					t.setFlySpeed(speed);
					p.sendMessage(main.getPrefix() + "§6La vitesse de §2" + t.getName() + "§6 pour le vol est maintenant de §2" + n + "§6 !");
					return true;
				} else {
					p.setFlySpeed(speed);
					p.sendMessage(main.getPrefix() + "§6Votre vitesse de vol est maintenant de §2" + n + "§6 !");
					return true;

				}


			}
		}

		return true;
	}

}
