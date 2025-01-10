package fr.mattmunich.admincmdsb.commands;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;

public class MuteChatCommand implements CommandExecutor {

    private Main main;

    public MuteChatCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {

        if (s instanceof BlockCommandSender) {
            s.sendMessage("§4Utilisation de Command Blocks désactivée !");
            return true;
        }

        if (s instanceof Player p) {
            if (!main.admin.contains(p)) {
                p.sendMessage(main.getPrefix() + "§4Vous n'avez pas la permission d'utiliser cette commande !");
                return true;
            }
        }


        if (args.length > 1) {
            s.sendMessage("§cSintax : /chat [on/off]");
            if (main.chatMuted) {
                s.sendMessage(main.getPrefix() + "§6Statut du chat : §4désactivé");
            } else {
                s.sendMessage(main.getPrefix() + "§6Statut du chat : §2activé");
            }
            return true;
        }
        if (args.length == 0) {
            if (main.chatMuted) {
                main.chatMuted = false;
                s.sendMessage(main.getPrefix() + "§2Le chat a été réactivé !");
                return true;
            } else {
                main.chatMuted = true;
                s.sendMessage(main.getPrefix() + "§2Le chat a été désactivé !");
                return true;
            }
        } else {
            if (args[0].equalsIgnoreCase("on")) {
                if (main.chatMuted) {
                    main.chatMuted = false;
                    s.sendMessage(main.getPrefix() + "§2Le chat a été réactivé !");
                    return true;
                } else {
                    s.sendMessage(main.getPrefix() + "§4Le chat est déjà activé !");
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("off")) {
                if (main.chatMuted) {
                    s.sendMessage(main.getPrefix() + "§4Le chat est déjà désactivé !");
                    return true;
                } else {
                    main.chatMuted = true;
                    s.sendMessage(main.getPrefix() + "§2Le chat a été désactivé !");
                    return true;
                }
            } else {
                s.sendMessage("§cSintax : /chat [on/off]");
                if (main.chatMuted) {
                    s.sendMessage(main.getPrefix() + "§6Statut du chat : §4désactivé");
                    return true;
                } else {
                    s.sendMessage(main.getPrefix() + "§6Statut du chat : §2activé");
                    return true;
                }
            }
        }
    }
}
