package fr.mattmunich.monplugin.commandhelper;

import fr.mattmunich.monplugin.MonPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.*;


public final class ASData {

	private final Plugin plugin;

	private MonPlugin main;

	public ASData(Plugin plugin, MonPlugin main) {
		this.plugin = plugin;
		this.main = main;
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

		File f = new File("plugins/AdminCmdsB/");
		if (!f.exists()) {
			f.mkdirs();
		}
		file = new File(f, "asdata.yml");
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

	public void registerArmorStand(ArmorStand armorStand) {
		String uuid = armorStand.getUniqueId().toString();
		String name = armorStand.getName();

		if(config.get("as." + uuid + ".name") != null) {
			return;
		}

		List<String> actions = new ArrayList<>();
		actions.add("!ignore-this");

		config.set("as." + uuid + ".name",name);
		config.set("as." + uuid + ".actions",null);
		config.set("as." + uuid + ".actions",actions);
		saveConfig();
	}

	public void changeName(ArmorStand armorStand, String newName) {
		String uuid = armorStand.getUniqueId().toString();

		if(config.getString("as." + uuid + ".name") !=null) {
			config.set("as." + uuid + ".name",newName);
			saveConfig();
			armorStand.setCustomName(newName);
			armorStand.setCustomNameVisible(true);
		}
	}

	public @Nullable ArmorStand getArmorStand(UUID uuid) {
		return (ArmorStand) Bukkit.getEntity(uuid);
	}

	public @Nullable ArmorStand getArmorStand(String name) {

		for(String key : config.getConfigurationSection("as").getKeys(false)) {
			if(Objects.equals(config.get("as" + key + "name"), name)) {
				return (ArmorStand) Bukkit.getEntity(UUID.fromString(key));
			}
		}
		return null;
	}

	public void addCommandAction(ArmorStand as, String command) {
		String uuid = as.getUniqueId().toString();
		if(config.get("as." + uuid + ".actions") == null) {
			return;
		}
		command = "!cmd" + command;
		List<String> actions = config.getStringList("as." + uuid + ".actions");
		actions.add(command);
		config.set("as." + uuid + ".actions", actions);
		saveConfig();
	}

	public void addMessageAction(ArmorStand as, String message) {
		String uuid = as.getUniqueId().toString();
		if(config.get("as." + uuid + ".actions") == null) {
			return;
		}

		List<String> actions = config.getStringList("as." + uuid + ".actions");
		actions.add(message);
		config.set("as." + uuid + ".actions", actions);
		saveConfig();
	}

	public void addTitleAction(ArmorStand as, String title, String subtitle) {
		String uuid = as.getUniqueId().toString();
		if(config.get("as." + uuid + ".actions") == null) {
			return;
		}
		String content = "!title;title=" + title + ";subtitle=" + subtitle + ";";
		List<String> actions = config.getStringList("as." + uuid + ".actions");
		actions.add(content);
		config.set("as." + uuid + ".actions", actions);
		saveConfig();
	}

	public void addAction(ArmorStand as, String action) {
		String uuid = as.getUniqueId().toString();
		if(config.get("as." + uuid + ".actions") == null) {
			return;
		}
		List<String> actions = config.getStringList("as." + uuid + ".actions");
		actions.add(action);
		config.set("as." + uuid + ".actions", actions);
		saveConfig();
	}

	public void addActionbarAction(ArmorStand as, String content) {
		String uuid = as.getUniqueId().toString();
		if(config.get("as." + uuid + ".actions") == null) {
			return;
		}
		content = "!actionbar" + content;
		List<String> actions = config.getStringList("as." + uuid + ".actions");
		actions.add(content);
		config.set("as." + uuid + ".actions", actions);
		saveConfig();
	}

	public void addTPAction(ArmorStand as, double x, double y, double z, World world) {
		String uuid = as.getUniqueId().toString();
		if(config.get("as." + uuid + ".actions") == null) {
			return;
		}

		String content = "!tp;x=" + x + ";y=" + y + ";z=" + z + ";worldName=" + world.getName() + ";";
		List<String> actions = config.getStringList("as." + uuid + ".actions");
		actions.add(content);
		config.set("as." + uuid + ".actions", actions);
		saveConfig();
	}

	public @Nullable List<String> getAction(ArmorStand as) {
		String uuid = as.getUniqueId().toString();
		if(config.get("as." + uuid + ".actions") == null) {
			return null;
		}

		return config.getStringList("as." + uuid + ".actions");
	}

	public void runActions(ArmorStand as, Player target) {
		List<String> actions = getAction(as);

        if(actions == null) {
			return;
		}

		try {
			if(actions.contains("!minigame:closed")) {
				target.sendTitle("§4§l❌ Fermé", "§r§cLe mini-jeu §4" + as.getName() + "§c n'est pas ouvert au public.", 20, 60, 20);
				target.sendMessage(main.getPrefix() + "§2Mini-jeu fermé/non trouvé !");
				return;
			}
			for (String action : actions) {
				if(action.startsWith("!cmd/")) {
					target.chat(action.replaceFirst("!cmd",""));
					continue;
				} else if (action.startsWith("!title")){
					String[] parts = action.split(";");
					String title = "";
					String subtitle = "";

					// Loop through the parts to find the desired values
					for (String part : parts) {
						if (part.startsWith("title=")) {
							title = part.substring(6); // Extract after "title="
						} else if (part.startsWith("subtitle=")) {
							subtitle = part.substring(9); // Extract after "subtitle="
						}
					}
					title = main.hex(title);
					subtitle = main.hex(subtitle);
					target.sendTitle(title,subtitle,20,60,20);
					continue;
				} else if (action.startsWith("!actionbar")) {
					String message = action.replace("!actionbar","");
					target.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder().append(main.hex(message)).build());
					continue;
				} else if (action.startsWith("!tp")) {
					String[] parts = action.split(";");
					double x = 0.0;
					double y = 0.0;
					double z = 0.0;
					String worldName = "";

					// Loop through the parts to find the desired values
					for (String part : parts) {
						if (part.startsWith("x=")) {
							x = Double.parseDouble(part.substring(2));
						} else if (part.startsWith("y=")) {
							y = Double.parseDouble(part.substring(2));
						} else if (part.startsWith("z=")) {
							z = Double.parseDouble(part.substring(2));
						} else if (part.startsWith("worldName=")) {
							worldName = part.substring(10);

						}
					}
					World world = Bukkit.getWorld(worldName);
					if(world==null) {
						world = target.getWorld();
					}
					Location loc = new Location(world,x,y,z);
					target.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
				} else if (action.equals("!ignore-this")) {
					continue;
				} else {
					action = action.replace("<prefix>", main.getPrefix());
					action = main.hex(action);
					target.sendMessage(action);
					continue;
				}
				return;
			}
		} catch (Exception e) {
			target.sendMessage(main.errorPrefix + "Une erreur s'est produite lors de l'éxécution d'une des actions.");
			Bukkit.getConsoleSender().sendMessage(main.prefix + "§4Couldn't run an action when clicking on armorstand : §r\n" + e + Arrays.toString(e.getStackTrace()).replace(",", ",\n"));
		}
	}

	public boolean isRegistred(ArmorStand as) {
		String uuid = as.getUniqueId().toString();
		if(config.get("as." + uuid) != null) {
			return true;
		}
		return false;
	}

	public boolean unregister(ArmorStand as) {
		String uuid = as.getUniqueId().toString();
		if(config.get("as." + uuid + ".name") == null) {
			return false;
		}

		config.set("as." + uuid, null);
		saveConfig();
		return true;
	}
}
