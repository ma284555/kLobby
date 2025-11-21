package dev.slickcollections.kiwizin.lobby.cmd.kl;

import dev.slickcollections.kiwizin.booster.Booster;
import dev.slickcollections.kiwizin.lobby.cmd.SubCommand;
import dev.slickcollections.kiwizin.player.Profile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCommand extends SubCommand {
  
  public GiveCommand() {
    super("dar", "dar [jogador] booster", "Dar multiplicadores.", false);
  }
  
  @Override
  public void perform(CommandSender sender, String[] args) {
    if (args.length <= 1) {
      sender.sendMessage(" \n§eAjuda - Dar\n \n§6/kl dar [jogador] booster [private/network] [multiplicador] [horas]\n ");
      return;
    }
    
    Profile target = Profile.getProfile(args[0]);
    if (target == null) {
      sender.sendMessage("§cUsuário não encontrado.");
      return;
    }
    
    String action = args[1];
    if (action.equalsIgnoreCase("booster")) {
      if (args.length < 5) {
        sender.sendMessage("§cUtilize /kl dar [jogador] booster [private/network] [multiplicador] [horas]");
        return;
      }
      
      try {
        Booster.BoosterType type = Booster.BoosterType.valueOf(args[2].toUpperCase());
        try {
          double multiplier = Double.parseDouble(args[3]);
          long hours = Long.parseLong(args[4]);
          if (multiplier < 1.0D || hours < 1) {
            throw new Exception();
          }
          
          target.getBoostersContainer().addBooster(type, multiplier, hours);
          sender.sendMessage("§aMultiplicador adicionado.");
        } catch (Exception ex) {
          sender.sendMessage("§cUtilize números válidos.");
        }
      } catch (Exception ex) {
        sender.sendMessage("§cUtilize /kl dar [jogador] booster [private/network] [multiplicador] [horas]");
      }
    } else {
      sender.sendMessage(" \n§eAjuda - Dar\n \n§6/kl dar [jogador] booster [private/network] [multiplicador] [horas]\n ");
    }
  }
  
  @Override
  public void perform(Player player, String[] args) {}
}
