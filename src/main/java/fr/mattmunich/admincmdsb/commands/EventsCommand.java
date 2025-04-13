package fr.mattmunich.admincmdsb.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;
import fr.mattmunich.admincmdsb.commandhelper.Events;
import fr.mattmunich.admincmdsb.commandhelper.GradeList;
import fr.mattmunich.admincmdsb.commandhelper.Grades;
import fr.mattmunich.admincmdsb.commandhelper.Warp;

import net.md_5.bungee.api.ChatColor;
@SuppressWarnings("unused")
public class EventsCommand implements CommandExecutor, TabCompleter{

	private final Main main;

	private final Events events;

	private final Warp warp;

	private final Grades grades;

	public EventsCommand(Main main, Events events, Grades grades, Warp warp) {
		this.main = main;
		this.events = events;
		this.grades = grades;
		this.warp = warp;
	}

	@Override
	public boolean onCommand(CommandSender p, Command cmd, String label, String[] args) {

		if(p instanceof BlockCommandSender) {
			p.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}


		if(args.length < 1) {
			if(p instanceof Player rp) {

                if(main.admin.contains(rp)) {
					rp.sendMessage("""
                            §2----------- §6§l/event : Aide  §r§2-----------§r
                            §7- §2/event admin create <startDate-fromat:[dd/MM/yyyy-HH:mm]/now> <eventDuration> <eventName> <eventDescription>
                            §7- §2/event admin modify <startDate/duration/title/description/grade/warp>
                            §7- §2/event admin cancel <eventName>
                            §7- §2/event admin delete <eventName>
                            §7- §2/event admin
                            §7- §2/event list
                            §7- §2/event info
                            §7- §2/event help
                            §7- §2/event
                            
                            §cSintax : /events <args>""");
					return true;
				} else {
					rp.sendMessage("""
                            §2----------- §6§l/events : Aide  §r§2-----------§r
                            §7- §2/event list
                            §7- §2/event info
                            §7- §2/event help
                            §7- §2/event
                            
                            §cSintax : /events <args>""");
					return true;
				}
			} else {
				p.sendMessage("""
                        §2----------- §6§l/event : Aide  §r§2-----------§r
                        §7- §2/event admin create <startDate-fromat:[dd/MM/yyyy-HH:mm]/now> <eventDuration> <eventName> <eventDescription>
                        §7- §2/event admin modify <startDate/duration/title/description/grade/warp>
                        §7- §2/event admin cancel <eventName>
                        §7- §2/event admin delete <eventName>
                        §7- §2/event admin
                        §7- §2/event list
                        §7- §2/event info
                        §7- §2/event help
                        §7- §2/event
                        
                        §cSintax : /events <args>""");
				return true;
			}

		}

		if(args[0].equalsIgnoreCase("admin")) {


			if(!main.admin.contains(p)) {
				p.sendMessage(main.noPermissionMsg);
				return true;
			}

			if(args.length == 1) {
				p.sendMessage("""
                        §2----------- §6§l/event §4§ladmin§a : Aide  §r§2-----------§r
                        §7- §2/event admin create <startDate-fromat:[dd/MM/yyyy-HH:mm]/now> <eventDuration> <eventName> <eventDescription>
                        §7- §2/event admin modify <start/duration/name/description(/grade/warp)>\s
                        §7- §2/event admin cancel <eventName>
                        §7- §2/event admin delete <eventName>
                        §7- §2/event admin
                        §cSintax : /event admin <create/modify/cancel/delete> <args>""");
				return true;
			}

			if(args[1].equalsIgnoreCase("create")) {

				if(args.length < 6) {
					p.sendMessage("§cSintax : /event admin create <startDate-fromat:[dd/MM/yyyy-HH:mm]/now> <eventDuration> <eventName> <eventDescription>");
					return true;
				}

				String name = ChatColor.translateAlternateColorCodes('&', main.hex(args[4]));

				if(name.equalsIgnoreCase("list")) {
					p.sendMessage(main.getErrorPrefix() + "§4Impossible d'utiliser le mot §c\"list\"§4 pour le nom d'un event !");
					return true;
				}

				String format = args[3].substring(args[3].length() - 1, args[3].length());
				int duration = Integer.valueOf(args[3].substring(0, args[3].length() - 1));


				switch(format) {

					case "s":
						duration = duration * 1000;
						break;

					case "m":
						duration = duration * 1000 * 60;
						break;

					case "h":
						duration = duration * 1000 * 60 * 60;
						break;

					case "d":
						duration = duration * 1000 * 60 * 60 * 24;
						break;

					case "w":
						duration = duration * 1000 * 60 * 60 * 24 * 7;
						break;

					default:
						p.sendMessage(main.getPrefix() + "§4Format de temps non reconnu !");
						return true;
				}

				String desc = "";

				for(int i = 5; i < args.length; i++) {
					desc = desc + args[i] + " ";

				}


				Date startDate = new Date();

				desc = desc.trim();
				try {
					long startMillis = System.currentTimeMillis(); // Default value

					if(!args[2].equalsIgnoreCase("now")) {
						try {
							startDate = new SimpleDateFormat("dd/MM/yyyy-HH:mm").parse(args[2]);
							startMillis = startDate.getTime();
							if(startMillis <= System.currentTimeMillis()) {
								p.sendMessage(main.getPrefix() + "§4La date du début de l'évent ne doit pas être passée !");
								return true;
							}
							startDate.setTime(duration);
							p.sendMessage("Start : " + startDate.toString());
						} catch (Exception e) {
							p.sendMessage(main.getPrefix() + "§4Format non reconnu, veuillez utiliser ce fromat : §cdd/MM/yyyy-HH:mm");
							return true;
						}

					} else if(args[2].equalsIgnoreCase("now")) {
						try {
							startDate = new Date(System.currentTimeMillis());
						} catch (Exception e) {
							p.sendMessage(main.getErrorPrefix() + "§4Impossible d'obtenir la date de maintenant, essayez de la définir.");
							return true;
						}
					} else {
						p.sendMessage("§cSintax : /event admin create <startDate-fromat:[dd/MM/yyyy-HH:mm]/now> <eventDuration> <eventName> <eventDescription>");
						return true;
					}

					events.setEvent(startMillis, duration, name, desc);
					p.sendMessage(main.getPrefix() + "§2----------- §6§lEvents :§2§l Succès  §r§2-----------§r\n"
							+ "§a§lL'événement a été créé avec les paramètres suivants : \n"
							+ "§2Nom : " + name + "\n"
							+ "§2Description : " + desc + "\n"
							+ "§2Date de début : §6" + startDate + "\n"
							+ "§2Durée §o(en ms)§r§2 : §6" + duration + "\n");
					return true;
				} catch (Exception e) {
					p.sendMessage(main.getPrefix() + "§2----------- §6§lEvents :§4§l Erreur  §r§2-----------§r\n"
							+ "§cUne erreure est survenue lors de la création de \n"
							+ "l'événement avec les paramètres suivants : \n"
							+ "§2Nom : " + name + "\n"
							+ "§2Description : " + desc + "\n"
							+ "§2Date de début : §6" + startDate + "\n"
							+ "§2Durée §o(en minutes)§r§2 : §6" + duration/1000/60 + "\n");

					main.logError("Une erreur est survenue lors de la création d'un événement", e);
					return true;
				}



			} else if(args[1].equalsIgnoreCase("cancel")) {
				if(args.length < 3) {
					p.sendMessage("§cSintax : /event admin cancel <eventName>");
					return true;
				}
				String eventName = args[2];
				if(events.exist(eventName)) {
					events.toggleCanceled(eventName);

					if(events.isCanceled(eventName)) {
						p.sendMessage(main.getPrefix() + "§2L'événement §6" + eventName + "§2 a été annulé avec succès !");
					} else {
						p.sendMessage(main.getPrefix() + "§2L'événement §6" + eventName + "§2 est maintenant de nouveau planifié avec succès !");
					}

					return true;
				} else {
					p.sendMessage(main.getPrefix() + "§4Impossible de trouver un événement avec le nom §c" + eventName + "§4 !");
					return true;
				}
			} else if (args[1].equalsIgnoreCase("modify")) {
				String eventName = args[3];

				if(!events.exist(eventName)) {
					p.sendMessage(main.getPrefix() + "§4Impossible de trouver un événement avec le nom §c" + eventName + "§4 !");
					return true;
				}


				if(args[2].equalsIgnoreCase("grade") || args[2].equalsIgnoreCase("rank")) {
					GradeList gradeList = null;

					try {
						gradeList = grades.getGradeById(Integer.parseInt(args[4]));
					} catch(NumberFormatException nbe){
						try {
							gradeList = GradeList.valueOf(args[4].toUpperCase());
						}catch(Exception e) {
							p.sendMessage(main.getPrefix() + "§4Grade non trouvé !");
							return true;
						}
					}
					int id = gradeList.getId();
					events.setMinGradeId(eventName, id);

					p.sendMessage(main.getPrefix() + "§2Le grade minimum requis poour l'event §6" + eventName + "§2 à été défini à §r" + main.hex(gradeList.getPrefix()) + "§r§2 !");
					return true;
				} else if(args[2].equalsIgnoreCase("warp")) {
					String warpName = args[4];

					if(!warp.getConfig().contains("warp." + warpName + ".")) {
						p.sendMessage(main.getPrefix() + "§4Le warp §c" + warpName + "§4 n'existe pas !");
						return true;
					}

					events.setTP(eventName, warpName);
					p.sendMessage(main.getPrefix() + "§2Le point de téléportation de l'event §6" + eventName + "§2 à été défini au warp §6§l" + warpName + "§r§2 !");
					return true;
				} else if(args[2].equalsIgnoreCase("duration")) {
					String format = args[4].substring(args[4].length() - 1, args[4].length());
					int duration = Integer.valueOf(args[4].substring(0, args[4].length() - 1));


					switch(format) {

						case "s":
							duration = duration * 1000;
							break;

						case "m":
							duration = duration * 1000 * 60;
							break;

						case "h":
							duration = duration * 1000 * 60 * 60;
							break;

						case "d":
							duration = duration * 1000 * 60 * 60 * 24;
							break;

						case "w":
							duration = duration * 1000 * 60 * 60 * 24 * 7;
							break;

						default:
							p.sendMessage(main.getPrefix() + "§4Format de temps non reconnu !");
							return true;
					}
					events.setDuration(eventName, duration);
					p.sendMessage(main.getPrefix() + "§2La durée de l'event §6" + eventName + "§2 à été modifiée à §6§l" + duration/1000/60/60 + "§r§ah§r§2 !");
					return true;
				} else if(args[2].equalsIgnoreCase("name")) {
					String newName = args[4];

					events.setName(eventName, newName);
					p.sendMessage(main.getPrefix() + "§2Le nom de l'event §6" + eventName + "§2 à été modifiée à §6§l" + newName + "§r§2 !");
					return true;
				}  else if(args[2].equalsIgnoreCase("description")) {

					String desc = "";

					for(int i = 4; i < args.length; i++) {
						desc = desc + args[i] + " ";

					}

					desc.trim();

					events.setDescription(eventName, desc);
					p.sendMessage(main.getPrefix() + "§2La description de l'event §6" + eventName + "§2 à été modifiée à §6§l" + desc + "§r§2 !");
					return true;
				}  else if(args[2].equalsIgnoreCase("startDate")) {

					Date startDate = new Date();
					TimeZone timezone = TimeZone.getTimeZone("CET");
					SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
					format.setTimeZone(timezone);
					try {
						long startMillis = System.currentTimeMillis(); // Default value

						if(!args[4].equalsIgnoreCase("now")) {
							try {
								startDate = format.parse(args[4]);
								startMillis = startDate.getTime();
								if(startMillis <= System.currentTimeMillis()) {
									p.sendMessage(main.getPrefix() + "§4La date du début de l'évent ne doit dans le passé !");
									return true;
								}
							} catch (Exception e) {
								p.sendMessage(main.getPrefix() + "§4Format non reconnu, veuillez utiliser ce fromat : §cdd/MM/yyyy-HH:mm");
								return true;
							}

						} else if(args[4].equalsIgnoreCase("now")) {
							try {
								startDate = new Date(System.currentTimeMillis());
							} catch (Exception e) {
								p.sendMessage(main.getErrorPrefix() + "§4Impossible d'obtenir la date de maintenant, essayez de la définir.");
								return true;
							}
						} else {
							p.sendMessage("§cSintax : /event admin modify startDate <eventName> <startDate-fromat:[dd/MM/yyyy-HH:mm]/now>");
							return true;
						}

						events.setStartDate(eventName, startMillis);
						p.sendMessage(main.getPrefix() + "§2La date du début de l'event §6" + eventName + "§2 à été modifiée à §6§l" + startDate.toString() + "§r§2 !");
						return true;
					} catch (Exception e) {
						p.sendMessage(main.getErrorPrefix() + "Une erreur s'est produite lors de la modification de la date de l'event !");
						main.logError("Une erreur s'est produite lors de la modification de la date de l'event",e);
						return true;
					}
				} else {
					p.sendMessage("§cSintax : /event admin modify <startDate/duration/name/description/grade/warp> <eventName> <args>");
					return true;
				}
			} else if (args[1].equalsIgnoreCase("delete")) {
				if(args.length != 3) {
					p.sendMessage("§cSintax : /event admin delete <eventName>");
					return true;
				}
				if(events.exist(args[2])) {
					String eventName = args[2];
					if(!events.isCanceled(eventName)) {
						p.sendMessage(main.getPrefix() + "§4Veuillez annuler l'événement avant de le supprimer !");
						return true;
					}

					events.delEvent(eventName);
					p.sendMessage(main.getPrefix() + "§2L'événement §6" + eventName + "§2 a été supprimé !");
					return true;
				} else {
					p.sendMessage(main.getPrefix() + "§4Impossible de trouver un événement avec le nom §c" + args[2].toString() + "§4 !");
					return true;
				}
			} else {
				p.sendMessage("§2----------- §6§l/event §4§ladmin§a : Aide  §r§2-----------§r\n"
						+ "§7- §2/event admin create <startDate-fromat:[dd/MM/yyyy-HH:mm]/now> <eventDuration> <eventName> <eventDescription>\n"
						+ "§7- §2/event admin modify <startDate/duration/name/description/grade/warp> <args>\n"
						+ "§7- §2/event admin cancel <eventName>\n"
						+ "§7- §2/event admin delete <eventName>\n"
						+ "§7- §2/event admin\n"
						+ "§cSintax : /event admin <create/modify/cancel/delete> <args>");
				return true;
			}



		} else {
			if(args[0].equalsIgnoreCase("list")) {
				events.eventListSendMsg(p);
				return true;

			} else if (args[0].equalsIgnoreCase("info")) {
				if(args.length != 2) {
					p.sendMessage("§cSintax : /event info <eventName>");
					return true;
				}

				String eventName = args[1];
				if(events.exist(eventName)) {
					//Is canceled
					if(events.isCanceled(eventName) && !events.isTPDefined(eventName) && (events.getMinGradeId(eventName) == 0)) {
						p.sendMessage(main.hex("§2----------- §6§l" + eventName + " §a: Info  §r§2-----------§r\n"
									+ "                    §4> #fb0000§lE#fb0e00§lV#fb1d00§lE#fc2b00§lN#fc3a00§lT #fc4800§lA#fc5600§lN#fc6500§lN#fd7300§lU#fd8200§lL#fd9000§lÉ §4<                    \n"
									+ "§8Description : §7" + events.getDescription(eventName) + "\n"
									+ "§8Date de début : §7" + events.getStartDate(eventName) + "\n"
									+ "§8Durée (en minutes) : §7" + events.getMilliseconds(eventName)/1000/60 + "\n"
									+ "§8Grade requis : §7Aucun \n"
									+ "§8Point de TP : §7Non Défini"));
						return true;
					} else if (events.isCanceled(eventName) && events.isTPDefined(eventName) && events.getMinGradeId(eventName) == 0) {
						p.sendMessage(main.hex("§2----------- §6§l" + eventName + " §a: Info  §r§2-----------§r\n"
								+ "                    §4> #fb0000§lE#fb0e00§lV#fb1d00§lE#fc2b00§lN#fc3a00§lT #fc4800§lA#fc5600§lN#fc6500§lN#fd7300§lU#fd8200§lL#fd9000§lÉ §4<                    \n"
								+ "§8Description : §7" + events.getDescription(eventName) + "\n"
								+ "§8Date de début : §7" + events.getStartDate(eventName) + "\n"
								+ "§8Durée (en minutes) : §7" + events.getMilliseconds(eventName)/1000/60 + "\n"
								+ "§8Grade requis : §7Aucun \n"
								+ "§8Point de TP : §7" + "Défini"));
						return true;
					} else if (events.isCanceled(eventName) && events.isTPDefined(eventName) && events.getMinGradeId(eventName) > 0) {
						p.sendMessage(main.hex("§2----------- §6§l" + eventName + " §a: Info  §r§2-----------§r\n"
								+ "                    §4> #fb0000§lE#fb0e00§lV#fb1d00§lE#fc2b00§lN#fc3a00§lT #fc4800§lA#fc5600§lN#fc6500§lN#fd7300§lU#fd8200§lL#fd9000§lÉ §4<                    \n"
								+ "§8Description : §7" + events.getDescription(eventName) + "\n"
								+ "§8Date de début : §7" + events.getStartDate(eventName) + "\n"
								+ "§8Durée (en minutes) : §7" + events.getMilliseconds(eventName)/1000/60 + "\n"
								+ "§8Grade requis : §7" + grades.getGradeById(events.getMinGradeId(eventName)).getPrefix() + "\n"
								+ "§8Point de TP : §7" + "Défini"));
						return true;
					} else if (events.isCanceled(eventName) && !events.isTPDefined(eventName) && events.getMinGradeId(eventName) > 0) {
						p.sendMessage(main.hex("§2----------- §6§l" + eventName + " §a: Info  §r§2-----------§r\n"
								+ "                    §4> #fb0000§lE#fb0e00§lV#fb1d00§lE#fc2b00§lN#fc3a00§lT #fc4800§lA#fc5600§lN#fc6500§lN#fd7300§lU#fd8200§lL#fd9000§lÉ §4<                    \n"
								+ "§8Description : §7" + events.getDescription(eventName) + "\n"
								+ "§8Date de début : §7" + events.getStartDate(eventName) + "\n"
								+ "§8Durée (en minutes) : §7" + events.getMilliseconds(eventName)/1000/60 + "\n"
								+ "§8Grade requis : §7" + grades.getGradeById(events.getMinGradeId(eventName)).getPrefix() + "\n"
								+ "§8Point de TP : §7Non Défini"));
						return true;
					}
					//Is now
					else if(events.isNow(eventName) && !events.isTPDefined(eventName) && (events.getMinGradeId(eventName) == 0)) {
						p.sendMessage(main.hex("§2----------- §6§l" + eventName + " §a: Info  §r§2-----------§r\n"
								+ "                    §2> #49fb00§lE#58fb0d§lV#66fb1a§lE#75fc26§lN#83fc33§lT #92fc40§lE#a1fc4d§lN #affc59§lC#befc66§lO#ccfd73§lU#dbfd80§lR#e9fd8c§lS #f8fd99§l! §2<                    \n"
								+ "§2Description : §6" + events.getDescription(eventName) + "\n"
								+ "§2Date de début : §6" + events.getStartDate(eventName) + "\n"
								+ "§2Durée (en minutes) : §6" + events.getMilliseconds(eventName)/1000/60 + "\n"
								+ "§2Grade requis : §6Aucun \n"
								+ "§8Point de TP : §7Non Défini"));
						return true;
					} else if (events.isNow(eventName) && events.isTPDefined(eventName) && events.getMinGradeId(eventName) == 0) {
						p.sendMessage(main.hex("§2----------- §6§l" + eventName + " §a: Info  §r§2-----------§r\n"
								+ "                    §2> #49fb00§lE#58fb0d§lV#66fb1a§lE#75fc26§lN#83fc33§lT #92fc40§lE#a1fc4d§lN #affc59§lC#befc66§lO#ccfd73§lU#dbfd80§lR#e9fd8c§lS #f8fd99§l! §2<                    \n"
								+ "§2Description : §6" + events.getDescription(eventName) + "\n"
								+ "§2Date de début : §6" + events.getStartDate(eventName) + "\n"
								+ "§2Durée (en minutes) : §6" + events.getMilliseconds(eventName)/1000/60 + "\n"
								+ "§2Grade requis : §6Aucun \n"
								+ "§8Point de TP : §7" + "Défini"));
						return true;
					} else if (events.isNow(eventName) && events.isTPDefined(eventName) && events.getMinGradeId(eventName) > 0) {
						p.sendMessage(main.hex("§2----------- §6§l" + eventName + " §a: Info  §r§2-----------§r\n"
								+ "                    §2> #49fb00§lE#58fb0d§lV#66fb1a§lE#75fc26§lN#83fc33§lT #92fc40§lE#a1fc4d§lN #affc59§lC#befc66§lO#ccfd73§lU#dbfd80§lR#e9fd8c§lS #f8fd99§l! §2<                    \n"
								+ "§2Description : §6" + events.getDescription(eventName) + "\n"
								+ "§2Date de début : §6" + events.getStartDate(eventName) + "\n"
								+ "§2Durée (en minutes) : §6" + events.getMilliseconds(eventName)/1000/60 + "\n"
								+ "§2Grade requis : §6" + grades.getGradeById(events.getMinGradeId(eventName)).getPrefix() + "\n"
								+ "§2Point de TP : §6" + "Défini"));
						return true;
					} else if (events.isNow(eventName) && !events.isTPDefined(eventName) && events.getMinGradeId(eventName) > 0) {
						p.sendMessage(main.hex("§2----------- §6§l" + eventName + " §a: Info  §r§2-----------§r\n"
								+ "                    §2> #49fb00§lE#58fb0d§lV#66fb1a§lE#75fc26§lN#83fc33§lT #92fc40§lE#a1fc4d§lN #affc59§lC#befc66§lO#ccfd73§lU#dbfd80§lR#e9fd8c§lS #f8fd99§l! §2<                    \n"
								+ "§2Description : §6" + events.getDescription(eventName) + "\n"
								+ "§2Date de début : §6" + events.getStartDate(eventName) + "\n"
								+ "§2Durée (en minutes) : §6" + events.getMilliseconds(eventName)/1000/60 + "\n"
								+ "§2Grade requis : §6" + grades.getGradeById(events.getMinGradeId(eventName)).getPrefix() + "\n"
								+ "§8Point de TP : §7Non Défini"));
						return true;
					}
					//Has ended
					else if(events.hasEnded(eventName) && !events.isTPDefined(eventName) && (events.getMinGradeId(eventName) == 0)) {
						p.sendMessage(main.hex("§2----------- §6§l" + eventName + " §a: Info  §r§2-----------§r\n"
								+ "                    §6> #fbad00§lE#fb9d00§lV#fb8e00§lE#fc7e00§lN#fc6e00§lT #fc5e00§lT#fc4f00§lE#fc3f00§lR#fc2f00§lM#fd1f00§lI#fd1000§lN#fd0000§lÉ §6<                    \n"
								+ "§8Description : §7" + events.getDescription(eventName) + "\n"
								+ "§8Date de début : §7" + events.getStartDate(eventName) + "\n"
								+ "§8Durée (en minutes) : §7" + events.getMilliseconds(eventName)/1000/60 + "\n"
								+ "§8Grade requis : §7Aucun \n"
								+ "§8Point de TP : §7Non Défini"));
					return true;
					} else if (events.hasEnded(eventName) && events.isTPDefined(eventName) && events.getMinGradeId(eventName) == 0) {
						p.sendMessage(main.hex("§2----------- §6§l" + eventName + " §a: Info  §r§2-----------§r\n"
								+ "                    §6> #fbad00§lE#fb9d00§lV#fb8e00§lE#fc7e00§lN#fc6e00§lT #fc5e00§lT#fc4f00§lE#fc3f00§lR#fc2f00§lM#fd1f00§lI#fd1000§lN#fd0000§lÉ §6<                    \n"
								+ "§8Description : §7" + events.getDescription(eventName) + "\n"
								+ "§8Date de début : §7" + events.getStartDate(eventName) + "\n"
								+ "§8Durée (en minutes) : §7" + events.getMilliseconds(eventName)/1000/60 + "\n"
								+ "§8Grade requis : §7Aucun \n"
								+ "§8Point de TP : §7" + "Défini"));
						return true;
					} else if (events.hasEnded(eventName) && events.isTPDefined(eventName) && events.getMinGradeId(eventName) < 0) {
						p.sendMessage(main.hex("§2----------- §6§l" + eventName + " §a: Info  §r§2-----------§r\n"
								+ "                    §6> #fbad00§lE#fb9d00§lV#fb8e00§lE#fc7e00§lN#fc6e00§lT #fc5e00§lT#fc4f00§lE#fc3f00§lR#fc2f00§lM#fd1f00§lI#fd1000§lN#fd0000§lÉ §6<                    \n"
								+ "§8Description : §7" + events.getDescription(eventName) + "\n"
								+ "§8Date de début : §7" + events.getStartDate(eventName) + "\n"
								+ "§8Durée (en minutes) : §7" + events.getMilliseconds(eventName)/1000/60 + "\n"
								+ "§8Grade requis : §7" + grades.getGradeById(events.getMinGradeId(eventName)).getPrefix() + "\n"
								+ "§8Point de TP : §7" + "Défini"));
						return true;
					} else if (events.hasEnded(eventName) && !events.isTPDefined(eventName) && events.getMinGradeId(eventName) < 0) {
						p.sendMessage(main.hex("§2----------- §6§l" + eventName + " §a: Info  §r§2-----------§r\n"
								+ "                    §6> #fbad00§lE#fb9d00§lV#fb8e00§lE#fc7e00§lN#fc6e00§lT #fc5e00§lT#fc4f00§lE#fc3f00§lR#fc2f00§lM#fd1f00§lI#fd1000§lN#fd0000§lÉ §6<                    \n"
								+ "§8Description : §7" + events.getDescription(eventName) + "\n"
								+ "§8Date de début : §7" + events.getStartDate(eventName) + "\n"
								+ "§8Durée (en minutes) : §7" + events.getMilliseconds(eventName)/1000/60 + "\n"
								+ "§8Grade requis : §7" + grades.getGradeById(events.getMinGradeId(eventName)).getPrefix() + "\n"
								+ "§8Point de TP : §7Non Défini"));
						return true;
					}
					//Everything else (except for last else)
					else if (!events.hasEnded(eventName) && !events.isCanceled(eventName) && !events.isNow(eventName) && events.isTPDefined(eventName) && events.getMinGradeId(eventName) > 0) {
						p.sendMessage(main.hex("§2----------- §6§l" + eventName + " §a: Info  §r§2-----------§r\n"
								+ "§2Description : §6" + events.getDescription(eventName) + "\n"
								+ "§2Date de début : §6" + events.getStartDate(eventName) + "\n"
								+ "§2Durée (en minutes) : §6" + events.getMilliseconds(eventName)/1000/60 + "\n"
								+ "§2Grade requis : §6" + grades.getGradeById(events.getMinGradeId(eventName)).getPrefix() + "\n"
								+ "§2Point de TP : §6" + "Défini"));
						return true;
					} else if (!events.hasEnded(eventName) && !events.isCanceled(eventName) && !events.isNow(eventName) && !events.isTPDefined(eventName) && events.getMinGradeId(eventName) > 0) {
						p.sendMessage(main.hex("§2----------- §6§l" + eventName + " §a: Info  §r§2-----------§r\n"
								+ "§2Description : §6" + events.getDescription(eventName) + "\n"
								+ "§2Date de début : §6" + events.getStartDate(eventName) + "\n"
								+ "§2Durée (en minutes) : §6" + events.getMilliseconds(eventName)/1000/60 + "\n"
								+ "§2Grade requis : §6" + grades.getGradeById(events.getMinGradeId(eventName)).getPrefix() + "\n"
								+ "§4Point de TP : §cNon Défini"));
						return true;
					} else if (!events.hasEnded(eventName) && !events.isCanceled(eventName) && !events.isNow(eventName) && events.isTPDefined(eventName) && events.getMinGradeId(eventName) == 0) {
						p.sendMessage(main.hex("§2----------- §6§l" + eventName + " §a: Info  §r§2-----------§r\n"
								+ "§2Description : §6" + events.getDescription(eventName) + "\n"
								+ "§2Date de début : §6" + events.getStartDate(eventName) + "\n"
								+ "§2Durée (en minutes) : §6" + events.getMilliseconds(eventName)/1000/60 + "\n"
								+ "§2Grade requis : §6Aucun\n"
								+ "§4Point de TP : §6" + "Défini"));
						return true;
					}
						//Else
					else {
						p.sendMessage("§2----------- §6§l" + eventName + " §a: Info  §r§2-----------§r\n"
								+ "§2Description : §6" + events.getDescription(eventName) + "\n"
								+ "§2Date de début : §6" + events.getStartDate(eventName) + "\n"
								+ "§2Durée (en minutes) : §6" + events.getMilliseconds(eventName)/1000/60 + "\n"
								+ "§2Grade requis : §6Aucun \n"
								+ "§4Point de TP : §cNon Défini \n");
						return true;
					}

				} else {
					p.sendMessage(main.getPrefix() + "§4Impossible de trouver un événement avec le nom §c" + eventName + "§4 !");
					return true;
				}


			} else if (args[0].equalsIgnoreCase("help")) {
				if(main.admin.contains(p)) {
					p.sendMessage("§2----------- §6§l/event : Aide  §r§2-----------§r\n"
							+ "§7- §2/event admin create <startDate-fromat:[dd/MM/yyyy-HH:mm]/now> <eventDuration> <eventName> <eventDescription>\n"
							+ "§7- §2/event admin modify <startDate/duration/title/description/grade/warp>\n"
							+ "§7- §2/event admin cancel <eventName>\n"
							+ "§7- §2/event admin delete <eventName>\n"
							+ "§7- §2/event admin\n"
							+ "§7- §2/event list\n"
							+ "§7- §2/event info <eventName>\n"
							+ "§7- §2/event help [create/modify/list/help/admin]\n"
							+ "§7- §2/event\n\n"
							+ "§cSintax : /events <args/help>");
					return true;
				} else {
					p.sendMessage("§2----------- §6§l/event : Aide  §r§2-----------§r\n"
							+ "§7- §2/event list\n"
							+ "§7- §2/event info\n"
							+ "§7- §2/event help [list/help]\n"
							+ "§7- §2/event\n\n"
							+ "§cSintax : /events <args/help>");
					return true;
				}
			} else {
				if(main.admin.contains(p)) {
					p.sendMessage("§2----------- §6§l/event : Aide  §r§2-----------§r\n"
							+ "§7- §2/event admin create <startDate-fromat:[dd/MM/yyyy-HH:mm]/now> <eventDuration> <eventName> <eventDescription>\n"
							+ "§7- §2/event admin modify <startDate/duration/title/description/grade/warp>\n"
							+ "§7- §2/event admin cancel <eventName>\n"
							+ "§7- §2/event admin delete <eventName>\n"
							+ "§7- §2/event admin\n"
							+ "§7- §2/event list\n"
							+ "§7- §2/event info\n"
							+ "§7- §2/event help [create/modify/list/help/admin]\n"
							+ "§7- §2/event\n\n"
							+ "§cSintax : /events <args/help>");
					return true;
				} else {
					p.sendMessage("§2----------- §6§l/event : Aide  §r§2-----------§r\n"
							+ "§7- §2/event list\n"
							+ "§7- §2/event info\n"
							+ "§7- §2/event help [list/help]\n"
							+ "§7- §2/event\n\n"
							+ "§cSintax : /events <args/help>");
					return true;
				}
			}

		}
	}
	//TODO fix all bugs in /events
	//init all args variables
	List<String> arguments = new ArrayList<>();
	List<String> adminarguments = new ArrayList<>();
	List<String> arguments1 = new ArrayList<>();
	List<String> adminarguments1 = new ArrayList<>();
	List<String> adminmodifyarguments = new ArrayList<>();
	List<String> warpargs = new ArrayList<>();

