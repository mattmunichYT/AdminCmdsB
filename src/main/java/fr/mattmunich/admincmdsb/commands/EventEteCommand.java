package fr.mattmunich.admincmdsb.commands;

import fr.mattmunich.admincmdsb.Main;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.math.BigInteger;

public class EventEteCommand implements CommandExecutor {

	private final Main main;

	public EventEteCommand(Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {
		String url = "https://drive.google.com/uc?export=download&id=1ZRf-nS523M_jZPtNUe3CdLalRZl3dOSA";

		if (!(s instanceof Player) || s instanceof BlockCommandSender) {
			s.sendMessage(main.getPrefix() + "§4Vous devez être un joueur pour utiliser cette commande !");
			if (args[0].equalsIgnoreCase("loadWorld")) {
				try {
					World mondeHalloween = Bukkit.getWorld("event-ete-2024");
                    assert mondeHalloween != null;
                    mondeHalloween.loadChunk(0, 0);
					s.sendMessage(main.getPrefix() + main.hex("§2Le monde #FCD05C§lÉ#FBDF94§lT#FAEDCB§lÉ a été chargé !"));
				} catch (Exception e) {
					s.sendMessage(main.getPrefix() + main.hex("§2Le monde #FCD05C§lÉ#FBDF94§lT#FAEDCB§lÉ a été créé !"));
					World mondeHalloween = WorldCreator.name("event-ete-2024").type(WorldType.FLAT)
							.environment(Environment.NORMAL).generateStructures(false).createWorld();
                    assert mondeHalloween != null;
                    mondeHalloween.loadChunk(0, 0);
				}
			}
			return true;
		}

		Player p = (Player) s;
// ANTI JOIN
		if (!main.staff.contains(p)) {
			p.sendMessage(main.getPrefix() + main.hex(
					"§2Le prochain Évent de l'#FCD05C§lÉ#FBDF94§lT#FAEDCB§lÉ aura lieu en juin, un peu de patience !"));
			return true;
		}
// END ANTI JOIN

		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("loadPack")) {
				try {
					//p.setResourcePack(url);
				} catch (Exception eLoadPack3) {
					eLoadPack3.printStackTrace();
					p.sendMessage(main.getErrorPrefix()
							+ "Impossible de charger le Resource Pack de #FCD05C§lÉ#FBDF94§lT#FAEDCB§lÉ !");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("unloadPack")) {
				if(p.getLocation().getWorld().getName().equalsIgnoreCase("event-ete-2024")) {
					p.sendMessage(main.getPrefix() + "§4Vous ne pouvez pas décharger le pack de Halloween dans le monde de Halloween");
					return true;
				}

				try {
					p.removeResourcePacks();
					p.sendMessage(main.getPrefix() + "§2Les Resource Packs ont été déchargés");
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					p.sendMessage(main.hex(main.getErrorPrefix()
							+ "Impossible de décharger le Resource Pack de #FCD05C§lÉ#FBDF94§lT#FAEDCB§lÉ !"));
				}
			} else if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {
				try {
					World mondeHalloween = Bukkit.getWorld("event-ete-2024");
					Location tpHalloween = new Location(mondeHalloween, -27.5, 142, -63.5);
					p.sendTitle("§a🚀 Téléportation...",
							main.hex("§2Téléportation au monde #FCD05C§lÉ#FBDF94§lT#FAEDCB§lÉ"), 20,
							60, 20);
					p.teleport(tpHalloween, TeleportCause.PLUGIN);
					try {
						//p.setResourcePack(url);
					} catch (Exception eLoadPack1) {
						eLoadPack1.printStackTrace();
						p.sendMessage(main.getErrorPrefix() + "Impossible de charger le Resource Pack de §6Halloween !");
					}
					p.sendMessage(main.getPrefix() + main.hex(
							"§2Vous avez été téléporté au monde #FCD05C§lÉ#FBDF94§lT#FAEDCB§lÉ §2!"));
					return true;
				} catch (Exception e) {
					try {
						World mondeHalloween = WorldCreator.name("event-ete-2024").type(WorldType.FLAT)
								.environment(Environment.NORMAL).createWorld();
						Location tpHalloween = new Location(mondeHalloween, -27.5, 142, -63.5);
						p.sendTitle("§a🚀 Téléportation...",
								main.hex("§2Téléportation au monde #FCD05C§lÉ#FBDF94§lT#FAEDCB§lÉ"),
								20, 60, 20);
						p.teleport(tpHalloween, TeleportCause.PLUGIN);
						p.sendMessage(main.getPrefix() + main.hex(
								"§2Vous avez été téléporté au monde #FCD05C§lÉ#FBDF94§lT#FAEDCB§lÉ §2!"));
						try {
							
							//p.setResourcePack(url);
						} catch (Exception eLoadPack2) {
							eLoadPack2.printStackTrace();
							p.sendMessage(main.getErrorPrefix()
									+ "Impossible de charger le Resource Pack de #FCD05C§lÉ#FBDF94§lT#FAEDCB§lÉ !");
						}
						return true;
					} catch (Exception e2) {
						e2.printStackTrace();
						p.sendMessage(main.getErrorPrefix() + main.hex(
								"§4Impossible de vous téléporter au §cMonde #FCD05C§lÉ#FBDF94§lT#FAEDCB§lÉ§4 !"));
						return true;
					}
				}
			} else {
				p.sendMessage("§cSintax : /halloween [args]");
				return true;
			}
		}

		try {
			World mondeHalloween = Bukkit.getWorld("event-ete-2024");
			Location tpHalloween = new Location(mondeHalloween, -27.5, 142, -63.5);
			p.sendTitle("§a🚀 Téléportation...",
					main.hex("§2Téléportation au monde de #FCD05C§lÉ#FBDF94§lT#FAEDCB§lÉ"), 20, 60,
					20);
			p.teleport(tpHalloween, TeleportCause.PLUGIN);
			try {
				byte[] hash = decodeUsingBigInteger("068b0f86ad580b98e38b950fb7db97584e62ecc3");
				//p.setResourcePack(url);
//				p.addResourcePack(UUID.randomUUID(),url,hash,"§2Veuillez accepter de l'installer le pack de l'Évent de l'§6Halloween§2.",true);
//				byte[] hash1 = decodeUsingBigInteger("51ad5133dac8a9c0dc024498e93c09f7eddd907b");
//				p.addResourcePack(UUID.randomUUID(),url1,hash1,"§2Veuillez accepter de l'installer le 2ème pack de l'Évent de l'§6Halloween§2.",true);

			} catch (Exception eLoadPack1) {
				eLoadPack1.printStackTrace();
				p.sendMessage(main.getErrorPrefix()
						+ "Impossible de charger le Resource Pack de #FCD05C§lÉ#FBDF94§lT#FAEDCB§lÉ !");
			}
			p.sendMessage(main.getPrefix() + main
					.hex("§2Vous avez été téléporté au monde #FCD05C§lÉ#FBDF94§lT#FAEDCB§lÉ §2!"));
			return true;
		} catch (Exception e) {
			try {
				World mondeHalloween = WorldCreator.name("event-ete-2024").type(WorldType.FLAT)
						.environment(Environment.NORMAL).createWorld();
				Location tpHalloween = new Location(mondeHalloween, -27.5, 142, -63.5);
				p.sendTitle("🚀 Téléportation...",
						main.hex("§2Téléportation au monde #FCD05C§lÉ#FBDF94§lT#FAEDCB§lÉ"), 20, 60,
						20);
				p.teleport(tpHalloween, TeleportCause.PLUGIN);
				p.sendMessage(main.getPrefix() + main.hex(
						"§2Vous avez été téléporté au monde #FCD05C§lÉ#FBDF94§lT#FAEDCB§lÉ §2!"));
				try {
					//p.setResourcePack(url);
				} catch (Exception eLoadPack2) {
					eLoadPack2.printStackTrace();
					p.sendMessage(main.getErrorPrefix()
							+ "Impossible de charger le Resource Pack de #FCD05C§lÉ#FBDF94§lT#FAEDCB§lÉ !");
				}
				return true;
			} catch (Exception e2) {
				e2.printStackTrace();
				p.sendMessage(main.getErrorPrefix() + main.hex(
						"§4Impossible de vous téléporter au §cMonde #FCD05C§lÉ#FBDF94§lT#FAEDCB§lÉ§4 !"));
				return true;
			}
		}

	}

	private byte[] decodeUsingBigInteger(String hexString) {
		byte[] byteArray = new BigInteger(hexString, 16)
				.toByteArray();
		if (byteArray[0] == 0) {
			byte[] output = new byte[byteArray.length - 1];
			System.arraycopy(
					byteArray, 1, output,
					0, output.length);
			return output;
		}
		return byteArray;
	}

}
