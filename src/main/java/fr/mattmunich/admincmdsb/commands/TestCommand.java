package fr.mattmunich.admincmdsb.commands;

import fr.mattmunich.admincmdsb.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {

    private Main main;

    public TestCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {

        if(s instanceof Player) {
            Player p = (Player)s;

            if(main.admin.contains(p)) {
//                p.transfer("91.197.6.201", 25589);
                p.sendMessage(main.getPrefix() + "§o§8Rien à tester...");
                return true;
            } else {
                p.sendMessage(main.noPermissionMsg);
                return true;
            }
        } else {
            s.sendMessage(main.requirePlayerToExcMsg);
            return true;
        }
    }
}
