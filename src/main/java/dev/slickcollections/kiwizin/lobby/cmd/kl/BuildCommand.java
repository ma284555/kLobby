package dev.slickcollections.kiwizin.lobby.cmd.kl;

import dev.slickcollections.kiwizin.lobby.cmd.SubCommand;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BuildCommand extends SubCommand {
  
  private static final List<String> BUILDERS = new ArrayList<>();
  
  public BuildCommand() {
    super("build", "build", "Ativar/Desativar modo construtor.", true);
  }
  
  public static void remove(Player player) {
    BUILDERS.remove(player.getName());
  }
  
  public static boolean isBuilder(Player player) {
    return BUILDERS.contains(player.getName());
  }
  
  @Override
  public void perform(CommandSender sender, String[] args) {}
  
  @Override
  public void perform(Player player, String[] args) {
    if (BUILDERS.contains(player.getName())) {
      BUILDERS.remove(player.getName());
      player.setGameMode(GameMode.ADVENTURE);
      player.sendMessage("§cModo construtor desativado.");
    } else {
      BUILDERS.add(player.getName());
      player.setGameMode(GameMode.CREATIVE);
      player.sendMessage("§aModo construtor ativado.");
    }
  }
}
