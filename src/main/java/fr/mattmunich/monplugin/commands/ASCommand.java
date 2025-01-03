package fr.mattmunich.monplugin.commands;

import fr.mattmunich.monplugin.MonPlugin;
import fr.mattmunich.monplugin.commandhelper.ASData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Objects;
import java.util.UUID;

public class ASCommand implements CommandExecutor {

    private MonPlugin main;
    private ASData asData;

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
                if(as.getLocation().distance(ploc) < 1) {
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
                if(as.getLocation().distance(ploc) < 1) {
                    if(!asData.isRegistred(as)) {
                        asData.registerArmorStand(as);
                    }

                    if (actionType.equalsIgnoreCase("message")) {
                        asData.addMessageAction(as,action);
                    } else if (actionType.equalsIgnoreCase("command")) {
                        if(!action.startsWith("/")) {
                            p.sendMessage(main.prefix + "§4Veuillez entrer une commande valide §c(avec le \"/\") §4!");
                            return true;
                        }
                        asData.addCommandAction(as,action);
                    } else if (actionType.equalsIgnoreCase("title")) {
                        if(args.length<4) {
                            p.sendMessage(main.prefix + "§cSintax : /armorstand addaction title §4§ltitle=§r§c<title> §4§lsubtitle=§r§c<subtitle>");
                            return true;
                        }
                        if(!action.contains("title=") || !action.contains("subtitle=")) {
                            p.sendMessage(main.prefix + "§cSintax : /armorstand addaction title §4§ltitle=§r§c<title> §4§lsubtitle=§r§c<subtitle>");
                            return true;
                        }

                        action.replace("subtitle=",";subtitle=");
                        action = "!title;" + action + ";";
                        asData.addAction(as,action);
                    } else if (actionType.equalsIgnoreCase("actionbar")){
                        asData.addActionbarAction(as,action);
                    } else if (actionType.equalsIgnoreCase("tp") || actionType.equalsIgnoreCase("teleport")) {
                        if(args.length==5) {
                            try {
                                double x = Double.parseDouble(args[2]);
                                double y = Double.parseDouble(args[3]);
                                double z = Double.parseDouble(args[4]);
                                World world = p.getWorld();
                                asData.addTPAction(as, x, y, z, world);
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

                    p.sendMessage(main.prefix + "§aL'action a été ajoutée !");
                    return true;
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
                return true;
            } else {
                p.sendMessage(main.prefix + "§2Un Armor Stand a été spawn !");
                return true;
            }
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
        //               if(allst.getLocation().distance(ploc) < 1) {
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
}
