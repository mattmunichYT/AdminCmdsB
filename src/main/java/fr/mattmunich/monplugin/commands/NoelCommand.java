package fr.mattmunich.monplugin.commands;

import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.mattmunich.monplugin.MonPlugin;
import org.bukkit.event.player.PlayerTeleportEvent;

@SuppressWarnings("unused")
public class NoelCommand implements CommandExecutor {

	private MonPlugin main;

	public NoelCommand(MonPlugin main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {
		if (!(s instanceof Player) || s instanceof BlockCommandSender) {
			s.sendMessage(main.getPrefix() + "§4Vous devez être un joueur pour utiliser cette commande !");
			if (args[0].equalsIgnoreCase("loadWorld")) {
				try {
					World mondeNoel = Bukkit.getWorld("noel");
					mondeNoel.loadChunk(0, 0);
					s.sendMessage(main.getPrefix() + "§2Le monde §cNoël§2 a été chargé !");
				} catch (Exception e) {
					World mondeNoel = WorldCreator.name("noel").type(WorldType.FLAT).environment(Environment.NORMAL)
							.createWorld();
				}
			}
			return true;
		}

		Player p = (Player) s;
//		p.sendMessage(main.getPrefix() + main.hex(
//				"§4Le prochain Évent #ff4848N#ff6b6bO#ff8e8eË#ffb1b1L §4 n'aura seulement lieu en en §cDécembre 2025 §4 !"));
//		return true;
		if(!main.admin.contains(p)) {
			p.sendMessage(main.getPrefix() + main.hex("§4Le prochain Évent #ff4848N#ff6b6bO#ff8e8eË#ffb1b1L §4 aura lieu en en §cDécembre 2025 §4 !"));
			return true;
		}

		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("loadPack")) {
				try {
					String url = "https://download.mc-packs.net/pack/817eaf47fe7943733217d0d5660b6142530159ad.zip";
					p.setResourcePack(url);

				} catch (Exception eLoadPack3) {
					eLoadPack3.printStackTrace();
					p.sendMessage(main.getErrorPrefix() + "Impossible de charger le Resource Pack de Noël !");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("unloadPack")) {
				try {
					p.setResourcePack("https://download.mc-packs.net/pack/ded541de98cc129d3d5967f142a419a89ad4c254.zip"); //Default Pack
				} catch (Exception e) {
					e.printStackTrace();
					p.sendMessage(main.getErrorPrefix() + "Impossible de décharger le Resource Pack de Noël !");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {
				try {
					World mondeNoel = Bukkit.getWorld("noel");
					Location tpNoel = new Location(mondeNoel, 0, 39, 0);
					p.sendTitle("§a🚀 Téléportation...", main.hex("§2Téléportation au monde #ff4848N#ff6b6bO#ff8e8eË#ffb1b1L"), 20, 60, 20);
					p.teleport(tpNoel, PlayerTeleportEvent.TeleportCause.PLUGIN);
					try {
						String url = "https://download.mc-packs.net/pack/817eaf47fe7943733217d0d5660b6142530159ad.zip";
						p.setResourcePack(url);

					} catch (Exception eLoadPack1) {
						eLoadPack1.printStackTrace();
						p.sendMessage(main.getErrorPrefix() + "Impossible de charger le Resource Pack de Noël !");
					}
					p.sendMessage(main.getPrefix() + main.hex("§2Vous avez été téléporté au monde #ff4848N#ff6b6bO#ff8e8eË#ffb1b1L §2!"));
					return true;
				} catch (Exception e) {
					try {
						World mondeNoel = WorldCreator.name("noel").type(WorldType.FLAT).environment(Environment.NORMAL).createWorld();
						Location tpNoel = new Location(mondeNoel, 0, 39, 0);
						p.sendTitle("§a🚀 Téléportation...", main.hex("§2Téléportation au monde #ff4848N#ff6b6bO#ff8e8eË#ffb1b1L"), 20, 60, 20);
						p.teleport(tpNoel, PlayerTeleportEvent.TeleportCause.PLUGIN);
						p.sendMessage(main.getPrefix() + main.hex("§2Vous avez été téléporté au monde #ff4848N#ff6b6bO#ff8e8eË#ffb1b1L §2!"));
						try {
							String url = "https://download.mc-packs.net/pack/817eaf47fe7943733217d0d5660b6142530159ad.zip";
							p.setResourcePack(url);

						} catch (Exception eLoadPack2) {
							eLoadPack2.printStackTrace();
							p.sendMessage(main.getErrorPrefix() + "Impossible de charger le Resource Pack de Noël !");
						}
						return true;
					} catch(Exception e2) {
						e2.printStackTrace();
						p.sendMessage(main.getErrorPrefix() + main.hex("§4Impossible de vous téléporter au §cMonde #ff4848N#ff6b6bO#ff8e8eË#ffb1b1L§4 !"));
						return true;
					}
				}
			} else {
				p.sendMessage("§cSintax : /noel [args]");
				return true;
			}
		}

		try {
			World mondeNoel = Bukkit.getWorld("noel");
			Location tpNoel = new Location(mondeNoel, 0, 39, 0);
			p.sendTitle("§a🚀 Téléportation...", main.hex("§2Téléportation au monde #ff4848N#ff6b6bO#ff8e8eË#ffb1b1L"), 20, 60, 20);
			p.teleport(tpNoel, PlayerTeleportEvent.TeleportCause.PLUGIN);
			try {
				String url = "https://download.mc-packs.net/pack/817eaf47fe7943733217d0d5660b6142530159ad.zip";
				p.setResourcePack(url);

			} catch (Exception eLoadPack1) {
				eLoadPack1.printStackTrace();
				p.sendMessage(main.getErrorPrefix() + "Impossible de charger le Resource Pack de Noël !");
			}
			p.sendMessage(main.getPrefix() + main.hex("§2Vous avez été téléporté au monde #ff4848N#ff6b6bO#ff8e8eË#ffb1b1L §2!"));
			return true;
		} catch (Exception e) {
			try {
				World mondeNoel = WorldCreator.name("noel").type(WorldType.FLAT).environment(Environment.NORMAL).createWorld();
				Location tpNoel = new Location(mondeNoel, 0, 39, 0);
				p.sendTitle("§a🚀 Téléportation...", main.hex("§2Téléportation au monde #ff4848N#ff6b6bO#ff8e8eË#ffb1b1L"), 20, 60, 20);
				p.teleport(tpNoel, PlayerTeleportEvent.TeleportCause.PLUGIN);
				p.sendMessage(main.getPrefix() + main.hex("§2Vous avez été téléporté au monde #ff4848N#ff6b6bO#ff8e8eË#ffb1b1L §2!"));
				try {
					String url = "https://download.mc-packs.net/pack/817eaf47fe7943733217d0d5660b6142530159ad.zip";
					p.setResourcePack(url);

				} catch (Exception eLoadPack2) {
					eLoadPack2.printStackTrace();
					p.sendMessage(main.getErrorPrefix() + "Impossible de charger le Resource Pack de Noël !");
				}
				return true;
			} catch(Exception e2) {
				e2.printStackTrace();
				p.sendMessage(main.getErrorPrefix() + main.hex("§4Impossible de vous téléporter au §cMonde #ff4848N#ff6b6bO#ff8e8eË#ffb1b1L§4 !"));
				return true;
			}
		}

	}

}
