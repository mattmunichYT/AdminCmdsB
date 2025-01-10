package fr.mattmunich.admincmdsb.commands;

import java.util.Arrays;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.mattmunich.admincmdsb.Main;

public class CInvCommand implements CommandExecutor {

	private Main main;

	public CInvCommand(Main main) {
		this.main = main;
	}

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

		if (!(main.superstaff.contains(player))) {
			player.sendMessage(main.noPermissionMsg);
			return true;
		}

		if (args.length > 0) {
			player.sendMessage(main.getPrefix() + "§4Cette commande ne comporte pas d'arguments !");
			return true;
		}

		Inventory inv = Bukkit.createInventory(player, 9, "§6Cheat Inventory");

		ItemStack compass = new ItemStack(Material.COMPASS);
		ItemMeta compasM = compass.getItemMeta();
        assert compasM != null;
        compasM.addEnchant(Enchantment.PROTECTION, 200, true);
		compasM.setUnbreakable(true);
		compasM.setDisplayName("§6La petite boussole :)");
		compasM.setLore(Arrays.asList("§2Cette boussole te sera utile", "§6Surement", "§e:)"));
		compasM.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		compasM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		compass.setItemMeta(compasM);

		inv.addItem(compass);

		ItemStack helmet = new ItemStack(Material.NETHERITE_HELMET);
		ItemMeta helmetM = helmet.getItemMeta();
        assert helmetM != null;
        helmetM.addEnchant(Enchantment.PROTECTION, 32767, true);
		helmetM.addEnchant(Enchantment.FIRE_PROTECTION, 32767, true);
		helmetM.addEnchant(Enchantment.BLAST_PROTECTION, 32767, true);
		helmetM.addEnchant(Enchantment.FEATHER_FALLING, 32767, true);
		helmetM.addEnchant(Enchantment.PROJECTILE_PROTECTION, 32767, true);
		helmetM.addEnchant(Enchantment.RESPIRATION, 32767, true);
		helmetM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		helmetM.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
		helmetM.addEnchant(Enchantment.THORNS, 32767, true);
		helmetM.setUnbreakable(true);
		helmetM.setLore(Collections.singletonList("§5§oCheat"));

		helmet.setItemMeta(helmetM);
		inv.addItem(helmet);

		ItemStack chestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
		ItemMeta chestplateM = chestplate.getItemMeta();
		assert chestplateM != null;
		chestplateM.addEnchant(Enchantment.PROTECTION, 32767, true);
		chestplateM.addEnchant(Enchantment.FIRE_PROTECTION, 32767, true);
		chestplateM.addEnchant(Enchantment.BLAST_PROTECTION, 32767, true);
		chestplateM.addEnchant(Enchantment.FEATHER_FALLING, 32767, true);
		chestplateM.addEnchant(Enchantment.PROJECTILE_PROTECTION, 32767, true);
		chestplateM.addEnchant(Enchantment.RESPIRATION, 32767, true);
		chestplateM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		chestplateM.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
		chestplateM.addEnchant(Enchantment.THORNS, 32767, true);
		chestplateM.setUnbreakable(true);
		chestplateM.setLore(Collections.singletonList("§5§oCheat"));

		chestplate.setItemMeta(chestplateM);
		inv.addItem(chestplate);

		ItemStack legging = new ItemStack(Material.NETHERITE_LEGGINGS);
		ItemMeta leggingM = legging.getItemMeta();
		assert leggingM != null;
		leggingM.addEnchant(Enchantment.PROTECTION, 32767, true);
		leggingM.addEnchant(Enchantment.FIRE_PROTECTION, 32767, true);
		leggingM.addEnchant(Enchantment.BLAST_PROTECTION, 32767, true);
		leggingM.addEnchant(Enchantment.FEATHER_FALLING, 32767, true);
		leggingM.addEnchant(Enchantment.PROJECTILE_PROTECTION, 32767, true);
		leggingM.addEnchant(Enchantment.RESPIRATION, 32767, true);
		leggingM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		leggingM.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
		leggingM.addEnchant(Enchantment.THORNS, 32767, true);
		leggingM.setUnbreakable(true);
		leggingM.setLore(Collections.singletonList("§5§oCheat"));

