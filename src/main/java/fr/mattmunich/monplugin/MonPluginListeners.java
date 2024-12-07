package fr.mattmunich.monplugin;

import java.util.*;

import fr.mattmunich.monplugin.commandhelper.*;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.sign.Side;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.mattmunich.monplugin.commands.InvSeeCommand;

@SuppressWarnings({ "unused", "deprecation" })
public class MonPluginListeners implements Listener {

	private final MonPlugin main;

	private final Grades grades;

	private final Settings settings;

	private final InvSeeCommand invsee;

	public MonPluginListeners(Grades grades, MonPlugin main, Settings settings, InvSeeCommand invsee) {
		this.grades = grades;
		this.main = main;
		this.settings = settings;
		this.invsee = invsee;
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (main.secondInv.contains(player)) {
			PlayerManager pm = PlayerManager.getFromPlayer(player);
			player.getInventory().clear();
			main.secondInv.remove(player);
			pm.giveInvetnory();
			pm.destroy();
			player.sendMessage(main.getPrefix() + "§2Vous n'avez maintenant plus votre inventaire secondaire !");
		}

		for (ArmorStand allst : player.getWorld().getEntitiesByClass(ArmorStand.class)) {
			if (allst.getScoreboardTags().contains(player.getName())) {
				allst.setInvulnerable(false);
				allst.setHealth(0);
				allst.damage(1000);
			}
		}

		GradeList gradeList = grades.getPlayerGrade(player);

		if (main.vanished.contains(player) || main.supervanished.contains(player)) {
			event.setQuitMessage("");

			player.sendMessage("§e(§6!§e) §4Vous n'êtes plus §6vanish");

			String tPrefix = main.hex(grades.getPlayerGrade(player).getPrefix());
			String tSuffix = main.hex(grades.getPlayerGrade(player).getSuffix());

			player.setPlayerListName(tPrefix + player.getName() + tSuffix);
			player.setDisplayName(tPrefix + player.getName() + tSuffix);

			player.setCanPickupItems(true);
			player.setInvulnerable(false);
			main.nochat.remove(player);
			main.god.remove(player);
		}

		PlayerData data = new PlayerData(player.getUniqueId());

		int x = player.getLocation().getBlockX();
		int y = player.getLocation().getBlockY();
		int z = player.getLocation().getBlockZ();
		String world = Objects.requireNonNull(player.getLocation().getWorld()).getName();

		data.getConfig().set("lastPos.x", x);
		data.getConfig().set("lastPos.y", y);
		data.getConfig().set("lastPos.z", z);
		data.getConfig().set("lastPos.world", world);
		data.saveConfig();

		grades.deletePlayer(player);
		if (!main.banni.contains(player)) {

			if (settings.getCoMsg() && !(main.vanished.contains(player) || main.supervanished.contains(player))) {
				event.setQuitMessage(main.hex(player.getDisplayName()) + "§e s'est déconnecté");
			} else {
				event.setQuitMessage("");
			}

		} else {
			event.setQuitMessage("");
		}
		// NPC Manager
//		PacketReader reader = new PacketReader();
//		reader.uninject(player);
		main.vanished.remove(player);
		main.supervanished.remove(player);
	}

	HashMap<Block, Integer> warnedCmdBlock = new HashMap<>();
	//   cmdBlock, timesRan -- (break at ??)

