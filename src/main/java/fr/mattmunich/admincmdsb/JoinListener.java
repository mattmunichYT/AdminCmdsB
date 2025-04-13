package fr.mattmunich.admincmdsb;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ArmorStand.LockType;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.mattmunich.admincmdsb.commandhelper.Ban;
import fr.mattmunich.admincmdsb.commandhelper.GradeList;
import fr.mattmunich.admincmdsb.commandhelper.Grades;
import fr.mattmunich.admincmdsb.commandhelper.PlayerData;
import fr.mattmunich.admincmdsb.commandhelper.Settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;

@SuppressWarnings("unused")
public class JoinListener implements Listener{


	public static JoinListener jl;

	private final Main main;
	private final Grades grades;
	private final Ban ban;
	private final Settings settings;

	public JoinListener(Grades grades, Main main, Ban ban, Settings settings) {
		this.grades = grades;
		this.main = main;
		this.ban = ban;
		this.settings = settings;
	}




	@EventHandler
	public void onJoin(PlayerJoinEvent event) throws IOException {

		Player player = event.getPlayer();
		PlayerData data = new PlayerData(player);

		//get main world spawn
		Properties props = new Properties();
		props.load(Files.newInputStream(Paths.get("server.properties")));

		String name = props.getProperty("level-name");

		Location spawn = Objects.requireNonNull(Bukkit.getWorld(name)).getSpawnLocation();
		GradeList gradeList = grades.getPlayerGrade(player);
		Scoreboard score = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard();
		Team t = score.getTeam("nhide");
		player.teleport(spawn);

		if(t == null) {
		     t = score.registerNewTeam("nhide");
		     t.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
		     t.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
		     t.setAllowFriendlyFire(true);
		}

		if(settings.getAdvancedNameTags()) {
			t.addEntry(player.getName());
		}else {
			t.removeEntry(player.getName());
			t.unregister();
		}

		// MODIFY THIS FOR EVENT


		grades.loadPlayer(player);
		player.setScoreboard(grades.getScoreboard());

		player.setFlying(false);
		//Comment this for event
		if(grades.hasPowerSup(player, 89)) {
			main.superstaff.add(player);
			main.staff.add(player);
			main.mod.add(player);
			if(!player.isOp()) {
				player.setOp(true);
			}
		}
		//End Comment

		//UNcomment this for event if mod
//		if(grades.hasPowerSup(player, 89) && (player.getName().equalsIgnoreCase("OeilVariable769"))) {
//			main.superstaff.add(player);
//			main.staff.add(player);
//			main.mod.add(player);
//			if(!player.isOp()) {
//				player.setOp(true);
//			}
//		}
		//End UNcomment

		if(grades.hasPowerSup(player, 99)) {
			main.admin.add(player);
			main.superstaff.add(player);
			main.staff.add(player);
		}
		if(grades.hasPowerSup(player, 999)) {
			main.owner.add(player);
		}

		//Comment this for event
		if(grades.hasPower(player, 50)) {
			main.guides.add(player);
		}
		if(grades.hasPowerSup(player, 8)) {
			main.staff.add(player);
		}
		//End comment

		if(main.superstaff.contains(player) || main.admin.contains(player) || main.owner.contains(player)) {
			main.staff.add(player);
		}

		//END EVENT MODIFICATION

		String tPrefix = main.hex(grades.getPlayerGrade(player).getPrefix());
		String tSuffix = main.hex(grades.getPlayerGrade(player).getSuffix());

		player.setPlayerListName(tPrefix + player.getName() + tSuffix);
		player.setDisplayName(tPrefix + player.getName() + tSuffix);

		if(settings.getAdvancedNameTags()) {
			Location asloc = player.getLocation().add( 0, 1, 0);

			//Creating stand1
			ArmorStand stand1 = (ArmorStand) Objects.requireNonNull(asloc.getWorld()).spawnEntity(asloc, EntityType.ARMOR_STAND);

			stand1.setCustomNameVisible(false);
			stand1.setInvulnerable(true);
			stand1.setCanPickupItems(false);
			stand1.setCollidable(false);
			stand1.setVisualFire(false);
			stand1.addScoreboardTag(player.getName() + "CODEHA@#*2");
			stand1.setInvisible(true);
			stand1.addEquipmentLock(EquipmentSlot.HEAD, LockType.ADDING_OR_CHANGING);
			stand1.addEquipmentLock(EquipmentSlot.CHEST, LockType.ADDING_OR_CHANGING);
			stand1.addEquipmentLock(EquipmentSlot.LEGS, LockType.ADDING_OR_CHANGING);
			stand1.addEquipmentLock(EquipmentSlot.FEET, LockType.ADDING_OR_CHANGING);
			stand1.setArrowsInBody(0);
			stand1.setSilent(true);
			stand1.setGravity(false);
			stand1.setRemoveWhenFarAway(false);
			stand1.setPersistent(true);
			player.addPassenger(stand1);
			stand1.setAI(false);
			stand1.setMarker(true);
			stand1.setBasePlate(false);


			//Creating stand
			ArmorStand stand = (ArmorStand) asloc.getWorld().spawnEntity(asloc, EntityType.ARMOR_STAND);

			stand.setCustomName(main.hex(player.getDisplayName()));
			stand.setCustomNameVisible(true);
			//stand.setSmall(true);
			stand.setInvulnerable(true);
			stand.setCanPickupItems(false);
			stand.setCollidable(false);
			stand.setVisualFire(false);
			stand.addScoreboardTag(player.getName());
			stand.setVisible(false);
			stand.setInvisible(true);
			stand.addEquipmentLock(EquipmentSlot.HEAD, LockType.ADDING_OR_CHANGING);
			stand.addEquipmentLock(EquipmentSlot.CHEST, LockType.ADDING_OR_CHANGING);
			stand.addEquipmentLock(EquipmentSlot.LEGS, LockType.ADDING_OR_CHANGING);
			stand.addEquipmentLock(EquipmentSlot.FEET, LockType.ADDING_OR_CHANGING);
			stand.setArrowsInBody(0);
			stand.setBasePlate(false);
			stand.setGravity(false);
			stand.setRemoveWhenFarAway(false);
			stand.setPersistent(true);
			stand1.addPassenger(stand);
			stand.setAI(false);
			stand.setMarker(true);
		}

		try {
            assert data != null;
            if(data.isMuted() || data.isTempmuted()) {
				main.mute.add(player);
			}
		} catch(Exception e) {
			Bukkit.getConsoleSender().sendMessage("Hmm... Counld't get player's PlayerData so player might be bypassing mute !");
		}



		/*for (Player playersAdmin : main.admin){
			playersAdmin.setPlayerListName("§c[§4Administrateur§c] §6" + player.getName() + "§r");
			main.staff.add(player);
		}
		for(Player playersMod : main.mod) {
			playersMod.setPlayerListName("§a[§2Modérateur§a] §6" + player.getName() + "§r");
			main.staff.add(player);
		}
		for(Player playerYT : main.youtuber) {
			playerYT.setPlayerListName("§c[§4You§fTuber§c] §6" + player.getName() + "§r");
		}
		for(Player pChevalier : main.chevalier) {
			pChevalier.setPlayerListName("§b[§1§lChevalier§b] §3" + player.getName() + "§r");
		}
		for(Player playerMember : main.joueur) {
			playerMember.setPlayerListName("§3[§bJoueur§3] §1" + player.getName() + "§r");
		}*/


//		ItemStack customcompass = new ItemStack(Material.COMPASS, 1);
//		ItemMeta customcM = customcompass.getItemMeta();
//		customcM.setDisplayName("§6Boussole du serveur");
//		customcM.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 200, true);
//		customcM.setLore(Arrays.asList(" ","C'est la boussole super utile du serveur ", ":)", "(elle est cool)"));
//		customcM.setUnbreakable(true);
//		customcM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
//		customcM.addItemFlags(ItemFlag.HIDE_DESTROYS);
//		customcM.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
//		customcompass.setItemMeta(customcM);
//
//		player.getInventory().addItem(customcompass);
//
//		player.updateInventory();

//		ItemStack luckyB = new ItemStack(Material.PLAYER_HEAD, 1);
//		SkullMeta meta = (SkullMeta) luckyB.getItemMeta();
//		meta.setOwningPlayer(Bukkit.getOfflinePlayer("luck"));
//		meta.setDisplayName("§eLucky Block");
//		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//		luckyB.setItemMeta(meta);
//
//		player.getInventory().addItem(luckyB);
//		player.getInventory().addItem(luckyB);
//		player.getInventory().addItem(luckyB);
//		player.getInventory().addItem(luckyB);



		if(!main.banni.contains(player)) {
			if(!(Objects.equals(settings.getServerName(), ""))) {
				player.sendMessage(main.hex("#3afb00B#41fa00o#48fa00n#50f900j#57f900o#5ef800u#65f700r #6df700à #74f600t#7bf600o#82f500i #89f400" + player.getDisplayName() + " #91f400e#98f300t #9ff300b#a6f200i#aef200e#b5f100n#bcf000v#c3f000e#caef00n#d2ef00u#d9ee00e #e0ed00s#e7ed00u#efec00r #f6ec00" + settings.getServerName() + " #fdeb00!"));
			} else {
				player.sendMessage("§2Bonjour à toi " + player.getDisplayName());
			}

			if(settings.getCoMsg()) {
				event.setJoinMessage( player.getDisplayName() + "§e s'est connecté");
			}
			if(!settings.getCoMsg()){
				event.setJoinMessage("");
			}
			if(settings.getMaintenance()) {
				event.setJoinMessage("");
			}

		} else {
			event.setJoinMessage("");
		}

		return;
	}
}
