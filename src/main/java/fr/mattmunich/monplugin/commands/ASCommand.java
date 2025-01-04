package fr.mattmunich.monplugin.commands;

import com.google.common.collect.Lists;
import fr.mattmunich.monplugin.MonPlugin;
import fr.mattmunich.monplugin.commandhelper.ASData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class ASCommand implements CommandExecutor, TabCompleter {

    private final MonPlugin main;
    private final ASData asData;

    public ASCommand(MonPlugin main, ASData asData) {
        this.main = main;
        this.asData = asData;
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {
        if(s instanceof BlockCommandSender) {
            s.sendMessage("§4Utilisation de Command Blocks désactivée !");
            return true;
        }

        if(!(s instanceof Player p)) {
            s.sendMessage(main.requirePlayerToExcMsg);
            return true;
        }

        if(!main.staff.contains(p)) {
            p.sendMessage(main.noPermissionMsg);
            return  true;
        }

        if(args.length==0) {
            p.sendMessage(main.prefix + "§cSintax : /armorstand <spawn/addaction/setname> [actionType/name] [actionParam.]");
            return true;
        }

        if (args[0].equalsIgnoreCase("setname")) {
            if(args.length<2) {
                p.sendMessage(main.prefix + "§4Veuillez entrer un nom valide !");
                return true;
            }

            Location ploc = p.getLocation();
            for(ArmorStand as : p.getWorld().getEntitiesByClass(ArmorStand.class)) {
                if(as == null) {
//                    p.sendMessage(main.getErrorPrefix() + "Impossible de charger un ArmorStand ! -> AS ignoré.");
                    continue;
                }
                if(as.getLocation().distance(ploc) < 2) {
                    if(!asData.isRegistred(as)) {
                        asData.registerArmorStand(as);
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i]);
                        sb.append(" ");
                    }

                    String name = sb.toString();
                    name = main.hex(name);
                    asData.changeName(as,name);
                    p.sendMessage(main.prefix + "§2Le nom de l'ArmorStand à proximité de vous a été défini à §6" + name + "§2 !");
                    return true;
                }
            }
            p.sendMessage(main.getPrefix() + "§4Aucun Armor Stand détecté proche de vous !");
            return true;
        } else if (args[0].equalsIgnoreCase("addaction")) {
            if(args.length<3) {
                p.sendMessage(main.prefix + "§cSintax : /armorstand addaction <actionType> <actionParam.>");
                return true;
            }

            String actionType = args[1];
            StringBuilder sb = new StringBuilder();
            for (int i = 2; i < args.length; i++) {
                sb.append(args[i]);
                sb.append(" ");
            }

            String action = sb.toString();
            Location ploc = p.getLocation();

            for(ArmorStand as : p.getWorld().getEntitiesByClass(ArmorStand.class)) {
                if (as == null) {
//                    p.sendMessage(main.getErrorPrefix() + "Impossible de charger un ArmorStand ! -> AS ignoré.");
                    continue;
                }
                if(as.getLocation().distance(ploc) < 2) {
                    if(!asData.isRegistred(as)) {
                        asData.registerArmorStand(as);
                    }

                    if (actionType.equalsIgnoreCase("message")) {
                        asData.addMessageAction(as,action);
                        p.sendMessage(main.prefix + "§aL'action §6message§2 a été ajoutée avec §5le message \"" + main.hex(action) + "\"§2 !");
                        return true;
                    } else if (actionType.equalsIgnoreCase("command")) {
                        if(!action.startsWith("/")) {
                            p.sendMessage(main.prefix + "§4Veuillez entrer une commande valide §c(avec le \"/\") §4!");
                            return true;
                        }
                        asData.addCommandAction(as,action);
                        p.sendMessage(main.prefix + "§aL'action §6commande§2 a été ajoutée avec §5la commande \"" + action + "\"§2 !");
                        return true;
                    } else if (actionType.equalsIgnoreCase("title")) {
                        if(args.length<4) {
                            p.sendMessage(main.prefix + "§cSintax : /armorstand addaction title §4§ltitle=§r§c<title> §4§lsubtitle=§r§c<subtitle>");
                            return true;
                        }
                        if(!action.contains("title=") || !action.contains("subtitle=")) {
                            p.sendMessage(main.prefix + "§cSintax : /armorstand addaction title §4§ltitle=§r§c<title> §4§lsubtitle=§r§c<subtitle>");
                            return true;
                        }

                        action = action.replace("subtitle=",";subtitle=");
                        action = "!title;" + action + ";";
                        asData.addAction(as,action);
                        p.sendMessage(main.prefix + "§aL'action §6title§2 a été ajoutée avec §5le " + main.hex(action.replace("=",":").replace(";","§\"5et le \"")).replace("title:","title:\"") + "\"§2 !");
                        return true;
                    } else if (actionType.equalsIgnoreCase("actionbar")){
                        asData.addActionbarAction(as,action);
                        p.sendMessage(main.prefix + "§aL'action §6Actionbar§2 a été ajoutée avec §5le message \"" + main.hex(action)  + "\"§2 !");
                        return true;
                    } else if (actionType.equalsIgnoreCase("tp") || actionType.equalsIgnoreCase("teleport")) {
                        if(args.length==5) {
                            try {
                                double x = Double.parseDouble(args[2]);
                                double y = Double.parseDouble(args[3]);
                                double z = Double.parseDouble(args[4]);
                                World world = p.getWorld();
                                asData.addTPAction(as, x, y, z, world);
                                p.sendMessage(main.prefix + "§aL'action §6téléportation§2 a été ajoutée avec §5x:" + x + ", y:" + y + ", z:" + z + " et le monde: " + world.getName() + "§2 !");
                                return true;
                            } catch (NumberFormatException e) {
                                p.sendMessage(main.prefix + "§cSintax : /armorstand addaction tp <x> <y> <z> [world]");
                                p.sendMessage(main.prefix + "§4Veuillez entrer des nombres valides !");
                                return true;
                            }
                        } else if (args.length==6) {
                            try {
                                double x = Double.parseDouble(args[2]);
                                double y = Double.parseDouble(args[3]);
                                double z = Double.parseDouble(args[4]);
                                World world = Bukkit.getWorld(args[5]);
                                if(world == null) {
                                    p.sendMessage(main.prefix + "§cSintax : /armorstand addaction tp <x> <y> <z> <worldName>");
                                    p.sendMessage(main.prefix + "§4Veuillez un nom de monde valide !");
                                    return true;
                                }
                                asData.addTPAction(as, x, y, z, world);
                                p.sendMessage(main.prefix + "§aL'action §6téléportation§2 a été ajoutée avec §5x:" + x + ", y:" + y + ", z:" + z + " et le monde: \"" + world.getName() + "\"§2 !");
                                return true;
                            } catch (NumberFormatException e) {
                                p.sendMessage(main.prefix + "§cSintax : /armorstand addaction tp <x> <y> <z> [world]");
                                p.sendMessage(main.prefix + "§4Veuillez entrer des nombres valides !");
                                return true;
                            }
                        }
                    } else {
                        p.sendMessage(main.prefix + "§cSintax : /armorstand addaction <actionType> <actionParam.>");
                        p.sendMessage(main.prefix + "§eLes types d'actions disponibles sont : §6message§e,§6 commande§e,§6title§e,§6 actionbar§e,§6 teleport");
                        return true;
                    }
                }
            }
            p.sendMessage(main.getPrefix() + "§4Aucun Armor Stand détecté proche de vous !");
            return true;

        } else if (args[0].equalsIgnoreCase("spawn")) {
            Location ploc = p.getLocation();
            ArmorStand as = Objects.requireNonNull(ploc.getWorld()).spawn(ploc, ArmorStand.class);
            asData.registerArmorStand(as);
            if(args.length>1) {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    sb.append(args[i]);
                    sb.append(" ");
                }

                String name = sb.toString();
                name = main.hex(name);

                asData.changeName(as,name);
                p.sendMessage(main.prefix + "§2Un Armor Stand a été spawn avec le nom §6" + name + "§2 !");
            } else {
                p.sendMessage(main.prefix + "§2Un Armor Stand a été spawn !");
            }
            return true;
        } else if (args[0].equalsIgnoreCase("setup-minigame")) {
            Location ploc = p.getLocation();

            for(ArmorStand as : p.getWorld().getEntitiesByClass(ArmorStand.class)) {
                if (as == null) {
                    continue;
                }
                if (as.getLocation().distance(ploc) < 1.5) {
                    main.settingUpMiniGameAS.put(p,as);
                    main.settingUpMiniGameAS_Stage.put(p,0);
                    p.sendTitle("§2Envoyez le nom du mini-jeu","§2§ldans le tchat", 20, 60 ,20);
                    p.sendMessage(main.prefix + " - §d[§5Setup-Mini-Jeu§d] §2Envoyez le nom du mini-jeu dans le tchat. §e§oEntrez §r§c&§e§o pour annuler.");
                    return true;
                }
            }
            p.sendMessage(main.getPrefix() + "§4Aucun Armor Stand détecté proche de vous !");
            return true;
        } else {
            p.sendMessage(main.prefix + "§cSintax : /armorstand <spawn/addaction/setname> [actionType/name] [actionParam.]");
            return true;
        }
        //if(args[0].equalsIgnoreCase("register")) {
        //            Location ploc = p.getLocation();
        //            for(ArmorStand allst : p.getWorld().getEntitiesByClass(ArmorStand.class)) {
        //               if(allst == null) {
        //                   p.sendMessage(main.getErrorPrefix() + "Impossible de charger un ArmorStand ! -> AS ignoré.");
        //                   return true;
        //               }
        //               if(allst.getLocation().distance(ploc) < 2) {
        //                   asdetected = true;
        //                   asData.registerArmorStand(allst);
        //                   p.sendMessage(main.prefix + "§2L'ArmorStand a été");
        //                   return true;
        //                }
        //            }
        //            p.sendMessage(main.getPrefix() + "§4Aucun Armor Stand détecté proche de vous !");
        //            return true;
        //        } else
    }
    @Override
    public List<String> onTabComplete(CommandSender s, Command cmd, String l, String[] args) {
        List<String> tabComplete = Lists.newArrayList();

        if(args.length == 1) {
            tabComplete.add("addaction");
            tabComplete.add("spawn");
            tabComplete.add("setname");
            tabComplete.add("setup-minigame");
        }
        if(args.length > 2 && args[0].equalsIgnoreCase("setname")) {
            tabComplete.add("<name>");
        }
        if(args.length > 2 && args[0].equalsIgnoreCase("spawn")) {
            tabComplete.add("[name]");
        }
        if(args.length == 2 && args[0].equalsIgnoreCase("addaction")) {
            tabComplete.add("message");
            tabComplete.add("command");
            tabComplete.add("title");
            tabComplete.add("actionbar");
            tabComplete.add("teleport");
        }
        if(args.length > 2 && args[0].equalsIgnoreCase("addaction") && args[1].equalsIgnoreCase("message") ) {
            tabComplete.add("<message>");
        }
        if(args.length > 2 && args[0].equalsIgnoreCase("addaction") && args[1].equalsIgnoreCase("command") ) {
            tabComplete.add("<command>");
        }
        if(args.length > 2 && args[0].equalsIgnoreCase("addaction") && args[1].equalsIgnoreCase("title") ) {
            tabComplete.add("title=<title> and subtitle=<subtitle>");
        }
        if(args.length > 2 && args[0].equalsIgnoreCase("addaction") && args[1].equalsIgnoreCase("actionbar") ) {
            tabComplete.add("<actionbar>");
        }
        if(args.length == 3 && args[0].equalsIgnoreCase("addaction") && args[1].equalsIgnoreCase("teleport") ) {
            tabComplete.add("<x>");
        }
        if(args.length == 4 && args[0].equalsIgnoreCase("addaction") && args[1].equalsIgnoreCase("teleport") ) {
            tabComplete.add("<y>");
        }
        if(args.length == 5 && args[0].equalsIgnoreCase("addaction") && args[1].equalsIgnoreCase("teleport") ) {
            tabComplete.add("<z>");
        }
        if(args.length == 6 && args[0].equalsIgnoreCase("addaction") && args[1].equalsIgnoreCase("teleport") ) {
            tabComplete.add("[world]");
        }

        return tabComplete;
    }
}
