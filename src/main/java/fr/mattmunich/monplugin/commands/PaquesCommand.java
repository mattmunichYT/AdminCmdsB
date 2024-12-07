package fr.mattmunich.monplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import fr.mattmunich.monplugin.MonPlugin;

public class PaquesCommand implements CommandExecutor {

	private MonPlugin main;
	private String url;

	public PaquesCommand(MonPlugin main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {
		if (!(s instanceof Player) || s instanceof BlockCommandSender) {
			s.sendMessage(main.getPrefix() + "§4Vous devez être un joueur pour utiliser cette commande !");
			if (args[0].equalsIgnoreCase("loadWorld")) {
				try {
					World mondePaques = Bukkit.getWorld("event-paques-2024");
					mondePaques.loadChunk(0, 0);
					s.sendMessage(main.getPrefix()
							+ "§2Le monde #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S§2 a été chargé !");
				} catch (Exception e) {
					s.sendMessage(main.getPrefix()
							+ "§2Le monde #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S§2 a été créé !");
					World mondePaques = WorldCreator.name("event-paques-2024").type(WorldType.FLAT)
							.environment(Environment.NORMAL).generateStructures(false).createWorld();
					mondePaques.loadChunk(0, 0);
				}
			}
			return true;
		}

		Player p = (Player) s;
//		p.sendMessage(main.getPrefix() + main.hex(
//					"§cLe monde de l'Évent #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S§2 §cn'est pas ouvert au public ! §aProchain Évent de #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S en avril 2026."));
//		return true;
		if (!main.staff.contains(p) && !p.isOp()) {
			p.sendMessage(main.getPrefix() + main.hex(
					"§cLe monde de l'Évent #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S§2 §cn'est pas ouvert au public ! §aProchain Évent de #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S en avril 2026."));
			return true;
		}

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("loadPack")) {
				try {
					String url = "https://download.mc-packs.net/pack/603931d51b0b929a63a64a4c627e2aa7b9ee2279.zip";
					p.setResourcePack(url);

				} catch (Exception eLoadPack3) {
					eLoadPack3.printStackTrace();
					p.sendMessage(main.getErrorPrefix()
							+ "Impossible de charger le Resource Pack de #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S !");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("unloadPack")) {
				try {
					p.setResourcePack(""); // Default Pack
				} catch (Exception e) {
					e.printStackTrace();
					p.sendMessage(main.getErrorPrefix()
							+ "Impossible de décharger le Resource Pack de #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S !");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {
				try {
					World mondePaques = Bukkit.getWorld("event-paques-2024");
					Location tpPaques = new Location(mondePaques, -27.5, 142, -63.5);
					p.sendTitle("§a🚀 Téléportation...",
							main.hex("§2Téléportation au monde #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S"), 20,
							60, 20);
					p.teleport(tpPaques, TeleportCause.PLUGIN);
					try {
						String url = "https://download.mc-packs.net/pack/603931d51b0b929a63a64a4c627e2aa7b9ee2279.zip";
						p.setResourcePack(url);

					} catch (Exception eLoadPack1) {
						eLoadPack1.printStackTrace();
						p.sendMessage(main.getErrorPrefix() + "Impossible de charger le Resource Pack de §aPâques !");
					}
					p.sendMessage(main.getPrefix() + main.hex(
							"§2Vous avez été téléporté au monde #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S §2!"));
					return true;
				} catch (Exception e) {
					try {
						World mondePaques = WorldCreator.name("event-paques-2024").type(WorldType.FLAT)
								.environment(Environment.NORMAL).createWorld();
						Location tpPaques = new Location(mondePaques, -27.5, 142, -63.5);
						p.sendTitle("§a🚀 Téléportation...",
								main.hex("§2Téléportation au monde #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S"),
								20, 60, 20);
						p.teleport(tpPaques, TeleportCause.PLUGIN);
						p.sendMessage(main.getPrefix() + main.hex(
								"§2Vous avez été téléporté au monde #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S §2!"));
						try {
							String url = "https://download.mc-packs.net/pack/603931d51b0b929a63a64a4c627e2aa7b9ee2279.zip";
							p.setResourcePack(url);

						} catch (Exception eLoadPack2) {
							eLoadPack2.printStackTrace();
							p.sendMessage(main.getErrorPrefix()
									+ "Impossible de charger le Resource Pack de #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S !");
						}
						return true;
					} catch (Exception e2) {
						e2.printStackTrace();
						p.sendMessage(main.getErrorPrefix() + main.hex(
								"§4Impossible de vous téléporter au §cMonde #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S§4 !"));
						return true;
					}
				}
			} else {
				p.sendMessage("§cSintax : /paques [args]");
				return true;
			}
		}

		try {
			World mondePaques = Bukkit.getWorld("event-paques-2024");
			Location tpPaques = new Location(mondePaques, -27.5, 142, -63.5);
			p.sendTitle("§a🚀 Téléportation...",
					main.hex("§2Téléportation au monde de #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S"), 20, 60,
					20);
			p.teleport(tpPaques, TeleportCause.PLUGIN);
			try {
				String url = "https://download.mc-packs.net/pack/603931d51b0b929a63a64a4c627e2aa7b9ee2279.zip";
				p.setResourcePack(url);

			} catch (Exception eLoadPack1) {
				eLoadPack1.printStackTrace();
				p.sendMessage(main.getErrorPrefix()
						+ "Impossible de charger le Resource Pack de #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S !");
			}
			p.sendMessage(main.getPrefix() + main
					.hex("§2Vous avez été téléporté au monde #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S §2!"));
			return true;
		} catch (Exception e) {
			try {
				World mondePaques = WorldCreator.name("event-paques-2024").type(WorldType.FLAT)
						.environment(Environment.NORMAL).createWorld();
				Location tpPaques = new Location(mondePaques, -27.5, 142, -63.5);
				p.sendTitle("§a🚀 Téléportation...",
						main.hex("§2Téléportation au monde #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S"), 20, 60,
						20);
				p.teleport(tpPaques, TeleportCause.PLUGIN);
				p.sendMessage(main.getPrefix() + main.hex(
						"§2Vous avez été téléporté au monde #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S §2!"));
				try {
					String url = "https://download.mc-packs.net/pack/603931d51b0b929a63a64a4c627e2aa7b9ee2279.zip";
					p.setResourcePack(url);

				} catch (Exception eLoadPack2) {
					eLoadPack2.printStackTrace();
					p.sendMessage(main.getErrorPrefix()
							+ "Impossible de charger le Resource Pack de #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S !");
				}
				return true;
			} catch (Exception e2) {
				e2.printStackTrace();
				p.sendMessage(main.getErrorPrefix() + main.hex(
						"§4Impossible de vous téléporter au §cMonde #0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S§4 !"));
				return true;
			}
		}

	}

}
