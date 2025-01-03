package fr.mattmunich.monplugin.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import fr.mattmunich.monplugin.MonPlugin;
import fr.mattmunich.monplugin.commandhelper.GradeList;
import fr.mattmunich.monplugin.commandhelper.Grades;

public class GradeCommand implements CommandExecutor, TabCompleter {

	private final MonPlugin main;

	private final Grades grades;

	public GradeCommand(MonPlugin main, Grades grades) {
		this.grades = grades;
		this.main = main;
	}

	@SuppressWarnings({ "deprecation", "unused" })
	@Override
	public boolean onCommand(CommandSender player, Command cmd, String label, String[] args) {

		if(player instanceof BlockCommandSender) {
			player.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}



		//Player player = (Player) sender;
		if(player instanceof Player) {

			Player p = (Player) player;

			if (!main.superstaff.contains(p)) {
				p.sendMessage(main.noPermissionMsg);
				return true;
			}
		}

		if(args.length != 2) {
			player.sendMessage(main.hex("§e------§6Grades§e------§r\n§l§6Liste des gardes : \n"
					+ "#b1b1b1M#c1c1c1E#d0d0d0M#e0e0e0B#efefefR#ffffffE , ID : 1§r\n"
					+ "#fbbc00T#e0c70dE#c4d21aS#a9dd27T#8de733E#72f240U#56fd4dR , ID : 3§r\n"
					+ "#ffcf00V#ffe55fI#fffabeP , ID : 4§r\n"
					+ "#fbcb00G#fcd62bU#fce156I#fdec80D#fdf7abE , ID : 5§r\n"
					+ "#fb00f8B#f812f9U#f524f9I#f236faL#f047fbD#ed59fcE#ea6bfcU#e77dfdR , ID : 6§r\n"
					+ "#084cfbM#0d52fbI#1258fcN#175efcI#1d65fcA#226bfcT#2771fdE#2c77fdR#317dfdR , ID : 7§r\n"
					+ "#3afb00D#40fb0aE#47fb13C#4dfc1dO#53fc27R#5afc30A#60fc3aT#66fd44E#6dfd4dU#73fd57R , ID : 8§r\n"
					+ "#fb0000R#fb0b0bE#fb1515D#fc2020S#fc2a2aT#fc3535O#fc3f3fN#fd4a4aI#fd5454E#fd5f5fN , ID : 9§r\n"
					+ "#00f3fbS#0cf4faC#19f5f9E#25f6f7N#32f7f6A#3ef9f5R#4bfaf4I#57fbf2S#64fcf1T#70fdf0E , ID : 10§r\n"
					+ "#747474D#888888E#9c9c9cS#b0b0b0I#c3c3c3G#d7d7d7N#ebebebE#ffffffR , ID : 11§r\n"
					+ "#e3ff00É#edff59T#f6ffb1É , ID : 12§r\n"
					+ "#ff4848N#ff6b6bO#ff8e8eË#ffb1b1L , ID : 13§r\n"
					+ "#fbbc00H#fbbf0eA#fcc21dL#fcc42bL#fcc73aO#fcca48W#fdcd56E#fdcf65E#fdd273N , ID : 14§r\n"
					+ "#0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S , ID : 15§r\n"
					+ "#000000P#1e1e1eV#3c3c3cP , ID : 16§r\n"
					+ "#fffc00T#e4fd37E#c9fe6dA#aeffa4M , ID : 17§r\n"
					+ "#d4ff00S#dffd29P#eafc51O#f4fa7aR#fff8a2T , ID : 18§r\n"
					+ "#00fb03P#31fc2eA#62fc58R#93fd83T#c4fdadY , ID : 19§r\n"
					+ "#0d00fbA#2116fbN#352dfcI#4943fcM#5d5afcA#7170fcT#8586fdE#999dfdU#adb3fdR , ID : 20§r\n"
					+ "#b304fbV#ba18fbI#c12cfcD#c840fcE#cf53fcA#d667fcS#dd7bfdT#e48ffdE , ID : 21§r\n"
					+ "#1b5d00C#1e6c00O#217b00M#248b00M#279a00A#29a900N#2cb800D#2fc800E#32d700U#35e600R , ID : 22§r\n"
					+ "#fb8f00§lM#fca42b§lO#fcba55§lD#fdcf80§lO , ID : 90§r\n"
					+ "§l#fb0000§lA#fc2727§lD#fc4e4e§lM#fd7474§lI#fd9b9b§lN , ID : 100§r\n"
					+ "\n§cSintax : /grade <player/& AS.HERE> <grade>"));
			return true;
		}

//		if(args[0].equalsIgnoreCase("&AS.HERE")) {
//
//			boolean asdetected = false;
//
//			if(player instanceof Player) {
//
//				Player p = (Player) player;
//
//				for(ArmorStand allst : p.getWorld().getEntitiesByClass(ArmorStand.class)) {
//
//					if(allst == null) {
//						p.sendMessage(main.getErrorPrefix() + "Impossible de charger un ArmorStand ! -> AS ignoré.");
//						return true;
//					}
//
//					Location ploc = p.getLocation();
//
//					if(allst.getLocation().distance(ploc) < 1) {
//						String stUUID = allst.getUniqueId().toString();
//
//						if(stUUID == null) {
//							p.sendMessage(main.getErrorPrefix() + "L'Armor Stand n'a pas d'UUID ! -> AS ignoré.");
//							return true;
//						}
//
//						asdetected = true;
//
//						GradeList gradeList = null;
//
//						try {
//							gradeList = grades.getGradeById(Integer.parseInt(args[1]));
//						} catch(NumberFormatException nbe){
//							try {
//								gradeList = GradeList.valueOf(args[1].toUpperCase());
//							}catch(Exception e) {
//								p.sendMessage(main.getPrefix() + "§4Grade non trouvé !");
//								return true;
//							}
//						}
//
//						String oldPrefix = main.hex(grades.getASGrade(allst).getPrefix());
//						String oldSuffix = main.hex(grades.getASGrade(allst).getSuffix());
//						String oldName = grades.getASGrade(allst).getName();
//
//
//
//
//						ASData data = new ASData(allst.getUniqueId());
//
//						if(data.getASName() == null) {
//							p.sendMessage(main.getErrorPrefix() + "L'Armor Stand n'a pas de nom défini dans les fichiers. Merci de faire /setstandname <nomdel'AS> !");
//							return true;
//						}
//
//						String newName = main.hex(grades.getGradeById(gradeList.getId()).getPrefix()) + data.getASName() + main.hex(grades.getGradeById(gradeList.getId()).getSuffix());
//						allst.setCustomName(newName);
//						grades.chageRank(stUUID, gradeList);
//
//						grades.deleteAS(allst);
//						grades.loadAS(allst);
//
//
//
////						if(allst.getCustomName() != null) {
////							//Removing Prefixes of allst's Name
////							Bukkit.getConsoleSender().sendMessage("replacing -- " + GradeList.ADMIN.getName());
////
////							allst.setCustomName(allst.getCustomName().replace(GradeList.ADMIN.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.ANIMATEUR.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.BUILDEUR.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.DECORATEUR.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.DESIGNER.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.ETE.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.GUIDE.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.HALLOWEEN.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.MEMBRE.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.MINIATEUR.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.MODO.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.NOEL.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.PAQUES.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.OWNER.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.PARTY.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.PVP.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.REDSTONIEN.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.SCENARISTE.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.SPORT.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.TEAM.getName(), ""));
////							allst.setCustomName(allst.getCustomName().replace(GradeList.VIP.getName(), ""));
////							allst.setCustomNameVisible(false);
////							____________________________
////	 						!! Removed bc no suffixes !!
////	 						!! Removed bc no suffixes !!
////	 						!! Removed bc no suffixes ||
////							||                         \\
////							||	||	  ||	|| 	|	    \\
////							||	||	  ||		|	     \\
////							||	||----||	||	|	      \\
////							||	||	  ||	||		       \\
////							||	||	  ||	||	|	        \\
////							//						         \\
////						   //						      	  \\
////						  // Removing Suffixes of allst's Name||
////						    allst.setCustomName(allst.getCustomName().replaceAll(GradeList.ADMIN.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.ANIMATEUR.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.BUILDEUR.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.DECORATEUR.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.DESIGNER.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.ETE.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.GUIDE.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.HALLOWEEN.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.MEMBRE.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.MINIATEUR.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.MODO.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.NOEL.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.PAQUES.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.OWNER.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.PARTY.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.PVP.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.REDSTONIEN.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.SCENARISTE.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.SPORT.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.TEAM.getSuffix(), ""));
////							allst.setCustomName(allst.getCustomName().replaceAll(GradeList.VIP.getSuffix(), ""));
//
//						allst.setCustomNameVisible(true);
//
//						p.sendMessage(main.getPrefix() + "§2Le §6grade§2 \"§6"+ gradeList.getName().toLowerCase() + "§2\" a été donné à §6l'Armor Stand à proximité de vous §2 avec succès !");
//						return true;
//					}
//				}
//
//				if(!asdetected) {
//					p.sendMessage(main.getPrefix() + "§4Aucun Armor Stand détecté proche de vous !");
//					return true;
//				} else {
//					return true;
//				}
//
//			} else {
//				player.sendMessage(main.getErrorPrefix() + "Vous devez être un joueur pour utiliser cet argument !");
//				return true;
//			}
//		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

		if(target.getPlayer() == null) {
			player.sendMessage(main.getPrefix() + "§4Joueur non trouvé !");
			return true;
		}

		String uuid = target.getPlayer().getUniqueId().toString();
		if(!grades.getConfig().contains(uuid)) {
			player.sendMessage(main.getPrefix() + "§4Le joueur ne s'est jamais connecté !");
			return true;
		}


		GradeList gradeList = null;

		try {
			gradeList = grades.getGradeById(Integer.parseInt(args[1]));
		} catch(NumberFormatException nbe){
			try {
				gradeList = GradeList.valueOf(args[1].toUpperCase());
			}catch(Exception e) {
				player.sendMessage(main.getPrefix() + "§4Grade non trouvé !");
				return true;
			}

			if(player instanceof Player) {
				Player p = (Player) player;
				int senderPower = grades.getPlayerGrade(p).getPower();
				int gradePower = gradeList.getPower();

				if(senderPower < gradePower) {
					p.sendMessage(main.getPrefix() + "§4Vous ne pouvez pas donner un grade plus haut que le votre !");
					return true;
				}
				try {
					OfflinePlayer t = Bukkit.getOfflinePlayer(args[0]);
					int targetPower = grades.getOffPlayerGrade(t).getPower();
					if(senderPower < gradePower) {
						p.sendMessage(main.getPrefix() + "§4Vous ne pouvez pas changer le grade d'un joueur qui a un grade plus haut que le votre !");
						return true;
					}
				} catch (Exception e) {
					Bukkit.getConsoleSender().sendMessage("[AdminCmdsB] §4An error encourred in command /grade : " + Arrays.toString(e.getStackTrace()));
					p.sendMessage(main.errorMsg);
					return true;
				}
			}


			grades.chageRank(uuid, gradeList);

			if(target.isOnline()) {

				Player onlineTarget = target.getPlayer();

				sendMessage(onlineTarget, "§2Un §4Administrateur/§eOpérateur§2 vous a donné le §6grade§2 \"§6" + gradeList.getName().toLowerCase() + "§2\" !");
				grades.deletePlayer(onlineTarget);
				grades.loadPlayer(onlineTarget);

				if(grades.hasPowerSup(onlineTarget, 69)) {
					main.superstaff.add(onlineTarget);
					main.mod.add(onlineTarget);
					if(!onlineTarget.isOp()) {
						onlineTarget.setOp(true);
					}
				}
				if(grades.hasPowerSup(onlineTarget, 99)) {
					main.admin.add(onlineTarget);
				}
				if(grades.hasPowerSup(onlineTarget, 999)) {
					main.owner.add(onlineTarget);
				}
				if(grades.hasPower(onlineTarget, 50)) {
					main.guides.add(onlineTarget);
				}
				if(grades.hasPowerSup(onlineTarget, 59)) {
					main.staff.add(onlineTarget);
				}

				String tPrefix = main.hex(grades.getPlayerGrade(onlineTarget).getPrefix());
				String tSuffix = main.hex(grades.getPlayerGrade(onlineTarget).getSuffix());

				onlineTarget.setPlayerListName(tPrefix + onlineTarget.getName() + tSuffix);
				onlineTarget.setDisplayName(tPrefix + onlineTarget.getName() + tSuffix);

			}
			grades.initConfig();
			player.sendMessage(main.getPrefix() + "§2Le §6grade§2 \"§6"+ gradeList.getName().toLowerCase() + "§2\" a été donné à §6" + target.getName() + "§2 avec succès !");

			return true;
		}



		return true;
	}

	public boolean sendMessage(CommandSender s, String msg) {
		s.sendMessage(main.getPrefix() + msg);
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
			for(GradeList gradeList : GradeList.values()) {
				if(gradeList.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
					tabComplete.add(gradeList.getName().toLowerCase());
				}
			}
		}

		return tabComplete;
	}


}
