package dev.slickcollections.kiwizin.lobby.listeners.player;

import dev.slickcollections.kiwizin.database.data.DataContainer;
import dev.slickcollections.kiwizin.game.Game;
import dev.slickcollections.kiwizin.lobby.Language;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AsyncPlayerChatListener implements Listener {
  
  private static final Map<String, Long> flood = new HashMap<>();
  
  private static final DecimalFormat df = new DecimalFormat("###.#");
  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    flood.remove(evt.getPlayer().getName());
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void AsyncPlayerChat(AsyncPlayerChatEvent evt) {
    if (evt.isCancelled()) {
      return;
    }
    
    Player player = evt.getPlayer();
    if (!player.hasPermission("klobby.chat.delay")) {
      long start = flood.containsKey(player.getName()) ? flood.get(player.getName()) : 0;
      if (start > System.currentTimeMillis()) {
        double time = (start - System.currentTimeMillis()) / 1000.0;
        if (time > 0.1) {
          evt.setCancelled(true);
          String timeString = df.format(time).replace(",", ".");
          if (timeString.endsWith("0")) {
            timeString = timeString.substring(0, timeString.lastIndexOf("."));
          }
          
          player.sendMessage(Language.chat$delay.replace("{time}", timeString));
          return;
        }
      }
      
      flood.put(player.getName(), System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(3));
    }
    
    Role role = Role.getPlayerRole(player);
    if (player.hasPermission("klobby.chat.color")) {
      evt.setMessage(StringUtils.formatColors(evt.getMessage()));
    }
    
    Profile profile = Profile.getProfile(player.getName());
    Game<?> game = profile.getGame();
    String suffix = "";
    DataContainer container = profile.getDataContainer("kCoreProfile", "clan");
    if (container != null) {
      suffix = container.getAsString().replace(" ", "") + " ";
      if (suffix.contains("§8")) {
        suffix = "";
      }
    }
    if (game == null) {
      evt.setFormat(
          Language.chat$format$lobby.replace("{player}", role.getPrefix() + "%s").replace("{color}", role.isDefault() ? Language.chat$color$default : Language.chat$color$custom)
              .replace("{message}", "%s"));
    }
    evt.setFormat((suffix.equals(" ") ? "" : suffix) + "§r" + evt.getFormat());
    evt.getRecipients().clear();
    for (Player players : Bukkit.getOnlinePlayers()) {
      Profile profiles = Profile.getProfile(players.getName());
      if (profiles != null) {
        evt.getRecipients().add(players);
      }
    }
  }
}
