package fr.mattmunich.monplugin.tasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class TimerTask extends BukkitRunnable {

	private int timer = 10;

	@Override
	public void run() {
		Bukkit.broadcastMessage("[Server]: La WhiteList va être activée dans " + timer + "secondes");

		if (timer == 0) {
			Bukkit.broadcastMessage("[MonPlugin]: Oups c'etait une blague");
			cancel();
		}

	}

}
