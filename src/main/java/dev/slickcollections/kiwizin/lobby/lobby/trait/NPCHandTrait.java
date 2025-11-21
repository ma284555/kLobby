package dev.slickcollections.kiwizin.lobby.lobby.trait;

import dev.slickcollections.kiwizin.libraries.npclib.api.npc.NPC;
import dev.slickcollections.kiwizin.libraries.npclib.trait.NPCTrait;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class NPCHandTrait extends NPCTrait {
  
  private final ItemStack inHand;
  
  public NPCHandTrait(NPC npc, ItemStack inHand) {
    super(npc);
    this.inHand = inHand;
  }
  
  @Override
  public void onSpawn() {
    ((Player) this.getNPC().getEntity()).setItemInHand(this.inHand);
  }
}
