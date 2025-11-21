package dev.slickcollections.kiwizin.lobby.hook;

import com.comphenix.protocol.ProtocolLibrary;
import dev.slickcollections.kiwizin.lobby.Language;
import dev.slickcollections.kiwizin.lobby.Main;
import dev.slickcollections.kiwizin.lobby.hook.hotbar.LHotbarActionType;
import dev.slickcollections.kiwizin.lobby.hook.protocollib.HologramAdapter;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.hotbar.Hotbar;
import dev.slickcollections.kiwizin.player.hotbar.HotbarAction;
import dev.slickcollections.kiwizin.player.hotbar.HotbarActionType;
import dev.slickcollections.kiwizin.player.hotbar.HotbarButton;
import dev.slickcollections.kiwizin.player.scoreboard.KScoreboard;
import dev.slickcollections.kiwizin.player.scoreboard.scroller.ScoreboardScroller;
import dev.slickcollections.kiwizin.plugin.config.KConfig;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class LCoreHook {

  public static void setupHook() {
    setupHotbars();
    new BukkitRunnable() {
      @Override
      public void run() {
        Profile.listProfiles().forEach(profile -> {
          if (profile.getScoreboard() != null) {
            profile.getScoreboard().scroll();
          }
        });
      }
    }.runTaskTimerAsynchronously(Main.getInstance(), 0, Language.scoreboards$scroller$every_tick);

    new BukkitRunnable() {
      @Override
      public void run() {
        Profile.listProfiles().forEach(profile -> {
          if (!profile.playingGame() && profile.getScoreboard() != null) {
            profile.update();
          }
        });
      }
    }.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);

    ProtocolLibrary.getProtocolManager().addPacketListener(new HologramAdapter());
  }

    public static void reloadScoreboard(Profile profile) {
        Player player = profile.getPlayer();

        List<String> lines = new ArrayList<>(Language.scoreboards$lobby);
        Collections.reverse(lines);

        // ✅ PROCESSAR PLACEHOLDERS IMEDIATAMENTE (SYNC)
        List<String> processedLines = PlaceholderAPI.setPlaceholders(player, lines);

        profile.setScoreboard(new KScoreboard() {
            @Override
            public void update() {
                this.updateHealth();

                // ✅ Usar linhas já processadas (rápido)
                for (int index = 0; index < processedLines.size(); index++) {
                    this.add(index + 1, processedLines.get(index));
                }
            }
        }.scroller(new ScoreboardScroller(Language.scoreboards$scroller$titles)).to(profile.getPlayer()).build());

        profile.update();
        profile.getScoreboard().scroll();
    }

  private static void setupHotbars() {
    HotbarActionType.addActionType("lobby", new LHotbarActionType());

    KConfig config = Main.getInstance().getConfig("hotbar");
    for (String id : new String[]{"lobby"}) {
      Hotbar hotbar = new Hotbar(id);

      ConfigurationSection hb = config.getSection(id);
      for (String button : hb.getKeys(false)) {
        try {
          hotbar.getButtons().add(new HotbarButton(hb.getInt(button + ".slot"), new HotbarAction(hb.getString(button + ".execute")), hb.getString(button + ".icon")));
        } catch (Exception ex) {
          Main.getInstance().getLogger().log(Level.WARNING, "Falha ao carregar o botao \"" + button + "\" da hotbar \"" + id + "\": ", ex);
        }
      }

      Hotbar.addHotbar(hotbar);
    }
  }
}
