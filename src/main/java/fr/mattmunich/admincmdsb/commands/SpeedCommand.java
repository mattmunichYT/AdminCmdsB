package fr.mattmunich.admincmdsb.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;

public class SpeedCommand implements CommandExecutor {

	private Main main;

	public SpeedCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {

		if (!(s instanceof Player)) {
			s.sendMessage(main.getPrefix() + "§4Vous devez etre un joueur pour utiliser cette commande !");
			return true;
		}
		Player p = (Player) s;

		if (l.equalsIgnoreCase("speed")) {

			if (!main.staff.contains(p)) {
				p.sendMessage(main.noPermissionMsg);
				return true;
			}

			if (args.length > 2 || args.length < 1) {
				p.sendMessage("§cSintax : /speed <nombre/get> [player]");
				return true;
			}

			if (args[0].equalsIgnoreCase("get")) {
				if (args.length == 1) {
					p.sendMessage(main.getPrefix() + "§6------------- Vitesse de §2" + p.getName()
							+ "§6 -------------\n§6Marche : §2" + p.getWalkSpeed() * 10 + "\n§6Vol : §2"
							+ p.getFlySpeed() * 10);
					return true;
				} else {
					String targetName = args[1];

					if (Bukkit.getPlayer(targetName) == null) {
						p.sendMessage(main.getPrefix() + "§4Le joueur est hors-ligne ou n'éxiste pas !");
						return true;
					}

					Player t = Bukkit.getPlayer(targetName);

					p.sendMessage(main.getPrefix() + "§6------------- Vitesse de §2" + t.getName()
							+ "§6 -------------\n§6Marche : §2" + t.getWalkSpeed() * 10 + "\n§6Vol : §2"
							+ t.getFlySpeed() * 10);
					return true;
				}
			} else {
				if (!args[0].matches("-?\\d+")) {
					p.sendMessage(main.getPrefix() + "§4Veuillez entrer un nombre !");
					return true;
				}

				float n = Float.parseFloat(args[0]);
				float speed = n / 10;
				if (n > 10 || n < 1) {
					p.sendMessage(main.getPrefix() + "§4Veuillez entrer un nombre de 1 à 10 !");
					return true;
				}
				if (args.length == 2) {

					String targetName = args[1];

					if (Bukkit.getPlayer(targetName) == null) {
						p.sendMessage(main.getPrefix() + "§4Le joueur est hors-ligne ou n'éxiste pas !");
						return true;
					}

					Player t = Bukkit.getPlayer(targetName);
					if (t.isFlying()) {
						t.setFlySpeed(speed);
						p.sendMessage(main.getPrefix() + "§6La vitesse de §2" + t.getName()
								+ "§6 pour le vol est maintenant de §2" + n + "§6 !");
						return true;
					} else {
						t.setWalkSpeed(speed);
						p.sendMessage(main.getPrefix() + "§6La vitesse de §2" + t.getName()
								+ "§6 pour la marche est maintenant de §2" + n + "§6 !");
						return true;
					}
				} else {
					if (p.isFlying()) {
						p.setFlySpeed(speed);
						p.sendMessage(main.getPrefix() + "§6Votre vitesse de vol est maintenant de §2" + n + "§6 !");
						return true;
					} else {
						p.setWalkSpeed(speed);
						p.sendMessage(main.getPrefix() + "§6Votre vitesse de marche est maintenant de §2" + n + "§6 !");
						return true;
					}

				}

			}
		}

		return true;
	}

}
