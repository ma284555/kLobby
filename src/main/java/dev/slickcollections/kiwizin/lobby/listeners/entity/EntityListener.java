package dev.slickcollections.kiwizin.lobby.listeners.entity;

import dev.slickcollections.kiwizin.Core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class EntityListener implements Listener {
  
  @EventHandler(priority = EventPriority.LOWEST)
  public void onEntityDamage(EntityDamageEvent evt) {
    if (evt.getEntity() instanceof Player) {
      Player player = (Player) evt.getEntity();
      evt.setCancelled(true);
      if (evt.getCause() == DamageCause.VOID) {
        player.teleport(Core.getLobby());
      }
    }
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onCreatureSpawn(CreatureSpawnEvent evt) {
    evt.setCancelled(evt.getSpawnReason() != SpawnReason.CUSTOM);
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onFoodLevelChange(FoodLevelChangeEvent evt) {
    evt.setCancelled(true);
  }
}
