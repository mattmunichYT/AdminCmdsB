package fr.mattmunich.monplugin.commandhelper;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import fr.mattmunich.monplugin.Utility;



public final class ASData_OLD {

    private final Plugin plugin;

    private FileConfiguration config;
    private File file;

    public ASData_OLD(Plugin plugin) {
        this.plugin = plugin;
    }

    File f = new File("plugins/AdminCmdsB/Data");
    @SuppressWarnings("static-access")
    public ASData_OLD(UUID uuid){
        if(!f.exists()) {
            f.mkdirs();
        }
        file = new File(f, uuid.toString() + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) { e.printStackTrace();}
        }
        new YamlConfiguration();
        config = YamlConfiguration.loadConfiguration(file);
        this.plugin = getPlugin();
        try {
            if(Utility.getASNameFromUUID(uuid) == null) {
                Bukkit.getConsoleSender().sendMessage("Error while getting  with UUID");
                return;
            }
        } catch (NullPointerException e) {
            Bukkit.getConsoleSender().sendMessage("Error while getting  with UUID, NullPointerException");
            return;
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("Error while getting  with UUID");
            e.printStackTrace();
            return;
        }

        String name = Utility.getASNameFromUUID(uuid);
        config.set("asname", name);
        saveConfig();
    }

    public boolean exist() {
        return file.exists();
    }

    public final Plugin getPlugin(){
        return plugin;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void setASName(String name) {
        config.set("armorstand.name", name);
        saveConfig();
    }

    public void resetASName() {
        config.set("armorstand", null);
        saveConfig();
    }

    public String getASName() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("armorstand.name"));
    }

    public void saveConfig() {
        try {
            getConfig().save(file);
        }catch(IOException ioe) { ioe.printStackTrace();}
    }

}