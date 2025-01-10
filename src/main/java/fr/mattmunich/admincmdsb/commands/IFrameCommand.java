package fr.mattmunich.admincmdsb.commands;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;

public class IFrameCommand implements CommandExecutor {

	private Main main;

	public IFrameCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {


		if(s instanceof BlockCommandSender) {
			s.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}



		if(!(s instanceof Player)){
			s.sendMessage(main.getPrefix() + "§4Vous devez être un joueur pour utiliser cette commande !");
			return true;
		}

		Player p = (Player) s;

		if(!main.staff.contains(p) && !main.superstaff.contains(p)) {
			p.sendMessage(main.noPermissionMsg);
			return true;
		}

		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("glow") || args[0].equalsIgnoreCase("glowing")) {
				p.chat("/give @s glow_item_frame[minecraft:custom_name='{text:\"Invisible Item Frame\",italic=false}',entity_data={id:\"minecraft:item_frame\",Invisible:1b}]");
				p.sendMessage(main.getPrefix() + "§2Vous avez reçu une §6Glow Item Frame §6invisible §2avec succès !");
				return true;
			} else {
				p.sendMessage("§cSintax : /itemframe [glow/glowing]");
				return true;
			}
		}else if(args.length == 0) {
			p.chat("/give @s item_frame[minecraft:custom_name='{text:\"Invisible Item Frame\",italic=false}',entity_data={id:\"minecraft:item_frame\",Invisible:1b}]");
			p.sendMessage(main.getPrefix() + "§2Vous avez reçu une §6Item Frame §6invisible §2avec succès !");
			return true;
		} else {
			p.sendMessage("§cSintax : /itemframe [glow/glowing]");
			return true;
		}
	}

}
