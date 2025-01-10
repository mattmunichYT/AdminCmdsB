package fr.mattmunich.admincmdsb.commands;

import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.mattmunich.admincmdsb.Main;

public class NVCommand implements CommandExecutor {

	private Main main;

	public NVCommand(Main main) {
		this.main = main;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {

		if (s instanceof BlockCommandSender) {
			s.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}

		if (!(s instanceof Player)) {
			s.sendMessage(main.getPrefix() + "§4Vous devez être un joueur pour utiliser cette commande !");
			return true;
		}

		Player p = (Player) s;

		if (!(main.staff.contains(p))) {
			p.sendMessage(main.noPermissionMsg);
			return true;
		}

		// p.performCommand("effect give @s night_vision 999999 255 true");

		PotionEffect nv = new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 100, true, false, true);
		p.addPotionEffect(nv, true);
		p.sendMessage(main.getPrefix() + "§2L'effet §1NightVision§6 vous à été appliqué !");

		return true;
	}

}