		legging.setItemMeta(leggingM);
		inv.addItem(legging);

		ItemStack boots = new ItemStack(Material.NETHERITE_BOOTS);
		ItemMeta bootsM = boots.getItemMeta();
		assert bootsM != null;
		bootsM.addEnchant(Enchantment.PROTECTION, 32767, true);
		bootsM.addEnchant(Enchantment.FIRE_PROTECTION, 32767, true);
		bootsM.addEnchant(Enchantment.BLAST_PROTECTION, 32767, true);
		bootsM.addEnchant(Enchantment.FEATHER_FALLING, 32767, true);
		bootsM.addEnchant(Enchantment.PROJECTILE_PROTECTION, 32767, true);
		bootsM.addEnchant(Enchantment.RESPIRATION, 32767, true);
		bootsM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		bootsM.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
		bootsM.addEnchant(Enchantment.THORNS, 32767, true);
		bootsM.setUnbreakable(true);
		bootsM.setLore(Collections.singletonList("§5§oCheat"));

		boots.setItemMeta(bootsM);
		inv.addItem(boots);

		ItemStack sword = new ItemStack(Material.NETHERITE_SWORD);
		ItemMeta swordM = sword.getItemMeta();
        assert swordM != null;
        swordM.addEnchant(Enchantment.SHARPNESS, 32767, true);
		swordM.addEnchant(Enchantment.FIRE_ASPECT, 32767, true);
		swordM.addEnchant(Enchantment.KNOCKBACK, 32767, true);
		swordM.addEnchant(Enchantment.LOOTING, 32767, true);
		swordM.addEnchant(Enchantment.SWEEPING_EDGE, 3, true);
		swordM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		swordM.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
		swordM.setLore(Collections.singletonList("§5§oCheat"));
		swordM.setUnbreakable(true);

		sword.setItemMeta(swordM);
		inv.addItem(sword);

		ItemStack pickaxe = new ItemStack(Material.NETHERITE_PICKAXE);
		ItemMeta pickaxeM = pickaxe.getItemMeta();
        assert pickaxeM != null;
        pickaxeM.addEnchant(Enchantment.SHARPNESS, 32767, true);
		pickaxeM.addEnchant(Enchantment.LOOTING, 32767, true);
		pickaxeM.addEnchant(Enchantment.EFFICIENCY, 32767, true);
		pickaxeM.addEnchant(Enchantment.FORTUNE, 32767, true);
		pickaxeM.addEnchant(Enchantment.PROTECTION, 32767, true);
		pickaxeM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		pickaxeM.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
		pickaxeM.setLore(Collections.singletonList("§5§oCheat"));
		pickaxeM.setUnbreakable(true);

		pickaxe.setItemMeta(pickaxeM);
		inv.addItem(pickaxe);

		ItemStack bow = new ItemStack(Material.BOW);
		ItemMeta bowM = bow.getItemMeta();
        assert bowM != null;
        bowM.addEnchant(Enchantment.POWER, 32767, true);
		bowM.addEnchant(Enchantment.FLAME, 32767, true);
		bowM.addEnchant(Enchantment.INFINITY, 32767, true);
		bowM.addEnchant(Enchantment.PUNCH, 10, true);
		bowM.addEnchant(Enchantment.SHARPNESS, 32767, true);
		bowM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		bowM.addEnchant(Enchantment.VANISHING_CURSE, 1, true);
		bowM.setLore(Collections.singletonList("§5§oCheat"));
		bowM.setUnbreakable(true);

		bow.setItemMeta(bowM);
		inv.addItem(bow);

		player.openInventory(inv);

		return true;
	}

}
