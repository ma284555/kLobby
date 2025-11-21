package dev.slickcollections.kiwizin.lobby.listeners.server;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class ServerListener implements Listener {
  
  @EventHandler
  public void onBlockIgnite(BlockIgniteEvent evt) {
    evt.setCancelled(true);
  }
  
  @EventHandler
  public void onBlockBurn(BlockBurnEvent evt) {
    evt.setCancelled(true);
  }
  
  @EventHandler
  public void onLeavesDecay(LeavesDecayEvent evt) {
    evt.setCancelled(true);
  }
  
  @EventHandler
  public void onEntityExplode(EntityExplodeEvent evt) {
    evt.setCancelled(true);
  }
  
  @EventHandler
  public void onWeatherChange(WeatherChangeEvent evt) {
    evt.setCancelled(evt.toWeatherState());
  }
}
