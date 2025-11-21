package dev.slickcollections.kiwizin.lobby.cmd;

import dev.slickcollections.kiwizin.lobby.Main;
import dev.slickcollections.kiwizin.lobby.cmd.kl.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class LobbyCommand extends Commands {
  
  private final List<SubCommand> commands = new ArrayList<>();
  
  public LobbyCommand() {
    super("kl", "klobby");
    
    this.commands.add(new UpdateCommand());
    this.commands.add(new SetSpawnCommand());
    this.commands.add(new BuildCommand());
    this.commands.add(new NPCPlayCommand());
    this.commands.add(new NPCDeliveryCommand());
    this.commands.add(new GiveCommand());
  }
  
  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (!sender.hasPermission("klobby.cmd.lobby")) {
      sender.sendMessage("§6kLobby §bv" + Main.getInstance().getDescription().getVersion() + " §7Criado por §6Nyskiwi§7.");
      return;
    }
    
    if (args.length == 0) {
      this.sendHelp(sender, 1);
      return;
    }
    
    try {
      this.sendHelp(sender, Integer.parseInt(args[0]));
    } catch (Exception ex) {
      SubCommand subCommand = this.commands.stream().filter(sc -> sc.getName().equalsIgnoreCase(args[0])).findFirst().orElse(null);
      if (subCommand == null) {
        this.sendHelp(sender, 1);
        return;
      }
  
      List<String> list = new ArrayList<>(Arrays.asList(args));
      list.remove(0);
      if (subCommand.onlyForPlayer()) {
        if (!(sender instanceof Player)) {
          sender.sendMessage("§cEsse comando pode ser utilizado apenas pelos jogadores.");
          return;
        }
        
        subCommand.perform((Player) sender, list.toArray(new String[0]));
      } else {
        subCommand.perform(sender, list.toArray(new String[0]));
      }
    }
  }
  
  private void sendHelp(CommandSender sender, int page) {
    List<SubCommand> commands = this.commands.stream().filter(subcommand -> sender instanceof Player || !subcommand.onlyForPlayer()).collect(Collectors.toList());
    Map<Integer, StringBuilder> pages = new HashMap<>();
    
    int pagesCount = (commands.size() + 6) / 7;
    for (int index = 0; index < commands.size(); index++) {
      int currentPage = (index + 7) / 7;
      if (!pages.containsKey(currentPage)) {
        pages.put(currentPage, new StringBuilder(" \n§eAjuda - " + currentPage + "/" + pagesCount + "\n \n"));
      }
      
      pages.get(currentPage).append("§6/kl ").append(commands.get(index).getUsage()).append(" §f- §7").append(commands.get(index).getDescription()).append("\n");
    }
    
    StringBuilder sb = pages.get(page);
    if (sb == null) {
      sender.sendMessage("§cPágina não encontrada.");
      return;
    }
    
    sb.append(" ");
    sender.sendMessage(sb.toString());
  }
}
