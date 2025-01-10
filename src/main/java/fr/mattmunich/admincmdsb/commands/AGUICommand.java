package fr.mattmunich.admincmdsb.commands;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.mattmunich.admincmdsb.Main;

public class AGUICommand implements CommandExecutor {

	private Main main;

	public AGUICommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String l, String[] args) {

		if(s instanceof BlockCommandSender) {
			s.sendMessage("§4Utilisation de Command Blocks désactivée !");
			BlockCommandSender cmds = (BlockCommandSender) s;
			cmds.sendMessage("Nope");
			return true;
		}

		if(!(s instanceof Player)) {
			s.sendMessage(main.getPrefix() + "§4Vous devez être un joueur pour utiliser cette commande !");
			return true;
		}

		Player p = (Player) s;

		if(!(main.superstaff.contains(p))) {
			p.sendMessage(main.noPermissionMsg);
			return true;
		}

		ItemStack customcompass = new ItemStack(Material.COMPASS, 1);
		ItemMeta customcM = customcompass.getItemMeta();
		customcM.setDisplayName("§4Admin§6GUI");
		customcM.addEnchant(Enchantment.PROTECTION, 200, true);
		customcM.setLore(Arrays.asList(" ","C'est la boussole de l'AdminGUI ", ":)", "(elle est cool)"));
		customcM.setUnbreakable(true);
		customcM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		customcM.addItemFlags(ItemFlag.HIDE_DESTROYS);
		customcM.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		customcM.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
		customcompass.setItemMeta(customcM);

		p.getInventory().addItem(customcompass);

		p.updateInventory();

		return true;
	}

}
