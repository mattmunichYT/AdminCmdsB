package fr.mattmunich.admincmdsb;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import fr.mattmunich.admincmdsb.commandhelper.Ban;
import fr.mattmunich.admincmdsb.commandhelper.Grades;
import fr.mattmunich.admincmdsb.commandhelper.Settings;

public class LoginListener implements Listener {

	public static LoginListener ll;

	private Main main;
	private final Grades grades;
	private final Ban ban;
	private final Settings settings;

	public LoginListener(Grades grades, Main main, Ban ban, Settings settings) {
		this.grades = grades;
		this.main = main;
		this.ban = ban;
		this.settings = settings;
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {

		Player player = event.getPlayer();

		if (settings.getMaintenance()) {
			if (grades.hasPowerInf(player, 60)) {
				event.disallow(Result.KICK_OTHER,
						"§6Le serveur es en maintenance\nVous pouvez seulement rejoindre le serveur en ayant le grade \n§bBuildeur§6, §2Modérateur §6ou §4Administrateur");
			}
		}

		if (ban.getConfig().getBoolean(player.getUniqueId() + ".isBanned.")) {
			main.banni.add(player);
			event.disallow(Result.KICK_OTHER,
					"§6§lServerBan§r\n\n§4Vous avez été banni du serveur !§r\n§4Sanction : §6Définitive§r\n§4Raison : §6"
							+ ban.getConfig().getString(player.getUniqueId() + ".reason.")
							+ "§r\n\n\n§4Contactez le staff en cas d'erreur");
		}

	}

}
