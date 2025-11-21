package dev.slickcollections.kiwizin.lobby.cmd.kl;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.lobby.Main;
import dev.slickcollections.kiwizin.lobby.cmd.SubCommand;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetSpawnCommand extends SubCommand {
  
  public SetSpawnCommand() {
    super("setspawn", "setspawn", "Setar o spawn do servidor.", true);
  }
  
  @Override
  public void perform(CommandSender sender, String[] args) {}
  
  @Override
  public void perform(Player player, String[] args) {
    Location location = player.getLocation().getBlock().getLocation().add(0.5, 0, 0.5);
    location.setYaw(player.getLocation().getYaw());
    location.setPitch(player.getLocation().getPitch());
    Main.getInstance().getConfig().set("spawn", BukkitUtils.serializeLocation(location));
    Main.getInstance().saveConfig();
    Core.setLobby(location);
    player.sendMessage("§aSpawn setado.");
  }
}
