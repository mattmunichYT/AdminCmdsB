package fr.mattmunich.monplugin.commandhelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public final class Events {

	private final Plugin plugin;

	public Events(Plugin plugin) {
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

		File f = new File("plugins/AdminCmdsB");
		if (!f.exists()) {
			f.mkdirs();
		}
		file = new File(f, "events.yml");
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

	public void setEvent(long startMillis, long time, String eventName, String description) {

		getConfig().createSection("eventConfig");
		if (!getConfig().isSet("events.") || !getConfig().isSet("list.")) {

			MemorySection.createPath(getConfig().getConfigurationSection("eventConfig"), "events");
			MemorySection.createPath(getConfig().getConfigurationSection("eventConfig"), "list");
		}

		config.set("events." + eventName + ".canceled", false);
		config.set("events." + eventName + ".hasended", false);
		config.set("events." + eventName + ".description", description);
		config.set("events." + eventName + ".duration", time);
		config.set("events." + eventName + ".start", startMillis);
		config.set("events." + eventName + ".end", startMillis + time);
		config.set("events." + eventName + ".mingradeid", 0);
		config.set("events." + eventName + ".tp", "noTP");
		// Add event to list !
		try {
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) config.getList("events.list");
			list.add(eventName.toString());
			config.set("events.list", list);
		} catch (NullPointerException e) {
			List<String> list = new ArrayList<>();
			list.add(eventName.toString());
			config.set("events.list", list);
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("§4Got error at creating : §c" + e);
			return;
		}

		saveConfig();
	}

	public void setCanceled(String eventName, boolean bool) {
		config.set("events." + eventName + ".canceled", bool);
		saveConfig();
	}

	public void setTP(String eventName, String warpName) {
		config.set("events." + eventName + ".tp", warpName);
		saveConfig();
	}

	public void setMinGradeId(String eventName, int id) {
		config.set("events." + eventName + ".mingradeid", id);
		saveConfig();
	}

	public void toggleCanceled(String eventName) {
		if (exist(eventName)) {
			if (config.getBoolean("events." + eventName + ".canceled")) {
				setCanceled(eventName, false);
				return;
			} else {
				setCanceled(eventName, true);
				return;
			}
		} else {
			return;
		}
	}

	public void delEvent(String eventName) {
		try {
			config.set("events." + eventName, null);
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) config.getList("events.list");
			list.remove(eventName);
			config.set("events.list", list);
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("§4Got error at delEvent : §c" + e);
			return;
		}

		saveConfig();
	}

	public boolean exist(String eventName) {
		return config.contains("events." + eventName);
	}

	public void setDescription(String eventName, String desc) {
		config.set("events." + eventName + ".description", desc);
		saveConfig();
	}

	public void setName(String oldName, String newName) {

		getConfig().createSection("eventConfig");
		if (!getConfig().isSet("events.") || !getConfig().isSet("events.list")) {

			MemorySection.createPath(getConfig().getConfigurationSection("eventConfig"), "events");
			MemorySection.createPath(getConfig().getConfigurationSection("eventConfig"), "events.list");
		}

		config.set("events." + newName + ".canceled", config.getBoolean("events." + oldName + ".canceled"));
		config.set("events." + newName + ".hasended", config.getBoolean("events." + oldName + ".hasended"));
		config.set("events." + newName + ".description", config.getString("events." + oldName + ".description"));
		config.set("events." + newName + ".duration", config.getLong("events." + oldName + ".duration"));
		config.set("events." + newName + ".start", config.getLong("events." + oldName + ".start"));
		config.set("events." + newName + ".end", config.getLong("events." + oldName + ".end"));
		config.set("events." + newName + ".mingradeid", config.getInt("events." + oldName + ".mingradeid"));
		config.set("events." + newName + ".tp", config.getString("events." + oldName + ".tp"));
		// Add event to list !
		try {
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) config.getList("events.list");
			list.add(newName.toString());
			config.set("events.list", list);
		} catch (NullPointerException e) {
			List<String> list = new ArrayList<>();
			list.add(newName.toString());
			config.set("events.list", list);
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("§4Got error at creating : §c" + e);
			return;
		}

		delEvent(oldName);
		saveConfig();
	}

	public void setDuration(String eventName, long time) {
		config.set("events." + eventName + ".duration", time);
		saveConfig();
	}

	public void setStartDate(String eventName, long startMillis) {

		config.set("events." + eventName + ".start", startMillis);
		saveConfig();
	}

	public String end(String eventName) {
		if (!exist(eventName) && !isNow(eventName)) {
			return "notNow";
		}
		config.set("events." + eventName + ".hasended", true);
		return "eventEnded";
	}

	public String getDescription(String eventName) {
		if (!exist(eventName)) {
			return "unknown";
		}
		return config.getString("events." + eventName + ".description");
	}

	public int getMinGradeId(String eventName) {
		if (!exist(eventName)) {
			return 0;
		}
		return config.getInt("events." + eventName + ".mingradeid");
	}

	public boolean isTPDefined(String eventName) {
		if (!exist(eventName)) {
			return false;
		}
		String tp = config.getString("events." + eventName + ".tp");
		if (tp.equalsIgnoreCase("noTP")) {
			return false;
		} else {
			return true;
		}
	}

	public String getTP(String eventName) {
		if (!exist(eventName)) {
			return "noTP";
		}
		return config.getString("events." + eventName + ".tp");
	}

	public String getStart(String eventName) {
		if (!exist(eventName)) {
			return "unknown";
		}
		return config.getString("events." + eventName + ".start");
	}

	public String getStartDate(String eventName) {
		if (!exist(eventName)) {
			return "unknown";
		}
		int millis = config.getInt("events." + eventName + ".start");

		String start = new SimpleDateFormat("dd MMM yyyy - HH:mm").format(System.currentTimeMillis() + millis);

		return start;
	}

	public long getMilliseconds(String eventName) {
		if (!exist(eventName)) {
			return 0;
		}
		return config.getLong("events." + eventName + ".duration");
	}

	public String getEnd(String eventName) {
		if (!exist(eventName)) {
			return "unknown";
		}
		return config.getString("events." + eventName + ".end");
	}

	public String getEventList() {
		try {
			@SuppressWarnings("unchecked")
			List<String> eventList = (List<String>) config.getList("events.list");
			String events = eventList.toString();
			return events.replace("[", "").replace("]", "");
		} catch (NullPointerException e) {
			return null;
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("§4Got error at getEventList : " + e);
			return "§c§oErreur de chargement";
		}
	}

	public boolean isCanceled(String eventName) {
		if (!exist(eventName)) {
			return false;
		}
		return config.getBoolean("events." + eventName + ".canceled");
	}

	public boolean hasEnded(String eventName) {
		if (!exist(eventName)) {
			return false;
		}
		return config.getBoolean("events." + eventName + ".hasended");
	}

	public boolean isNow(String eventName) {
		if (!exist(eventName)) {
			return false;
		}
		long millisStart = config.getLong("events." + eventName + ".start");
		long millisEnd = config.getLong("events." + eventName + ".end");
		long current = System.currentTimeMillis();
		boolean hasEnded = config.getBoolean("events." + eventName + ".hasended");

		if (millisStart < current && millisEnd > current && !hasEnded) {
			return true;
		} else {
			return false;
		}
	}

	public void eventListSendMsg(CommandSender s) {
		String events = getEventList();
		if (events != null && !events.isEmpty()) {
			s.sendMessage("§2--------§6§lEvents§2--------\n§2Événement(s) planinifié(s) : §r\n§l§6" + events);
        } else {
			s.sendMessage("§2--------§6§lEvents§2--------\n§8§oAucun événement n'est planifié !");
        }
        return;
    }

}
