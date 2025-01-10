package fr.mattmunich.admincmdsb.commands;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;

public class SetStandNameCommand implements CommandExecutor {

	private Main main;

	public SetStandNameCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {
		boolean asdetected = false;


		if(s instanceof BlockCommandSender) {
			s.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}



		if(s instanceof Player) {

			Player p = (Player) s;

			if(args.length == 0) {
				p.sendMessage(main.getPrefix() + "§cSintax : /setstandname <name/&AS.name#reset>");
				return true;
			}

			if(!main.staff.contains(p)) {
				p.sendMessage(main.noPermissionMsg);
				return true;
			}

			String name = "";

			for (String arg : args) {
				name = name + arg + " ";

			}

			name = name.trim();

			Location ploc = p.getLocation();
            for(ArmorStand allst : p.getWorld().getEntitiesByClass(ArmorStand.class)) {

				if(allst == null) {
					p.sendMessage(main.getErrorPrefix() + "Impossible de charger un ArmorStand ! -> AS ignoré.");
					return true;
				}


				if(allst.getLocation().distance(ploc) < 1) {
					UUID stUUID = allst.getUniqueId();


					if(stUUID == null) {
						p.sendMessage(main.getErrorPrefix() + "L'Armor Stand n'a pas d'UUID ! -> AS ignoré.");
						return true;
					}
//					ASData pdata = new ASData(stUUID);
//					asdetected = true;
//
//					if(name.equalsIgnoreCase("&AS.name#reset")) {
//						pdata.resetASName();
//						return true;
//					}
//					ChatColor.translateAlternateColorCodes('&', name);
//					try{
//						pdata.setASName(name);
//						allst.setCustomName(name);
//					} catch(Exception e) {
//						p.sendMessage(main.getPrefix() + "§4Merci d'entrer un nom valide !");
//						return true;
//					}

					p.sendMessage(main.getPrefix() + "§2L'Armor Stand à proximité de vous a maintenant le nom §6" + name + "§2 !");
					return true;

				}
			}

			if(!asdetected) {
				p.sendMessage(main.getPrefix() + "§4Aucun Armor Stand détecté proche de vous !");
				return true;
			} else {
				return true;
			}

		} else {
			s.sendMessage(main.getErrorPrefix() + "Vous devez être un joueur pour utiliser cet argument !");
			return true;
		}
	}

}
