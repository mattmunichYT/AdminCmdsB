package fr.mattmunich.admincmdsb.commandhelper;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class Ban {

	private final Plugin plugin;

	public Ban(Plugin plugin) {
		this.plugin = plugin;
		initConfig();
	}

	public final Plugin getPlugin() {
		return plugin;
	}

	private FileConfiguration config;
	private File file;

	public FileConfiguration getConfig() {
		return config;
	}

	public void initConfig() {

		File f = new File("plugins/AdminCmdsB/PlayerData");
		if (!f.exists()) {
			f.mkdirs();
		}
		file = new File(f, "bans.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		config = YamlConfiguration.loadConfiguration(file);

	}

	public void saveConfig() {
		try {
			config.save(file);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void setBanned(Player player, Boolean bool) {
		config.set(player.getUniqueId() + ".isBanned", bool);
		saveConfig();

	}

	public void setReason(Player player, String reason) {
		getConfig().set(player.getUniqueId() + ".reason.", reason);
		saveConfig();
	}

	public void kickPlayer(Player player, String reason) {
		player.kickPlayer("§6§lServerBan§r\n\n§4Vous avez été kick du serveur§r\n§4Raison : §6"
				+ (reason.isEmpty() ? "§c§oAucune" : reason.toString()));
		Bukkit.broadcastMessage(
				"§e[§6Bans§e] §6 " + player.getName().toString() + " §4a été kick par un membre du §2Staff "
						+ (reason.isEmpty() ? "!" : ", §4raison : §6" + reason.toString()));

	}

	public void kickAll(String sender, String reason) {
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (!(all.getName() == sender)) {
				all.kickPlayer("§6§lServerBan§r\n\n§4Vous avez été KickAll du serveur§r\n§4Raison : §6"
						+ (reason.isEmpty() ? "§c§oAucune" : reason.toString()));
			}
		}
	}

	public void kickAllClassic(String sender, String reason) {
		for (Player all : Bukkit.getOnlinePlayers()) {
			if (!(all.getName() == sender)) {
				all.kickPlayer((reason.isEmpty() ? "Timed Out" : reason.toString()));
			}
		}
	}

	public void systemKickPlayer(Player player, String reason) {
		player.kickPlayer("§6§lServerBan§r\n\n§4Vous avez été kick du serveur par le §osystème§r\n§4Raison : §6"
				+ (reason.isEmpty() ? "§c§oAucune" : reason.toString()));
		Bukkit.broadcastMessage("§e[§6Bans§e] §6 " + player.getName().toString() + " §4a été kick par le §osystème "
				+ (reason.isEmpty() ? "!" : ", §4raison : §6" + reason.toString()));

	}

	public boolean isBanned(Player player) {

		if (getConfig().getBoolean(player.getUniqueId() + ".isBanned.")) {

			return true;
		} else if (!getConfig().getBoolean(player.getUniqueId() + ".isBanned.")) {
			return false;
		} else {
			return false;
		}

	}

}
