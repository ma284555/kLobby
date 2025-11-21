package dev.slickcollections.kiwizin.lobby.listeners.player;

import dev.slickcollections.kiwizin.lobby.Language;
import dev.slickcollections.kiwizin.lobby.Main;
import dev.slickcollections.kiwizin.lobby.hook.LCoreHook;
import dev.slickcollections.kiwizin.nms.NMS;
import dev.slickcollections.kiwizin.player.Profile;
import dev.slickcollections.kiwizin.player.hotbar.Hotbar;
import dev.slickcollections.kiwizin.player.role.Role;
import dev.slickcollections.kiwizin.utils.tagger.TagUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evt) {
        evt.setJoinMessage(null);

        Player player = evt.getPlayer();
        Profile profile = Profile.getProfile(player.getName());

        // Setup básico
        profile.setHotbar(Hotbar.getHotbarById("lobby"));
        profile.refresh();

        // Title e Tab
        NMS.sendTitle(player, "", "", 0, 1, 0);
        if (Language.lobby$tab$enabled) {
            NMS.sendTabHeaderFooter(player, Language.lobby$tab$header, Language.lobby$tab$footer);
        }

        // Scoreboard
        LCoreHook.reloadScoreboard(profile);

        // ✅ IMPORTANTE: Aplicar tag ANTES de sendTeams!
        TagUtils.setTag(player); // Isso detecta automaticamente a role correta

        // Teams
        TagUtils.sendTeams(player);

        // Tag + Broadcast (async)
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            // ✅ Recarregar tag (caso venha do Redis)
            TagUtils.CachedTag cachedTag = TagUtils.loadTag(player.getName());

            if (cachedTag != null) {
                TagUtils.setTag(player.getName(), cachedTag.prefix, cachedTag.suffix, cachedTag.sortPriority);
            } else {
                // Fallback: usar tag do player
                TagUtils.setTag(player);
            }

            Role playerRole = Role.getPlayerRole(player);
            if (playerRole.isBroadcast()) {
                String broadcast = Language.lobby$broadcast.replace("{player}", Role.getPrefixed(player.getName()));

                Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                    for (Profile pf : Profile.listProfiles()) {
                        if (!pf.playingGame()) {
                            Player p = pf.getPlayer();
                            if (p != null && p.isOnline()) {
                                p.sendMessage(broadcast);
                            }
                        }
                    }
                });
            }
        });
    }
}