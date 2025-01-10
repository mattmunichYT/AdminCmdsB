package fr.mattmunich.admincmdsb.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;

@SuppressWarnings("NullableProblems")
public class GameModeCommand implements CommandExecutor {

    private final Main main;

    public GameModeCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {

        if (s instanceof BlockCommandSender) {
            s.sendMessage("§4Utilisation de Command Blocks désactivée !");
            return true;
        }

        if(!(s instanceof Player)) {
            s.sendMessage(main.requirePlayerToExcMsg);
            return true;
        }

        Player p = (Player) s;

        if (!(main.superstaff.contains(p))) {
            p.sendMessage(main.noPermissionMsg);
            return true;
        }

        if(args.length < 1 || args.length > 2) {
            p.sendMessage("§cSintax : /gm <GameMode> [Player]");
            return true;
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("creatif") || args[0].equalsIgnoreCase("1")) {
                p.setGameMode(GameMode.CREATIVE);
                return true;
            } else if(args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("survie") || args[0].equalsIgnoreCase("0")) {
                p.setGameMode(GameMode.SURVIVAL);
                return true;
            } else if(args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("aventure") || args[0].equalsIgnoreCase("2")) {
                p.setGameMode(GameMode.ADVENTURE);
                return true;
            } else if(args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("spectateur") || args[0].equalsIgnoreCase("spec") || args[0].equalsIgnoreCase("3")) {
                p.setGameMode(GameMode.SPECTATOR);
                return true;
            } else {
                p.sendMessage(main.getPrefix() + "§4Veuillez entrer un §5mode de jeu §2valide§6 !");
                return true;
            }
        }

        //args.length == 2
        String tName = args[1];

        Player t;

        if (tName.equalsIgnoreCase("@s")) {
            t = p;
        } else if (tName.equalsIgnoreCase("@a")) {
            for (Player all : Bukkit.getOnlinePlayers()) {
                if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("creatif") || args[0].equalsIgnoreCase("1")) {
                    all.setGameMode(GameMode.CREATIVE);
                } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("survie") || args[0].equalsIgnoreCase("0")) {
                    all.setGameMode(GameMode.SURVIVAL);
                } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("aventure") || args[0].equalsIgnoreCase("2")) {
                    all.setGameMode(GameMode.ADVENTURE);
                } else if (args[0].equalsIgnoreCase("stectator") || args[0].equalsIgnoreCase("st") || args[0].equalsIgnoreCase("stectateur") || args[0].equalsIgnoreCase("stec") || args[0].equalsIgnoreCase("3")) {
                    all.setGameMode(GameMode.SPECTATOR);
                } else {
                    p.sendMessage(main.getPrefix() + "§4Veuillez entrer un §5mode de jeu §2valide§6 !");
                    return true;
                }
            }
            return true;
        } else {
            if(Bukkit.getPlayer(tName) == null) {
                p.sendMessage(main.playerNotFoundMsg);
                return true;
            }

            t = Bukkit.getPlayer(tName);
        }

        if(t == null) {
            p.sendMessage(main.playerNotFoundMsg);
            return true;
        }


        if (args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("creatif") || args[0].equalsIgnoreCase("1")) {
            t.setGameMode(GameMode.CREATIVE);
            return true;
        } else if (args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("survie") || args[0].equalsIgnoreCase("0")) {
            t.setGameMode(GameMode.SURVIVAL);
            return true;
        } else if (args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("aventure") || args[0].equalsIgnoreCase("2")) {
            t.setGameMode(GameMode.ADVENTURE);
            return true;
        } else if (args[0].equalsIgnoreCase("stectator") || args[0].equalsIgnoreCase("st") || args[0].equalsIgnoreCase("stectateur") || args[0].equalsIgnoreCase("stec") || args[0].equalsIgnoreCase("3")) {
            t.setGameMode(GameMode.SPECTATOR);
            return true;
        } else {
            p.sendMessage(main.getPrefix() + "§4Veuillez entrer un §5mode de jeu §2valide§6 !");
            return true;
        }

    }
}
