package fr.mattmunich.admincmdsb;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;

import fr.mattmunich.admincmdsb.commandhelper.PlayerData;

public class PreLoginEvent implements Listener {
	@EventHandler
	public void preLoginEvent(AsyncPlayerPreLoginEvent e) {
		String pname = e.getName();
		String pUUID = e.getUniqueId().toString();
		String pIP = e.getAddress().getHostAddress();
		PlayerData data = new PlayerData(Utility.getUUIDFromName(pname));

		if(data.changedIP(pIP)) {
			Bukkit.getConsoleSender().sendMessage("§e[§4§lAnti§0-§6§lVPN§e] §5§l" + pname + " §6has changed IP from §a§l" + data.getStoredIP() + "§6 to §c§l" + pIP + "§6 !");
		}

		data.setIP(pIP);
		Bukkit.getConsoleSender().sendMessage("§e[§6§lServerConnections§e] §5§l" + pname + "§6 has requested to log in...");

		if(!e.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED)){
			Bukkit.getConsoleSender().sendMessage("§e[§6§lServerConnections§e] §5§l" + pname + "§4§l couldn't to log in§c with UUID §5§l" + pUUID + "§c and with IP §5§l" + pIP + "§c because of §5§l" + e.getKickMessage() + "§c !");
		} else {
			Bukkit.getConsoleSender().sendMessage("§e[§6§lServerConnections§e] §5§l" + pname + "§2§l successfully logged in§a with UUID §5§l" + pUUID + "§a and with IP §5§l" + pIP + "§a !");
		}
	}
}
