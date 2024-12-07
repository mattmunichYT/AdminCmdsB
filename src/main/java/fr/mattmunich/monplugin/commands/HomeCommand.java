package fr.mattmunich.monplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;

import fr.mattmunich.monplugin.MonPlugin;
import fr.mattmunich.monplugin.Utility;
import fr.mattmunich.monplugin.commandhelper.PlayerData;

public class HomeCommand implements CommandExecutor{

	private MonPlugin main;

	public HomeCommand(MonPlugin main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


		if(sender instanceof BlockCommandSender) {
			sender.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}
		//EVENT
//		if(sender instanceof Player) {
//			Player p = (Player)sender;
//			if(!main.staff.contains(p)) {
//				p.sendMessage(main.getErrorPrefix() + "Cette commande est désactivée durant l'Évent de Pâques !");
//				return true;
//			}
//		}
		//END


		if(label.equalsIgnoreCase("sethome") || label.equalsIgnoreCase("seth"))  {

			if(!(sender instanceof Player)) {
				sender.sendMessage(main.getPrefix() + "§4Vous devez etre un joueur pour utiliser cette commande !");
				return true;
			}

			Player player = (Player) sender;

			if(!(main.superstaff.contains(player))) {
				player.sendMessage(main.noPermissionMsg);
				return true;
			}

			PlayerData data = new PlayerData(Utility.getUUIDFromName(player.getName()));
			if(args.length == 0 || args.length > 1) {
				player.sendMessage("§cSintax : /sethome <HomeName>");
				return true;
			}
			data.getConfig().createSection("homes");
			if(!data.getConfig().isSet("home.")) {
				;
				data.getConfig();
				MemorySection.createPath(data.getConfig().getConfigurationSection("homes"), "home");
				;
				data.getConfig();
				MemorySection.createPath(data.getConfig().getConfigurationSection("homes"), "home.list");
			}
			data.getConfig().set("home."  + args[0] + ".world", player.getLocation().getWorld().getName());
			data.getConfig().set("home."  + args[0] + ".x", player.getLocation().getX());
			data.getConfig().set("home."  + args[0] + ".y", player.getLocation().getY());
			data.getConfig().set("home."  + args[0] + ".z", player.getLocation().getZ());
			data.getConfig().set("home."  + args[0] + ".pitch",player.getEyeLocation().getPitch());
			data.getConfig().set("home."  + args[0] + ".yaw", player.getEyeLocation().getYaw());
			data.saveConfig();
			if(data.getConfig().isSet("home.count")) {
				data.getConfig().set("home.count", data.getConfig().getInt("home.count") + 1);
			}else{
				data.getConfig().set("home.count", 1);
			}
			data.saveConfig();

			if(!data.getConfig().isSet("home.list")) {
				;
				data.getConfig();
				MemorySection.createPath(data.getConfig().getConfigurationSection("homes"), "home.list");

				data.getConfig().set("home.list", args[0] + ",");
				player.sendMessage(main.getPrefix() + "§2Le home \"§6" + args[0] + "§2\" à été défini à votre position !");
				data.saveConfig();
			}else {
				if(!data.getConfig().contains(args[0])) {
					data.getConfig().set("home.list", data.getConfig().get("home.list") + args[0] + ",");
					player.sendMessage(main.getPrefix() + "§2Le home \"§6" + args[0] + "§2\" à été défini à votre position !");
				}else {
					player.sendMessage(main.getPrefix() + "§2Le home \"§6" + args[0] + "§2\" à été redéfini à votre position !");
				}

				data.saveConfig();
			}
			data.saveConfig();

			PlayerData rldata = new PlayerData(Utility.getUUIDFromName(player.getName()));
			rldata.getHomes();

			return true;
		}

		if(label.equalsIgnoreCase("home") || label.equalsIgnoreCase("h")) {

			if(!(sender instanceof Player)) {
				sender.sendMessage(main.getPrefix() + "§4Vous devez etre un joueur pour utiliser cette commande !");
				return true;
			}

			Player player = (Player) sender;
			PlayerData data = new PlayerData(Utility.getUUIDFromName(player.getName()));

			if(args.length > 1 && args.length <= 3) {
				if(main.admin.contains(player)) {
					String tName = args[0];
					PlayerData tdata = new PlayerData(Utility.getUUIDFromName(tName));
					if(args[1].equalsIgnoreCase("home") && args[2].equalsIgnoreCase("list")) {
						if(tdata.getHomes() == null) {
							player.sendMessage("§e--------§2§lHomes§e--------\n§4Ce joueur n'a pas de homes !");
						}else {
							player.sendMessage("§e--------§2§lHomes§e--------\n§2Homes de " + tName + "  : §r\n§l§6" + tdata.getHomes().replace(",", ", "));
						}
						return true;
						
					}
//					else if(args.length == 3 || args.length == 1){
//						player.sendMessage(main.getPrefix() + "§cMatt ! Tu te trompes voici les possibilités : \n"
//								+ "§2/home <joueur> <NomDuHome> §cou\n"
//								+ "§2/home <joueur> home list §8(liste des homes)");
//						return true;
//					}
					else {
						if(!tdata.getHomes().contains(args[1])) {
							player.sendMessage(main.getPrefix() + "§4Le joueur n'a défini le home \"§6" + args[1] + "§4\" !");
							return true;
						}
						if(tdata.getConfig().contains("home." + args[1] + ".")) {
							World w = Bukkit.getWorld(tdata.getConfig().getString("home."  + args[1] + ".world"));
							double x = tdata.getConfig().getDouble("home."  + args[1] + ".x");
							double y = tdata.getConfig().getDouble("home."  + args[1] + ".y");
							double z = tdata.getConfig().getDouble("home."  + args[1] + ".z");
							double pitch = tdata.getConfig().getDouble("home."  + args[1] + ".pitch");
							double yaw = tdata.getConfig().getDouble("home."  + args[1] + ".yaw");

							try {
								player.teleport(new Location(w, x, y, z, (float) yaw, (float) pitch));
							} catch (Exception e) {
								player.sendMessage(main.getPrefix() + "§4Le monde n'est pas chargé. §ePour accéder au Home, veuillez charger le monde §a\"§6" + tdata.getConfig().getString("home."  + args[1] + ".world") + "§a\"§e !");
							}
							
							//success
							player.sendMessage(main.getPrefix() + "§2Vous avez été téléporté au home \"§6" + args[1] + "§2\" de " + tName + " !");
							return true;
						} else {
							player.sendMessage(main.getPrefix() + "§4Le joueur n'a défini le home \"§6" + args[1] + "§4\" !");
							return true;
						}
					}
				}
			}

			if(args.length == 0 || args.length > 1) {

				if(data.getHomes() == null) {
					player.sendMessage("§e--------§2§lHomes§e--------\n§8§oVous n'avez pas de homes\n§8§oCréez des homes avec /sethome <NomDuHome>");
				}else {
					player.sendMessage("§e--------§2§lHomes§e--------\n§2Vos Homes : §r\n§l§6" + data.getHomes().replace(",", ", "));
				}
				player.sendMessage("§cSintax : /home <HomeName>");
				return true;
			}

			if(data.getConfig().contains("home." + args[0] + ".")) {
				World w = Bukkit.getServer().getWorld(data.getConfig().getString("home."  + args[0] + ".world"));
				double x = data.getConfig().getDouble("home."  + args[0] + ".x");
				double y = data.getConfig().getDouble("home."  + args[0] + ".y");
				double z = data.getConfig().getDouble("home."  + args[0] + ".z");
				double pitch = data.getConfig().getDouble("home."  + args[0] + ".pitch");
				double yaw = data.getConfig().getDouble("home."  + args[0] + ".yaw");

				player.teleport(new Location(w, x, y, z, (float) yaw, (float) pitch));
				player.sendMessage(main.getPrefix() + "§2Vous avez été téléporté au home \"§6" + args[0] + "§2\" !");
				return true;
			} else {
				player.sendMessage(main.getPrefix() + "§4Le home \"§6" + args[0] + "§4\" n'existe pas !");
				return true;
			}
		}

		if(label.equalsIgnoreCase("delhome") || label.equalsIgnoreCase("delh")) {

			if(!(sender instanceof Player)) {
				sender.sendMessage(main.getPrefix() + "§4Vous devez etre un joueur pour utiliser cette commande !");
				return true;
			}

			Player player = (Player) sender;
			PlayerData data = new PlayerData(Utility.getUUIDFromName(player.getName()));

			if(args.length == 0 || args.length > 1) {
				player.sendMessage("§cSintax : /delhome <HomeName>");
				return true;
			}

			if(data.getConfig().contains("home."  + args[0] + ".")) {
				if(data.getConfig().getString("home.list").contains(args[0])) {
					String result = data.getConfig().getString("home.list").replace(args[0] + ","  , "").toString();

					data.getConfig().set("home.list", result);
					data.saveConfig();
				}else {
					player.sendMessage(main.getErrorPrefix() + "Une erreur s'est produite lors de la suppression du home. Annulation...");
					return true;
				}
				data.getConfig().set("home."  + args[0], null);
				data.getConfig().set("home.count", data.getConfig().getInt("home.count") - 1);

				player.sendMessage(main.getPrefix() + "§2Le home \"§6" + args[0] + "§2"+ "§2\" a été supprimé !");
				data.saveConfig();
				return true;
			} else {
				player.sendMessage(main.getPrefix() + "§4Le home \"§6" + args[0] + "§4\" n'existe pas !");
				return true;
			}

		}

		return true;
	}




}