	@EventHandler
	public void serverCommand(ServerCommandEvent e) {
		CommandSender s = e.getSender();
		String cmd = e.getCommand();
		if(s instanceof BlockCommandSender) {
			Block sBlock = ((BlockCommandSender) s).getBlock();
			if(cmd.contains("@e") && !cmd.contains("[")) {
				e.setCancelled(true);
				s.sendMessage("§4Cannot run command - blocked");
			}
			if(cmd.contains("@e") && cmd.contains("type=armor_stand")) {
				e.setCancelled(true);
				s.sendMessage("§4Cannot run command - blocked");
			}
			if(cmd.contains("@a") && !cmd.contains("@a[") && !cmd.contains("execute")) {
				if(warnedCmdBlock.containsKey(sBlock)) {
					warnedCmdBlock.replace(sBlock, warnedCmdBlock.get(sBlock), warnedCmdBlock.get(sBlock) + 1);
				} else {
					warnedCmdBlock.put(sBlock, 1);
				}
			}
			if(cmd.contains("@p") && !cmd.contains("@p[") && !cmd.contains("execute")) {
				if(warnedCmdBlock.containsKey(sBlock)) {
					warnedCmdBlock.replace(sBlock, warnedCmdBlock.get(sBlock), warnedCmdBlock.get(sBlock) + 1);
				} else {
					warnedCmdBlock.put(sBlock, 1);
				}
			}
			if(cmd.contains("@e") && !cmd.contains("@e[") && !cmd.contains("execute")) {
				if(warnedCmdBlock.containsKey(sBlock)) {
					warnedCmdBlock.replace(sBlock, warnedCmdBlock.get(sBlock), warnedCmdBlock.get(sBlock) + 1);
				} else {
					warnedCmdBlock.put(sBlock, 1);
				}
			}
			if(cmd.contains("@r") && !cmd.contains("@r[") && !cmd.contains("execute")) {
				if(warnedCmdBlock.containsKey(sBlock)) {
					warnedCmdBlock.replace(sBlock, warnedCmdBlock.get(sBlock), warnedCmdBlock.get(sBlock) + 1);
				} else {
					warnedCmdBlock.put(sBlock, 1);
				}
			}

			if(warnedCmdBlock.containsKey(sBlock) && warnedCmdBlock.get(sBlock) >= 15) {
				s.sendMessage("§4Cannot run command - blocked");
				e.setCancelled(true);
				sBlock.setType(Material.AIR);
				Bukkit.getConsoleSender().sendMessage(main.getPrefix() + "§aDestroyed Command Block §2§o(" + sBlock.getX()  + " " + sBlock.getY() + " " + sBlock.getZ() + ")§a containing command : §c\"" + cmd + "\"§a !");
				warnedCmdBlock.remove(sBlock);
			}

			if(warnedCmdBlock.containsKey(sBlock)){
				Bukkit.getScheduler().runTaskLaterAsynchronously(main, () -> warnedCmdBlock.remove(sBlock),20);
			}
		}
	}
	@EventHandler
	public void playerCommand(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();

		String cmd = e.getMessage();

		if (cmd.contains("@e") && !cmd.contains("[")) {
			p.sendMessage(main.getErrorPrefix()
					+ "§4Impossible de sélectionner §c§l\"@e\"§r§4 sans §c§l\"[]\"§r§4 ou autre précision pour le selecteur.\n"
					+ "§2Info: §6Ceci est une protection pour empêcher tout problème sur le serveur.");
			//p.kickPlayer("§6Le serveur a été protégé. \n §4§lMerci de ne pas utiliser le selecteur \"@e\" !!");
			Bukkit.getConsoleSender().sendMessage("§e[§6ServerSecurity§e] §c" + p.getName()
					+ " §4 tried to use §c\"@e\"§4 or §c\"@e[type=armor_stand]\"§6, command cancelled and player got informed.");
			e.setCancelled(true);
			return;
		}

		if (cmd.contains("/kill")) {
			for (Player checkP : Bukkit.getOnlinePlayers()) {
				if (cmd.toLowerCase().contains(checkP.getName().toLowerCase()) && !main.admin.contains(p)
						&& p.getName().equals(checkP.getName())) {
					p.sendMessage(main.getErrorPrefix()
							+ "§4Impossible d'utiliser §c§l\"/kill\"§r§4 sur un autre joueur que vous-même.\n"
							+ "§2Info: §6Ceci est une protection pour empêcher toute embrouille sur le serveur.");
					Bukkit.getConsoleSender().sendMessage("§e[§6ServerSecurity§e] §c" + p.getName()
							+ " §4 tried to §c\"/kill\"§4 §c\"" + checkP.getName() + "\"§6, command cancelled and got informed.");
					e.setCancelled(true);
					return;
				}
			}

		}

		if (cmd.contains("@e[type=armor_stand]") || cmd.contains("@e[type=minecraft:armor_stand]")) {
			p.sendMessage(main.getErrorPrefix()
					+ "§4Impossible de sélectionner §c§l\"@e[type=minecraft:armor_stand]\"§r§4 sans autre précision pour le selecteur.\n"
					+ "§2Info: §6Ceci est une protection pour empêcher la destruction du serveur.");
			p.kickPlayer("§6Le serveur a été protégé. \n §4§lMerci de ne pas utiliser le selecteur \"@e\" !!");
			Bukkit.getConsoleSender().sendMessage("§e[§6ServerSecurity§e] §c" + p.getName()
					+ " §4 tried to use §c\"@e\"§4 or §c\"@e[type=armor_stand]\"§6, player got kicked.");
			e.setCancelled(true);
			return;
		}

		if (cmd.startsWith("execute") || cmd.startsWith("/execute")) {
			if(!main.superstaff.contains(p)) {
				p.sendMessage(main.noPermissionMsg);
				e.setCancelled(true);
				if (cmd.contains("kick") || cmd.contains("ban")) {
					PlayerData pdata = new PlayerData(p.getUniqueId());
					pdata.setTempbanned("Système", "§6Vous ne pouvez pas executer cette commande ! \\n§cUn administateur vous a bloqué l'accès à cette commande.", 30*1000, "30s");
					p.kickPlayer("§6Vous ne pouvez pas executer cette commande ! \n§cUn administateur vous a bloqué l'accès à cette commande.");
				}
				return;
			}


		}
//		if ((cmd.startsWith("execute") && p.getName().equalsIgnoreCase("creeperanimation") && cmd.contains("ban")) || (cmd.startsWith("execute") && p.getName().equalsIgnoreCase("creeperanimation") && cmd.contains("kick")) || (cmd.startsWith("execute") && p.getName().equalsIgnoreCase("creeperanimation") && cmd.contains("grade"))) {
//			p.sendMessage(main.noPermissionMsg + "\n §6§oPS : Tu n'as pas le droit de faire §7§o/execute run ban §6§oou §7§o/execute run kick");
//			return;
//		}



		for (Player allp : Bukkit.getOnlinePlayers()) {
			if (main.directlog.contains(allp)) {
				allp.sendMessage(main.getDirectLogPrefix() + p.getDisplayName() + " §8§l>>§2 " + cmd);
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {

		Player p = e.getEntity();
		PlayerData data = new PlayerData(p.getUniqueId());

		int x = p.getLocation().getBlockX();
		int y = p.getLocation().getBlockY();
		int z = p.getLocation().getBlockZ();
		String world = Objects.requireNonNull(p.getLocation().getWorld()).getName();

		data.getConfig().set("lastPos.x", x);
		data.getConfig().set("lastPos.y", y);
		data.getConfig().set("lastPos.z", z);
		data.getConfig().set("lastPos.world", world);
		data.saveConfig();

		if (!main.superstaff.contains(p)) {
			p.sendMessage(main.getPrefix() + "§4Vous êtes mort en §c " + x + ", " + y + ", " + z + " §4monde : §c"
					+ world + " §4!");
		}
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent pce) {
		Player player = pce.getPlayer();
		GradeList gradeList = grades.getPlayerGrade(player);
		PlayerData data = new PlayerData(player.getUniqueId());

		if (!data.exist()) {
			player.sendMessage(main.getErrorPrefix()
					+ "Une erreur s'est produite lors de l'envoi du message ! Essayez de vous déconnecter et reconnecter au serveur.");
			pce.setCancelled(true);
			return;
		}

		if(main.chatMuted && !main.admin.contains(player)) {
			pce.setCancelled(true);
			player.sendMessage(main.getErrorPrefix() + "§cLe chat est désactivé !");
			return;
		}

		if (data.getTempmuteMilliseconds() <= System.currentTimeMillis()) {

			data.setUnTempmuted();
			main.mute.remove(player);
		}

		if (data.isTempmuted()) {
			pce.getPlayer()
					.sendMessage(main.getPrefix() + "§4Vous êtes TempMute, vous ne pouvez pas parler dans le chat !");
			pce.getPlayer()
					.sendMessage("§e[§6MutedChat§e] §2--> §r" + player.getDisplayName() + gradeList.getChatSeparator()
							+ ChatColor.translateAlternateColorCodes('&', String.join(" ", pce.getMessage())));
			for (Player all : Bukkit.getOnlinePlayers()) {
				if (main.mutedChat.contains(all)) {
					if (main.guides.contains(all)) {
						all.sendMessage("§e[§6MutedChat§e] §2--> §r" + player.getDisplayName()
								+ gradeList.getChatSeparator()
								+ ChatColor.translateAlternateColorCodes('&', String.join(" ", pce.getMessage())));
					}
					if (main.staff.contains(all)) {
						all.sendMessage("§e[§6MutedChat§e] §2--> §r" + player.getDisplayName()
								+ gradeList.getChatSeparator()
								+ ChatColor.translateAlternateColorCodes('&', String.join(" ", pce.getMessage())));
					}
				}
			}

			Bukkit.getConsoleSender()
					.sendMessage("§e[§6MutedChat§e] §2--> §r" + player.getDisplayName() + gradeList.getChatSeparator()
							+ ChatColor.translateAlternateColorCodes('&', String.join(" ", pce.getMessage())));

			pce.setCancelled(true);
        } else if (data.isMuted()) {
			pce.setCancelled(true);
			player.sendMessage(
					main.getPrefix() + "§4Vous êtes mute, vous ne pouvez pas parler dans le chat principal !");
			player.sendMessage("§e[§6MutedChat§e] §2--> §r" + player.getDisplayName() + gradeList.getChatSeparator()
					+ ChatColor.translateAlternateColorCodes('&', String.join(" ", pce.getMessage())));
			for (Player all : Bukkit.getOnlinePlayers()) {
				if (main.mutedChat.contains(all)) {
					if (main.guides.contains(all)) {
						all.sendMessage("§e[§6MutedChat§e] §2--> §r" + player.getDisplayName()
								+ gradeList.getChatSeparator()
								+ ChatColor.translateAlternateColorCodes('&', String.join(" ", pce.getMessage())));
					}
					if (main.staff.contains(all)) {
						all.sendMessage("§e[§6MutedChat§e] §2--> §r" + player.getDisplayName()
								+ gradeList.getChatSeparator()
								+ ChatColor.translateAlternateColorCodes('&', String.join(" ", pce.getMessage())));
					}
				}
			}

			Bukkit.getConsoleSender()
					.sendMessage("§e[§6MutedChat§e] §2--> §r" + player.getDisplayName() + gradeList.getChatSeparator()
							+ ChatColor.translateAlternateColorCodes('&', String.join(" ", pce.getMessage())));

			pce.setCancelled(true);
        } else if (main.nochat.contains(player)) {

			player.sendMessage(main.getPrefix() + "§4Votre chat est désactivé entrez /chat pour le réactiver !");
			pce.setCancelled(true);
			pce.setCancelled(true);

        } else if (main.schat.contains(player)) {

			for (Player all : Bukkit.getOnlinePlayers()) {
				if (main.staff.contains(all)) {
					all.sendMessage("§e[§2Staff§aChat§e] §2--> §r" + player.getDisplayName()
							+ gradeList.getChatSeparator() + ChatColor.translateAlternateColorCodes('&',
									String.join(" ", main.hex(pce.getMessage()))));
				}
			}
			Bukkit.getConsoleSender().sendMessage("§e[§2Staff§aChat§e] §2--> §r" + player.getDisplayName()
					+ gradeList.getChatSeparator()
					+ ChatColor.translateAlternateColorCodes('&', String.join(" ", main.hex(pce.getMessage()))));

			pce.setCancelled(true);

        } else {
//			String msg = main.hex(player.getDisplayName() + gradeList.getChatSeparator() +  ChatColor.translateAlternateColorCodes('&', String.join(" ", pce.getMessage())));
//
//			for(Player all : Bukkit.getOnlinePlayers()) {
//				all.sendMessage(msg);
//				player.sendMessage(main.getPrefix() + "§2Message envoyé à §6" + all.getName());
//			}

			pce.setFormat(main.hex(player.getDisplayName() + gradeList.getChatSeparator()
					+ ChatColor.translateAlternateColorCodes('&', String.join(" ", pce.getMessage()))));

		}
		// pce.setFormat(main.hex(player.getDisplayName() + gradeList.getChatSeparator()
		// + ChatColor.translateAlternateColorCodes('&', String.join(" ",
		// pce.getMessage()))));
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {

		Block b = e.getBlock();
		Material m = b.getType();

		if (m.equals(Material.COMMAND_BLOCK) || m.equals(Material.REPEATING_COMMAND_BLOCK) || m.equals(Material.CHAIN_COMMAND_BLOCK)) {
			Player p = e.getPlayer();
			if(!(p.getName().equalsIgnoreCase("mattmunich"))) {
				Bukkit.getConsoleSender().sendMessage("§e[§6AdminCmdsB§e] §c§lCommand Block was placed by §4§l" + p.getName() + "§c§l at position §4§l" + b.getX() + " " + b.getY() + " " + b.getZ() + "§c§l !");
			}
			if(p.getName().equalsIgnoreCase("Blackfox00156")) {
				p.sendMessage(main.getErrorPrefix() + "Vous n'avez pas la permission de poser ce block !");
				e.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler
	public void onEntityDamaged(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player damager) {
            if(settings.getOldPVP()) {
				damager.setCooldown(damager.getInventory().getItemInMainHand().getType(), 0);
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {

		Player p = e.getPlayer();
		Block block = e.getBlock();
		BlockState bs = block.getState();

		if (main.nomine.contains(p)) {
			e.setCancelled(true);
		}

		if (bs instanceof Skull) {
			try {
				Skull skull = (Skull) bs;
				if (skull.getOwner() == null) {
					return;
				}
				if (skull.getOwner().equalsIgnoreCase("luck")) {
					e.setCancelled(true);
					block.setType(Material.AIR);

					/*
					 * Using LBReward void (Declared below)
					 *
					 */
					LBReward(p, block);
				}
			} catch (Exception ignored) {
            }

			/*
			 * ItemStack wb = new ItemStack(Material.WATER_BUCKET);
			 * 
			 * if(!Bukkit.getWorlds().contains(p.getName())) {
			 * Bukkit.createWorld(WorldCreator.name(p.getName())); p.teleport(new
			 * Location(Bukkit.getWorld(p.getName()), 0, 70, 0));
			 * p.getInventory().addItem(wb); }
			 * if(Bukkit.getWorlds().contains(p.getName().toString())) { p.teleport(new
			 * Location(Bukkit.getWorld(p.getName()), 0, 70, 0));
			 * p.getInventory().addItem(wb); }
			 * 
			 * ItemStack tnt = new ItemStack(Material.TNT); ItemMeta tM = tnt.getItemMeta();
			 * 
			 * tM.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 1, true);
			 * tM.addItemFlags(ItemFlag.HIDE_ENCHANTS); tM.setDisplayName("§4Place TNT");
			 * tM.setLore(Arrays.asList("", "§5§oExplose quand on la pose"));
			 * 
			 * tnt.setItemMeta(tM);
			 * 
			 * ItemStack tnt = new ItemStack(Material.TNT, 1); ItemMeta meta = (SkullMeta)
			 * tnt.getItemMeta(); meta.setDisplayName("§4Place TNT");
			 * meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, 1, true);
			 * meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			 * meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); tnt.setItemMeta(meta);
			 */
		}
	}

	@EventHandler
	public void onCristal(EntityExplodeEvent e) {
		if (!settings.getTNTsEnabled()) {
			for (Entity nEntity : e.getEntity().getNearbyEntities(10, 10, 10)) {
				if (nEntity instanceof Player) {
					Player p = (Player) nEntity;
					if (main.admin.contains(p)) {
						p.sendMessage(main.getPrefix()
								+ "§4Les explosions sont désactivées sur le serveur !\n§cUtilisez §e\"§6/admincmdsb settings TNTsEnabled true§e\"§c pour les réactiver !");
					} else {
						p.sendMessage(main.getPrefix() + "§4Les explosions sont désactivées sur le serveur !");
					}
					Bukkit.getConsoleSender()
							.sendMessage(main.getPrefix() + "§c" + p.getName()
									+ "§4 a essayé de faire exploser une entité aux coordonnées : §cX: "
									+ e.getEntity().getLocation().getBlockX() + "§4, §cY: "
									+ e.getEntity().getLocation().getBlockY() + "§4, §cZ: "
									+ e.getEntity().getLocation().getBlockZ());
				}
			}
			e.getEntity().remove();
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onExplosion(ExplosionPrimeEvent e) {
		if (!settings.getTNTsEnabled()) {
			for (Entity nEntity : e.getEntity().getNearbyEntities(10, 10, 10)) {
				if (nEntity instanceof Player) {
					Player p = (Player) nEntity;
					if (main.admin.contains(p)) {
						p.sendMessage(main.getPrefix()
								+ "§4Les TNTs sont désactivées sur le serveur !\n§cUtilisez §e\"§6/admincmdsb settings TNTsEnabled true§e\"§c pour les réactiver !");
					} else {
						p.sendMessage(main.getPrefix() + "§4Les TNTs sont désactivées sur le serveur !");
					}
					Bukkit.getConsoleSender()
							.sendMessage(main.getPrefix() + "§c" + p.getName()
									+ "§4 a essayé de faire exploser une TNT aux coordonnées : §cX: "
									+ e.getEntity().getLocation().getBlockX() + "§4, §cY: "
									+ e.getEntity().getLocation().getBlockY() + "§4, §cZ: "
									+ e.getEntity().getLocation().getBlockZ());
				}
			}
			e.setFire(false);
			e.getEntity().remove();
			e.setCancelled(true);
		}

    }

	@EventHandler
	public void onInteractAtEntity(PlayerInteractAtEntityEvent e) {
		Entity entity = e.getRightClicked();
		Player p = e.getPlayer();
		Location eLoc = e.getClickedPosition().toLocation(Objects.requireNonNull(Bukkit.getWorld("world")));
		double x = eLoc.getX();
		double y = eLoc.getY();
		double z = eLoc.getZ();

		if (x > 52 || x < -52 || z < -52 || z > 52) {
			return;
		}

        //noinspection ConstantValue
        if (entity == null || entity.getCustomName() == null || p.isSneaking() || main.bypassastp.contains(p)) {
			return;
		}

		if (entity.getType().equals(EntityType.ARMOR_STAND)) {
			try {
				ArmorStand as = (ArmorStand) entity;
				UUID uuid = as.getUniqueId();

				ASData asData = new ASData(uuid);
				String asName = asData.getASName();

                //noinspection ConstantValue
                if (asName == null) {
					if (main.staff.contains(p)) {
						p.sendMessage(main.getPrefix() + "§2Mini-jeu fermé/non trouvé !");
						p.sendTitle("§4§l❌ Fermé", "§r§cLe mini-jeu n'est pas enregitré dans notre liste.", 5, 60, 5);
						e.setCancelled(true);
					}
					return;
				}

				if (asName.equalsIgnoreCase("Build Battle")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					// tp @p -12.639 91.06250 240.414
					Location loc = new Location(Bukkit.getWorld("world"), -12.639, 91, 240.414);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("Cache Cache")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), -259, 39.00, 217, -360, 0);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("Capture de Drapeau")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), -87.500, 45.06250, -32.300);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("Labyrinthe")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), -425, 39, 270);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("ForestGump")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), -628, 106, 415);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("Curling")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), -305, 39, 267, 450, 0);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("Sumo")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), 145, 91, -93);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("Speed Horse")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), 34, 39, 153);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("Rugby")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), -174, 39.00, 54, 180, 0);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("SheepShot")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), 296, 39, 404);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("Tir à l'Arc")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), 596, 72, -826, 90, 0);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("Chaise Musicale")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), 398, 39, -290);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("Dé À Coudre")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), 397, 118, 290, 90, 0);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("Course de Bateau")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), 475, 47, -47.399);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("Jump")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), -37, 76, -127);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("Magic Wars")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), 217, 39, 206);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("TheBridge")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), 169, 108, -152);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("Sky Wars")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), -136, 42, 96);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("PVP")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), -240.449, 62, -77.343);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("Pig Battle")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), 87, 39, 117);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("Tridant Wars")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), 67, 137, 410);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("Murder Mystery")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), -445, 38, 393);
					p.teleport(loc);
					e.setCancelled(true);
                } else if (asName.equalsIgnoreCase("Pêche")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), -211, 40, 584);
					p.teleport(loc);
					e.setCancelled(true);
				} else if (asName.equalsIgnoreCase("The Blitz")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", 5,
							60, 5);
					Location loc = new Location(Bukkit.getWorld("world"), 207, 282, 69);
					p.teleport(loc);
					e.setCancelled(true);
				} else if (asName.equalsIgnoreCase("Forest Gump")) {
					p.sendMessage(main.getPrefix() + "§2Téléportation au mini-jeu §6" + asName + "§2 !");
					p.sendTitle("§a🚀 Téléportation...", "§r§2Téléportation au mini-jeu §6§l" + asName + "§r§2 !", -628,
							107, 415);
					Location loc = new Location(Bukkit.getWorld("world"), -628, 107, 415);
					p.teleport(loc);
					e.setCancelled(true);


				//EVENTS

                } else if (asName.equalsIgnoreCase("Évent Halloween en Octobre")) {
//					p.sendMessage(main.getPrefix() + main.hex(
//							"§cDomage !§4 L'Évent d'#FFC400§lH#FFB500§la#FFA500§ll#FF9600§ll#FF8600§lo#FF7700§lw#FF6700§le#FF5800§le#FF4800§ln§4 n'aura pas lieu cette année... §2Mais le prochain sera en §aOctobre 2024§2 !"));
//					p.sendTitle("§4❌ Erreur", main.hex(
//							"§r§cL'Évent d'#FFC400§lH#FFB500§la#FFA500§ll#FF9600§ll#FF8600§lo#FF7700§lw#FF6700§le#FF5800§le#FF4800§ln§c n'est pas §4ouvert au public§c."),
//							5, 60, 5);
//					e.setCancelled(true);

					//EVENT

					if (!main.admin.contains(p)) {
						p.sendMessage(main.getPrefix() + main.hex(
								"§cDomage !§4 L'Évent d'#FFC400§lH#FFB500§la#FFA500§ll#FF9600§ll#FF8600§lo#FF7700§lw#FF6700§le#FF5800§le#FF4800§ln§4 n'est pas ouvert au public... §2Mais il aura lieu §6fin Octobre 2026§2 !"));
						p.sendTitle("§4❌ Erreur", main.hex(
										"§r§cL'Évent d'#FFC400§lH#FFB500§la#FFA500§ll#FF9600§ll#FF8600§lo#FF7700§lw#FF6700§le#FF5800§le#FF4800§ln§c n'est pas §4ouvert au public§c."),
								5, 60, 5);
						e.setCancelled(true);
						return;
					}

					PotionEffect effect = new PotionEffect(PotionEffectType.BLINDNESS, 3, 255, true, false, false);
					p.addPotionEffect(effect);
					p.sendMessage(main.getPrefix() + main.hex(
							"§2Téléportation à §6l'Évent d'#FFC400§lH#FFB500§la#FFA500§ll#FF9600§ll#FF8600§lo#FF7700§lw#FF6700§le#FF5800§le#FF4800§ln§2 ! §c(§4Accès §5archives§c)"));
					p.sendTitle("§a🚀 Téléportation...", main.hex(
							"§r§2Téléportation à §6l'Évent d'#FFC400§lH#FFB500§la#FFA500§ll#FF9600§ll#FF8600§lo#FF7700§lw#FF6700§le#FF5800§le#FF4800§ln§r§2 !"),
							5, 60, 5);
					p.chat("/halloween");
					e.setCancelled(true);


                } else if (asName.equalsIgnoreCase("Évent de Pâques en Avril")) {
					if (!main.admin.contains(p)) {
						p.sendMessage(main.getPrefix() + main.hex(
								"§cDomage !§4 L'Évent de #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S§4 n'est pas ouvert au public... §2Mais il aura lieu en §aAvril 2026§2 !"));
						p.sendTitle("§4❌ Erreur", main.hex(
										"§r§cL'Évent de #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S§c n'est pas §4ouvert au public§c."),
								5, 60, 5);
						p.sendMessage(main.getPrefix() + main.hex(
								"§4L'Évent de #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S§4 aura lieu en §cAvril 2026 §4!"));
						e.setCancelled(true);
						return;
					}

					PotionEffect effect = new PotionEffect(PotionEffectType.BLINDNESS, 3, 255, true, false, false);
					p.addPotionEffect(effect);
					p.sendMessage(main.getPrefix() + main.hex(
							"§2Téléportation à §6l'Évent de #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S§2 ! §c(§4Accès §5archives§c)"));
					p.sendTitle("§a🚀 Téléportation...", main.hex(
							"§r§2Téléportation à §6l'Évent de #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S§r§2 !"),
							5, 60, 5);
					p.chat("/paques");
					e.setCancelled(true);
				} else if (asName.equalsIgnoreCase("Évent de l'Été en Juillet")) {
					p.sendMessage(main.getPrefix() + main.hex(
							"§cDomage !§4 L'Évent de l'#e3ff00É#edff59T#f6ffb1É§4 n'aura pas lieu cette année... §2Mais le prochain sera en §aJuillet 2025 §2!"));
					p.sendTitle("§4❌ Erreur",
							main.hex("§r§cL'Évent de l'#e3ff00É#edff59T#f6ffb1É§c n'est pas §4ouvert au public§c."), 5,
							60, 5);
					e.setCancelled(true);



				} else if (asName.equalsIgnoreCase("Évent Noël en Décembre")) {
//					p.sendMessage(main.getPrefix() + main.hex(
//							"§4L'Évent de #ff4848N#ff6b6bO#ff8e8eË#ffb1b1L§4 n'aura pas lieu cette année... §2Mais le prochain aura lieu en §aDécembre 2025 §2!"));
					if(!main.admin.contains(p)) {
						p.sendMessage(main.getPrefix() + main.hex("§4L'Évent de #ff4848N#ff6b6bO#ff8e8eË#ffb1b1L§4 n'aura pas lieu cette année... §2Mais le prochain aura lieu en §aDécembre 2025 §2!"));
						e.setCancelled(true);
						return;
					}
					PotionEffect effect = new PotionEffect(PotionEffectType.BLINDNESS, 3, 255, true, false, false);
					p.addPotionEffect(effect);
					p.sendMessage(main.getPrefix() + main.hex("§2Téléportation à §6l'Évent de #ff4848N#ff6b6bO#ff8e8eË#ffb1b1L§2 ! §c(§4Accès §5archives§c)"));
					p.sendTitle("§a🚀 Téléportation...", main.hex("§r§2Téléportation à §6l'Évent de #ff4848N#ff6b6bO#ff8e8eË#ffb1b1L§r§2 !"), 5, 60, 5);
					p.chat("/noel");
					e.setCancelled(true);
					return;
				} else {
					p.sendMessage(main.getPrefix() + "§2Mini-jeu fermé !");
					p.sendTitle("§4§l❌ Fermé", "§r§cLe mini-jeu §4" + asName + "§c n'est pas encore ouvert au public.",
							5, 60, 5);
					e.setCancelled(true);
                }
			} catch (Exception ASTPUnkownError) {
//				ASTPUnkownError.printStackTrace();
//				p.sendTitle("§4❌ Erreur", "§r§cUne erreur s'est produite lors de l'action.", 5, 60, 5);
//				p.sendMessage(main.getErrorPrefix() + "§cUne erreur s'est produite lors de l'action, §lmerci de contacter §4mattmunich§c !");
//				e.setCancelled(true);
            }

		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		Action action = event.getAction();
		ItemStack it = event.getItem();
		Block block = event.getClickedBlock();

		if (event.getClickedBlock() != null && action == Action.RIGHT_CLICK_BLOCK) {

			BlockState bs = event.getClickedBlock().getState();

			if (bs instanceof Sign) {

				Sign sign = (Sign) bs;

				if (sign.getLine(1).equalsIgnoreCase("[Clear]")) {
					player.getInventory().clear();
					player.sendMessage("§e(§6!§e) §2Votre inventaire à été clear avec succès !");

				}

				if (sign.getLine(1).equalsIgnoreCase("[Disconnect]")) {
					player.kickPlayer("Disconnected");
				}

				if (sign.getLine(1).equalsIgnoreCase("[Spawn]")) {

					player.chat("/spawn");

				}

			}

			if (bs instanceof Skull) {
				try {
					Skull skull = (Skull) bs;
					if (skull.getOwner() == null) {
						return;
					}
					if (skull.getOwner().equalsIgnoreCase("luck") && !player.isSneaking()) {
						event.setCancelled(true);
                        assert block != null;
                        block.setType(Material.AIR);

						/*
						 * Using LBReward void (Declared below)
						 *
						 */
						LBReward(player, block);
					}
				} catch (Exception LBeRight) {
					return;
				}
			}
		}

		if (it == null) {
			return;
		}

