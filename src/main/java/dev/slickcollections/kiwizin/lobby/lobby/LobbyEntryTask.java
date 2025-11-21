package dev.slickcollections.kiwizin.lobby.lobby;

import org.bukkit.scheduler.BukkitRunnable;

import static dev.slickcollections.kiwizin.lobby.lobby.Lobby.QUERY;

public class LobbyEntryTask extends BukkitRunnable {
  
  @Override
  public void run() {
    QUERY.forEach(Lobby::fetch);
  }
}
