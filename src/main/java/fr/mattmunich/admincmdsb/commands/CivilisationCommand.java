package fr.mattmunich.admincmdsb.commands;

import fr.mattmunich.admincmdsb.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CivilisationCommand implements CommandExecutor {
    private Main main;

    public CivilisationCommand(Main main) {
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {
        if (!(s instanceof Player)) {
            s.sendMessage(main.requirePlayerToExcMsg);
            return true;
        }

        Player p = (Player) s;
        if (main.admin.contains(p) || main.superstaff.contains(p)) {
            p.sendMessage(main.getPrefix() + "§2Transfert en cours...");
            p.transfer("91.197.6.162", 25624);
            return true;
        } else {
            p.sendMessage(main.getPrefix() + "§8§oComming soon...");
            return true;
        }
    }
}