//		if(it.getType() == Material.DIAMOND_HOE) {
//			if(Action.RIGHT_CLICK_AIR != null) {
//				player.sendMessage("§2Clic !\n§6Tu as trouvé un EasterEgg !\n§4Récompense à venir");
//				System.out.println(player.getDisplayName() + " à trouvé l'EasterEgg DiamsHoe !");
//			}
//		}

		// AdminGUI

		if (it.getType() == Material.COMPASS && it.hasItemMeta() && Objects.requireNonNull(it.getItemMeta()).hasDisplayName()
				&& it.getItemMeta().getDisplayName().equalsIgnoreCase("§4Admin§6GUI")) {

			Inventory inv = Bukkit.createInventory(null, 27,
					main.hex("§c§lMenu §l#fb0000§lA#fc2727§lD#fc4e4e§lM#fd7474§lI#fd9b9b§lN"));
			ItemStack none = ItemBuilder.getItem(Material.YELLOW_STAINED_GLASS_PANE, " ", false, true, "", "", "");

			inv.setItem(14, ItemBuilder.getItem(Material.IRON_SWORD, "§2🗡 GameMode 0", true, true, " ",
					"§5§oJuste pour se mettre en GameMode Survival", " "));
			inv.setItem(13, ItemBuilder.getItem(Material.GRASS_BLOCK, "§2🚀 GameMode 1", true, true, " ",
					"§5§oJuste pour se mettre en GameMode Creative", " "));
			inv.setItem(12, ItemBuilder.getItem(Material.COMPASS, "§2👻 GameMode 3", true, true, " ",
					"§5§oJuste pour se mettre en GameMode Spectator !", " "));
			inv.setItem(4, ItemBuilder.getItem(Material.ENDER_PEARL, "§bTP §1-> §bjoueur", false, true, " ",
					"§5§oPour se téléporter à des joueurs !", " "));
			inv.setItem(18, ItemBuilder.getItem(Material.FEATHER, "§2Toggle §6Fly", false, true, " ",
					"§5§oJuste pour activer/désactiver le fly", " "));
			inv.setItem(19, ItemBuilder.getItem(Material.NETHERITE_CHESTPLATE, "§2Toggle §6God", false, true, " ",
					"§5§oJuste pour activer/désactiver le God Mode", " "));
			inv.setItem(0, ItemBuilder.getItem(Material.GLASS, "§2Toggle §6Vanish", false, true, " ",
					"§5§oJuste pour activer/désactiver le vanish", " "));
			inv.setItem(1, ItemBuilder.getItem(Material.BELL, "§2Toggle §6Chat", false, true, " ",
					"§5§oJuste pour activer/désactiver le tchat", " "));
			inv.setItem(7, ItemBuilder.getItem(Material.COOKED_BEEF, "§6Feed", false, true, " ",
					"§5§oJuste pour se feed !", " "));
			inv.setItem(8, ItemBuilder.getItem(Material.ENCHANTED_GOLDEN_APPLE, "§6Heal", false, true, " ",
					"§5§oJuste pour se heal !", " "));
			inv.setItem(26, ItemBuilder.getItem(Material.BARRIER, "§4✘ Fermer le menu", false, true, " ",
					"Pour fermer le menu", "Pas plus d'action :)"));
			// none
			inv.setItem(2, none);
			inv.setItem(3, none);
			inv.setItem(5, none);
			inv.setItem(6, none);
			inv.setItem(9, none);
			inv.setItem(10, none);
			inv.setItem(11, none);
			inv.setItem(15, none);
			inv.setItem(16, none);
			inv.setItem(17, none);
			inv.setItem(20, none);
			inv.setItem(21, none);
			inv.setItem(22, none);
			inv.setItem(23, none);
			inv.setItem(24, none);
			inv.setItem(25, none);

//			ItemStack apple = new ItemStack(Material.APPLE);
//			ItemMeta appleM = apple.getItemMeta();
//			appleM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 200, true);
//			appleM.setUnbreakable(true);
//			appleM.setDisplayName("§3Retour au spawn :)");
//			appleM.setLore(Arrays.asList(" ", "Juste pour retourner au spawn :)", "Comme /spawn"));
//			appleM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
//			appleM.addItemFlags(ItemFlag.HIDE_DESTROYS);
//			appleM.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
//			apple.setItemMeta(appleM);
//			inv.setItem(13, apple);

			player.openInventory(inv);

		}

	}

