package fr.mattmunich.admincmdsb.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.mattmunich.admincmdsb.Main;


public class CommandAdmin implements CommandExecutor {

	private Main main;

	public CommandAdmin(Main main) {
		this.main = main;
	}

	//@SuppressWarnings("deprecation")
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof BlockCommandSender) {
			sender.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		if(sender instanceof Player) {


			//Commande "/fly"

			if(label.equalsIgnoreCase("fly") || label.equalsIgnoreCase("admincmdsb--mjep:fly")) {

				Player player = (Player)sender;
				if(!(main.staff.contains(player))) {
					player.sendMessage(main.noPermissionMsg);
					return true;
				}
				if(args.length == 0) {
					if(!player.getAllowFlight()) {
						player.setAllowFlight(true);
						player.setFlying(true);
						player.sendMessage(main.getPrefix() + (main.getConfig().getString("commands.messages.fly.on") == null ? "§6Fly §2activé" : main.getConfig().getString("commands.messages.fly.on")));
					}else if(player.getAllowFlight()) {
						player.setFlying(false);
						player.setAllowFlight(false);
						player.sendMessage(main.getPrefix() + (main.getConfig().getString("commands.messages.fly.off") == null ? "§6Fly §4désactivé" : main.getConfig().getString("commands.messages.fly.off")));
					}else {
						player.sendMessage(main.errorMsg);
						throw new Error(main.errorMsg);
					}


					return true;
				}

				if(args.length >= 1) {

					String arg = args[0];

					if(arg.equalsIgnoreCase("on")) {
						player.setAllowFlight(true);
						player.setFlying(true);
						player.sendMessage(main.getPrefix() + (main.getConfig().getString("commands.messages.fly.on") == null ? "§6Fly §2activé" : main.getConfig().getString("commands.messages.fly.on")));

						return true;
					}else if(arg.equalsIgnoreCase("off")) {
						player.setFlying(false);
						player.setAllowFlight(false);
						player.sendMessage(main.getPrefix() + (main.getConfig().getString("commands.messages.fly.off") == null ? "§6Fly §4désactivé" : main.getConfig().getString("commands.messages.fly.off")));

						return true;
					}else {

						if(!(main.superstaff.contains(player))) {
							player.sendMessage(main.noPermissionArgMsg);
							return true;
						}

						String tName = args[0];

						if(Bukkit.getPlayer(tName) == null) {
							player.sendMessage(main.playerNotFoundMsg);
							return true;
						}

						Player t = Bukkit.getPlayer(tName);

						if(!t.isFlying()) {
							t.setAllowFlight(true);
							t.setFlying(true);
							t.sendMessage(main.getPrefix() + (main.getConfig().getString("commands.messages.fly.enabledByPlayer") == null ?
									"§6Fly §2activé par §6" + player.getName() :
										(main.getConfig().getString("commands.messages.fly.enabledByPlayer").contains("%player%") ?
												main.getConfig().getString("commands.messages.fly.enabledByPlayer").replace("%player%", player.getName()) :
													main.getConfig().getString("commands.messages.fly.enabledByPlayer"))));
						}else if(t.isFlying()) {
							t.setFlying(false);
							t.setAllowFlight(false);
							t.sendMessage(main.getPrefix() + main.getMessageWithVar("commands.messages.fly.disabledByPlayer", "§6Fly §4déactivé par §6" + player.getName(), "%player%", player.getName()));
						}else {
							throw new Error(main.errorMsg);
						}

					}
			}



				}


			//Commande "/feed"

			if(label.equalsIgnoreCase("feed") || label.equalsIgnoreCase("admincmdsb--mjep:feed")) {
				Player player = (Player)sender;

				if(!(main.staff.contains(player))) {
					player.sendMessage(main.noPermissionMsg);
					return true;
				}

				if(args.length > 1) {
					player.sendMessage("§cSintax : /feed [Player]");
					return true;
				}

				if(args.length == 1) {

					String tname = args[0];

					if(Bukkit.getPlayer(tname) == null) {
						player.sendMessage(main.playerNotFoundMsg);
						return true;
					}

					Player t = Bukkit.getPlayer(tname);

					t.setFoodLevel(255);
					t.sendMessage(main.getPrefix() + main.getMessageWithVar("commands.messages.feed.fedByPlayer", "§2Vous avez été /feed par §6" + player.getName() + "§2 !", "%player%", player.getName()));
					player.sendMessage(main.getPrefix() + main.getMessageWithVar("commands.messages.feed.fedTarget", "§2Vous avez /feed §6" + t.getName() + "§2 !", "%target%", t.getName()));

					return true;
				}

				player.setFoodLevel(255);
				player.sendMessage(main.getPrefix() + main.getMessage("commands.messages.feed.feed", "§2Vous avez été §6feed §2!"));




					return true;
				}





			//Commande "/heal"

			if(label.equalsIgnoreCase("heal") || label.equalsIgnoreCase("admincmdsb--mjep:heal")) {
				Player player = (Player)sender;

				if (!main.staff.contains(player)) {
					player.sendMessage(main.noPermissionMsg);
					return true;
				}

				if(args.length >= 1) {

					String tname = args[0];

					if(Bukkit.getPlayer(tname) == null) {
						player.sendMessage(main.playerNotFoundMsg);
						return true;
					}

					Player t = Bukkit.getPlayer(tname);

					t.setHealth(t.getMaxHealth());
					t.setFireTicks(0);
					t.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, 2, 255, false, false));
					t.sendMessage(main.getPrefix() + main.getMessageWithVar("commands.messages.heal.healedByPlayer", "§2Vous avez été heal par §6" + player.getName() + "§2 !", "%player%", player.getName()));
					player.sendMessage(main.getPrefix() + main.getMessageWithVar("commands.messages.heal.healedTarget", "§2Vous avez heal §6" + t.getName() + "§2 !", "%target%", t.getName()));

					return true;
				}

				player.setFireTicks(0);
				player.setHealth(player.getMaxHealth());
				player.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH, 2, 255, false, false));
				player.sendMessage(main.getPrefix() + main.getMessage("commands.messages.heal.heal", "§2Vous avez été §6heal§2 !"));

				return true;

				}



			} else {
				sender.sendMessage(main.requirePlayerToExcMsg);
				return true;
			}



		return true;


}
}
