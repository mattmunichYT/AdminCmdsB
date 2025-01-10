package fr.mattmunich.admincmdsb.commands;

import fr.mattmunich.admincmdsb.Main;
import fr.mattmunich.admincmdsb.commandhelper.Backup;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackupCommand implements CommandExecutor {

    private final Main main;
    private final Backup backup;
    public BackupCommand(Main main, Backup backup) { this.main = main; this.backup = backup;}

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {
        if(s instanceof Player p) {
            if(!main.staff.contains(p)) {
                p.sendMessage(main.noPermissionMsg);
                return true;
            }
        }
        s.sendMessage(main.getPrefix() + "§a§oBackup en cours...");
        backup.run();
        s.sendMessage(main.getPrefix() + "§2Les mondes ont été sauvegardés avec succès !");
        if(s instanceof Player p) {
            Bukkit.getConsoleSender().sendMessage(main.prefix + "§6" + p.getName() + "§e a créé un §6backup manuel§e !");
        }
        return true;
    }
}
