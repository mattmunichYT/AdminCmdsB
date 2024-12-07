package fr.mattmunich.monplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import fr.mattmunich.monplugin.MonPlugin;

public class LuckyBCommand implements CommandExecutor {

	private MonPlugin main;

	public LuckyBCommand(MonPlugin main) {
		this.main = main;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {


		if(s instanceof BlockCommandSender) {
			s.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}



		if(!(s instanceof Player)) {
			s.sendMessage(main.getPrefix() + "§4Vous devez etre un joueur pour utiliser cette commande !");
			return true;
		}

		Player p = (Player) s;

		if (!p.getName().equalsIgnoreCase("mattmunich")) {
			p.sendMessage(main.getErrorPrefix() + "§4Commande inconnue/désactivée !");
			return true;
		}

		if(args.length == 0 || args.length > 1 || args[0].isEmpty()) {
			p.sendMessage("§cSintax : /luckyblock <nombre>");
			return true;
		}

		if(!args[0].matches("-?\\d+")) {
			p.sendMessage(main.getPrefix() + "§4Veuillez entrer un nombre !");
			return true;
		}


		int n = Integer.parseInt(args[0]);
		if(n > 640 || n < 1) {
			p.sendMessage(main.getPrefix() + "§4Veuillez entrer un nombre de 1 à 640 !");
			return true;
		}
		ItemStack luckyB = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta meta = (SkullMeta) luckyB.getItemMeta();
		meta.setOwningPlayer(Bukkit.getOfflinePlayer("luck"));
		meta.setDisplayName("§eLucky Block");
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		luckyB.setItemMeta(meta);

		for(int i = n; i !=0;i--) {
			p.getInventory().addItem(luckyB);
		}

		p.updateInventory();
		p.sendMessage(main.getPrefix() + "§2" + n + " §eLucky Block(s) vous ont été donné !");


		//p.getInventory().addItem(luckyB);
		//p.updateInventory();

		return true;
	}

}
