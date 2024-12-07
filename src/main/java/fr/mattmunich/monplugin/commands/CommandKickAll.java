package fr.mattmunich.monplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.monplugin.MonPlugin;
import fr.mattmunich.monplugin.commandhelper.Ban;

public class CommandKickAll implements CommandExecutor {

	private MonPlugin main;

	private final Ban ban;

	public CommandKickAll(MonPlugin main, Ban ban) {
		this.main = main;
		this.ban = ban;

	}

	@Override
	public boolean onCommand(CommandSender player, Command cmd, String label, String[] args) {

		if (player instanceof BlockCommandSender) {
			player.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		if (!(main.superstaff.contains(player)) && player instanceof Player) {
			player.sendMessage(main.noPermissionMsg);
			return true;
		}

		String reason = " ";
		for (String arg : args) {
			reason = reason + " " + arg;
		}

		reason = reason.trim();

		reason = ChatColor.translateAlternateColorCodes('&', main.hex(reason));

		ban.kickAllClassic(player.getName(), reason);

		return true;
	}
}
