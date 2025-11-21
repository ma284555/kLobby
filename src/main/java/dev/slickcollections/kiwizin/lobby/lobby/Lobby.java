package dev.slickcollections.kiwizin.lobby.lobby;

import dev.slickcollections.kiwizin.lobby.Main;
import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.servers.ServerItem;
import dev.slickcollections.kiwizin.servers.ServerPing;
import org.bukkit.Bukkit;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Lobby {
  
  public static final List<Lobby> QUERY = new ArrayList<>();
  public static final KConfig CONFIG = Main.getInstance().getConfig("lobbies");
  public static final List<String> WARNINGS = new ArrayList<>();
  private static final List<Lobby> LOBBIES = new ArrayList<>();
  private final int slot;
  private final ServerPing serverPing;
  private final int maxPlayers;
  private final String icon;
  private final String serverName;
  
  public Lobby(int slot, String icon, int maxPlayers, String ip, String serverName) {
    this.slot = slot;
    this.icon = icon;
    this.serverPing = new ServerPing(
        new InetSocketAddress(ip.split(":")[0], Integer.parseInt(ip.split(":")[1])));
    this.maxPlayers = maxPlayers;
    this.serverName = serverName;
  }
  
  public static void setupLobbies() {
    new LobbyEntryTask().runTaskTimerAsynchronously(Main.getInstance(), 0, 20 * 30);
    
    for (String key : CONFIG.getSection("items").getKeys(false)) {
      String servername = CONFIG.getString("items." + key + ".servername");
      if (servername.split(" ; ").length < 2) {
        WARNINGS.add(" - (" + key + ") " + servername);
        continue;
      }
      
      LOBBIES.add(
          new Lobby(CONFIG.getInt("items." + key + ".slot"),
              CONFIG.getString("items." + key + ".icon"),
              CONFIG.getInt("items." + key + ".max-players"), servername.split(" ; ")[0],
              servername.split(" ; ")[1]));
    }
    
    for (Lobby lobby : LOBBIES) {
      if (!ServerItem.alreadyQuerying(lobby.getServerName())) {
        QUERY.add(lobby);
      }
    }
  }
  
  public static Collection<Lobby> listLobbies() {
    return LOBBIES;
  }
  
  public void fetch() {
    this.serverPing.fetch();
    ServerItem.SERVER_COUNT.put(this.serverName, this.serverPing.getOnline());
  }

  public int getSlot() {
    return this.slot;
  }

  public String getIcon() {
    return this.icon;
  }

  public int getPlayers() {
    return this.serverName.equals(Main.currentServerName) ? Bukkit.getOnlinePlayers().size()
        : ServerItem.getServerCount(this.serverName);
  }
  
  public int getMaxPlayers() {
    return this.maxPlayers;
  }
  
  public String getServerName() {
    return this.serverName;
  }
}
