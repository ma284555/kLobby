package dev.slickcollections.kiwizin.lobby.listeners.player;

import dev.slickcollections.kiwizin.game.Game;
import dev.slickcollections.kiwizin.lobby.cmd.kl.BuildCommand;
import dev.slickcollections.kiwizin.player.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {
  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getWhoClicked() instanceof Player) {
      Player player = (Player) evt.getWhoClicked();
      Profile profile = Profile.getProfile(player.getName());
      
      if (profile != null) {
        Game<?> game = profile.getGame();
        if (game == null) {
          evt.setCancelled(!BuildCommand.isBuilder(player));
        }
      }
    }
  }
}
