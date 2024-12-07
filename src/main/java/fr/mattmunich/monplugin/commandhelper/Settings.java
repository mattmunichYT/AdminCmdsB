package fr.mattmunich.monplugin.commandhelper;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class Settings {

	private FileConfiguration config;
	private File file;

	private final Plugin plugin;

	public Settings(Plugin plugin) {
		this.plugin = plugin;
		initConfig();
	}

	public final Plugin getPlugin() {
		return plugin;
	}

	public boolean exist() {
		return file.exists();
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public void saveConfig() {
		try {
			config.save(file);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	File f = new File("plugins/AdminCmdsB");

	@SuppressWarnings("static-access")
	public void initConfig() {
		if (!f.exists()) {
			f.mkdirs();
		}
		file = new File(f, "settings.yml");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		new YamlConfiguration();
		config = YamlConfiguration.loadConfiguration(file);

	}

	public void setServerName(String serverName) {
		config.set("settings.servername", serverName);
		saveConfig();
	}

	public String getServerName() {
		if (config.contains("settings.servername")) {
			return config.getString("settings.servername");
		} else {
			return "";
		}
	}

	public void CTabListTrue() {
		config.set("settings.ctablist", true);
		saveConfig();
	}

	public void CTabListFalse() {
		config.set("settings.ctablist", false);
		saveConfig();
	}

	public boolean getCTabList() {
		return config.getBoolean("settings.ctablist");
	}

	public void coMsgTrue() {
		config.set("settings.comsg", true);
		saveConfig();
	}

	public void coMsgFalse() {
		config.set("settings.comsg", false);
		saveConfig();
	}

	public boolean getCoMsg() {
		return config.getBoolean("settings.comsg");
	}

	public void setOldPVP(boolean result) {
		config.set("setting.oldpvp", result);
		saveConfig();
	}

	public boolean getOldPVP() {
		return config.getBoolean("settings.oldpvp");
	}

	public void TNTsEnabledTrue() {
		config.set("settings.tntsenabled", true);
		saveConfig();
	}

	public void TNTsEnabledFalse() {
		config.set("settings.tntsenabled", false);
		saveConfig();
	}

	public boolean getTNTsEnabled() {
		return config.getBoolean("settings.tntsenabled");
	}

	public void seeVanishedTrue() {
		config.set("settings.seevanished", true);
		saveConfig();
	}

	public void seeVanishedFalse() {
		config.set("settings.seevanished", false);
		saveConfig();
	}

	public boolean getSeeVanished() {
		return config.getBoolean("settings.seevanished");
	}

	public void advancedNameTagsTrue() {
		config.set("settings.advancednametags", true);
		saveConfig();
	}

	public void advancedNameTagsFalse() {
		config.set("settings.advancednametags", false);
		saveConfig();
	}

	public boolean getAdvancedNameTags() {
		return config.getBoolean("settings.advancednametags");
	}

	public void maintenanceTrue() {
		config.set("settings.maintenance", true);
	}

	public void maintenanceFalse() {
		config.set("settings.maintenance", false);
	}

	public boolean getMaintenance() {
		return config.getBoolean("settings.maintenance");
	}

	public void antiCheatTrue() {
		config.set("settings.anticheat", true);
	}

	public void antiCheatFalse() {
		config.set("settings.anticheat", false);
	}

	public boolean getAntiCheat() {
		return config.getBoolean("settings.anticheat");
	}

}
