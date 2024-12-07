package fr.mattmunich.monplugin.commandhelper;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.mattmunich.monplugin.MonPlugin;

public class PlayerManager {

	private Player p;
	private ItemStack[] items = new ItemStack[41];

	public PlayerManager(Player p) {
		this.p = p;
	}

	public static PlayerManager getFromPlayer(Player p) {
		return MonPlugin.getInstance().secInv.get(p.getUniqueId());

	}

	public void init() {
		MonPlugin.getInstance().secInv.put(p.getUniqueId(), this);
	}

	public void destroy() {
		MonPlugin.getInstance().secInv.remove(p.getUniqueId());
	}

	public ItemStack[] getItems() {
		return items;
	}

	public void saveInventory() {
		for (int slot = 0; slot < 36; slot++) {
			ItemStack item = p.getInventory().getItem(slot);
			if (item != null) {
				items[slot] = item;
			}
		}
		items[36] = p.getInventory().getHelmet();
		items[37] = p.getInventory().getChestplate();
		items[38] = p.getInventory().getLeggings();
		items[39] = p.getInventory().getBoots();
		items[40] = p.getInventory().getItemInOffHand();
		p.getInventory().clear();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		p.getInventory().setItemInOffHand(null);
		p.updateInventory();
	}

	public void giveInvetnory() {
		p.getInventory().clear();
		for (int slot = 0; slot < 36; slot++) {
			ItemStack item = items[slot];
			if (item != null) {
				p.getInventory().setItem(slot, item);
			}

		}
		p.getInventory().setHelmet(items[36]);
		p.getInventory().setChestplate(items[37]);
		p.getInventory().setLeggings(items[38]);
		p.getInventory().setBoots(items[39]);
		p.getInventory().setItemInOffHand(items[40]);

		p.updateInventory();

	}

}
