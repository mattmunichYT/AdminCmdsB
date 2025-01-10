package fr.mattmunich.admincmdsb.commandhelper;

public enum GradeList {

	// Grades
	// rgb.birdflop.com - Presets :

	OWNER(100, 1000, "#ffed00§lO#e2e317§lW#c6d82e§lN#a9ce44§lE#8cc35b§lR §6§l", "§6", " §8§l>>§6§l "),

	ADMIN(99, 100, "§l#fb0000§lA#fc2727§lD#fc4e4e§lM#fd7474§lI#fd9b9b§lN §6§l", "§6", " §8§l>>§6§l "),

	MODO(90, 90, "#fb8f00§lM#fca42b§lO#fcba55§lD#fdcf80§lO §6§l", "§6", " §8§l>>§e "),

	DEVELOPPEUR(80, 90, "#696969§lD#878787§lE#A5A5A5§lV §7§l", "§8", " §8§l>>§7 "),

	COMMANDEUR(23, 70, "#1b5d00C#1e6c00O#217b00M#248b00M#279a00A#29a900N#2cb800D#2fc800E#32d700U#35e600R §2", "§2",
			" §8>>§2 "),

	MINIATEUR(7, 60, "#084cfbM#0d52fbI#1258fcN#175efcI#1d65fcA#226bfcT#2771fdE#2c77fdU#317dfdR §1", "§1", "§8>>§1 "),

	DECORATEUR(8, 60, "#3afb00D#40fb0aE#47fb13C#4dfc1dO#53fc27R#5afc30A#60fc3aT#66fd44E#6dfd4dU#73fd57R §a", "§1",
			"§8>>§a "),

	REDSTONIEN(9, 60, "#fb0000R#fb0b0bE#fb1515D#fc2020S#fc2a2aT#fc3535O#fc3f3fN#fd4a4aI#fd5454E#fd5f5fN §c", "§c",
			" §8>>§c "),

	SCENARISTE(10, 60, "#00f3fbS#0cf4faC#19f5f9E#25f6f7N#32f7f6A#3ef9f5R#4bfaf4I#57fbf2S#64fcf1T#70fdf0E §b", "§b",
			" §8>>§b "),

	DESIGNER(11, 60, "#747474D#888888E#9c9c9cS#b0b0b0I#c3c3c3G#d7d7d7N#ebebebE#ffffffR §r", "§r", " §8>>§r "),

	ETE(12, 0, "#e3ff00É#edff59T#f6ffb1É §e", "§e", " §8>>§e "),

	NOEL(13, 0, "#ff4848N#ff6b6bO#ff8e8eË#ffb1b1L §c", "§c", " §8>>§c "),

	HALLOWEEN(14, 0, "#fbbc00H#fbbf0eA#fcc21dL#fcc42bL#fcc73aO#fcca48W#fdcd56E#fdcf65E#fdd273N §6", "§6", " §8>>§6 "),

	PAQUES(15, 0, "#0dff00P#2dff1eA#4dff3cQ#6dff59U#8dff77E#adff95S §a", "§a", " §8>>§a "),

	PVP(16, 0, "#CB2D3EP#DD3A3CV#EF473AP §c", "§4", " §8>>§4 "),

	TEAM(17, 0, "#00f3fbT#3af3fcE#73f3fcA#adf3fdM §b", "§b", " §8>>§3 "),

	SPORT(18, 0, "#d4ff00S#dffd29P#eafc51O#f4fa7aR#fff8a2T §e", "§e", " §8>>§e "),

	PARTY(19, 0, "#00fb03P#31fc2eA#62fc58R#93fd83T#c4fdadY §a", "§a", " §8>>§a "),

	ANIMATEUR(20, 70, "#0d00fbA#2116fbN#352dfcI#4943fcM#5d5afcA#7170fcT#8586fdE#999dfdU#adb3fdR §b", "§b", " §8>>§b "),

	VIDEASTE(21, 65, "#b304fbV#ba18fbI#c12cfcD#c840fcE#cf53fcA#d667fcS#dd7bfdT#e48ffdE §5", "§5", " §8>>§d "),

	MODELISATEUR(22, 60, "#008B8BM#177E7EO#2E7272D#466565E#5D5858L#744C4CI#8B3F3FS#A23333A#B92626T#D11919E#E80D0DU#FF0000R §3", "§3", " §8>>§b "),

	BUILDEUR(6, 60, "#fb00f8B#f812f9U#f524f9I#f236faL#f047fbD#ed59fcE#ea6bfcU#e77dfdR §5", "§6", " §8>>§2 "),

	GUIDE(5, 70, "#fbcb00G#fcd62bU#fce156I#fdec80D#fdf7abE §e", "§6", "§8§l>> §a "),

	VIP(4, 50, "#ffcf00V#ffe55fI#fffabeP §a", "§a", " §8>>§a "),

	TESTEUR(3, 30, "#fbbc00T#e0c70dE#c4d21aS#a9dd27T#8de733E#72f240U#56fd4dR §6", "§6", " §2>>§6 "),

	AS(0, 0, "", "", " -AS-§l§8>>§7 "),

	MEMBRE(1, 1, "#b1b1b1M#c1c1c1E#d0d0d0M#e0e0e0B#efefefR#ffffffE §7", "§1", " §l§8>>§3 ");

	// end("Grades")

	// Fields

	private final int power, id;
	private final String prefix, suffix, chatSeparator;

	// end("Fields")

	// Constreucteur
	private GradeList(int id, int power, String prefix, String suffix, String chatSeparator) {
		this.id = id;
		this.power = power;
		this.prefix = prefix;
		this.suffix = suffix;
		this.chatSeparator = chatSeparator;

	}
	// end("Constructeur")

	// Methode GETTER

	public final int getPower() {
		return power;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public String getName() {
		return this.toString();
	}

	public String getChatSeparator() {
		return chatSeparator;
	}

	public int getId() {
		return id;
	}

	// end("Methode GETTER")

}
