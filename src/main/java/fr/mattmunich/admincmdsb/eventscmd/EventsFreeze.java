package fr.mattmunich.admincmdsb.eventscmd;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.mattmunich.admincmdsb.Main;

public class EventsFreeze implements Listener {

	private Main main;

	public EventsFreeze(Main main) {
		this.main = main;
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player player = e.getPlayer();

		if (main.freeze.contains(player)) {
			e.setCancelled(true);
			player.sendTitle("§3Vous êtes freeze !", "§4N'essayez pas de bouger !", 1, 1000, 1);
		}

		if (main.banni.contains(player)) {
			player.kickPlayer(
					"§6§lServerBan§r\n\n§6Vous avez été banni !§r\n§2Sanction : §eDéfinitive\n\n§4§oContactez le staff en cas d'erreur ");
		}

//		if(player.isFlying() && grades.hasPowerInf(player, 50) && player.getLocation().getBlock().getType().isAir()) {
//			player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.LIGHTNING);
//			Bukkit.broadcastMessage(main.getPrefix() + "§6" + player.getName() + " §4est en train de cheat !!!");
//			e.getPlayer().kickPlayer("§l§6AntiCheat§r\n\n§eVous avez été kick du serveur§r\n\n§eRaison : §r§4Interdiction de fly !!\n\n\n§4Désinstalez votre cheat !");
//			main.freeze.add(player);
//		}

	}

}
