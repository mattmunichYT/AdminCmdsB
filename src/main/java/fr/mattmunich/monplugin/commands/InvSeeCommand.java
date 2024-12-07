package fr.mattmunich.monplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.mattmunich.monplugin.MonPlugin;

public class InvSeeCommand implements CommandExecutor {

	private MonPlugin main;

	public InvSeeCommand(MonPlugin main) {
		this.main = main;
	}

	public ItemStack[] items = new ItemStack[54];

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof BlockCommandSender) {
			sender.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage(main.getPrefix() + "§4Vous devez etre un joueur pour utiliser cette commande !");
			return true;
		}

		Player player = (Player) sender;

		if (!main.superstaff.contains(player)) {
			player.sendMessage(main.getPrefix() + "§4Vous n'avez pas la permission d'utiliser cette commande !");
			return true;
		}

		if (!(args.length == 1)) {
			player.sendMessage("§cSintax : /admininvsee <Player>");
			return true;
		}

		String targetName = args[0];
		if (Bukkit.getPlayer(targetName) == null) {
			player.sendMessage(main.getPrefix() + "§4Le joueur est hors-ligne ou n'éxiste pas !");
//			Inventory tINV = Bukkit.getOfflinePlayer(targetName).getPlayer().getInventory();
//			player.openInventory(tINV);
//			player.updateInventory();
			return true;
		}

		Player target = Bukkit.getPlayer(targetName);

		Inventory invSee = Bukkit.createInventory(target, 54, "§6§lInv§4§lSee §0§l- §6" + target.getName());
		// ItemStack air = new ItemStack(Material.AIR);
		// Empty item creation
		ItemStack empty = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta emptyM = empty.getItemMeta();
		emptyM.setDisplayName("§0_");
		empty.setItemMeta(emptyM);
		// Refresh item creation
		ItemStack refresh = new ItemStack(Material.SLIME_BALL);
		ItemMeta refreshM = empty.getItemMeta();
		refreshM.setDisplayName("§a⟳ Recharger");
		refresh.setItemMeta(refreshM);

		// Close item creation
		ItemStack close = new ItemStack(Material.BARRIER);
		ItemMeta closeM = empty.getItemMeta();
		closeM.setDisplayName("§4❌ Fermer");
		close.setItemMeta(closeM);

//		player.openInventory(target.getInventory());

		for (int slot = 0; slot < 36; slot++) {
			ItemStack item = target.getInventory().getItem(slot);
			if (item != null) {
				items[slot] = item;
			}
		}
		for (int slot = 36; slot < 45; slot++) {
			items[slot] = empty;
		}
		try {
			items[45] = target.getInventory().getHelmet();
			items[46] = target.getInventory().getChestplate();
			items[47] = target.getInventory().getLeggings();
			items[48] = target.getInventory().getBoots();
			items[49] = empty;
			items[50] = target.getInventory().getItemInOffHand();
			items[51] = empty;
			items[52] = refresh;
			items[53] = close;
		} catch (Exception eInvSeeItms) {
			eInvSeeItms.printStackTrace();
		}

		player.openInventory(invSee);
		for (int slot = 0; slot < 36; slot++) {
			ItemStack item = items[slot];
			if (item != null) {
				target.getInventory().setItem(slot, item);
			}
		}

		target.getInventory().setHelmet(items[45]);
		target.getInventory().setChestplate(items[46]);
		target.getInventory().setLeggings(items[47]);
		target.getInventory().setBoots(items[48]);
		target.getInventory().setItemInOffHand(items[50]);

		invSee.setStorageContents(items);

		return true;
	}

}
