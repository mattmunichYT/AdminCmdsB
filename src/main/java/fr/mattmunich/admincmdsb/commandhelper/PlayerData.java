package fr.mattmunich.admincmdsb.commandhelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.UUID;

import fr.mattmunich.admincmdsb.Main;
import fr.mattmunich.admincmdsb.Utility;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


public final class PlayerData {

	private final Plugin plugin;
	private Main main;

	private FileConfiguration config;
	private File file;
	private Player p;


	@SuppressWarnings("unused")
	private UUID uuid;

	public PlayerData(Plugin plugin, Main main) {
		this.plugin = plugin;
		this.main = main;
	}

	private void logError(String message, Exception error){
		main.logError(message, error);
	}

	File f = new File("plugins/AdminCmdsB/PlayerData");
	public PlayerData(Player p){
		if(!f.exists()) {
			f.mkdirs();
		}
		file = new File(f, uuid.toString() + ".yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) { logError("Couldn't create PlayerData file", e); }
		}
		new YamlConfiguration();
		config = YamlConfiguration.loadConfiguration(file);
		this.plugin = getPlugin();
		this.uuid = p.getUniqueId();

		String pName = p.getName();
		config.set("name", pName);
		saveConfig();
	}

	public PlayerData(UUID uuid) throws Exception{
		if(!f.exists()) {
			f.mkdirs();
		}
		file = new File(f, uuid.toString() + ".yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) { logError("Couldn't create PlayerData file", e); }
		}
		new YamlConfiguration();
		config = YamlConfiguration.loadConfiguration(file);
		this.plugin = getPlugin();
		this.p = Bukkit.getPlayer(uuid);
		if(Utility.getNameFromUUID(uuid) == null) {
			throw new Exception("Error while getting player with UUID");
		}

		String pName = Utility.getNameFromUUID(uuid);
		config.set("player.name", pName);
		if(config.getString("player.rank") == null) {
			config.set("player.rank", "membre");
		}
		saveConfig();
	}

	public boolean exist() {
		return file.exists();
	}

	public Plugin getPlugin(){
		return plugin;
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public void setTempbanned(String from, String reason, long duration, String sanction) {
		config.set("tempban.istempbanned", true);
		config.set("tempban.from", from);
		config.set("tempban.reason", reason);
		config.set("tempban.sanction", sanction);
		config.set("tempban.duration", duration + System.currentTimeMillis());
		config.set("tempban.end", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(duration + System.currentTimeMillis()));
		config.set("tempban.timestamp", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(duration));

		saveConfig();
	}

	public void setUnTempbanned() {
		config.set("tempban", null);
		saveConfig();
	}

	public String getTempbannedReason() {
		return config.getString("tempban.reason");
	}
	public String getTempbannedFrom() {
		return config.getString("tempban.from");
	}

	public String getTempbanSanction() {
		return config.getString("tempban.sanction");
	}

	public long getTempbanMilliseconds() {
		return config.getLong("tempban.duration");
	}

	public String getTempbanEnd() {
		return config.getString("tempban.end");
	}

	public String getTempbanTimestamp() {
		return config.getString("tempban.timestamp");
	}

	public boolean isTempbanned() {
		return config.contains("tempban");
	}
	public void setTempmuted(String from, String reason, long time, String sanction) {
		config.set("tempmute.isTempmuted", true);
		config.set("tempmute.from", from);
		config.set("tempmute.reason", reason);
		config.set("tempmute.sanction", sanction);
		config.set("tempmute.duration", time + System.currentTimeMillis());
		config.set("tempmute.end", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(time + System.currentTimeMillis()));
		config.set("tempmute.timestamp", new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(time));

		saveConfig();
	}

	public void setUnTempmuted() {
		config.set("tempmute", null);
		saveConfig();
	}

	public String getTempmutedReason() {
		return config.getString("tempmute.reason");
	}
	public String getTempmutedFrom() {
		return config.getString("tempmute.from");
	}

	public String getTempmuteSanction() {
		return config.getString("tempmute.sanction");
	}

	public long getTempmuteMilliseconds() {
		return config.getLong("tempmute.duration");
	}

	public String getTempmuteEnd() {
		return config.getString("tempmute.end");
	}

	public String getTempmuteTimestamp() {
		return config.getString("tempmute.timestamp");
	}

	public boolean isTempmuted() {
		return config.contains("tempmute");
	}


	public void setBanned(String from, String reason) {
		config.set("ban.isbanned", true);
		config.set("ban.from", from);
		config.set("ban.reason", reason);
		saveConfig();
	}

	public void setUnBanned() {
		config.set("ban", null);
		saveConfig();
	}

	public String getBannedReason() {
		return config.getString("ban.reason");
	}
	public String getBannedFrom() {
		return config.getString("ban.from");
	}

	public boolean isBanned() {
		return config.contains("ban");
	}

	public boolean isMuted() {
		return getConfig().getBoolean("muted");
	}

	public void setMuted(boolean muted) {
		config.set("muted", muted);
		saveConfig();
	}

	public void setASName(String name) {
		config.set("armorstand.name", name);
		saveConfig();
	}

	public String getASName() {
		return config.getString("armorstand.name");
	}

	public void saveConfig() {
		try {
			getConfig().save(file);
		}catch(IOException ioe) { ioe.printStackTrace();}
	}

	public boolean hasHomes(){
		if(getConfig().getString("home.list") != null) {
			return true;
		}else {
			return false;
		}
	}

	public String getHomes(){
		if(getConfig().getString("home.list") != null) {
			String homes = getConfig().get("home.list").toString();
			return homes;
		} else {
			return "";
		}
	}

	public void setIP(String ip) {
		config.set("ip", ip);
		saveConfig();
	}

	public String getStoredIP() {
		return config.getString("ip");
	}

	public boolean changedIP(String newIP) {
		if(config.get("ip") == null) {
			return false;
		} else return !Objects.equals(config.getString("ip"), newIP);
	}
}
