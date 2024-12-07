package fr.mattmunich.monplugin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Utility {

	@SuppressWarnings("deprecation")
	public static UUID getUUIDFromName(String name) {

		Player player = Bukkit.getPlayer(name);

		if(player != null) {
			return player.getUniqueId();
		} else {

			OfflinePlayer oplayer = Bukkit.getOfflinePlayer(name);

			if(oplayer != null) {
				return oplayer.getUniqueId();
			}
		}

		return null;
	}

public static UUID getUUIDFromASName(String ASname) {

	for(Entity e : Bukkit.getWorld("world").getEntitiesByClass(ArmorStand.class)) {
		try {
			if(e != null) {
				if(e.getCustomName().equalsIgnoreCase(ASname)) {
					return e.getUniqueId();
				}
			}
		} catch (NullPointerException nullE) {
			return null;
		}


	}

	return null;
	}

	public static String getNameFromUUID(UUID UUID) {

		Player player = Bukkit.getPlayer(UUID);

		if(player != null) {
			return player.getName();
		} else {

			OfflinePlayer oplayer = Bukkit.getOfflinePlayer(UUID);

			if(oplayer != null) {
				return oplayer.getName();
			}
		}

		return "#://error";
	}

	public static String getASNameFromUUID(UUID UUID) {

		for(Entity e : Bukkit.getWorld("world").getEntitiesByClass(ArmorStand.class)) {
			if(e != null) {
				if(e.getUniqueId().equals(UUID)) {
					return e.getName();
				}
			}

		}



		return null;
	}

}