	//onTabComplete
	@Override
	public List<String> onTabComplete(CommandSender s, Command c, String l, String[] args) {
		//add arguments to variables
		if(arguments.isEmpty()) {
			arguments.add("info");
			arguments.add("help");
			arguments.add("list");
		}

		if(adminarguments.isEmpty()) {
			adminarguments.add("admin");
		}

		if(arguments1.isEmpty()) {

			try {
				String eventList = events.getEventList().replace(" ", "");
				arguments1 = Arrays.asList(eventList.split(","));

			} catch (Exception e) {
				System.err.println(main.errorMsg);
			}

		}

		if(adminarguments1.isEmpty()) {
			adminarguments1.add("create");
			adminarguments1.add("modify");
			adminarguments1.add("cancel");
			adminarguments1.add("delete");
			adminarguments1.add("help");
		}

		if(adminmodifyarguments.isEmpty()) {
			adminmodifyarguments.add("startDate");
			adminmodifyarguments.add("duration");
			adminmodifyarguments.add("name");
			adminmodifyarguments.add("description");
			adminmodifyarguments.add("grade");
			adminmodifyarguments.add("warp");
		}

		if(warpargs.isEmpty()) {
			String warps = warp.getConfig().get("warp.list").toString();
			if(!warps.isEmpty()) {
				warpargs = Arrays.asList(warps.split(","));
			}
		}

		//putting variables in TabComplete
		List<String> result = new ArrayList<>();
		//Args lenght = 1
		if(args.length == 1) {
			for (String a : arguments) {
				if(a.toLowerCase().startsWith(args[0].toLowerCase())) {
					result.add(a);
				}
			}
			Player p = (Player) s;
			if(main.admin.contains(p)) {
				for (String a : adminarguments) {
					if(a.toLowerCase().startsWith(args[0].toLowerCase())) {
						result.add(a);
					}
				}
			}
			return result;
		}

		//Args lenght = 2
		if(args.length == 2 && args[0].equalsIgnoreCase("info")) {

			for (String a : arguments1) {
				if(a.toLowerCase().startsWith(args[1].toLowerCase())) {
					result.add(a);
				}
			}
			return result;
		} else if(args.length == 2 && args[0].equalsIgnoreCase("admin")) {
			Player p = (Player) s;
			if(main.admin.contains(p)) {
				for (String a : adminarguments1) {
					if(a.toLowerCase().startsWith(args[1].toLowerCase())) {
						result.add(a);
					}
				}
			}
			return result;
		}

		//Args length = 3
		if(args.length == 3) {
			Player p = (Player) s;
			if(main.admin.contains(p)) {

				if(args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("cancel")) {

					for (String a : arguments1) {
						if(a.toLowerCase().startsWith(args[2].toLowerCase())) {
							result.add(a);
						}
					}

				} else if(args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("delete")) {

					for (String a : arguments1) {
						if(a.toLowerCase().startsWith(args[2].toLowerCase())) {
							result.add(a);
						}
					}
				} else if (args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("modify")) {
					for (String a : adminmodifyarguments) {
						if(a.toLowerCase().startsWith(args[2].toLowerCase())) {
							result.add(a);
						}
					}
				 }
			}

			return result;

		} else if(args.length >= 4 && args.length <= 5) {
			Player p = (Player) s;
			if(main.admin.contains(p)) {
				 if (args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("modify")) {
					if(args.length == 4) {
						for (String a : arguments1) {
							if(a.toLowerCase().startsWith(args[3].toLowerCase())) {
								result.add(a);
							}
						}
					}


					if(args[2].equalsIgnoreCase("warp") && args.length == 5) {
						for (String a : warpargs) {
							if(a.toLowerCase().startsWith(args[4].toLowerCase())) {
								result.add(a);
							}
						}
					} else if (args[2].equalsIgnoreCase("grade") && args.length == 5 || args[2].equalsIgnoreCase("rank") && args.length == 5) {
						for(GradeList gradeList : GradeList.values()) {
							if(gradeList.getName().toLowerCase().startsWith(args[4].toLowerCase())) {
								result.add(gradeList.getName().toLowerCase());
							}
						}
					}
				}
			}
			return result;
		}
		return null;
	}

}
