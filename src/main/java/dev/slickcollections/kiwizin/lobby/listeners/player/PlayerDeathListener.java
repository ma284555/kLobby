package dev.slickcollections.kiwizin.lobby.listeners.player;


import dev.slickcollections.kiwizin.game.Game;
import dev.slickcollections.kiwizin.lobby.Main;
import dev.slickcollections.kiwizin.player.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {
  
  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent evt) {
    Player player = evt.getEntity();
    evt.setDeathMessage(null);
    
    Profile profile = Profile.getProfile(player.getName());
    if (profile != null) {
      evt.setDroppedExp(0);
      evt.getDrops().clear();
      player.setHealth(20.0);
      
      Game<?> game = profile.getGame();
      if (game == null) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), profile::refresh, 3);
      }
    }
  }
}
