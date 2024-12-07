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

public class SRP_TPCommand implements CommandExecutor {

	private MonPlugin main;

	public SRP_TPCommand(MonPlugin main) {
		this.main = main;
	}

	@SuppressWarnings("unused")
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String l, String[] args) {

		//EVENT PAQUES
//		s.sendMessage(main.getErrorPrefix() + "Cette commande est désactivée durant l'Évent de Pâques !");
//		return true;
		//END

		if (!(s instanceof Player) || s instanceof BlockCommandSender) {
			s.sendMessage(main.getPrefix() + "§4Vous devez être un joueur pour utiliser cette commande !");
			if (args[0].equalsIgnoreCase("loadWorld")) {
				try {
					World mondeRP = Bukkit.getWorld("survieRP");
					World mondeRPNether = Bukkit.getWorld("survieRP_nether");
					World mondeRPEnd = Bukkit.getWorld("survieRP_end");
					mondeRP.loadChunk(1, 1);
					mondeRPNether.loadChunk(1, 1);
					mondeRPEnd.loadChunk(1, 1);
					Location tpSRP = mondeRP.getSpawnLocation();
					Location tpSRPNether = mondeRP.getSpawnLocation();
					Location tpSRPEnd = mondeRP.getSpawnLocation();
					s.sendMessage(main.getPrefix() + "§2Le monde §aSurvie RP a été chargé !");
				} catch (Exception e) {
					Bukkit.getConsoleSender().sendMessage(main.getErrorPrefix()
							+ "§cUne erreur s'est produite lors du chargement des mondes RP, création des mondes");
					World mondeRP = WorldCreator.name("survieRP").environment(Environment.NORMAL).type(WorldType.NORMAL)
							.createWorld();
					World mondeRPNether = WorldCreator.name("survieRP_nether").environment(Environment.NETHER)
							.type(WorldType.NORMAL).createWorld();
					World mondeRPEnd = WorldCreator.name("survieRP_end").environment(Environment.THE_END)
							.type(WorldType.NORMAL).createWorld();
				}
			}
			return true;
		}

		Player p = (Player) s;

		if(!main.admin.contains(p)) {
			p.sendMessage(main.errorPrefix + "Le Survie-RP n'est plus ouvert au public !\n§eConsultez le salon");
			return true;
		}

		if (args.length == 1 || args.length == 2) {
			if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport")) {
				if (args.length == 2 && (args[1].equalsIgnoreCase("end") && args[1] != null)) {
					try {
						World mondeRP = Bukkit.getWorld("survieRP");
						World mondeRPNether = Bukkit.getWorld("survieRP_nether");
						World mondeRPEnd = Bukkit.getWorld("survieRP_end");
						Location tpSRP = mondeRPEnd.getSpawnLocation();
						p.sendTitle("§a🚀 Téléportation...", main.hex("§2Téléportation au monde §aSurvie RP §0- §5End"),
								20, 60, 20);
						p.teleport(tpSRP, TeleportCause.PLUGIN);
						p.sendMessage(main.getPrefix()
								+ main.hex("§2Vous avez été téléporté au monde §aSurvie RP §0- §5End §2!"));
						return true;
					} catch (Exception e) {
						try {
							World mondeRP = WorldCreator.name("survieRP").environment(Environment.NORMAL)
									.type(WorldType.NORMAL).createWorld();
							World mondeRPNether = WorldCreator.name("survieRP_nether").environment(Environment.NETHER)
									.type(WorldType.NORMAL).createWorld();
							World mondeRPEnd = WorldCreator.name("survieRP_end").environment(Environment.THE_END)
									.type(WorldType.NORMAL).createWorld();
							Location tpSRP = mondeRPEnd.getSpawnLocation();
							p.sendTitle("§a🚀 Téléportation...",
									main.hex("§2Téléportation au monde §aSurvie RP §0- §5End"), 20, 60, 20);
							p.teleport(tpSRP, TeleportCause.PLUGIN);
							p.sendMessage(main.getPrefix()
									+ main.hex("§2Vous avez été téléporté au monde §aSurvie RP §0- §5End §2!"));

							return true;
						} catch (Exception e2) {
							e2.printStackTrace();
							p.sendMessage(main.getErrorPrefix()
									+ main.hex("§4Impossible de vous téléporter au §cMonde Survie RP §0- §5End§4 !"));
							return true;
						}
					}
				} else if (args.length == 2 && (args[1].equalsIgnoreCase("nether") && args[1] != null)) {

					try {
						World mondeRP = Bukkit.getWorld("survieRP");
						World mondeRPNether = Bukkit.getWorld("survieRP_nether");
						World mondeRPEnd = Bukkit.getWorld("survieRP_end");
						Location tpSRP = mondeRPNether.getSpawnLocation();
						p.sendTitle("§a🚀 Téléportation...",
								main.hex("§2Téléportation au monde §aSurvie RP §0- §4Nether"), 20, 60, 20);
						p.teleport(tpSRP, TeleportCause.PLUGIN);
						p.sendMessage(main.getPrefix()
								+ main.hex("§2Vous avez été téléporté au monde §aSurvie RP §0- §4Nether §2!"));
						return true;
					} catch (Exception e) {
						try {
							World mondeRP = WorldCreator.name("survieRP").environment(Environment.NORMAL)
									.type(WorldType.NORMAL).createWorld();
							World mondeRPNether = WorldCreator.name("survieRP_nether").environment(Environment.NETHER)
									.type(WorldType.NORMAL).createWorld();
							World mondeRPEnd = WorldCreator.name("survieRP_end").environment(Environment.THE_END)
									.type(WorldType.NORMAL).createWorld();
							Location tpSRP = mondeRPNether.getSpawnLocation();
							p.sendTitle("§a🚀 Téléportation...",
									main.hex("§2Téléportation au monde §aSurvie RP §0- §4Nether"), 20, 60, 20);
							p.teleport(tpSRP, TeleportCause.PLUGIN);
							p.sendMessage(main.getPrefix()
									+ main.hex("§2Vous avez été téléporté au monde §aSurvie RP §0- §4Nether §2!"));

							return true;
						} catch (Exception e2) {
							e2.printStackTrace();
							p.sendMessage(main.getErrorPrefix() + main
									.hex("§4Impossible de vous téléporter au §cMonde Survie RP §0- §c§bNether §r§4!"));
							return true;
						}
					}
				} else if (args.length == 2 && (args[1].equalsIgnoreCase("overworld") && args[1] != null)) {
					try {
						World mondeRP = Bukkit.getWorld("survieRP");
						World mondeRPNether = Bukkit.getWorld("survieRP_nether");
						World mondeRPEnd = Bukkit.getWorld("survieRP_end");
						Location tpSRP = mondeRP.getSpawnLocation();
						p.sendTitle("§a🚀 Téléportation...", main.hex("§2Téléportation au monde §aSurvie RP"), 20, 60,
								20);
						p.teleport(tpSRP, TeleportCause.PLUGIN);
						p.sendMessage(
								main.getPrefix() + main.hex("§2Vous avez été téléporté au monde §aSurvie RP §2!"));
						return true;
					} catch (Exception e) {
						try {
							World mondeRP = WorldCreator.name("survieRP").environment(Environment.NORMAL)
									.type(WorldType.NORMAL).createWorld();
							World mondeRPNether = WorldCreator.name("survieRP_nether").environment(Environment.NETHER)
									.type(WorldType.NORMAL).createWorld();
							World mondeRPEnd = WorldCreator.name("survieRP_end").environment(Environment.THE_END)
									.type(WorldType.NORMAL).createWorld();
							Location tpSRP = mondeRP.getSpawnLocation();
							p.sendTitle("§a🚀 Téléportation...", main.hex("§2Téléportation au monde §aSurvie RP"), 20,
									60, 20);
							p.teleport(tpSRP, TeleportCause.PLUGIN);
							p.sendMessage(
									main.getPrefix() + main.hex("§2Vous avez été téléporté au monde §aSurvie RP §2!"));

							return true;
						} catch (Exception e2) {
							e2.printStackTrace();
							p.sendMessage(main.getErrorPrefix()
									+ main.hex("§4Impossible de vous téléporter au §cMonde Survie RP§4 !"));
							return true;
						}
					}
				} else {
					try {
						World mondeRP = Bukkit.getWorld("survieRP");
						World mondeRPNether = Bukkit.getWorld("survieRP_nether");
						World mondeRPEnd = Bukkit.getWorld("survieRP_end");
						Location tpSRP = mondeRP.getSpawnLocation();
						p.sendTitle("§a🚀 Téléportation...", main.hex("§2Téléportation au monde §aSurvie RP"), 20, 60,
								20);
						p.teleport(tpSRP, TeleportCause.PLUGIN);
						p.sendMessage(
								main.getPrefix() + main.hex("§2Vous avez été téléporté au monde §aSurvie RP §2!"));
						return true;
					} catch (Exception e) {
						try {
							World mondeRP = WorldCreator.name("survieRP").environment(Environment.NORMAL)
									.type(WorldType.NORMAL).createWorld();
							World mondeRPNether = WorldCreator.name("survieRP_nether").environment(Environment.NETHER)
									.type(WorldType.NORMAL).createWorld();
							World mondeRPEnd = WorldCreator.name("survieRP_end").environment(Environment.THE_END)
									.type(WorldType.NORMAL).createWorld();
							Location tpSRP = mondeRP.getSpawnLocation();
							p.sendTitle("§a🚀 Téléportation...", main.hex("§2Téléportation au monde §aSurvie RP"), 20,
									60, 20);
							p.teleport(tpSRP, TeleportCause.PLUGIN);
							p.sendMessage(
									main.getPrefix() + main.hex("§2Vous avez été téléporté au monde §aSurvie RP §2!"));

							return true;
						} catch (Exception e2) {
							e2.printStackTrace();
							p.sendMessage(main.getErrorPrefix()
									+ main.hex("§4Impossible de vous téléporter au §cMonde Survie RP§4 !"));
							return true;
						}
					}
				}
			} else {
				p.sendMessage("§cSintax : /survietp [args]");
				return true;
			}
		}

		try {
			World mondeRP = Bukkit.getWorld("survieRP");
			World mondeRPNether = Bukkit.getWorld("survieRP_nether");
			World mondeRPEnd = Bukkit.getWorld("survieRP_end");
			Location tpSRP = mondeRP.getSpawnLocation();
			p.sendTitle("§a🚀 Téléportation...", main.hex("§2Téléportation au monde §sSurvie RP"), 20, 60, 20);
			p.teleport(tpSRP, TeleportCause.PLUGIN);

			p.sendMessage(main.getPrefix() + main.hex("§2Vous avez été téléporté au monde §sSurvie RP §2!"));
			return true;
		} catch (Exception e) {
			try {
				World mondeRP = WorldCreator.name("survieRP").environment(Environment.NORMAL).type(WorldType.NORMAL)
						.createWorld();
				World mondeRPNether = WorldCreator.name("survieRP_nether").environment(Environment.NETHER)
						.type(WorldType.NORMAL).createWorld();
				World mondeRPEnd = WorldCreator.name("survieRP_end").environment(Environment.THE_END)
						.type(WorldType.NORMAL).createWorld();
				Location tpSRP = mondeRP.getSpawnLocation();
				p.sendTitle("§a🚀 Téléportation...", main.hex("§2Téléportation au monde §aSurvie RP"), 20, 60, 20);
				p.teleport(tpSRP, TeleportCause.PLUGIN);
				p.sendMessage(main.getPrefix() + main.hex("§2Vous avez été téléporté au monde §aSurvie RP §2!"));

				return true;
			} catch (Exception e2) {
				e2.printStackTrace();
				p.sendMessage(
						main.getErrorPrefix() + main.hex("§4Impossible de vous téléporter au §cMonde Survie RP !"));
				return true;
			}
		}

	}

}
