package fr.mattmunich.monplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.monplugin.MonPlugin;
import fr.mattmunich.monplugin.commandhelper.Settings;

public class CommandAlert implements CommandExecutor {

	@SuppressWarnings("unused") //Debug
	private Settings settings;
	private MonPlugin main;

	public CommandAlert(Settings settings, MonPlugin main) {
		this.settings = settings;
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {

		if(sender instanceof BlockCommandSender) {
			sender.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		if(sender instanceof Player) {

			Player player = (Player)sender;

			if(!(main.superstaff.contains(player))) {
				player.sendMessage(main.noPermissionMsg);
				return true;
			}

			if(args.length == 0) {
				player.sendMessage("§cSintax : /alert <message>");
			}

				Bukkit.broadcastMessage("§e[Broadcast]: §r" + ChatColor.translateAlternateColorCodes('&', String.join(" ", args)));
				return true;


		} else {
			sender.sendMessage("§e(§6!§e) §4Vous devez être un joueur pour executer cette commande !");
		}



		return true;
	}

}
