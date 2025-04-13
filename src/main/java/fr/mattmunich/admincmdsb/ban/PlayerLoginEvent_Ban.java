package fr.mattmunich.admincmdsb.ban;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import fr.mattmunich.admincmdsb.commandhelper.PlayerData;

public class PlayerLoginEvent_Ban implements Listener {

	@EventHandler
	public void onTempBan(PlayerLoginEvent e) {

		Player p = e.getPlayer();

		PlayerData data = new PlayerData(p);

		if (!data.exist() || !data.isBanned()) {
			return;
		}

		e.disallow(Result.KICK_OTHER, "§6§lServerBan§r\n\n§4Vous avez été banni du serveur par " + data.getBannedFrom()
				+ "§r\n§4Raison : §6" + data.getBannedReason() + "§r\n\n\n§4Contactez le staff en cas d'erreur");

		return;

	}

}
