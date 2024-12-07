package fr.mattmunich.monplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.mattmunich.monplugin.MonPlugin;
import fr.mattmunich.monplugin.commandhelper.PlayerData;

public class BackCommand implements CommandExecutor {

	private MonPlugin main;

	public BackCommand(MonPlugin main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {

		if(s instanceof BlockCommandSender) {
			s.sendMessage("§4Utilisation de Command Blocks désactivée !");
			return true;
		}


		if(!(s instanceof Player)) {
			s.sendMessage(main.getPrefix() + "§cVous devez être un joueur pour utiliser cette commande !");
			return true;
		}
		Player p = (Player)s;
		PlayerData data = new PlayerData(p.getUniqueId());

		if(!main.staff.contains(p)) {
			p.sendMessage(main.getPrefix() + "§4Vous n'avez pas la permission d'utiliser cette commande !");
			return true;
		}
		if(data.getConfig().contains("deathPos.world") && data.getConfig().contains("deathPos.coords.x")&& data.getConfig().contains("deathPos.coords.y")&& data.getConfig().contains("deathPos.coords.z")) {
			data.getConfig().set("deathPos.world", null);
			data.getConfig().set("deathPos.coords.x", null);
			data.getConfig().set("deathPos.coords.y", null);
			data.getConfig().set("deathPos.coords.z", null);
		}
		
		if(data.getConfig().getString("lastPos.world") == null || data.getConfig().getString("lastPos.x") == null || data.getConfig().getString("lastPos.y") == null || data.getConfig().getString("lastPos.z") == null) {
			p.sendMessage(main.getPrefix() + "§4Aucun lieu trouvé.");
			return true;
		}else {
			try {
				String worldName = data.getConfig().getString("lastPos.world");
                assert worldName != null;
                if(Bukkit.getWorld(worldName) == null) {
					p.sendMessage(main.getErrorPrefix() + "Le monde ciblé n'existe pas ou n'est pas chargré!");
					return true;
				}else {
					World world = Bukkit.getWorld(worldName);
					double x = data.getConfig().getDouble("lastPos.x");
					double y = data.getConfig().getDouble("lastPos.y");
					double z = data.getConfig().getDouble("lastPos.z");

					p.teleport(new Location(world, x, y, z));
					p.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20, 10, true, false, true));

				}

			}catch (Exception e) {
				p.sendMessage(main.getErrorPrefix() + "Impossible de vous téléporter à vos coordonnés de mort !");
				System.err.println(main.errorMsg + "/nErreur :/n" + e.getMessage());

				return true;
			}
		}
		return true;

	}

}
