package dev.slickcollections.kiwizin.lobby.listeners.player;

import dev.slickcollections.kiwizin.database.cache.RedisCache;
import dev.slickcollections.kiwizin.lobby.cmd.kl.BuildCommand;
import dev.slickcollections.kiwizin.utils.tagger.TagUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent evt) {
        evt.setQuitMessage(null);

        Player player = evt.getPlayer();
        String playerName = player.getName();

        // Remove do modo build
        BuildCommand.remove(player);

        // ✅ CORREÇÃO: Sempre tentar salvar a tag atual
        try {
            // Verifica se já tem cache
            TagUtils.CachedTag currentTag = TagUtils.loadTag(playerName);

            // Se não tiver cache OU se tiver FakeTeam, salvar tag atual
            if (currentTag == null || TagUtils.getFakeTeam(playerName) != null) {
                TagUtils.setTag(player);
            }
        } catch (Exception ex) {
            // Fallback: tentar salvar mesmo com erro
            try {
                TagUtils.setTag(player);
            } catch (Exception ignored) {}
        }

        // ✅ Remove a tag VISUAL (mas mantém no cache Redis/Local)
        TagUtils.reset(playerName);

        // ✅ Atualizar Redis
        if (RedisCache.isEnabled()) {
            try {
                RedisCache.removePlayerServer(playerName);
            } catch (Exception ignored) {}
        }
    }
}