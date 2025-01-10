package fr.mattmunich.admincmdsb.ban;

import java.time.Duration;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import fr.mattmunich.admincmdsb.Main;
import fr.mattmunich.admincmdsb.commandhelper.PlayerData;
import fr.mattmunich.admincmdsb.commandhelper.Settings;

public class PlayerLoginEvent_TempBan implements Listener{

	private Main main;

	private Settings settings;

	public PlayerLoginEvent_TempBan(Main main, Settings settings) {
		this.main = main;
		this.settings = settings;
	}

	@EventHandler
	public void onTempBan(PlayerLoginEvent e) {

		Player p = e.getPlayer();

		PlayerData data = new PlayerData(p.getUniqueId());

		if(!data.exist() || !data.isTempbanned()) {
			return;
		}

		if(data.getTempbanMilliseconds() <= System.currentTimeMillis()){
			data.setUnTempbanned();
			return;
		}

		long restantMs = data.getTempbanMilliseconds() - System.currentTimeMillis();

		Duration remainingTime = Duration.ofMillis(restantMs);
	    long days = remainingTime.toDays();
	    remainingTime = remainingTime.minusDays(days);
	    long weeks = days / 7;
	    days %= 7; // or if you prefer, days = days % 7;
	    long years = days / 365;
	    days %= 365; // or if you prefer, days = days % 7;
	    long hours = remainingTime.toHours();
	    remainingTime = remainingTime.minusHours(hours);
	    long minutes = remainingTime.toMinutes();
	    remainingTime = remainingTime.minusMinutes(minutes);
	    long seconds = remainingTime.getSeconds();

	    Bukkit.getConsoleSender().sendMessage(main.getBanPrefix() + p.getName() + " tried to join but is still tempbanned for " + years + " years " + weeks + " weeks " + days + " days "
	            + hours + " hours " + minutes + " minutes " + seconds + " seconds");

		e.disallow(Result.KICK_OTHER, (settings.getServerName() == null ? "§6§lServerBan" : settings.getServerName()) + "§r\n\n§4Vous avez été banni du serveur par " + data.getTempbannedFrom()  + " !§r\n§4Fin du bannissement : §6" + data.getTempbanEnd() + " !§r\n§4Raison : §6" + data.getTempbannedReason() + "§r\n§4Temps restant : §c" + (years == 0 ? "" : String.valueOf(years) + " §ean(s),§c ") + (weeks == 0 ? "" : String.valueOf(weeks) + " §esemaine(s), §c") + (hours == 0 ? "" : String.valueOf(hours) + "§eh, §c") + (minutes == 0 ? "" : String.valueOf(minutes) + "§em, §c") + (seconds == 0 ? "" : String.valueOf(seconds) + "§es§c") + "§r\n\n\n§4Contactez le staff en cas d'erreur");

		return;

	}

}
