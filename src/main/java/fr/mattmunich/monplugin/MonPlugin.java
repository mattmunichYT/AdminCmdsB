package fr.mattmunich.monplugin;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.mattmunich.monplugin.commandhelper.*;
import fr.mattmunich.monplugin.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.mattmunich.monplugin.anticheat.Movement_AntiCheat;
import fr.mattmunich.monplugin.ban.PlayerLoginEvent_Ban;
import fr.mattmunich.monplugin.ban.PlayerLoginEvent_TempBan;
import fr.mattmunich.monplugin.eventscmd.EventsFreeze;

@SuppressWarnings({"unused", "SpellCheckingInspection", "DataFlowIssue"})
public class MonPlugin extends JavaPlugin {
	/*
	* HOW TO INSTALL NMS:
	* - GO TO https://www.spigotmc.org AND DOWNLOAD LATEST BUILDTOOLS
	* - RUN BUILDTOOLS
	* - WHEN DONE, GO TO pom.xml (in this project)
	* - CHANGE VERSION TO THE BUILT VERSION (most likely the latest)
	* */



	public String hex(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

	public String getMessage(String path, String defaultMessage) {
		return (getConfig().getString(path) == null ? defaultMessage : getConfig().getString(path));
	}

	public String getMessageWithVar(String path, String defaultMessage, String var, String replacement) {
		return (getConfig().getString(path) == null ? defaultMessage : (getConfig().getString(path).contains(var) ? getConfig().getString(path).replace(var, replacement) : getConfig().getString(path)));
	}

	public String getMessageWithMultVars(String path, String defaultMessage, String var1, String replacement1, String var2, String replacement2) {
		return (getConfig().getString(path) == null ? defaultMessage : (getConfig().getString(path).contains(var1) ? (getConfig().getString(path).contains(var2) ? getConfig().getString(path).replace(var1, replacement1).replace(var2, replacement2) : getConfig().getString(path).replace(var1, replacement1)) : getConfig().getString(path)));
	}

	//Version
	public String version = "2.0";
	//
	public double serverVersion = VersionChecker.getVersion();

	//ArrayLists (contien aussi les grades)

	public ArrayList<Player> owner = new ArrayList<>();

	public ArrayList<Player> admin = new ArrayList<>();

	public ArrayList<Player> superstaff = new ArrayList<>();

	public ArrayList<Player> joueur = new ArrayList<>();

	public ArrayList<Player> youtuber = new ArrayList<>();

	public ArrayList<Player> mod = new ArrayList<>();

	public ArrayList<Player> guides = new ArrayList<>();

	public ArrayList<Player> staff = new ArrayList<>();

	public ArrayList<Player> schat = new ArrayList<>();

	public ArrayList<Player> banni = new ArrayList<>();

	public ArrayList<Player> mute = new ArrayList<>();

	public ArrayList<Player> vanished = new ArrayList<>();

	public ArrayList<Player> supervanished = new ArrayList<>();

	public ArrayList<Player> nochat = new ArrayList<>();

	public ArrayList<Player> mutedChat = new ArrayList<>();

	public ArrayList<Player> graded = new ArrayList<>();

	public ArrayList<Player> secondInv = new ArrayList<>();

	public ArrayList<Player> nomine = new ArrayList<>();

	public ArrayList<Player> directlog = new ArrayList<>();

	public ArrayList<Player> invseet = new ArrayList<>();

	public ArrayList<Player> invseep = new ArrayList<>();

	public ArrayList<Player> speedhacking = new ArrayList<>();

	public ArrayList<Player> bypassastp = new ArrayList<>();

	public HashMap<UUID, PlayerManager> secInv = new HashMap<>();

	public ArrayList<Player> freeze = new ArrayList<>();

	public ArrayList<Player> god = new ArrayList<>();

	public boolean chatMuted = false;



	//

	//Prefixes et Messages

	public String prefix = (getConfig().getString("system.prefixes.main") == null ? "§e(§6!§e) §r" : getConfig().getString("system.prefixes.main"));

	public String banPrefix = (getConfig().getString("system.prefixes.bans") == null ? "§e[§6Bans§e] §r" : getConfig().getString("system.prefixes.bans"));

	public String errorPrefix =  (getConfig().getString("system.prefixes.error") == null ? "§4§lErreur !§r§c " : getConfig().getString("system.prefixes.error"));

	public String errorMsg = (getConfig().getString("system.messages.error") == null ? errorPrefix + "§4§lUne erreur s'est produite lors de l'éxécution de cette commande, §c§oconsultez la console pour obteenir plus de détails." : getConfig().getString("system.messages.error"));

	public String LuckPrefix = (getConfig().getString("system.prefixes.luckyBlocks") == null ? "§6[§eLuckyBlock§6] §r" : getConfig().getString("system.prefixes.luckyBlocks"));

	public String clearlagMessage = (getConfig().getString("commands.messages.clearLag") == null ? "§c[§4ClearLag§c] §6Les items ont été suprimés !" : getConfig().getString("commands.messages.clearLag"));

	public String InDevMsg = (getConfig().getString("system.messages.inDevCommand") == null ? "§4§lOups ! §r§6Commande en developpement" : getConfig().getString("system.messages.inDevCommand"));

	public String InDevArgMsg = (getConfig().getString("system.messages.inDevArgument") == null ? "§4§lOups ! §r§6Cet argument est en developpement" : getConfig().getString("system.messages.inDevArgument"));

	public String noPermissionMsg = getPrefix() + (getConfig().getString("system.messages.noPermission") == null ? "§4Vous n'avez pas la permission d'utiliser cette commande !" : getConfig().getString("system.messages.noPermission"));

	public String noPermissionArgMsg = getPrefix() + (getConfig().getString("system.messages.noPermissionArgument") == null ? "§4Vous n'avez pas la permission d'utiliser cette argument !" : getConfig().getString("system.messages.noPermissionArgument"));

	public String playerNotFoundMsg = getPrefix() + (getConfig().getString("system.messages.playerNotFound") == null ? "§4Les joueur est hors-ligne ou n'existe pas !" : getConfig().getString("system.messages.playerNotFound"));

	public String requirePlayerToExcMsg = getPrefix() + (getConfig().getString("system.messages.requireToBePlayerToExecuteCommand") == null ? "§4Vous devez être un joueur pour utiliser cette commande !" : getConfig().getString("system.messages.requireToBePlayerToExecuteCommand"));

	public String logPrefix = (getConfig().getString("commands.prefixes.directLog") == null ? hex("#00fb21§lD#20f92a§li#3ff732§lr#5ff43b§le#7ff244§lc#9ef04c§lt#beee55§lL#ddeb5d§lo#fde966§lg §r§2 : §6") : getConfig().getString("commands.prefixes.directLog"));

	public String anticheatPrefix = (getConfig().getString("system.prefixes.antiCheat") == null ? "§e[§6§lAnti§r§0-§4§lCheat§e] §4" : getConfig().getString("system.prefixes.antiCheat"));

	//Init

    private Grades grades;

	private Ban ban;

	private Events events;

	private Settings settings;

	private Warp warp;

	private PlayerData pdata;

	private InvSeeCommand invsee;

	private ASData asdata;

	private SkinManager skinmanager;

	private Backup backup;


	//

	private int ndj = 0;

	private static MonPlugin instance;

	@Override
	public void onLoad() {
		super.onLoad();

		grades = new Grades(this,this);
		skinmanager = new SkinManager(this);
		grades.initConfig();
		ban = new Ban(this);
		events = new Events(this);
		pdata = new PlayerData(this);
		asdata = new ASData(this);
		settings = new Settings(this);
		invsee = new InvSeeCommand(this);
		warp = new Warp(this, this, grades);
		backup = new Backup(this);
		warp.initConfig();
		saveDefaultConfig();
		events.initConfig();
		ban.initConfig();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {



		if(serverVersion != 0 && serverVersion < 16) {
			Bukkit.getConsoleSender().sendMessage("§c--------------------------------------------------------------------");
			Bukkit.getConsoleSender().sendMessage("§e[§6Admin§cCmds§4B§e] : §4Your server version is :§61." + serverVersion);
			Bukkit.getConsoleSender().sendMessage("§e[§6Admin§cCmds§4B§e] : §4This version is not supported. Please use versions from 1.16.x to 1.21.x ");
			Bukkit.getConsoleSender().sendMessage("§e[§6Admin§cCmds§4B§e] : §4Disabling plugin");
			Bukkit.getConsoleSender().sendMessage("§e------------------------------------------");
			Bukkit.getConsoleSender().sendMessage("§e[§6Admin§cCmds§4B§e] : §4La version de votre serveur est : §61." + serverVersion);
			Bukkit.getConsoleSender().sendMessage("§e[§6Admin§cCmds§4B§e] : §4Cette version n'est pas supportée ! Merci d'utiliser une version de 1.16.x à 1.21.x");
			Bukkit.getConsoleSender().sendMessage("§e[§6Admin§cCmds§4B§e] : §4Désactivation du plugin.");
			Bukkit.getConsoleSender().sendMessage("§c--------------------------------------------------------------------");
			Bukkit.getPluginManager().disablePlugin(this);
		} else if (serverVersion != 0){
			Bukkit.getConsoleSender().sendMessage("§e[§6AdminCmdsB§e] §aDetected supported server version : §2§l1." + serverVersion);
		}

//		int pluginIdbStats = 16161;
//		try {
//
//			Metrics metrics = new Metrics(this, pluginIdbStats);
//
//		}catch (Exception e) {
//			Bukkit.getConsoleSender().sendMessage("[AdminCmdsB] Couldn't start Metrics : " + e);
//		}



		grades = new Grades(this,this);
		grades.initConfig();
		ban = new Ban(this);
		events = new Events(this);
		pdata = new PlayerData(this);
		asdata = new ASData(this);
		settings = new Settings(this);
		invsee = new InvSeeCommand(this);
		warp = new Warp(this, this, grades);
		backup = new Backup(this);
		warp.initConfig();
		saveDefaultConfig();
		events.initConfig();
		ban.initConfig();
		//grades.initScoreboard();



		instance = this;
		super.onEnable();


		//Executor (Liste des commandes)
		getCommand("fly").setExecutor(new CommandAdmin(this));
		getCommand("feed").setExecutor(new CommandAdmin(this));
		getCommand("heal").setExecutor(new CommandAdmin(this));
		getCommand("vanish").setExecutor(new VanishClass(this, grades, settings));
		getCommand("supervanish").setExecutor(new SuperVanishClass(this, grades, settings));
		getCommand("alert").setExecutor(new CommandAlert(settings, this));
		getCommand("spawn").setExecutor(new CommandSpawn()); //ADD 'this' FOR EVENT
		getCommand("tphere").setExecutor(new CommandTp(this));
		getCommand("freeze").setExecutor(new FreezeCommand(this, grades));
		getCommand("avertir").setExecutor(new AvertirCommand(this));
		getCommand("god").setExecutor(new GodCommand(this));
		getCommand("sethome").setExecutor(new HomeCommand(this));
		getCommand("home").setExecutor(new HomeCommand(this));
		getCommand("delhome").setExecutor(new HomeCommand(this));
		getCommand("setspawn").setExecutor(new SpawnCommand(this));
		getCommand("staffchat").setExecutor(new StaffChatCommand(this, grades));
		getCommand("grade").setExecutor(new GradeCommand(this, grades));
		getCommand("tempban").setExecutor(new Tempban(this, grades, settings));
		getCommand("tempmute").setExecutor(new Tempmute(this, grades));
		getCommand("ban").setExecutor(new BanCommand(this, grades));
		getCommand("unban").setExecutor(new Unban(this));
		getCommand("kick").setExecutor(new KickCommand(this, ban, grades));
		getCommand("mute").setExecutor(new MuteCommand(this));
		getCommand("unmute").setExecutor(new UnMuteCommand(this));
		getCommand("ec").setExecutor(new EcCommand(this));
		getCommand("cheatinv").setExecutor(new CInvCommand(this));
		getCommand("clearlag").setExecutor(new CLCommand(this));
		getCommand("admincmdsb").setExecutor(new PlCommand(this, grades, settings));
		getCommand("nightvision").setExecutor(new NVCommand(this));
		getCommand("admingui").setExecutor(new AGUICommand(this));
		getCommand("maintenance").setExecutor(new MaintenanceCommand(this, grades, settings));
		getCommand("nick").setExecutor(new NickCommand(this, grades));
		getCommand("unnick").setExecutor(new UnNickCommand(this, grades));
		getCommand("kickall").setExecutor(new CommandKickAll(this, ban));
		getCommand("chat").setExecutor(new ChatCommand(this));
		getCommand("mutedchat").setExecutor(new MutedChatCommand(this));
		getCommand("back").setExecutor(new BackCommand(this));
		getCommand("warp").setExecutor(new WarpCommand(this, warp, grades));
		getCommand("setwarp").setExecutor(new WarpCommand(this, warp, grades));
		getCommand("delwarp").setExecutor(new WarpCommand(this, warp, grades));
		getCommand("sudo").setExecutor(new SudoCommand(this));
		getCommand("secondaryinventory").setExecutor(new SecInvCommand(this));
		getCommand("nomine").setExecutor(new NoMineCommand(this));
		getCommand("speed").setExecutor(new SpeedCommand(this));
		getCommand("flyspeed").setExecutor(new FlySpeedCommand(this));
		getCommand("walkspeed").setExecutor(new WalkSpeedCommand(this));
		getCommand("getpos").setExecutor(new GetPosCommand(this));
		getCommand("setstandname").setExecutor(new SetStandNameCommand(this));
		getCommand("directlog").setExecutor(new DirectLogCommand(this));
		getCommand("admininvsee").setExecutor(new InvSeeCommand(this));
		getCommand("itemframe").setExecutor(new IFrameCommand(this));
		getCommand("luckyblock").setExecutor(new LuckyBCommand(this));
		getCommand("bypassastp").setExecutor(new AntiASTPCommand(this));
		getCommand("event").setExecutor(new EventsCommand(this, events, grades, warp));
		getCommand("noel").setExecutor(new NoelCommand(this));
		getCommand("paques").setExecutor(new PaquesCommand(this));
		getCommand("halloween").setExecutor(new HalloweenCommand(this));
		getCommand("survie").setExecutor(new SRP_TPCommand(this));
		getCommand("invsee").setExecutor(new ClassicInvSeeCommand(this, grades));
		getCommand("ecsee").setExecutor(new EcSeeCommand(this, grades));
		getCommand("fakejoin").setExecutor(new FakeJoinCommand(this, grades, settings));
		getCommand("fakeleave").setExecutor(new FakeLeaveCommand(this, grades, settings));
		getCommand("changeskin").setExecutor(new ChangeSkinCommand(this, skinmanager));
		getCommand("gm").setExecutor(new GameModeCommand(this));
		getCommand("test").setExecutor(new TestCommand(this));
		getCommand("civilisation").setExecutor(new CivilisationCommand(this));
		getCommand("backup").setExecutor(new BackupCommand(this,backup));
		getCommand("mutechat").setExecutor(new MuteChatCommand(this));
		//End "Executor"

		//TabCompleter

		getCommand("home").setTabCompleter(new HomeTab(this));
		getCommand("delhome").setTabCompleter(new HomeTab(this));
		getCommand("fly").setTabCompleter(new FlyTab());
		getCommand("vanish").setTabCompleter(new VanishTab());
		getCommand("supervanish").setTabCompleter(new VanishTab());
		getCommand("avertir").setTabCompleter(new AvertirTab());
		getCommand("admincmdsb").setTabCompleter(new PlTab());
		getCommand("maintenance").setTabCompleter(new MaintenanceTab());
		getCommand("warp").setTabCompleter(new WarpTab(warp));
		getCommand("delwarp").setTabCompleter(new WarpTab(warp));
		//getCommand("directlog").setTabCompleter(new DirectLogCommand(this));

		//End TabCompleter

		//Listener

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new EventsFreeze(this), this);
		pm.registerEvents(new MonPluginListeners(grades, this, settings, invsee), this);
		pm.registerEvents(new JoinListener(grades, this, ban, settings), this);
		pm.registerEvents(new LoginListener(grades, this, ban, settings), this);
		pm.registerEvents(new PlayerLoginEvent_TempBan(this, settings), this);
		pm.registerEvents(new PlayerLoginEvent_Ban(), this);
		pm.registerEvents(new PreLoginEvent(), this);
		if(settings.getAntiCheat()) {
			pm.registerEvents(new Movement_AntiCheat(this), this);
		}

		//End Listener

		Bukkit.getScheduler().runTaskTimer(this, () -> {
            if(settings.getCTabList()) {
                for(Player all : Bukkit.getOnlinePlayers()) {
                    if(!Objects.equals(settings.getServerName(), "")) {
                        all.setPlayerListHeader("§2§lBienvenue " + all.getDisplayName() +  "§2§l\n sur §6" + settings.getServerName() + "\n");
                    }

                    if(!(vanished == null)) {
                        ndj = Bukkit.getOnlinePlayers().size() - vanished.size() - supervanished.size();

                    }

                    int h = new Date().getHours();
                    int mInt = new Date().getMinutes();
                    String m = String.valueOf(mInt);
                    if(mInt < 10) {
                        m = "0" + m;
                    }

					String worldname = getWorldname(all);

					all.setPlayerListFooter("\n§2Nombre de joueurs en ligne : §6" + ndj + "§r\n§2IP : §6§lminijeux.mine.fun" + "§r\n§2Heure : §6" + h + "§e:§6" + m + "§r\n§2Monde : §6" + worldname + "§r§2, Position : §eX: §6" + all.getLocation().getBlockX() + "§r§2, §eY: §6" + all.getLocation().getBlockY() + "§r§2, §eZ: §6" + all.getLocation().getBlockZ());
                }
            }else {
                for(Player all : Bukkit.getOnlinePlayers()) {
                    all.setPlayerListHeader("");
                    all.setPlayerListFooter("");
                }
            }
        }, 1, 1);

		Calendar cal = Calendar.getInstance();
		long now = cal.getTimeInMillis();
		if(cal.get(Calendar.HOUR_OF_DAY) >= 22)
			cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 22);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		long offset = cal.getTimeInMillis() - now;
		long ticks = offset / 50L;
		try {
			Bukkit.getScheduler().runTaskTimer(this, () -> backup.run(), ticks,1728000);
		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage(errorPrefix + "§cCoulnd't schedule backup : §r" + e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()).replace(",",",\n"));
		}

	}


	private static String getWorldname(Player all) {
		String worldname = all.getLocation().getWorld().getName();

		if(worldname.equalsIgnoreCase("world")) {
			worldname = "Monde des Mini-Jeux";

		} else if(worldname.equalsIgnoreCase("event-halloween-2024")) {
			worldname = "§aÉvent d'§6Halloween";

		} else if(worldname.equalsIgnoreCase("survieRP")) {
			worldname = "§eSurvie-RP : §aOverworld";

		} else if(worldname.equalsIgnoreCase("survieRP_nether")) {
			worldname = "§eSurvie-RP : §4Nether";

		} else if(worldname.equalsIgnoreCase("survieRP_end")) {
			worldname = "§eSurvie-RP : §5End";

		} else if(worldname.equalsIgnoreCase("world_the_end")) {
			worldname = "§eMonde des Mini-Jeux : §5End";

		} else if(worldname.equalsIgnoreCase("world_nether")) {
			worldname = "§eMonde des Mini-Jeux : §4Nether";

		} else {
			worldname = all.getLocation().getWorld().getName();
		}
		return worldname;
	}


	@Override
	public void onDisable() {
		super.onDisable();
	}



	public static MonPlugin getInstance() {return instance;}

	public String getPrefix() {
		return prefix;
	}

	public String getDirectLogPrefix() {
		return logPrefix;
	}

	public String getBanPrefix() {
		return banPrefix;
	}

	public String getErrorPrefix() {
		return errorPrefix;
	}
	public String getAntiCheatPrefix() {
		return anticheatPrefix;
	}

	public String getLuckyBPrefix() {
		return LuckPrefix;
	}

	public String getClearLagMessage() {
		return clearlagMessage;
	}

}