package fr.mattmunich.admincmdsb.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.admincmdsb.Main;

public class GetPosCommand implements CommandExecutor {

	private Main main;

	public GetPosCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender p, Command cmd, String l, String[] args) {

		if(p instanceof BlockCommandSender) {
			p.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		if(p instanceof Player) {
			Player rp = (Player) p;

			if(!(main.superstaff.contains(rp))) {
				rp.sendMessage(main.noPermissionMsg);
				return true;
			}
		}

		if(args.length > 1) {
			p.sendMessage("§cSintax : /getpos [player]");
			return true;
		}

		if(args.length == 1) {

			String tname = args[0];

			if(Bukkit.getPlayer(tname) == null) {
				p.sendMessage(main.getPrefix() + "§4Le joueur est hors ligne ou n'existe pas !");
				return true;
			}

			Player t = Bukkit.getPlayer(tname);

			String world = t.getLocation().getWorld().getName();
			int x = t.getLocation().getBlockX();
			int y = t.getLocation().getBlockY();
			int z = t.getLocation().getBlockZ();
			float yaw = t.getLocation().getYaw();
			float pitch = t.getLocation().getPitch();

			p.sendMessage(main.getPrefix() + "§6---------- Position de §2" + t.getName() + "§6 ----------"
					+ "\n§6§lMonde : §2" + world
					+ "\n§6§lBlock X : §2" + x
					+ "\n§6§lBlock Y : §2" + y
					+ "\n§6§lBlock Z : §2" + z
					+ "\n§6§lBYaw : §2" + yaw
					+ "\n§6§lPitch : §2" + pitch);
			return true;

		}else if(args.length == 0) {

			if(!(p instanceof Player)) {
				p.sendMessage(main.getPrefix() + "§4Merci de préciser le nom d'un joueur en ligne.");
				return true;
			}
			Player t = (Player)p;

			String world = t.getLocation().getWorld().getName();
			int x = t.getLocation().getBlockX();
			int y = t.getLocation().getBlockY();
			int z = t.getLocation().getBlockZ();
			float yaw = t.getLocation().getYaw();
			float pitch = t.getLocation().getPitch();

			p.sendMessage(main.getPrefix() + "§6---------- Position de §2" + t.getName() + "§6 ----------"
					+ "\n§6§lMonde : §2" + world
					+ "\n§6§lBlock X : §2" + x
					+ "\n§6§lBlock Y : §2" + y
					+ "\n§6§lBlock Z : §2" + z
					+ "\n§6§lBYaw : §2" + yaw
					+ "\n§6§lPitch : §2" + pitch);
			return true;
		} else {
			System.err.println(main.errorMsg);
			p.sendMessage(main.errorMsg);
		}

		return true;
	}

}
