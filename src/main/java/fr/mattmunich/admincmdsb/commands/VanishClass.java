package fr.mattmunich.admincmdsb.commands;


import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import fr.mattmunich.admincmdsb.Main;
import fr.mattmunich.admincmdsb.commandhelper.Grades;
import fr.mattmunich.admincmdsb.commandhelper.Settings;

public class VanishClass implements CommandExecutor, Listener {

	//public ArrayList<Player> vanished = new ArrayList<>();


	public VanishClass(Main main, Grades grades, Settings settings) {
		this.grades =  grades;
		this.main = main;
		this.settings = settings;
	}
	// /vanish <on/off/player>

	private Main main;

	private final Grades grades;

	private final Settings settings;


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof BlockCommandSender) {
			sender.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		if(sender instanceof Player player) {

            if(!(main.superstaff.contains(player))) {
				player.sendMessage(main.noPermissionMsg);
				return true;
			}

			if(args.length == 0) {

				if(!main.vanished.contains(player)) {
					main.vanished.add(player);

					for(Player players : Bukkit.getOnlinePlayers()) {
						if(settings.getSeeVanished()) {
							if(!(main.superstaff.contains(players) || !main.vanished.contains(players))) {
								players.hidePlayer(JavaPlugin.getPlugin(main.getClass()), player);
								}
							if(main.superstaff.contains(players) || main.vanished.contains(players)) {
								players.showPlayer(JavaPlugin.getPlugin(main.getClass()), player);
							}
						}else {
							if(!main.vanished.contains(players)) {
								players.hidePlayer(JavaPlugin.getPlugin(main.getClass()), player);
							}
							if(main.vanished.contains(players)) {
								players.showPlayer(JavaPlugin.getPlugin(main.getClass()), player);
							}
						}

					}

					String tPrefix = main.hex(grades.getPlayerGrade(player).getPrefix());
					String tSuffix = main.hex(grades.getPlayerGrade(player).getSuffix());

					player.setPlayerListName(tPrefix + player.getName() + tSuffix);
					player.setDisplayName(tPrefix + player.getName() + tSuffix);


					player.sendMessage("§e(§6!§e) §2Vous êtes maintenant §6vanish");
					player.setCanPickupItems(false);
					player.setInvulnerable(true);
					main.nochat.add(player);
					main.god.add(player);
					player.setPlayerListName("§4§o[Vanish] §6" + player.getName());
					if(settings.getCoMsg()) {
						Bukkit.broadcastMessage(player.getDisplayName() + "§e s'est déconnecté");
					}

					player.setDisplayName("§4§o[Vanish] §6" + player.getName());

					return true;
				}

				if(main.vanished.contains(player)) {
					main.vanished.remove(player);

					for(Player players : Bukkit.getOnlinePlayers()) {
						players.showPlayer(JavaPlugin.getPlugin(main.getClass()), player);
					}

					player.sendMessage("§e(§6!§e) §4Vous n'êtes plus §6vanish");

					String tPrefix = main.hex(grades.getPlayerGrade(player).getPrefix());
					String tSuffix = main.hex(grades.getPlayerGrade(player).getSuffix());

					player.setPlayerListName(tPrefix + player.getName() + tSuffix);
					player.setDisplayName(tPrefix + player.getName() + tSuffix);

					player.setCanPickupItems(true);
					player.setInvulnerable(false);
					main.nochat.remove(player);
					main.god.remove(player);
					if(settings.getCoMsg()) {
						Bukkit.broadcastMessage(("§e" + player.getDisplayName() + "§e s'est connecté"));
					}

					player.setDisplayName(player.getPlayerListName());
					return true;
				}

				return true;

			}else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("on")) {
					if(main.vanished.contains(player)) {
						player.sendMessage("§e(§6!§e) §4Vous êtes déjà §6vanish");

						return true;
					}

					main.vanished.add(player);
					for (Player players : Bukkit.getOnlinePlayers()){
						if(settings.getSeeVanished()) {
							if(!(main.superstaff.contains(players) || !main.vanished.contains(players))) {
								players.hidePlayer(JavaPlugin.getPlugin(main.getClass()), player);
							}
							if(main.superstaff.contains(players) || main.vanished.contains(players)) {
								players.showPlayer(JavaPlugin.getPlugin(main.getClass()), player);
							}
						}else {
							if(!main.vanished.contains(players)) {
								players.hidePlayer(JavaPlugin.getPlugin(main.getClass()), player);
							}
							if(main.vanished.contains(players)) {
								players.showPlayer(JavaPlugin.getPlugin(main.getClass()), player);
							}
						}
					}

					String tPrefix = main.hex(grades.getPlayerGrade(player).getPrefix());
					String tSuffix = main.hex(grades.getPlayerGrade(player).getSuffix());

					player.setPlayerListName(tPrefix + player.getName() + tSuffix);
					player.setDisplayName(tPrefix + player.getName() + tSuffix);

					player.sendMessage("§e(§6!§e) §2Vous êtes maintenant §6vanish");
					player.setPlayerListName("§4§o[Vanish] §6" + player.getName());
					player.setInvulnerable(true);
					player.setCanPickupItems(false);
					main.nochat.add(player);
					main.god.add(player);
					if(settings.getCoMsg()) {
						Bukkit.broadcastMessage(player.getDisplayName() + "§e s'est déconnecté");
					}
					player.setDisplayName("§4§o[Vanish] §6" + player.getName());


					return true;
				}else if(args[0].equalsIgnoreCase("off")) {
					if(!main.vanished.contains(player)) {
						player.sendMessage(main.getPrefix() + "Vous n'êtes pas §6vanish");

						return true;
					}

					main.vanished.remove(player);

					for(Player players : Bukkit.getOnlinePlayers()) {
						players.showPlayer(JavaPlugin.getPlugin(main.getClass()), player);
					}

					player.sendMessage(main.getPrefix() + "Vous n'êtes maintenant plus §6vanish");

					String tPrefix = main.hex(grades.getPlayerGrade(player).getPrefix());
					String tSuffix = main.hex(grades.getPlayerGrade(player).getSuffix());

					player.setPlayerListName(tPrefix + player.getName() + tSuffix);
					player.setDisplayName(tPrefix + player.getName() + tSuffix);

//					if(main.admin.contains(player)) {
//						player.setPlayerListName("§c[§4Administrateur§c] §6" + player.getName());
//					}
//					if(main.mod.contains(player)) {
//						player.setPlayerListName("§a[§2Modérateur§a] §6" + player.getName());
//					}

					player.setCanPickupItems(true);
					player.setInvulnerable(false);
					main.nochat.remove(player);
					main.god.remove(player);
					if(settings.getCoMsg()) {
						Bukkit.broadcastMessage(("§e" + player.getDisplayName() + "§e s'est connecté"));
					}
					player.setDisplayName(player.getPlayerListName());

					return true;
				}

				String targetName = args[0];

				if(Bukkit.getPlayer(targetName) == null) {
					player.sendMessage(main.getPrefix() + "§4Le joueur est hors-ligne ou n'éxiste pas !");
					return true;
				}

				Player target = Bukkit.getPlayer(targetName);


				if(!main.vanished.contains(target)) {

					main.vanished.add(target);

					for(Player players : Bukkit.getOnlinePlayers()) {
						if(settings.getSeeVanished()) {
							if(!(main.superstaff.contains(players) || !main.vanished.contains(players))) {
								players.hidePlayer(JavaPlugin.getPlugin(main.getClass()), target);
							}
							if(main.superstaff.contains(players) || main.vanished.contains(players)) {
								players.showPlayer(JavaPlugin.getPlugin(main.getClass()), target);
							}
						}else {
							if(!main.vanished.contains(players)) {
								players.hidePlayer(JavaPlugin.getPlugin(main.getClass()), target);
							}
							if(main.vanished.contains(players)) {
								players.showPlayer(JavaPlugin.getPlugin(main.getClass()), target);
							}
						}
					}

					player.sendMessage("§e(§6!§e) §2Vous avez §6vanish§2 le joueur §6" + args[0]);
					target.setCanPickupItems(false);
					target.setPlayerListName("§4§o[Vanish] §6" + target.getName());


					target.sendMessage("§e(§6!§e) §2Vous avez été §6vanish §2par §6" + player.getName());
					target.setInvulnerable(true);

					main.nochat.add(target);
					main.god.add(target);
					if(settings.getCoMsg()) {
						Bukkit.broadcastMessage(target.getDisplayName() + "§e s'est déconnecté");
					}
					target.setDisplayName("§4§o[Vanish] §6" + target.getName());

//						if(main.admin.contains(target)) {
//							Bukkit.broadcastMessage(("§c[§4Administrateur§c] §6" + target.getName() + "§e s'est déconnecté"));
//						}
//						if(main.mod.contains(target)) {
//							Bukkit.broadcastMessage(("§a[§2Modérateur§a] §6" + target.getName() + "§e s'est déconnecté"));
//						}
//						if(main.chevalier.contains(target)) {
//							Bukkit.broadcastMessage(("§b[§1§lChevalier§b] §3" + target.getName() + "§e s'est déconnecté"));
//						}
//						if(main.joueur.contains(target)) {
//							Bukkit.broadcastMessage(("§3[§bJoueur§3] §1" + target.getName() + "§e s'est déconnecté"));
//						}
//						if(main.youtuber.contains(target)) {
//							Bukkit.broadcastMessage(("§c[§4You§fTuber§c] §6" + target.getName() + "§e s'est déconnecté"));
//						}

					return true;
				}

				if(main.vanished.contains(target)) {
					main.vanished.remove(target);

					for(Player players : Bukkit.getOnlinePlayers()) {
						players.showPlayer(JavaPlugin.getPlugin(main.getClass()), target);
					}

					player.sendMessage("§e(§6!§e) §6" + args[0] + "§2 a été §6dévanish");
					target.sendMessage("§e(§6!§e) §4Vous avez été §6dévanish§4 par §6" + player.getName());

					String tPrefix = main.hex(grades.getPlayerGrade(target).getPrefix());
					String tSuffix = main.hex(grades.getPlayerGrade(target).getSuffix());

					target.setPlayerListName(tPrefix + target.getName() + tSuffix);
					target.setDisplayName(tPrefix + target.getName() + tSuffix);

//					if(main.admin.contains(target)) {
//						target.setPlayerListName("§c[§4Administrateur§c] §6" + target.getName());
//						target.setDisplayName("§c[§4Administrateur§c] §6" + target.getName());
//					}
//					if(main.mod.contains(target)) {
//						target.setPlayerListName("§a[§2Modérateur§a] §6" + target.getName());
//						target.setDisplayName("§a[§2Modérateur§a] §6" + target.getName());
//					}
//					if(main.chevalier.contains(target)) {
//						target.setPlayerListName("§b[§1§lChevalier§b] §3" + target.getName());
//						target.setDisplayName("§b[§1§lChevalier§b] §3" + target.getName());
//					}
//					if(main.joueur.contains(target)) {
//						target.setPlayerListName("§3[§bJoueur§3] §1" + target.getName());
//						target.setDisplayName("§3[§bJoueur§3] §1" + target.getName());
//					}
//					if(main.youtuber.contains(target)) {
//						target.setPlayerListName("§c[§4You§fTuber§c] §6" + target.getName());
//						target.setDisplayName("§c[§4You§fTuber§c] §6" + target.getName());
//					}
					target.setCanPickupItems(true);
					target.setInvulnerable(false);
					main.nochat.remove(target);
					main.god.remove(target);
					if(settings.getCoMsg()) {
						Bukkit.broadcastMessage((target.getDisplayName() + "§e s'est connecté"));
					}
					return true;
				}

			}else {
				player.sendMessage("§cSintax : /vanish <on/off/player>");

				return true;
			}

		}else {
			sender.sendMessage(main.getPrefix() + "§4Vous devez etre un joueur pour utiliser cette commande !");
			return true;
		}
		return true;
	}

	@SuppressWarnings({"static-access" })
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if(settings.getSeeVanished()) {
		if(!(main.superstaff.contains(player) || !main.vanished.contains(player))) {
				player.hidePlayer(JavaPlugin.getPlugin(main.getClass()), player);
			}
			if(main.superstaff.contains(player) || main.vanished.contains(player)) {
				player.showPlayer(JavaPlugin.getPlugin(main.getClass()), player);
			}
		}else {
			if(!main.vanished.contains(player)) {
				player.hidePlayer(JavaPlugin.getPlugin(main.getClass()), player);
			}
			if(main.vanished.contains(player)) {
				player.showPlayer(JavaPlugin.getPlugin(main.getClass()), player);
			}
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		main.vanished.remove(e.getPlayer());
	}


}
