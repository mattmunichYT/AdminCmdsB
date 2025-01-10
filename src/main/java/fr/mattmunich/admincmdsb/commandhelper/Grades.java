package fr.mattmunich.admincmdsb.commandhelper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import com.google.common.collect.Maps;

import fr.mattmunich.admincmdsb.Main;

public final class Grades {

	private final Map<String, GradeList> playerGrades = Maps.newHashMap();
	private Scoreboard scoreboard;
	private final Plugin plugin;
	private final Main main;

	private FileConfiguration config;
	private File file;

	public Grades(Plugin plugin, Main main) {
		this.plugin = plugin;
		this.main = main;
		initScoreboard();
	}

	public final Plugin getPlugin(){
		return plugin;
	}
	public final Scoreboard getScoreboard() {
		return scoreboard;
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public void initConfig() {

		File f = new File("plugins/AdminCmdsB/PlayerData");
		if(!f.exists()) {
			f.mkdirs();
		}
		file = new File(f, "grades.yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
			}catch(IOException ioe) { ioe.printStackTrace();}
		}

		config = YamlConfiguration.loadConfiguration(file);

	}

	@SuppressWarnings("unlikely-arg-type")
	public void initScoreboard() {

		if(scoreboard != null) {
			throw new UnsupportedOperationException("[AdminCmdsB] Scoreboard déjà initialisé. - THROW ERROR");
		}

		try {
			ScoreboardManager scMan = Bukkit.getScoreboardManager();
			scoreboard = scMan.getMainScoreboard();


			for(GradeList gradeList : GradeList.values()){
				if(scoreboard.getTeams().contains(gradeList.getName())) {
					try {
						scoreboard.getTeams().remove(scoreboard.getTeam(gradeList.getName()));
						scoreboard.getTeams().forEach(t -> {
							if(t.getName() == gradeList.getName()) {
								t.unregister();
								Bukkit.getConsoleSender().sendMessage("[AdminCmdsB] Unregistred team \"" + gradeList.getName() + "\", then re registering it !");
							}
						});
						Team team = scoreboard.registerNewTeam(gradeList.getName());
						team.setPrefix(main.hex(gradeList.getPrefix()));
						team.setSuffix(main.hex(gradeList.getSuffix()));
						team.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.ALWAYS);
						team.setOption(Option.COLLISION_RULE,OptionStatus.NEVER);
						scoreboard.getTeams().add(team);
					} catch (Exception Error) {
						//Error.printStackTrace();
						Bukkit.getConsoleSender().sendMessage("[AdminCmdsB] An error encourred : " + Error.getMessage() + " ; Probably : Team \"" + gradeList.getName() + "\" already created (in catch)");
					}
				} else {
					try {
						Team team = scoreboard.registerNewTeam(gradeList.getName());
						team.setPrefix(main.hex(gradeList.getPrefix()));
						team.setSuffix(main.hex(gradeList.getSuffix()));
						team.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.ALWAYS);
						team.setOption(Option.COLLISION_RULE,OptionStatus.NEVER);
						scoreboard.getTeams().add(team);
					} catch (Exception Heree) {
						//Bukkit.getConsoleSender().sendMessage("[AdminCmdsB] A minor error encourred : " + Heree.getMessage() + " ; Probably : Team \"" + gradeList.getName() + "\" already created (in else - catch)");
					}
				}

			}
		} catch(NullPointerException nullError) {

			Bukkit.getConsoleSender().sendMessage("[AdminCmdsB] Impossible d'obtenir scMan.getNewScoreBoard() : NullPointerException. - CATCH");
		} catch(Exception e) {
			e.printStackTrace();

			Bukkit.getConsoleSender().sendMessage("[AdminCmdsB] Scoreboard déjà initialisé. - CATCH");
		}





	}

	public void loadPlayer(Player player) {
		String uuid = player.getUniqueId().toString();

		if(playerGrades.containsKey(uuid)) {
			return;
		}
		if(!config.contains(uuid)) {
			chageRank(uuid, GradeList.MEMBRE);
		}

		playerGrades.put(uuid, getGradeById(config.getInt(uuid)));
		scoreboard.getTeam(playerGrades.get(uuid).getName()).addEntry(player.getName());

	}

	public void deletePlayer(Player player) {
		if(!playerGrades.containsKey(player.getUniqueId().toString())) {
			return;
		}
		playerGrades.remove(player.getUniqueId().toString());
	}

	public void loadAS(ArmorStand stand) {
		String uuid = stand.getUniqueId().toString();

		if(playerGrades.containsKey(uuid)) {
			return;
		}
		if(!config.contains(uuid)) {
			chageRank(uuid, GradeList.AS);
		}

		playerGrades.put(uuid, getGradeById(config.getInt(uuid)));
		scoreboard.getTeam(playerGrades.get(uuid).getName()).addEntry(stand.getName());

	}

	public void deleteAS(ArmorStand stand) {
		if(!playerGrades.containsKey(stand.getUniqueId().toString())) {
			return;
		}
		playerGrades.remove(stand.getUniqueId().toString());
	}

	public GradeList getPlayerGrade(Player player) {
		if(!playerGrades.containsKey(player.getUniqueId().toString())) {
			loadPlayer(player);
		}
		return playerGrades.get(player.getUniqueId().toString());
	}

	public GradeList getOffPlayerGrade(OfflinePlayer player) {
		String uuid = player.getUniqueId().toString();
		if(!playerGrades.containsKey(uuid)) {
			loadPlayer(player.getPlayer());
			try {
				GradeList grade = playerGrades.get(uuid);
				return grade;
			} catch(Exception e) {
				Bukkit.getConsoleSender().sendMessage("Une erreur s'est produite !!!!!! : " + e);
				return GradeList.MEMBRE;
			}
		}
		try {
			GradeList grade = playerGrades.get(uuid);
			return grade;
		} catch(Exception e) {
			Bukkit.getConsoleSender().sendMessage("Une erreur s'est produite !!!!!! : " + e);
			return GradeList.MEMBRE;
		}

	}

	public GradeList getASGrade(ArmorStand stand) {
		if(!playerGrades.containsKey(stand.getUniqueId().toString())) {
			loadAS(stand);
		}
		return playerGrades.get(stand.getUniqueId().toString());
	}

	public GradeList getGradeById(int id) {
		for(GradeList gradeList : GradeList.values()) {
			if(gradeList.getId() == id) {
				return gradeList;
			}
		}

		return GradeList.MEMBRE;
	}

	public void saveConfig() {
		try {
			config.save(file);
		}catch(IOException ioe) { ioe.printStackTrace();}
	}

	public boolean hasPower(Player player, int power) {
		return (getPlayerGrade(player).getPower() == power);
	}
	public boolean hasPowerSup(Player player, int power) {
		return (getPlayerGrade(player).getPower() > power);
	}
	public boolean hasPowerInf(Player player, int power) {
		return (getPlayerGrade(player).getPower() < power);
	}

	public void chageRank(String uuid, GradeList gradeList) {
		config.set(uuid, gradeList.getId());
		saveConfig();
	}

}
