package fr.mattmunich.monplugin;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPreLoginEvent;

import fr.mattmunich.monplugin.commandhelper.PlayerData;

@SuppressWarnings("deprecation")
public class PreLoginEvent implements Listener{
	@EventHandler
	public void preLoginEvent(PlayerPreLoginEvent e) {
		String pname = e.getName();
		String pUUID = e.getUniqueId().toString();
		String pIP = e.getAddress().getHostAddress();
		PlayerData data = new PlayerData(Utility.getUUIDFromName(pname));

		if(data.changedIP(pIP)) {
			Bukkit.getConsoleSender().sendMessage("§e[§4§lAnti§0-§6§lVPN§e] §5§l" + pname + " §6has changed IP from §a§l" + data.getStoredIP() + "§6 to §c§l" + pIP + "§6 !");
		} else {
			data.setIP(pIP);
		}

		Bukkit.getConsoleSender().sendMessage("§e[§6§lServerConnections§e] §5§l" + pname + "§6 logged in with UUID §5§l" + pUUID + "§6 and with IP §5§l" + pIP + " !");


	}
}
