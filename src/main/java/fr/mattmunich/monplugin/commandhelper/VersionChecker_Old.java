package fr.mattmunich.monplugin.commandhelper;

import org.bukkit.Bukkit;

import java.util.regex.PatternSyntaxException;

public class VersionChecker_Old {
    public int getVersion() {
        String serverPackage = null;
        try {
            serverPackage = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        } catch (Exception e) {
            try {
                serverPackage = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[2];
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        try {
            int version = 0;
           try {
               version = Integer.parseInt(serverPackage.split("_")[1]);
           } catch (ArrayIndexOutOfBoundsException ex) {
               version = Integer.parseInt(serverPackage.split("_")[0]);
           }
           Bukkit.getConsoleSender().sendMessage("§a[AdminCmdsB] : Detected server version §2" + version);
           return version;
        }catch (NumberFormatException | PatternSyntaxException e){
            Bukkit.getConsoleSender().sendMessage("§c--------------------------------------------------------------------");
            Bukkit.getConsoleSender().sendMessage("§6§l[AdminCmdsB] : §4Couldn't get server version !");
            Bukkit.getConsoleSender().sendMessage("§6§l[AdminCmdsB] : §4Some features might not work.");
            Bukkit.getConsoleSender().sendMessage("§e------------------------------------------");
            Bukkit.getConsoleSender().sendMessage("§6§l[AdminCmdsB] : §4La version du server n'a pas pu être obtenue !");
            Bukkit.getConsoleSender().sendMessage("§6§l[AdminCmdsB] : §4Certaines fonctionnalitées pourraient ne pas fonctionner.");
            Bukkit.getConsoleSender().sendMessage("§c--------------------------------------------------------------------");
            return 0;
        }
    }
}
