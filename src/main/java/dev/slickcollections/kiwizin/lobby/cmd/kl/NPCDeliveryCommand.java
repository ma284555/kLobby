package dev.slickcollections.kiwizin.lobby.cmd.kl;

import dev.slickcollections.kiwizin.lobby.cmd.SubCommand;
import dev.slickcollections.kiwizin.lobby.lobby.DeliveryNPC;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NPCDeliveryCommand extends SubCommand {
  
  public NPCDeliveryCommand() {
    super("npcentregas", "npcentregas", "Adicione/remova NPC de Entregas", true);
  }
  
  @Override
  public void perform(CommandSender sender, String[] args) {
  }
  
  @Override
  public void perform(Player player, String[] args) {
    if (args.length == 0) {
      player.sendMessage(" \n§eAjuda\n \n§6/kl npcentregas adicionar [id] §f- §7Adicionar NPC.\n§6/kl npcentregas remover [id] §f- §7Remover NPC.\n ");
      return;
    }
    
    String action = args[0];
    if (action.equalsIgnoreCase("adicionar")) {
      if (args.length <= 1) {
        player.sendMessage("§cUtilize /kl npcentregas adicionar [id]");
        return;
      }
      
      String id = args[1];
      if (DeliveryNPC.getById(id) != null) {
        player.sendMessage("§cJá existe um NPC de Entregas utilizando \"" + id + "\" como ID.");
        return;
      }
      
      Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
      location.setYaw(player.getLocation().getYaw());
      location.setPitch(player.getLocation().getPitch());
      DeliveryNPC.add(id, location);
      player.sendMessage("§aNPC de Entregas adicionado com sucesso.");
    } else if (action.equalsIgnoreCase("remover")) {
      if (args.length <= 1) {
        player.sendMessage("§cUtilize /kl npcentregas remover [id]");
        return;
      }
      
      String id = args[1];
      DeliveryNPC npc = DeliveryNPC.getById(id);
      if (npc == null) {
        player.sendMessage("§cNão existe um NPC de Entregas utilizando \"" + id + "\" como ID.");
        return;
      }
      
      DeliveryNPC.remove(npc);
      player.sendMessage("§cNPC de Entregas removido com sucesso.");
    } else {
      player.sendMessage(" \n§eAjuda\n \n§6/kl npcentregas adicionar [id] §f- §7Adicionar NPC.\n§6/kl npcentregas remover [id] §f- §7Remover NPC.\n ");
    }
  }
}
