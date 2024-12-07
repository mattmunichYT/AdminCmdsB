package fr.mattmunich.monplugin.commandhelper;

import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.mattmunich.monplugin.MonPlugin;

import net.minecraft.server.network.PlayerConnection;
import org.bukkit.Bukkit;

import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;


import java.io.InputStreamReader;
import java.net.URL;


import com.google.gson.JsonObject;

public class SkinManager {

	private final MonPlugin main;

	public SkinManager(MonPlugin main) {
		this.main = main;
	}

	@SuppressWarnings("deprecated")
	public void changeSkin(Player p, String nameOfTarget, boolean fromName) {

//		if(main.serverVersion != 21) {
//			p.sendMessage("§c---------------------------------------------------------------------------");
//			p.sendMessage("§e[§6Admin§cCmds§4B§e] : §4Your server version is :§61." + main.serverVersion);
//			p.sendMessage("§e[§6Admin§cCmds§4B§e] : §4This feature is not available in this version.");
//			p.sendMessage("§e[§6Admin§cCmds§4B§e] : §4Please use 1.21.x for it to work");
//			p.sendMessage("§e------------------------------------------");
//			p.sendMessage("§e[§6Admin§cCmds§4B§e] : §4La version de votre serveur est : §61." + main.serverVersion);
//			p.sendMessage("§e[§6Admin§cCmds§4B§e] : §4Cette fonctionnalité n'est pas disponible dans cette version.");
//			p.sendMessage("§e[§6Admin§cCmds§4B§e] : §4Merci d'utiliser la version 1.21.x pour qu'elle fonctionne.");
//			p.sendMessage("§c---------------------------------------------------------------------------");
//			return;
//		}


		//PlayerTextures ttextures = null;
		if(!fromName) {
			p.sendMessage(main.InDevArgMsg);
			return;
		}

		GameProfile profile = ((CraftPlayer)p).getHandle().getBukkitEntity().getProfile();
		PlayerConnection connection = ((CraftPlayer)p).getHandle().c;

		String[] textures = getSkin(nameOfTarget);

		if(textures[0].equalsIgnoreCase("error") && textures[1].equalsIgnoreCase("not_found")) {
			p.sendMessage(main.getPrefix() + "§4Skin non trouvé !§e Vérifiez le nom du skin.");
			return;
		}
		Bukkit.getOnlinePlayers().forEach(all -> {
			all.hidePlayer(main, p);
		});

		profile.getProperties().removeAll("textures");
		profile.getProperties().put("textures", new Property("textures",textures[0], textures[1]));

//        if(Objects.equals(ttextures.toString(), "CraftPlayerTextures [data=null]")) {
//			p.sendMessage(main.getPrefix() + "§4Une erreur s'est produite lors de la récupération du skin du joueur. §eVérifiez le nom du skin.");
//			return;
//		}


//		profile.update();
//		Bukkit.getConsoleSender().sendMessage("profile after update before clear : " + profile);
//		profile.setTextures(null);
//		Bukkit.getConsoleSender().sendMessage("profile after clear before set textures : " + profile);
//		profile.setTextures(ttextures);
//		Bukkit.getConsoleSender().sendMessage("profile after set textures : " + profile);
//		profile.update();
//		Bukkit.getConsoleSender().sendMessage("profile after set textures after update : " + profile);

		//TODO change name

		Bukkit.getConsoleSender().sendMessage("p.getPlayerProfile : " + p.getPlayerProfile().toString());
		p.sendMessage(main.getPrefix() + "§2Le skin a été changé au skin de §6" + nameOfTarget + "§2 ! §8§o(Visible seulement poiur les autres joueurs)");

		Bukkit.getOnlinePlayers().forEach(all -> {
			all.showPlayer(main, p);
		});
	}

	private String[] getSkin(String name) {
		try {

			URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
			InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
			String uuid = new JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();

			URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
			InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
			JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
			String texture = textureProperty.get("value").getAsString();
			String signature = textureProperty.get("signature").getAsString();

			return new String[] {texture, signature};
//			Bukkit.getConsoleSender().sendMessage("texture = " + texture + "\n signature = " + signature);
//
//
//			byte[] bytedecoded = Base64.getDecoder().decode(texture);
//			String decoded = new String(bytedecoded);
//			JsonObject jsonObject = new JsonParser().parse(decoded).getAsJsonObject();
//
//			String url = jsonObject.get("textures").getAsJsonObject().get("SKIN").getAsJsonObject().get("url").getAsString();
//			String capeUrl = jsonObject.get("textures").getAsJsonObject().get("CAPE").getAsJsonObject().get("url").getAsString();


		} catch (Exception e) {
			Bukkit.getConsoleSender().sendMessage("[AdminCmdsB] §cUne erreur s'est produite : §4" + e + "\nDetails : " + e.getMessage() + e.toString());
			return new String[] {"error","not_found"};
		}
	}
}
