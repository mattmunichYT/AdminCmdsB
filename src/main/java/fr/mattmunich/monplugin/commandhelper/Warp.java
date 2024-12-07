package fr.mattmunich.monplugin.commandhelper;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import fr.mattmunich.monplugin.MonPlugin;

public class Warp {


	private MonPlugin main;
	private FileConfiguration config;
	private File file;

	private final Plugin plugin;

	private Grades grades;

	public Warp(Plugin plugin, MonPlugin main, Grades grades) {
		this.plugin = plugin;
		this.main = main;
		this.grades = grades;
		initConfig();
	}

	public final Plugin getPlugin(){
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
		}catch(IOException ioe) { ioe.printStackTrace();}
	}

	File f = new File("plugins/AdminCmdsB");
	public void initConfig() {
		if(!f.exists()) {
			f.mkdirs();
		}
		file = new File(f,"warps.yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) { e.printStackTrace();}
		}
		new YamlConfiguration();
		config = YamlConfiguration.loadConfiguration(file);
	}

	public void setWarp(String warpName, Player player , String worldName, int x, int y, int z, float pitch, float yaw, int power) {

		getConfig().createSection("warps");
		if(!getConfig().isSet("warp.")) {

			getConfig();
			MemorySection.createPath(getConfig().getConfigurationSection("warps"), "warp");

			getConfig();
			MemorySection.createPath(getConfig().getConfigurationSection("warps"), "warp.list");
		}

		getConfig().set("warp." + warpName + ".world", worldName);
		getConfig().set("warp." + warpName + ".x", x);
		getConfig().set("warp." + warpName + ".y", y);
		getConfig().set("warp." + warpName + ".z", z);
		getConfig().set("warp." + warpName + ".pitch", pitch);
		getConfig().set("warp." + warpName + ".yaw", yaw);
		getConfig().set("warp." + warpName + ".power", power);
		if(getConfig().isSet("warp.count")) {
			getConfig().set("warp.count", getConfig().getInt("warp.count") + 1);
		}else{
			getConfig().set("warp.count", 1);
		}

		if(!getConfig().isSet("warp.list")) {
			getConfig();
			MemorySection.createPath(getConfig().getConfigurationSection("warps"), "warp.list");

			getConfig().set("warp.list", warpName + ",");
			player.sendMessage(main.getPrefix() + "§2Le warp \"§6" + warpName + "§2\" à été défini à votre position !");
			saveConfig();
		}else {
			if(!getConfig().get("warp.list").toString().contains(warpName)) {
				getConfig().set("warp.list", getConfig().get("warp.list") + warpName + ",");
				player.sendMessage(main.getPrefix() + "§2Le warp \"§6" + warpName + "§2\" à été défini à votre position !");
			}else {
				player.sendMessage(main.getPrefix() + "§2Le warp \"§6" + warpName + "§2\" à été redéfini à votre position !");
			}
		}
		saveConfig();
		return;
	}

	public void warp(String warpName, Player player) {
		if(getConfig().contains("warp." + warpName + ".")) {
			World w = null;
			try {
				w = Bukkit.getServer().getWorld(getConfig().getString("warp."  + warpName + ".world"));
			} catch (Exception e) {
				Bukkit.getConsoleSender().sendMessage("Error while warp");
			}
			double x = getConfig().getDouble("warp."  + warpName + ".x");
			double y = getConfig().getDouble("warp."  + warpName + ".y");
			double z = getConfig().getDouble("warp."  + warpName + ".z");
			double pitch = getConfig().getDouble("warp."  + warpName + ".pitch");
			double yaw = getConfig().getDouble("warp."  + warpName + ".yaw");
			int power = getConfig().getInt("warp." + warpName + ".power");

			if(grades.hasPowerInf(player, power)) {
				player.sendMessage(main.getPrefix() + "§4Vous n'avez pas la permission de vous téléporter à ce warp !");
				return;
			}else {
				if(w == null) {
					player.sendMessage(main.getErrorPrefix() + "Une erreur s'est produite lors de la téléportation !");
					return;
				}
				player.teleport(new Location(w, x, y, z, (float) yaw, (float) pitch));
				player.sendMessage(main.getPrefix() + "§2Vous avez été téléporté au warp \"§6" + warpName + "§2\" !");
				return;
			}
		} else {
			player.sendMessage(main.getPrefix() + "§4Le warp \"§6" + warpName + "§4\" n'existe pas !");
			return;
		}

	}

	public void delWarp(String warpName, Player player) {
		if(getConfig().contains("warp."  + warpName + ".")) {
			if(getConfig().getString("warp.list").contains(warpName)) {
				String result = getConfig().getString("warp.list").replace(warpName + ","  , "").toString();

				getConfig().set("warp.list", result);
				saveConfig();
			}else {
				player.sendMessage(main.getErrorPrefix() + "Une erreur s'est produite lors de la suppression du warp. Annulation...");
				System.err.println(main.errorMsg);
				return;
			}
			getConfig().set("warp."  + warpName, null);
			getConfig().set("warp.count", getConfig().getInt("warp.count") - 1);

			player.sendMessage(main.getPrefix() + "§2Le warp \"§6" + warpName + "§2"+ "§2\" a été supprimé !");
			saveConfig();
			return;
		} else {
			player.sendMessage(main.getPrefix() + "§4Le warp \"§6" + warpName + "§4\" n'existe pas !");
			return;
		}
	}

	public void warpListSendMsg(Player player){
		if(getConfig().get("warp.list").toString() != "" || getConfig().get("warp.list").toString() != null) {
			String warps = getConfig().get("warp.list").toString();
			player.sendMessage("§e--------§2§lWarps§e--------\n§2Warp(s) défini(s) : §r\n§l§6" + warps.replace(",", ", "));
			return;
		}else {
			player.sendMessage("§e--------§2§lWarps§e--------\n§8§oAucun warp n'a été défini !\n§8§oUn §4Administrateur §8§o peut en définir un en utilisant la commande /setwarp <warpName>");
			return;
		}
	}
}
