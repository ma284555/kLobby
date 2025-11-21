package dev.slickcollections.kiwizin.lobby.listeners.player;

import dev.slickcollections.kiwizin.libraries.npclib.api.event.NPCRightClickEvent;
import dev.slickcollections.kiwizin.libraries.npclib.api.npc.NPC;
import dev.slickcollections.kiwizin.menus.MenuDeliveries;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.servers.ServerItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerInteractListener implements Listener {
  
  @EventHandler
  public void onNPCRightClick(NPCRightClickEvent evt) {
    Player player = evt.getPlayer();
    Profile profile = Profile.getProfile(player.getName());
    
    if (profile != null) {
      NPC npc = evt.getNPC();
      if (npc.data().has("play-npc")) {
        ServerItem si = ServerItem.getServerItem(npc.data().get("play-npc"));
        if (si != null) {
          si.connect(profile);
        }
      } else if (npc.data().has("delivery-npc")) {
        new MenuDeliveries(profile);
      }
    }
  }
}
