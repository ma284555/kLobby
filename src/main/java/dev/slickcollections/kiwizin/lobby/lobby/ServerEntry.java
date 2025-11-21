package dev.slickcollections.kiwizin.lobby.lobby;

import dev.slickcollections.kiwizin.lobby.Main;
import dev.slickcollections.kiwizin.plugin.config.KConfig;
import dev.slickcollections.kiwizin.plugin.logger.KLogger;
import dev.slickcollections.kiwizin.servers.ServerItem;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ServerEntry {
  
  public static final KLogger LOGGER = ((KLogger) Main.getInstance().getLogger()).getModule("ENTRIES");
  private static final List<ServerEntry> ENTRIES = new ArrayList<>();
  
  private final String key;
  private final List<String> holograms;
  private final ItemStack hand;
  private final String skinValue;
  private final String skinSignature;
  
  public ServerEntry(String key, List<String> holograms, ItemStack hand, String skinValue, String skinSignature) {
    this.key = key;
    this.holograms = holograms;
    this.hand = hand;
    this.skinValue = skinValue;
    this.skinSignature = skinSignature;
  }
  
  public static void setupEntries() {
    KConfig config = Main.getInstance().getConfig("entries");
    for (String key : config.getKeys(false)) {
      if (!config.contains(key + ".hand")) {
        config.set(key + ".hand", "AIR : 1");
      }
      ServerEntry se = new ServerEntry(config.getString(key + ".key"), config.getStringList(key + ".holograms"), BukkitUtils.deserializeItemStack(config.getString(key + ".hand")),
          config.getString(key + ".skin.value"), config.getString(key + ".skin.signature"));
      if (se.getServerItem() == null) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> LOGGER.warning("A entry " + key + " (entries.yml) possui uma key invalida."));
        continue;
      }
      
      ENTRIES.add(se);
    }
  }
  
  public static ServerEntry getByKey(String key) {
    return ENTRIES.stream().filter(entry -> entry.getKey().equals(key)).findFirst().orElse(null);
  }
  
  public String getKey() {
    return this.key;
  }
  
  public ServerItem getServerItem() {
    return ServerItem.getServerItem(this.key);
  }
  
  public List<String> listHologramLines() {
    return this.holograms;
  }

  public ItemStack getHand() {
    return this.hand;
  }
  
  public String getSkinValue() {
    return this.skinValue;
  }
  
  public String getSkinSignature() {
    return this.skinSignature;
  }
}
