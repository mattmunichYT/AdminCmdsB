package fr.mattmunich.monplugin.anticheat;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import fr.mattmunich.monplugin.MonPlugin;

import java.util.Objects;

public class Movement_AntiCheat implements Listener {

	private MonPlugin main;

	public Movement_AntiCheat(MonPlugin main) {
		this.main = main;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent e) {

		Player p = e.getPlayer();
		double speed = e.getFrom().distance(Objects.requireNonNull(e.getTo()));

		if (speed > 8.0 && !p.getActivePotionEffects().toString().contains("SPEED") && !(p.getWalkSpeed() > 2)
				&& !(p.getFlySpeed() > 1) && !p.isGliding() && !main.staff.contains(p)) {
			p.sendMessage(main.getAntiCheatPrefix() + "Vous avez été détecté comme §cSpeedHacker§4 !");
			main.speedhacking.add(p);
			Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				@Override
				public void run() {
					main.speedhacking.remove(p);
					for (Player all : Bukkit.getOnlinePlayers()) {
						if (main.superstaff.contains(all)) {
							all.sendMessage(main.getAntiCheatPrefix() + "§6Le joueur §c" + p.getName()
									+ "§6 a été détecté comme §4Speed Hacker§6 avec une vitesse de §c" + speed * 20
									+ "§4 blocks/s§6 !");
						}
					}
					Bukkit.getConsoleSender().sendMessage(main.getAntiCheatPrefix() + "§6Le joueur §c" + p.getName()
							+ "§6 a été détecté comme §4Speed Hacker§6 avec une vitesse de §c" + speed + "§6 !");
				}
			}, 60);

		}

		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, new Runnable() {
			int timeAir = 0;
			float fallStart = p.getFallDistance();

			@Override
			public void run() {
				if (timeAir == 5 && fallStart == p.getFallDistance() && (p.getFallDistance() != 0) && fallStart != 0
						&& p.isOnline() && !main.staff.contains(p)) {
					p.kickPlayer("§6§lAnti-Cheat\n\n§4Vous n'avez pas la permission de §c§lfly§r§4 sur ce serveur !");
					Bukkit.getConsoleSender().sendMessage("§e[§6§lAnti§r§0-§4§lCheat§e] §4Player §c\"" + p.getName()
							+ "\"§4 is detected as flying by not moving in air !");
					return;
				}
				if (timeAir == 5 && fallStart == p.getFallDistance() && (p.getFallDistance() != 0) && fallStart != 0
						&& p.isOnline() && main.staff.contains(p)) {
					Bukkit.getConsoleSender().sendMessage("§e[§6§lAnti§r§0-§4§lCheat§e] §aStaff Member §c\""
							+ p.getName() + "\"§4 is detected as flying by not moving in air !");
					return;
				}

//				if(timeAir == 20) {
//					p.kickPlayer("§6§lAnti-Cheat\n\n§4Vous n'avez pas la permission de §lfly§r§4 sur ce serveur !");
//					Bukkit.getConsoleSender().sendMessage("§e[§6§lAnti§r§0-§4§lCheat§e] §4Player §c\"" + p.getName() + "\"§4 is detected as flying by beeing in air for 20 seconds !");
//					return;
//				}
				Location loc1 = p.getLocation();
				Location loc2 = p.getLocation();
				loc1.setY(loc1.getY() - 1);
				loc2.setY(loc2.getY() - 2);

				Material block1 = loc1.getBlock().getType();
				Material block2 = loc2.getBlock().getType();
				if (!p.isOnGround() && block1.isAir() && block2.isAir() && !p.isFlying()
						&& !(p.getGameMode() == GameMode.CREATIVE) && !(p.getGameMode() == GameMode.SPECTATOR)
						&& !(p.hasPotionEffect(PotionEffectType.LEVITATION))
						&& !(p.hasPotionEffect(PotionEffectType.JUMP_BOOST))
						&& !(p.hasPotionEffect(PotionEffectType.SLOW_FALLING)) && !p.isGliding()) {
					timeAir++;
				} else {
					timeAir = 0;
				}

			}
		}, 20, 25);

		if (main.speedhacking.contains(p)) {
			e.setCancelled(true);
			p.sendTitle("§cSpeedHack §4§ldétecté !", "§6Vous serez unfreeze dans 3s", 5, 60, 5);
		}
	}

}
