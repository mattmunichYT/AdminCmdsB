package fr.mattmunich.monplugin.commands;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.monplugin.MonPlugin;

public class EcCommand implements CommandExecutor {

	private MonPlugin main;

	public EcCommand(MonPlugin main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof BlockCommandSender) {
			sender.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}


		if(!(sender instanceof Player)) {
			sender.sendMessage(main.getPrefix() + "§4Vous devez etre un joueur por utiliser cette commande !");
			return true;
		}

		Player p = (Player) sender;

		if(!(main.staff.contains(p))) {
			p.sendMessage(main.noPermissionMsg);
			return true;
		}

		if(args.length > 0) {
			p.sendMessage(main.getPrefix() + "§4Cette commande ne comporte pas d'arguments !");
			return true;
		}

		p.openInventory(p.getEnderChest());
		p.updateInventory();

		return true;
	}

}