//	public ItemStack getItem(Material material, String customName, boolean Enchanted, boolean Unbreakable, String DescLine1, String DescLine2, String DescLine3) {
//
//		ItemStack item = new ItemStack(material, 1);
//		ItemMeta itemM = item.getItemMeta();
//
//		if(customName != null) itemM.setDisplayName(customName); else itemM.setDisplayName("§6No Named Item");;
//		if(Enchanted == true) itemM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, Enchanted);
//		if(Unbreakable == true) itemM.setUnbreakable(Unbreakable);
//		if(DescLine1 != null && DescLine2 != null && DescLine3 != null) itemM.setLore(Arrays.asList(DescLine1, DescLine2, DescLine3));
//		itemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
//		itemM.addItemFlags(ItemFlag.HIDE_DESTROYS);
//		itemM.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
//
//
//		item.setItemMeta(itemM);
//		return item;
//
//	}

//	@EventHandler
//	public void OnInteractAtEntity(PlayerInteractAtEntityEvent e) {
//		Player p = e.getPlayer();
//		if (e.getRightClicked().getClass() == ArmorStand.class) {
//			ArmorStand stand = (ArmorStand) e.getRightClicked();
//			if (stand.getScoreboardTags().contains(p.getName())) {
//				e.setCancelled(true);
//			}
//		}
//	}

