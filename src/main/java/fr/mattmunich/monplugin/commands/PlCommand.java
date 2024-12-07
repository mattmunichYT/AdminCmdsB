package fr.mattmunich.monplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ArmorStand.LockType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.mattmunich.monplugin.MonPlugin;
import fr.mattmunich.monplugin.commandhelper.Grades;
import fr.mattmunich.monplugin.commandhelper.Settings;

import java.util.Arrays;
import java.util.Objects;

public class PlCommand implements CommandExecutor {

	private final MonPlugin main;

	private final Grades grades;

	private final Settings settings;

	public PlCommand(MonPlugin main, Grades grades, Settings settings) {
		this.main = main;
		this.grades = grades;
		this.settings = settings;
	}

	@Override
	public boolean onCommand(CommandSender p, Command cmd, String label, String[] args) {

		if(p instanceof BlockCommandSender) {
			p.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}


		Scoreboard score = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard();
		Team t = score.getTeam("nhide");

		if(args.length == 0) {
			p.sendMessage("§cSintax : /admincmdsb <args>");
			return true;
		}

		if(p instanceof Player rp) {

            if(!(main.staff.contains(rp))) {
				rp.sendMessage(main.noPermissionMsg);
				return true;
			}

		}


		if(args[0].equalsIgnoreCase("credits")) {
			p.sendMessage("§e----------§6§lCrédits§e----------§r\n\n§2Développeur : §6mattmunich\n§r§4You§fTube §2: §6mattmunich\n§r§aPseudo §lMinecraft §r§2: §6mattmunich\n§r");
			return true;
		} else if(args[0].equalsIgnoreCase("settings")) {
			if(p instanceof Player pr) {
                if(grades.hasPowerInf(pr, 69)) {
					pr.sendMessage(main.getPrefix() + "§4Vous n'avez pas la permission d'utiliser cet argument !");
					return true;
				}
				if(!(args.length > 1)) {
					pr.sendMessage("""
                            §e--------------------§6§lRéglages§e--------------------§r
                            §aRéglages disponibles :\
                            
                            §2coMsg, §6§oPermet d'activer/désactiver les message de (dé)connection§r\
                            
                            §2serverName, §6§oPermet de définir le nom du serveur§r\
                            
                            §2customTabList§6§oPermet de (dés)activer la liste des joueurs (Menu Tab) customisée§r\
                            
                            §2seeVanished, §6§oPermet aux joueurs ayant la permisson de voir ou pas les joueurs vanish§r\
                            
                            §2oldPVP, §6§oPermet d'activer/désactiver le PvP 1.8§r\
                            
                            §2TNTsEnabled, §6§oPermet d'activer/désactiver les TNTs§r\
                            
                            §2antiCheat, §6§oPermet d'activer/désactiver l'AntiCheats§r §b[§1Fonctionnalité §5§lExpérimentale§b]§r\
                            
                            §2advancedNameTags, §6§oPermet d'activer/désactiver les NameTags améliorés§r §b[§1Fonctionnalité §3§lBeta§b]§r\
                            
                            
                            §7§oAucun autre réglage disponible en ce moment""");
					return true;

				}
			if(args[1].equalsIgnoreCase("coMsg")) {
					if(args.length == 2 || args.length > 3) {
						pr.sendMessage("§cSintax : /admincmds <args>");
						return true;
					}else if(args[2].equalsIgnoreCase("true")) {
					if(settings.getCoMsg()) {
						pr.sendMessage(main.getPrefix() + "§4Les messages de (dé)connection sont déjà activés !");
						return true;
					}

					settings.coMsgTrue();
					pr.sendMessage(main.getPrefix() + "§2Les messages de (dé)connection ont été activés !");
					return true;
				} else if(args[2].equalsIgnoreCase("false")) {
					if(!settings.getCoMsg()) {
						pr.sendMessage(main.getPrefix() + "§4Les messages de (dé)connection sont déjà désactivés !");
						return true;
					}

					settings.coMsgFalse();
					pr.sendMessage(main.getPrefix() + "§2Les messages de (dé)connection ont été désactivés !");
					return true;
				} else {
					pr.sendMessage(main.getPrefix() + "§4Argument non trouvé ! Essayez plutôt :"
							+ "\n§6true §4ou §6false");
					return true;
				}

			} else if(args[1].equalsIgnoreCase("serverName")) {
				if(args.length == 2) {
					pr.sendMessage("§cSintax : /admincmds <args>");
					return true;
				}

				StringBuilder serverName = new StringBuilder();

				for(int i = 2; i < args.length; i++) {
					serverName.append(args[i]).append(" ");

				}


				serverName = new StringBuilder(main.hex(serverName.toString().trim()));
				serverName = new StringBuilder(ChatColor.translateAlternateColorCodes('&', String.join(" ", serverName.toString())));

				settings.setServerName(serverName.toString());
				pr.sendMessage(main.getPrefix() + "§2Le nom du serveur est maintenant \"§6" + serverName + "§2\" !");
				return true;

			}

			if(args[1].equalsIgnoreCase("customTabList")) {
				if(args.length == 2 || args.length > 3) {
					pr.sendMessage("§cSintax : /admincmds <args>");
					return true;
				}else if(args[2].equalsIgnoreCase("true")) {
					if(settings.getCTabList()) {
						pr.sendMessage(main.getPrefix() + "§4La liste des joueurs customisée est déjà activée !");
						return true;
					}

					settings.CTabListTrue();
					pr.sendMessage(main.getPrefix() + "§2La liste des joueurs customisée a été activée !");
					return true;
				} else if(args[2].equalsIgnoreCase("false")) {
					if(!settings.getCTabList()) {
						pr.sendMessage(main.getPrefix() + "§4La liste des joueurs customisée est déjà désactivée !");
						return true;
					}

					settings.CTabListFalse();
					pr.sendMessage(main.getPrefix() + "§2La liste des joueurs customisée a été désactivée !");
					return true;
				} else {
					pr.sendMessage(main.getPrefix() + "§4Argument non trouvé ! Essayez plutôt :"
							+ "\n§6true §4ou§6 false");
					return true;
				}
			} else if(args[1].equalsIgnoreCase("seeVanished")) {
				if(args.length == 2 || args.length > 3) {
					pr.sendMessage("§cSintax : /admincmds <args>");
					return true;
				}else if(args[2].equalsIgnoreCase("true")) {
					if(settings.getSeeVanished()) {
						pr.sendMessage(main.getPrefix() + "§4Les joueurs ayant la permisson peuvent déjà voir les joueurs vanish !");
						return true;
					}

					settings.seeVanishedTrue();
					pr.sendMessage(main.getPrefix() + "§2Les joueurs ayant la permisson peuvent maintenant voir les joueurs vanish !");
					return true;
				} else if(args[2].equalsIgnoreCase("false")) {
					if(!settings.getSeeVanished()) {
						pr.sendMessage(main.getPrefix() + "§4Les joueurs ayant la permission ne peuvent déjà pas voir les joueurs vanish !");
						return true;
					}

					settings.seeVanishedFalse();
					pr.sendMessage(main.getPrefix() + "§4Les joueurs ayant la permisson ne peuvent plus voir les joueurs vanish !");
					return true;
				} else {
					pr.sendMessage(main.getPrefix() + "§4Argument non trouvé ! Essayez plutôt :"
							+ "\n§6true§4 ou §6false");
					return true;
				}

		} else if(args[1].equalsIgnoreCase("antiCheat")) {
			if(args.length == 2 || args.length > 3) {
				pr.sendMessage("§cSintax : /admincmds <args>");
				return true;
			}else if(args[2].equalsIgnoreCase("true")) {
				if(settings.getAntiCheat()) {
					pr.sendMessage(main.getPrefix() + "§4L'AntiCheat est déjà actif !");
					return true;
				}

				settings.antiCheatTrue();
				pr.sendMessage(main.getPrefix() + "§2L'AntiCheat a été activé !");
				return true;
			} else if(args[2].equalsIgnoreCase("false")) {
				if(!settings.getAntiCheat()) {
					pr.sendMessage(main.getPrefix() + "§4L'AntiCheat est déjà désactivé !");
					return true;
				}

				settings.antiCheatFalse();
				pr.sendMessage(main.getPrefix() + "§4L'AntiCheat a été désactivé !");
				return true;
			} else {
				pr.sendMessage(main.getPrefix() + "§4Argument non trouvé ! Essayez plutôt :"
						+ "\n§6true§4 ou §6false");
				return true;
			}

	} else if(args[1].equalsIgnoreCase("oldPVP")) {
			if(args.length == 2 || args.length > 3) {
				pr.sendMessage("§cSintax : /admincmds <args>");
				return true;
			}else if(args[2].equalsIgnoreCase("true")) {
				if(settings.getOldPVP()) {
					pr.sendMessage(main.getPrefix() + "§4Le PvP 1.8 est déjà actif !");
					return true;
				}

				settings.setOldPVP(true);
				pr.sendMessage(main.getPrefix() + "§2Le PvP 1.8 a été activé !");
				return true;
			} else if(args[2].equalsIgnoreCase("false")) {
				if(!settings.getOldPVP()) {
					pr.sendMessage(main.getPrefix() + "§4Le PvP 1.8 est déjà inactif !!");
					return true;
				}

				settings.setOldPVP(false);
				pr.sendMessage(main.getPrefix() + "§2Le PvP 1.8 a été desactivé !");
				return true;
			} else {
				pr.sendMessage(main.getPrefix() + "§4Argument non trouvé ! Essayez plutôt :"
						+ "\n§6true§4 ou §6false");
				return true;
			}


		}else if(args[1].equalsIgnoreCase("advancedNameTags")) {
			if(args.length == 2 || args.length > 3) {
				pr.sendMessage("§cSintax : /admincmds <args>");
				return true;
			} else if(args[2].equalsIgnoreCase("true")) {
				if(settings.getAdvancedNameTags()) {
					pr.sendMessage(main.getPrefix() + "§4Les NameTags améliorés sont déjà activés !");
					return true;
				}

				settings.advancedNameTagsTrue();
				pr.sendMessage(main.getPrefix() + "§2Activation des NameTags améliorés... (L'opération peut prendre un certain temps)");
				if(t == null) {
				     t = score.registerNewTeam("nhide");
				     t.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
				     t.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
				     t.setAllowFriendlyFire(true);
				}

				for(Player player : Bukkit.getOnlinePlayers()) {

					t.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
					t.addEntry(player.getName());

					Location asloc = player.getLocation().add(0, 1.5, 0);

					//Creating stand1
					ArmorStand stand1 = (ArmorStand) Objects.requireNonNull(asloc.getWorld()).spawnEntity(asloc, EntityType.ARMOR_STAND);

					stand1.setCustomNameVisible(false);
					stand1.setInvulnerable(true);
					stand1.setCanPickupItems(false);
					stand1.setCollidable(false);
					stand1.setVisualFire(false);
					stand1.addScoreboardTag(player.getName() + "CODEHA@#*2");
					stand1.setInvisible(true);
					stand1.addEquipmentLock(EquipmentSlot.HEAD, LockType.ADDING_OR_CHANGING);
					stand1.addEquipmentLock(EquipmentSlot.CHEST, LockType.ADDING_OR_CHANGING);
					stand1.addEquipmentLock(EquipmentSlot.LEGS, LockType.ADDING_OR_CHANGING);
					stand1.addEquipmentLock(EquipmentSlot.FEET, LockType.ADDING_OR_CHANGING);
					stand1.setArrowsInBody(0);
					stand1.setSilent(true);
					stand1.setGravity(false);
					stand1.setRemoveWhenFarAway(false);
					stand1.setPersistent(true);
					player.addPassenger(stand1);
					stand1.setAI(false);
					stand1.setMarker(true);
					stand1.setBasePlate(false);


					//Creating stand
					ArmorStand stand = (ArmorStand) asloc.getWorld().spawnEntity(asloc, EntityType.ARMOR_STAND);

					stand.setCustomName(main.hex(player.getDisplayName()));
					stand.setCustomNameVisible(true);
					//stand.setSmall(true);
					stand.setInvulnerable(true);
					stand.setCanPickupItems(false);
					stand.setCollidable(false);
					stand.setVisualFire(false);
					stand.addScoreboardTag(player.getName());
					stand.setVisible(false);
					stand.setInvisible(true);
					stand.addEquipmentLock(EquipmentSlot.HEAD, LockType.ADDING_OR_CHANGING);
					stand.addEquipmentLock(EquipmentSlot.CHEST, LockType.ADDING_OR_CHANGING);
					stand.addEquipmentLock(EquipmentSlot.LEGS, LockType.ADDING_OR_CHANGING);
					stand.addEquipmentLock(EquipmentSlot.FEET, LockType.ADDING_OR_CHANGING);
					stand.setArrowsInBody(0);
					stand.setBasePlate(false);
					stand.setGravity(false);
					stand.setRemoveWhenFarAway(false);
					stand.setPersistent(true);
					stand1.addPassenger(stand);
					stand.setAI(false);
					stand.setMarker(true);
				}
				pr.sendMessage(main.getPrefix() + "§2Les NameTags améliorés sont maintenant activés !");
				return true;
			} else if(args[2].equalsIgnoreCase("false")) {
				if(!settings.getAdvancedNameTags()) {
					pr.sendMessage(main.getPrefix() + "§4Les NameTags améliorés sont déjà désactivés !");
					return true;
				}

				settings.advancedNameTagsFalse();
				pr.sendMessage(main.getPrefix() + "§2Désactivation des NameTags améliorés... (L'opération peut prendre un certain temps)");
				for(Player player : Bukkit.getOnlinePlayers()) {
                    assert t != null;
                    t.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
					t.removeEntry(player.getName());

					for(ArmorStand allst : player.getWorld().getEntitiesByClass(ArmorStand.class)) {
						if(allst.getScoreboardTags().contains(player.getName() + "CODEHA@#*2")){
							allst.leaveVehicle();
							allst.setInvulnerable(false);
							allst.setHealth(0);
							allst.damage(1000);
						}
					}
					for(ArmorStand allst : player.getWorld().getEntitiesByClass(ArmorStand.class)) {
						if(allst.getScoreboardTags().contains(player.getName() + "CODEHA@#*2")){
							allst.leaveVehicle();
							allst.setInvulnerable(false);
							allst.setHealth(0);
							allst.damage(1000);
						}
					}
				}
                assert t != null;
                t.unregister();
				pr.sendMessage(main.getPrefix() + "§2Les NameTags améliorés sont maintenant désactivés !");
				return true;
			} else {
				pr.sendMessage(main.getPrefix() + "§4Argument non trouvé ! Essayez plutôt :"
						+ "\n§6true §4ou §6false");
				return true;
			}
		} else if(args[1].equalsIgnoreCase("TNTsEnabled")) {
			if(args.length == 2 || args.length > 3) {
				pr.sendMessage("§cSintax : /admincmds <args>");
				return true;
			}else if(args[2].equalsIgnoreCase("true")) {
				if(settings.getTNTsEnabled()) {
					pr.sendMessage(main.getPrefix() + "§4Les TNTs sont déjà activées !");
					return true;
				}

				settings.TNTsEnabledTrue();
				pr.sendMessage(main.getPrefix() + "§2Les TNTs ont été activées !");
				return true;
			} else if(args[2].equalsIgnoreCase("false")) {
				if(!settings.getTNTsEnabled()) {
					pr.sendMessage(main.getPrefix() + "§4Les TNTs sont déjà désactivées !");
					return true;
				}

				settings.TNTsEnabledFalse();
				pr.sendMessage(main.getPrefix() + "§2Les TNTs ont été désactivées !");
				return true;
			} else {
				pr.sendMessage(main.getPrefix() + "§4Argument non trouvé ! Essayez plutôt :"
						+ "\n§6true §4ou §6false");
				return true;
			}

		} else {
			pr.sendMessage(main.getPrefix() + "§4Paramètre non trouvé ! Essayez plutôt :"
					+ "\n§6coMsg§4, §6serverName§4, §6customTabList§4, §6seeVanished§4, §6oldPVP§4, §6TNTsEnabled §4ou §6aAdvancedNameTagsTags");
			return true;
		}

		} else {
			if(!(args.length > 1)) {
				p.sendMessage("""
                        §e--------------------§6§lRéglages§e--------------------§r
                        §aRéglages disponibles :\
                        
                        §2coMsg, §6§oPermet d'activer/désactiver les message de (dé)connection§r\
                        
                        §2serverName, §6§oPermet de définir le nom du serveur§r\
                        
                        §2customTabList, §6§oPermet de (dés)activer la liste des joueurs(Menu Tab) customisée§r\
                        
                        §2seeVanished, §6§oPermet aux joueurs ayant la permisson de voir ou pas les joueurs vanish§r\
                        
                        §2oldPVP, §6§oPermet d'activer/désactiver le PvP 1.8§r\
                        
                        §2TNTsEnabled, §6§oPermet d'activer/désactiver les TNTs§r\
                        
                        §2antiCheat, §6§oPermet d'activer/désactiver l'AntiCheats§r §b[§1Fonctionnalité §5§lExpérimentale§b]§r\
                        
                        §2advancedNameTags, §6§oPermet d'activer/désactiver les NameTags améliorés§r §b[§1Fonctionnalité §3§lBeta§b]§r\
                        
                        §2moreLogs, §8§oDisponible bientôt...\
                        
                        
                        §7§oAucun autre réglage disponible en ce moment\s""");
				return true;

			}
			if(args[1].equalsIgnoreCase("coMsg")) {
					if(args.length == 2 || args.length > 3) {
						p.sendMessage("§cSintax : /admincmds <args>");
						return true;
					}

				if(args[2].equalsIgnoreCase("true")) {
					if(settings.getCoMsg()) {
						p.sendMessage(main.getPrefix() + "§4Les messages de (dé)connection sont déjà activés !");
						return true;
					}

					settings.coMsgTrue();
					p.sendMessage(main.getPrefix() + "§2Les messages de (dé)connection ont été activés !");
					return true;
				} else if(args[2].equalsIgnoreCase("false")) {
					if(!settings.getCoMsg()) {
						p.sendMessage(main.getPrefix() + "§4Les messages de (dé)connection sont déjà désactivés !");
						return true;
					}

					settings.coMsgFalse();
					p.sendMessage(main.getPrefix() + "§2Les messages de (dé)connection ont été désactivés !");
					return true;
				}

			}else if(args[1].equalsIgnoreCase("serverName")) {
				if(args.length == 2 || args.length > 3) {
					p.sendMessage("§cSintax : /admincmds <args>");
					return true;
				}

				StringBuilder serverName = new StringBuilder();

				for(int i = 2; i < args.length; i++) {
					serverName.append(args[i]).append(" ");

				}


				serverName = new StringBuilder(serverName.toString().trim());
				serverName = new StringBuilder(ChatColor.translateAlternateColorCodes('&', String.join(" ", serverName.toString())));

				settings.setServerName(serverName.toString());
				p.sendMessage(main.getPrefix() + "§2Le nom du serveur est maintenant \"§6" + serverName + "§2\" !");
				return true;

			}else if(args[1].equalsIgnoreCase("customTabList")) {
				if(args.length == 2 || args.length > 3) {
					p.sendMessage("§cSintax : /admincmds <args>");
					return true;
				}
				if(args[2].equalsIgnoreCase("true")) {
					if(settings.getCTabList()) {
						p.sendMessage(main.getPrefix() + "§4La liste des joueurs customisée est déjà activée !");
						return true;
					}

					settings.CTabListTrue();
					p.sendMessage(main.getPrefix() + "§2La liste des joueurs customisée a été activée !");
					return true;
				} else if(args[2].equalsIgnoreCase("false")) {
					if(!settings.getCTabList()) {
						p.sendMessage(main.getPrefix() + "§4La liste des joueurs customisée est déjà désactivée !");
						return true;
					}

					settings.CTabListFalse();
					p.sendMessage(main.getPrefix() + "§2La liste des joueurs customisée a été désactivée !");
					return true;
				}
			}else if(args[1].equalsIgnoreCase("seeVanished")) {
				if(args.length == 2 || args.length > 3) {
					p.sendMessage("§cSintax : /admincmds <args>");
					return true;
				}

				if(args[2].equalsIgnoreCase("true")) {
					if(settings.getSeeVanished()) {
						p.sendMessage(main.getPrefix() + "§4Les joueurs ayant la permisson peuvent déjà voir les joueurs vanish !");
						return true;
					}

					settings.seeVanishedTrue();
					p.sendMessage(main.getPrefix() + "§2Les joueurs ayant la permisson peuvent maintenant voir les joueurs vanish !");
					return true;
				} else if(args[2].equalsIgnoreCase("false")) {
					if(!settings.getSeeVanished()) {
						p.sendMessage(main.getPrefix() + "§4Les joueurs ayant la permission ne peuvent déjà pas voir les joueurs vanish !");
						return true;
					}

					settings.seeVanishedFalse();
					p.sendMessage(main.getPrefix() + "§4Les joueurs ayant la permisson ne peuvent plus voir les joueurs vanish !");
					return true;
				}

			} else if(args[1].equalsIgnoreCase("antiCheat")) {
				if(args.length == 2 || args.length > 3) {
					p.sendMessage("§cSintax : /admincmds <args>");
					return true;
				}else if(args[2].equalsIgnoreCase("true")) {
					if(settings.getAntiCheat()) {
						p.sendMessage(main.getPrefix() + "§4L'AntiCheat est déjà actif !");
						return true;
					}

					settings.antiCheatTrue();
					p.sendMessage(main.getPrefix() + "§2L'AntiCheat a été activé !");
					return true;
				} else if(args[2].equalsIgnoreCase("false")) {
					if(!settings.getAntiCheat()) {
						p.sendMessage(main.getPrefix() + "§4L'AntiCheat est déjà désactivé !");
						return true;
					}

					settings.antiCheatFalse();
					p.sendMessage(main.getPrefix() + "§4L'AntiCheat a été désactivé !");
					return true;
				} else {
					p.sendMessage(main.getPrefix() + "§4Argument non trouvé ! Essayez plutôt :"
							+ "\n§6true§4 ou §6false");
					return true;
				}

		} else if(args[1].equalsIgnoreCase("oldPVP")) {
				if(args.length == 2 || args.length > 3) {
					p.sendMessage("§cSintax : /admincmds <args>");
					return true;
				}else if(args[2].equalsIgnoreCase("true")) {
					if(settings.getOldPVP()) {
						p.sendMessage(main.getPrefix() + "§4Le PvP 1.8 est déjà actif !");
						return true;
					}

					settings.setOldPVP(true);
					p.sendMessage(main.getPrefix() + "§2Le PvP 1.8 a été activé !");
					return true;
				} else if(args[2].equalsIgnoreCase("false")) {
					if(!settings.getOldPVP()) {
						p.sendMessage(main.getPrefix() + "§4Le PvP 1.8 est déjà inactif !!");
						return true;
					}

					settings.setOldPVP(false);
					p.sendMessage(main.getPrefix() + "§2Le PvP 1.8 a été desactivé !");
					return true;
				} else {
					p.sendMessage(main.getPrefix() + "§4Argument non trouvé ! Essayez plutôt :"
							+ "\n§6true§4 ou §6false");
					return true;
				}


			}else if(args[1].equalsIgnoreCase("moreLogs")){
				p.sendMessage(main.InDevArgMsg);
				return true;

			}else if(args[1].equalsIgnoreCase("advancedNameTags")) {
				if(args.length == 2 || args.length > 3) {
					p.sendMessage("§cSintax : /admincmds <args>");
					return true;
				} else if(args[2].equalsIgnoreCase("true")) {
					if(settings.getAdvancedNameTags()) {
						p.sendMessage(main.getPrefix() + "§4Les NameTags améliorés sont déjà activés !");
						return true;
					}

					settings.advancedNameTagsTrue();
					p.sendMessage(main.getPrefix() + "§2Activation des NameTags améliorés... (L'opération peut prendre un certain temps)");
					if(t == null) {
					    t = score.registerNewTeam("nhide");
					    t.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
					    t.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
					    t.setAllowFriendlyFire(true);
					}

					for(Player player : Bukkit.getOnlinePlayers()) {

						t.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
						t.addEntry(player.getName());

						Location asloc = player.getLocation().add(0, 1.5, 0);

						//Creating stand1
						ArmorStand stand1 = (ArmorStand) Objects.requireNonNull(asloc.getWorld()).spawnEntity(asloc, EntityType.ARMOR_STAND);

						stand1.setCustomNameVisible(false);
						stand1.setInvulnerable(true);
						stand1.setCanPickupItems(false);
						stand1.setCollidable(false);
						stand1.setVisualFire(false);
						stand1.addScoreboardTag(player.getName() + "CODEHA@#*2");
						stand1.setInvisible(true);
						stand1.addEquipmentLock(EquipmentSlot.HEAD, LockType.ADDING_OR_CHANGING);
						stand1.addEquipmentLock(EquipmentSlot.CHEST, LockType.ADDING_OR_CHANGING);
						stand1.addEquipmentLock(EquipmentSlot.LEGS, LockType.ADDING_OR_CHANGING);
						stand1.addEquipmentLock(EquipmentSlot.FEET, LockType.ADDING_OR_CHANGING);
						stand1.setArrowsInBody(0);
						stand1.setSilent(true);
						stand1.setGravity(false);
						stand1.setRemoveWhenFarAway(false);
						stand1.setPersistent(true);
						player.addPassenger(stand1);
						stand1.setAI(false);
						stand1.setMarker(true);
						stand1.setBasePlate(false);


						//Creating stand
						ArmorStand stand = (ArmorStand) asloc.getWorld().spawnEntity(asloc, EntityType.ARMOR_STAND);

						stand.setCustomName(main.hex(player.getDisplayName()));
						stand.setCustomNameVisible(true);
						//stand.setSmall(true);
						stand.setInvulnerable(true);
						stand.setCanPickupItems(false);
						stand.setCollidable(false);
						stand.setVisualFire(false);
						stand.addScoreboardTag(player.getName());
						stand.setVisible(false);
						stand.setInvisible(true);
						stand.addEquipmentLock(EquipmentSlot.HEAD, LockType.ADDING_OR_CHANGING);
						stand.addEquipmentLock(EquipmentSlot.CHEST, LockType.ADDING_OR_CHANGING);
						stand.addEquipmentLock(EquipmentSlot.LEGS, LockType.ADDING_OR_CHANGING);
						stand.addEquipmentLock(EquipmentSlot.FEET, LockType.ADDING_OR_CHANGING);
						stand.setArrowsInBody(0);
						stand.setBasePlate(false);
						stand.setGravity(false);
						stand.setRemoveWhenFarAway(false);
						stand.setPersistent(true);
						stand1.addPassenger(stand);
						stand.setAI(false);
						stand.setMarker(true);
					}
					p.sendMessage(main.getPrefix() + "§2Les NameTags améliorés sont maintenant activés !");
					return true;
				} else if(args[2].equalsIgnoreCase("false")) {
					if (!settings.getAdvancedNameTags()) {
						p.sendMessage(main.getPrefix() + "§4Les NameTags améliorés sont déjà désactivés !");
						return true;
					}

					settings.advancedNameTagsFalse();
					p.sendMessage(main.getPrefix() + "§2Désactivation des NameTags améliorés... (L'opération peut prendre un certain temps)");
					for (Player player : Bukkit.getOnlinePlayers()) {
						assert t != null;
						t.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
						t.removeEntry(player.getName());

						for (ArmorStand allst : player.getWorld().getEntitiesByClass(ArmorStand.class)) {
							if (allst.getScoreboardTags().contains(player.getName() + "CODEHA@#*2")) {
								allst.leaveVehicle();
								allst.setInvulnerable(false);
								allst.setHealth(0);
								allst.damage(1000);
							}
						}
						for (ArmorStand allst : player.getWorld().getEntitiesByClass(ArmorStand.class)) {
							if (allst.getScoreboardTags().contains(player.getName() + "CODEHA@#*2")) {
								allst.leaveVehicle();
								allst.setInvulnerable(false);
								allst.setHealth(0);
								allst.damage(1000);
							}
						}
					}
					assert t != null;
					t.unregister();
					p.sendMessage(main.getPrefix() + "§2Les NameTags améliorés sont maintenant désactivés !");
					return true;
				}
			} else if(args[1].equalsIgnoreCase("TNTsEnabled")) {
				if(args.length == 2 || args.length > 3) {
					p.sendMessage("§cSintax : /admincmds <args>");
					return true;
				}else if(args[2].equalsIgnoreCase("true")) {
					if(settings.getTNTsEnabled()) {
						p.sendMessage(main.getPrefix() + "§4Les TNTs sont déjà activées !");
						return true;
					}

					settings.TNTsEnabledTrue();
					p.sendMessage(main.getPrefix() + "§2Les TNTs ont été activées !");
					return true;
				} else if(args[2].equalsIgnoreCase("false")) {
					if(!settings.getTNTsEnabled()) {
						p.sendMessage(main.getPrefix() + "§4Les TNTs sont déjà désactivées !");
						return true;
					}

					settings.TNTsEnabledFalse();
					p.sendMessage(main.getPrefix() + "§2Les TNTs ont été désactivées !");
					return true;
				} else {
					p.sendMessage(main.getPrefix() + "§4Argument non trouvé ! Essayez plutôt :"
							+ "\n§6true §4ou §6false");
					return true;
				}

			} else {
				p.sendMessage(main.getPrefix() + "§4Paramètre non trouvé ! Essayez plutôt :"
						+ "\n§6coMsg§4, §6serverName§4, §6customTabList§4, §6seeVanished§4, §6oldPVP§4, §6TNTsEnabled §4ou §6aAdvancedNameTagsTags");
				return true;
			}

		}
	} else if(args[0].equalsIgnoreCase("version")) {
			p.sendMessage("§4Admin§eCmds§6B §8- §aCustom §4§lM§c§lJ§6§lE§e§lP \n §2Version " + main.version +"\n§aMinecraft version 1.21 or later");
	} else if(args[0].equalsIgnoreCase("grades")){

		p.sendMessage(main.hex("""
                §e------§6Grades§e------§r
                §l§6Liste des gardes :\s
                #b1b1b1M#c1c1c1E#d0d0d0M#e0e0e0B#efefefR#ffffffE§r, ID : 1§r
                #fbbc00T#e0c70dE#c4d21aS#a9dd27T#8de733E#72f240U#56fd4dR§r, ID : 3§r
                #ffcf00V#ffe55fI#fffabeP§r, ID : 4§r
                #fbcb00G#fcd62bU#fce156I#fdec80D#fdf7abE§r, ID : 5§r
                #fb00f8B#f812f9U#f524f9I#f236faL#f047fbD#ed59fcE#ea6bfcU#e77dfdR§r, ID : 6§r
                #084cfbM#0d52fbI#1258fcN#175efcI#1d65fcA#226bfcT#2771fdE#2c77fdR#317dfdR§r, ID : 7§r
                #3afb00D#40fb0aE#47fb13C#4dfc1dO#53fc27R#5afc30A#60fc3aT#66fd44E#6dfd4dU#73fd57R§r, ID : 8§r
                #fb0000R#fb0b0bE#fb1515D#fc2020S#fc2a2aT#fc3535O#fc3f3fN#fd4a4aI#fd5454E#fd5f5fN§r, ID : 9§r
                #00f3fbS#0cf4faC#19f5f9E#25f6f7N#32f7f6A#3ef9f5R#4bfaf4I#57fbf2S#64fcf1T#70fdf0E§r, ID : 10§r
                #747474D#888888E#9c9c9cS#b0b0b0I#c3c3c3G#d7d7d7N#ebebebE#ffffffR§r, ID : 11§r
                #e3ff00É#edff59T#f6ffb1É§r, ID : 12§r
                #ff4848N#ff6b6bO#ff8e8eË#ffb1b1L§r, ID : 13§r
                #fbbc00H#fbbf0eA#fcc21dL#fcc42bL#fcc73aO#fcca48W#fdcd56E#fdcf65E#fdd273N§r, ID : 14§r
                #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S§r, ID : 15§r
                #000000P#1e1e1eV#3c3c3cP§r, ID : 16§r
                #fffc00T#e4fd37E#c9fe6dA#aeffa4M§r, ID : 17§r
                #d4ff00S#dffd29P#eafc51O#f4fa7aR#fff8a2T§r, ID : 18§r
                #00fb03P#31fc2eA#62fc58R#93fd83T#c4fdadY§r, ID : 19§r
                #0d00fbA#2116fbN#352dfcI#4943fcM#5d5afcA#7170fcT#8586fdE#999dfdU#adb3fdR§r, ID : 20§r
                #b304fbV#ba18fbI#c12cfcD#c840fcE#cf53fcA#d667fcS#dd7bfdT#e48ffdE§r, ID : 21§r
                #008B8BM#177E7EO#2E7272D#466565E#5D5858L#744C4CI#8B3F3FS#A23333A#B92626T#D11919E#E80D0DU#FF0000R§r, ID : 22§r
                #1b5d00C#1e6c00O#217b00M#248b00M#279a00A#29a900N#2cb800D#2fc800E#32d700U#35e600R§r, ID : 23§r
                #696969§lD#878787§lE#A5A5A5§lV§r, ID : 80§r
                #fb8f00§lM#fca42b§lO#fcba55§lD#fdcf80§lO§r, ID : 90§r
                §l#fb0000§lA#fc2727§lD#fc4e4e§lM#fd7474§lI#fd9b9b§lN§r, ID : 99§r
                #ffed00§lO#e2e317§lW#c6d82e§lN#a9ce44§lE#8cc35b§lR§r, ID : 100§r
               \s"""));
			return true;
		} else if(args[0].equalsIgnoreCase("reload")) {
			p.sendMessage(main.getPrefix() + "§2§oRechargement du plugin... §r§7(Cela peut prendre un certain temps !)");

			try {
				main.onDisable();
				main.onLoad();
				main.onEnable();
				p.sendMessage(main.getPrefix() + "§2Le plugin a été rechargé avec succès !");
				return true;
			} catch(Exception e) {
				p.sendMessage(main.getErrorPrefix() + "Une erreur s'est produite lors du rechargement du plugin. §l§4Merci de redémarrer le serveur pour éviter tout problème !");
				Bukkit.getConsoleSender().sendMessage(main.errorPrefix + "§cCoulnd't reload plugin : §r" + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()).replace(",",",\n"));
				return true;
			}

		} else {
			p.sendMessage(main.getPrefix() + "§cSintax : /admincmdsb <args>");
			return true;
		}

		return true;
	}
}