//	@EventHandler
//	public void onChunkLoad(ChunkLoadEvent e) {
//		if(!e.isNewChunk()) {
//			for(ArmorStand allst : e.getWorld().getEntitiesByClass(ArmorStand.class)) {
//				PlayerData pdata = new PlayerData(allst.getUniqueId());
//				if(pdata.exist() && pdata.getASName() == allst.getName()) {
//
//				}else if(allst.getName() == null) {
//
//					if(!pdata.exist()) {
//						pdata.setASName("Armor Stand");
//						allst.setCustomName("Armor Stand");
//						Bukkit.getConsoleSender().sendMessage("[AdminCmdsB] New ArmorStandData Created with name = null before");
//					} else {
//						pdata.setASName("Armor Stand");
//						allst.setCustomName("Armor Stand");
//						Bukkit.getConsoleSender().sendMessage("[AdminCmdsB] Saved ArmorStand new name because AS name was null");
//					}
//
//				}else {
//					if(!pdata.exist()) {
//						pdata.setASName(allst.getName());
//						Bukkit.getConsoleSender().sendMessage("[AdminCmdsB] New ArmorStandData Created who had already a name/customname");
//					} else if(pdata.getASName() == null) {
//						pdata.setASName(allst.getName());
//						Bukkit.getConsoleSender().sendMessage("[AdminCmdsB] Saved ArmorStand name bc name in config was null");
//					}
//				}
//				grades.loadAS(allst);
//			}
//		}
//	}
//
//	@EventHandler
//	public void onEntitySpawn(EntitySpawnEvent e) {
//
//		if(e.getEntity().getClass() == ArmorStand.class) {
//			ArmorStand allst = (ArmorStand) e.getEntity();
//			PlayerData pdata = new PlayerData(allst.getUniqueId());
//			if(allst.getName() == null) {
//
//				if(!pdata.exist()) {
//					pdata.setASName("Armor Stand");
//					allst.setCustomName("Armor Stand");
//				} else {
//					pdata.setASName("Armor Stand");
//					allst.setCustomName("Armor Stand");
//				}
//
//			}else {
//				if(!pdata.exist()) {
//					pdata.setASName(allst.getName());
//				} else {
//					pdata.setASName(allst.getName());
//				}
//			}
//			grades.loadAS(allst);
//		}
//	}

	// stand1 ScoreboardTag : CODEHA@#*2

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (settings.getAdvancedNameTags()) {
			// Getting all ArmorStand of the world
			for (ArmorStand allst : p.getWorld().getEntitiesByClass(ArmorStand.class)) {
				// Chacking if it's stand1
				if (allst.getScoreboardTags().contains(p.getName() + "CODEHA@#*2")) {
                    p.addPassenger(allst);
					allst.setArrowsInBody(0);
					allst.setVisible(false);
					allst.setInvisible(true);
				}
				// Checking if it's stand
				if (allst.getScoreboardTags().contains(p.getName())
						&& !allst.getScoreboardTags().contains(p.getName() + "CODEHA@#*2")) {

                    // Checking all passengers of stand
					for (Entity entity : allst.getPassengers()) {
						// Checking if the selected passenger is bat
                        //noinspection ConstantValue
                        if (entity.getClass() == ArmorStand.class && entity.getScoreboardTags().contains(p.getName() + "CODEHA@#*2")) {
							ArmorStand stand1 = (ArmorStand) entity;
							allst.setArrowsInBody(0);
							allst.setVisible(false);
							allst.setInvisible(true);
							stand1.addPassenger(allst);
							stand1.setInvisible(true);

							// For stand
							if (p.getGameMode() != GameMode.SPECTATOR && !p.isSwimming() && !p.isGliding()
									&& !main.vanished.contains(p) && !p.isSneaking()) {
								allst.setCustomNameVisible(true);
								// stand.teleport(p.getLocation().add(0, 0.8, 0));
								allst.setCustomName(main.hex(p.getDisplayName()));

							} else if (p.isSwimming() || p.isGliding()) {
								allst.setCustomNameVisible(true);
								// stand.teleport(p.getLocation().add(0, -0.2, 0));
								allst.setCustomName(main.hex(p.getDisplayName()));

							} else if (main.vanished.contains(p)) {
								// stand.teleport(p.getLocation().add(0, 0.8, 0));
								allst.setCustomNameVisible(false);

							} else {
								// stand.teleport(p.getLocation().add(0, 0.8, 0));
								allst.setCustomNameVisible(false);
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onClic(InventoryClickEvent event) {

		// Inventory inv = event.getClickedInventory();
		Player player = (Player) event.getWhoClicked();
		ItemStack current = event.getCurrentItem();

		boolean invseepaused = false;

		if (event.getView().getTitle().contains("§6§lInv§4§lSee §0§l- §6")) //noinspection RedundantSuppression
        {
			String targetName = event.getView().getTitle().replace("§6§lInv§4§lSee §0§l- §6", "");
			if (Bukkit.getPlayer(targetName) == null) {
				player.sendMessage(main.getPrefix() + "§4Le joueur s'est déconnecté !");
				player.sendMessage(main.getPrefix() + "§4Impossible d'obtenir l'inventaire du joueur §c" + targetName
						+ "§c, car il s'est déconnecté !");
				Bukkit.getConsoleSender().sendMessage("[AdminCmdsB] -> [InvSee-Listener] : Player " + targetName
						+ " disconnected while " + player.getName() + " was watching his inventory !");
				event.setCancelled(true);
				event.getView().close();
				return;
			}

			try {
				Player target = Bukkit.getPlayer(targetName);
			} catch (Exception e) {
				player.sendMessage(
						main.getPrefix() + "§4Impossible d'obtenir l'inventaire du joueur §c" + targetName + "§4 !");
				event.setCancelled(true);
				event.getView().close();
				return;
			}

			Player target = Bukkit.getPlayer(targetName);
			try {
				Player checkOnline = Bukkit.getPlayer(targetName);
			} catch (Exception e) {
				player.sendMessage(
						main.getPrefix() + "§4Impossible d'obtenir l'inventaire du joueur §c" + targetName + "§4 !");
				Bukkit.getConsoleSender().sendMessage("[AdminCmdsB] -> [InvSee-Listener] : Player " + targetName
						+ " disconnected while " + player.getName() + " was seeing his inventory !");
				event.setCancelled(true);
				event.getView().close();
				return;
			}

			Inventory invSee = event.getInventory();
            // noinspection ConstantExpression,ConstantValue
            if (invSee == null) {
				player.sendMessage(
						main.getPrefix() + "§4Impossible d'obtenir l'inventaire du joueur §c" + targetName + "§4 !");
				event.getView().close();
				event.setCancelled(true);
				return;
			}
			// ItemStack air = new ItemStack(Material.AIR);
			// Empty item creation
			ItemStack empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
			ItemMeta emptyM = empty.getItemMeta();
            assert emptyM != null;
            emptyM.setDisplayName("§0_");
			empty.setItemMeta(emptyM);
			// Refresh item creation
			ItemStack refresh = ItemBuilder.getItem(Material.SLIME_BALL, "§a🚀 Éxécuter", false, false,
					"§e§oCliquez pour executer", "§e§oet recharger", "");
			// ⟳
			// Close item creation
			ItemStack close = ItemBuilder.getItem(Material.BARRIER, "§4❌ Fermer", false, false,
					"§e§oCliquez pour fermer", "§e§ole menu", "");

//			player.openInventory(target.getInventory());

			for (int slot = 0; slot < 36; slot++) {
				ItemStack item = target.getInventory().getItem(slot);
				if (item != null) {
					invsee.items[slot] = item;
				}
			}
			for (int slot = 36; slot < 45; slot++) {
				invsee.items[slot] = empty;
			}
			try {
				invsee.items[45] = target.getInventory().getHelmet();
				invsee.items[46] = target.getInventory().getChestplate();
				invsee.items[47] = target.getInventory().getLeggings();
				invsee.items[48] = target.getInventory().getBoots();
				invsee.items[49] = empty;
				invsee.items[50] = target.getInventory().getItemInOffHand();
				invsee.items[51] = empty;
				invsee.items[52] = refresh;
				invsee.items[53] = close;
			} catch (Exception eInvSeeItms) {
				eInvSeeItms.printStackTrace();
			}

			if (!invseepaused) {
				invSee.setStorageContents(invsee.items);
			}

			if (current != null) {
				if (current.getItemMeta().getDisplayName().equalsIgnoreCase("§0_")
						&& current.getType() == Material.BLACK_STAINED_GLASS_PANE) {
					event.setCancelled(true);
				} else if (current.getItemMeta().getDisplayName().equalsIgnoreCase("§a🚀 Éxécuter")
						&& current.getType() == Material.SLIME_BALL) {
					invseepaused = true;
					try {
						for (int slot = 0; slot < 36; slot++) {
							ItemStack item = invsee.items[slot];
							if (item != null) {
								target.getInventory().setItem(slot, item);
							}
						}

						target.getInventory().setHelmet(invsee.items[45]);
						target.getInventory().setChestplate(invsee.items[46]);
						target.getInventory().setLeggings(invsee.items[47]);
						target.getInventory().setBoots(invsee.items[48]);
						target.getInventory().setItemInOffHand(invsee.items[50]);
						event.setCancelled(true);
						player.updateInventory();
						target.updateInventory();
					} catch (Exception eDisconnect) {
						player.sendMessage(main.getPrefix() + "§4Impossible d'obtenir l'inventaire du joueur §c"
								+ targetName + "§4 !");
						Bukkit.getConsoleSender().sendMessage("[AdminCmdsB] -> [InvSee-Listener] : Player " + targetName
								+ " disconnected while " + player.getName() + " was seeing his inventory !");
						event.setCancelled(true);
						event.getView().close();
						return;
					}
					invseepaused = false;
				} else if (current.getItemMeta().getDisplayName().equalsIgnoreCase("§4❌ Fermer")
						&& current.getType() == Material.BARRIER) {
					invseepaused = true;
					try {
						event.getView().close();
						event.setCancelled(true);
						player.updateInventory();
						target.updateInventory();
					} catch (Exception eDisconnected) {
						player.sendMessage(main.getPrefix() + "§4Impossible d'obtenir l'inventaire du joueur §c"
								+ targetName + "§4 !");
						Bukkit.getConsoleSender().sendMessage("[AdminCmdsB] -> [InvSee-Listener] : Player " + targetName
								+ " disconnected while " + player.getName() + " was seeing his inventory !");
						event.setCancelled(true);
						event.getView().close();
					}
					invseepaused = false;
					return;
				}
			}

		}

		if (current == null) {
			return;
		}

		if (event.getView().getTitle()
				.equalsIgnoreCase(main.hex("§c§lMenu §l#fb0000§lA#fc2727§lD#fc4e4e§lM#fd7474§lI#fd9b9b§lN"))) {
			if (!main.admin.contains(player)) {
				event.setCancelled(true);
				player.sendMessage(main.getPrefix() + "§4Vous n'avez pas la permission d'utiliser cet objet !");
				event.getView().close();
				return;
			}

			event.setCancelled(true);

			switch (current.getType()) {
			case COMPASS:
				player.chat("/gamemode spectator");
				break;

			case BARRIER:
				player.closeInventory();
				player.sendMessage(main.getPrefix() + "§2Menu fermé !");
				break;

			case GRASS_BLOCK:
				player.chat("/gamemode creative");
				break;
			case IRON_SWORD:
				player.chat("/gamemode survival");
				break;
			case FEATHER:
				player.chat("/fly");
				break;
			case NETHERITE_CHESTPLATE:
				player.chat("/god");
				break;
			case GLASS:
				player.chat("/vanish");
				break;
			case COOKED_BEEF:
				player.chat("/feed");
				break;
			case ENCHANTED_GOLDEN_APPLE:
				player.chat("/heal");
				break;
			case BELL:
				player.chat("/chat");
				break;
			case ENDER_PEARL:
				player.closeInventory();
				Inventory inv1 = Bukkit.createInventory(null, 54, "§bTP §1-> §bjoueur");

				ItemStack it1 = new ItemStack(Material.BARRIER);
				ItemMeta itm = it1.getItemMeta();

				itm.setDisplayName("§4Fermer le menu");
				itm.setLore(Arrays.asList(" ", "§5Cliquez ici fermer le menu"));

				it1.setItemMeta(itm);
				inv1.setItem(53, it1);
				for (Player all : Bukkit.getOnlinePlayers()) {

					ItemStack pHead = new ItemStack(Material.PLAYER_HEAD);
					SkullMeta phm = (SkullMeta) pHead.getItemMeta();

					phm.setOwningPlayer(Bukkit.getOfflinePlayer(all.getName()));
					phm.setDisplayName(all.getName());
					phm.setLore(Collections.singletonList("§6Cliquez pour vous téléporter à §5" + all.getDisplayName()));

					pHead.setItemMeta(phm);

					inv1.addItem(pHead);

				}
				player.closeInventory();
				player.openInventory(inv1);

				onClick(event);
				break;
			default:
				break;
			}

			player.updateInventory();
		}
	}

	// START;
	@EventHandler
	public void onClick(InventoryClickEvent event1) {

		Player player1 = (Player) event1.getWhoClicked();
		ItemStack current1 = event1.getCurrentItem();

		if (current1 == null) {
			return;
		}

		if (event1.getView().getTitle().equalsIgnoreCase("§bTP §1-> §bjoueur")) {

			switch (current1.getType()) {

			case BARRIER:
				player1.closeInventory();
				player1.sendMessage(main.getPrefix() + "§2Menu fermé !");
				break;
			default:
				ItemMeta it = current1.getItemMeta();

				String targetName = it.getDisplayName();

				if (Bukkit.getPlayer(targetName) == null) {
					player1.sendMessage(main.getPrefix() + "§4Le joueur est hors-ligne ou n'éxiste pas !");
					return;
				}

				Player t = Bukkit.getPlayer(targetName);

				player1.chat("/tp " + t.getName());

				player1.closeInventory();
//										try {
//											//Player t = Bukkit.getPlayer(skull.getOwner());
//											player.chat("/tp " + t.getName());
//											event.setCancelled(true);
//											player.closeInventory();
//										} catch(Exception e) {
//											e.printStackTrace();
//											System.err.println("[AdminCmdsB] Une erreur est survenue lors de la téléportation d'un joueur !");
//											Bukkit.broadcastMessage(main.getErrorPrefix() + "[AdminCmdsB] An error occurred while using AdminCmdsB ! Error : Une erreur est survenue lors de la téléportation d'un joueur !");
//											for(Player all : Bukkit.getOnlinePlayers()) {
//												if(main.staff.contains(all)) {
//													all.sendMessage(main.getErrorPrefix() + "Une erreur est survenue lors de la téléportation d'un joueur !");
//													all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 100, 100);
//													all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 100, 100);
//													all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 100, 100);
//													all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 100, 100);
//													all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 100, 100);
//												}
//											}
//										}

				break;
			}

			// else {
//							System.err.println("[AdminCmdsB] An error occurred while using AdminCmdsB !");
//							Bukkit.broadcastMessage(main.getErrorPrefix() + "[AdminCmdsB] An error occurred while using AdminCmdsB !");
//							for(Player all : Bukkit.getOnlinePlayers()) {
//								if(main.staff.contains(all)) {
//									all.sendMessage(main.getErrorPrefix() + "Une erreur est survenue lors de la téléportation d'un joueur !");
//									all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 100, 100);
//									all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 100, 100);
//									all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 100, 100);
//									all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 100, 100);
//									all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 100, 100);
//
//								}
//							}
//							return;
//						}

		}
	}

	public void LBReward(Player p, Block block) {
		// Lucky Sword Item

		ItemStack cSword = new ItemStack(Material.NETHERITE_SWORD);
		ItemMeta sM = cSword.getItemMeta();

        assert sM != null;
        sM.addEnchant(Enchantment.SHARPNESS, 10, true);
		sM.addEnchant(Enchantment.FIRE_ASPECT, 5, true);
		sM.addEnchant(Enchantment.KNOCKBACK, 3, true);
		sM.addEnchant(Enchantment.SWEEPING_EDGE, 10, true);
		sM.addEnchant(Enchantment.UNBREAKING, 10, true);
		sM.setDisplayName("§2[§6§lLEGENDARY §e§lITEM§2]§r§eLucky Sword");

		cSword.setItemMeta(sM);

		// 30min NV potion

		ItemStack potion = new ItemStack(Material.POTION);
		PotionMeta pM = (PotionMeta) potion.getItemMeta();
		pM.addCustomEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 36000, 1, false), true);
		pM.setDisplayName("§3[§5§lEXTRA §d§lRARE §b§lITEM§r§3] §1Night §bVision");
		pM.addEnchant(Enchantment.PROTECTION, 1, true);
		pM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		pM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		pM.setLore(Arrays.asList("§eLucky Block", "§1Night Vision"));
		pM.setColor(Color.BLUE);
		potion.setItemMeta(pM);

		// Luck process and give item.

		double luck = Math.random();

		if (luck <= 0.001D) {
			// 0,1% (grade VIP)

			if (grades.hasPowerInf(p, 50)) {
				grades.chageRank(p.getUniqueId().toString(), GradeList.VIP);
				p.sendMessage(main.getLuckyBPrefix() + main.hex(
						"§2Vous avez de la chance... §2[§5§lMEGA §6§lLEGENDARY§2]§r §eGRADE #ffcf00V#ffe55fI#fffabeP §e !!"));
			} else if (grades.hasPower(p, 50)) {
				p.sendMessage(main.getLuckyBPrefix()
						+ main.hex("§2Vous avez de la chance... §cMais vous avez déjà le grade §2VIP §c!"));
				try {
					wait(3000);
				} catch (InterruptedException ebruh) {
				}
				p.sendMessage(main.getLuckyBPrefix() + main
						.hex("§2Mais heureusement... §2[§5§lMEGA §6§lLEGENDARY§2]§r §e§lLOT DE COMPENSATION§e !!"));

				try {
					wait(1000);
				} catch (InterruptedException ebruh) {
				}
				p.getInventory().addItem(cSword);
				p.sendMessage(
						main.getLuckyBPrefix() + "§2Vous avez de la chance... §2[§6§lLEGENDARY§2]§r §eLUCKY SWORD !!");

				block.getWorld().dropItem(block.getLocation(), potion);
				p.sendMessage(main.getLuckyBPrefix()
						+ "§2Vous avez de la chance... §3[§5§lEXTRA §d§lRARE§r§3] §1Night §bVision potion ! ");

				block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.NETHERITE_INGOT, 5));
				p.sendMessage(main.getLuckyBPrefix() + "§2Vous avez de la chance... §0NETHERITE INGOT !!!");
			}

		} else if (luck <= 0.01D) {
			// 1% (LuckySword)

			p.getInventory().addItem(cSword);
			p.sendMessage(
					main.getLuckyBPrefix() + "§2Vous avez de la chance... §2[§6§lLEGENDARY§2]§r §eLUCKY SWORD !!");

		} else if (luck <= 0.05D) {
			// 5% (30min NV potion)

			block.getWorld().dropItem(block.getLocation(), potion);
			p.sendMessage(main.getLuckyBPrefix()
					+ "§2Vous avez de la chance... §3[§5§lEXTRA §d§lRARE§r§3] §1Night §bVision potion ! ");

		} else if (luck <= 0.15D) {
			// 15% (Netherite ingot)

			block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.NETHERITE_INGOT, 5));
			p.sendMessage(main.getLuckyBPrefix() + "§2Vous avez de la chance... §0NETHERITE INGOT !!!");

		} else if (luck <= 0.20D) {
			// 20% Diams Block

			block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.DIAMOND_BLOCK, 1));
			p.sendMessage(main.getLuckyBPrefix() + "§2Vous avez de la chance... §bDes Diams !!");
		} else if (luck <= 0.2878D) {
			// 28,78% XP

			block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.EXPERIENCE_BOTTLE, 32));
			p.sendMessage(main.getLuckyBPrefix() + "§2Vous avez de la chance... §a32 EXP BOTTLE !!");

		} else if (luck <= 0.35D) {
			// 35% Faim

			p.setFoodLevel(0);
			p.setAbsorptionAmount(0);
			p.sendMessage(main.getLuckyBPrefix() + "§cPas de chance... §4 Faim tu as !");

		} else if (luck <= 0.7937D) {
			// 79,37% explosion

			p.sendMessage(main.getLuckyBPrefix() + "§cPas de chance... §4EXPLOSION !!!");
			block.getWorld().createExplosion(block.getLocation(), 1);

		} else if (luck <= 0.8333D) {
			// 83,33% (MLG 300 blocks)

			p.teleport(new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() + 300,
					p.getLocation().getZ()));
			ItemStack WB = new ItemStack(Material.WATER_BUCKET);
			p.getInventory().addItem(WB);
			p.sendMessage(main.getLuckyBPrefix() + "§cPas de chance... §4TP + 300 BLOCS EN HAUTEUR !!! §b MLG ?!");

		} else if (luck <= 0.9153D) {
			// 91,53% Lightning

			p.sendMessage(main.getLuckyBPrefix() + "§cPas de chance... §4ÉCLAIR!!");
			block.getWorld().spawnEntity(block.getLocation(), EntityType.LIGHTNING_BOLT);
		} else {
			// 91,545 -> 100% 7 coal
			block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.COAL, 7));
			p.sendMessage(main.getLuckyBPrefix() + "§eJe ne sais pas si c'est de la chance... §07 de charbon §6:/");
		}
	}

	@EventHandler
	public void mye(PlayerTeleportEvent e){
		Location from = e.getFrom();
		Player p = e.getPlayer();
		PlayerData data = new PlayerData(p.getUniqueId());

		data.getConfig().set("lastPos.x", from.getBlockX());
		data.getConfig().set("lastPos.y", from.getBlockY());
		data.getConfig().set("lastPos.z", from.getBlockZ());
		data.getConfig().set("lastPos.world", Objects.requireNonNull(from.getWorld()).getName());
		data.saveConfig();
	}
//	END
}
