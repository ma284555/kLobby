package dev.slickcollections.kiwizin.lobby;

import dev.slickcollections.kiwizin.Core;
import dev.slickcollections.kiwizin.database.cache.RedisCache;
import dev.slickcollections.kiwizin.lobby.cmd.Commands;
import dev.slickcollections.kiwizin.lobby.hook.LCoreHook;
import dev.slickcollections.kiwizin.lobby.listeners.Listeners;
import dev.slickcollections.kiwizin.lobby.lobby.DeliveryNPC;
import dev.slickcollections.kiwizin.lobby.lobby.Lobby;
import dev.slickcollections.kiwizin.lobby.lobby.PlayNPC;
import dev.slickcollections.kiwizin.plugin.KPlugin;
import dev.slickcollections.kiwizin.utils.BukkitUtils;
import dev.slickcollections.kiwizin.utils.tagger.TagUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class Main extends KPlugin {

    public static String currentServerName;
    private static Main instance;
    private static boolean validInit;

    // ✅ NOVO: Guardar referência da task do Redis
    private BukkitTask redisHeartbeatTask;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void start() {
        instance = this;
    }

    @Override
    public void load() {
        // Pode ser usado no futuro
    }

    @Override
    public void enable() {
        try {
            saveDefaultConfig();
            currentServerName = getConfig().getString("lobby");

            if (getConfig().getString("spawn") != null) {
                Core.setLobby(BukkitUtils.deserializeLocation(getConfig().getString("spawn")));
            }

            LCoreHook.setupHook();
            Lobby.setupLobbies();
            Listeners.setupListeners();
            Language.setupLanguage();
            PlayNPC.setupNPCs();
            DeliveryNPC.setupNPCs();
            Commands.setupCommands();

            // ✅ HEARTBEAT: Atualizar contagem de players online no Redis
            if (RedisCache.isEnabled()) {
                redisHeartbeatTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
                    try {
                        int online = Bukkit.getOnlinePlayers().size();
                        RedisCache.setOnlineCount(currentServerName, online);
                    } catch (Exception ex) {
                        getLogger().warning("Erro ao atualizar heartbeat no Redis: " + ex.getMessage());
                    }
                }, 0L, 20L * 10); // A cada 10 segundos

                getLogger().info("Redis heartbeat iniciado (10s).");
            }

            validInit = true;
            getLogger().info("Plugin ativado com sucesso!");

        } catch (Exception ex) {
            getLogger().severe("ERRO CRÍTICO ao ativar plugin!");
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void disable() {
        if (!validInit) {
            getLogger().warning("Plugin sendo desativado sem ter sido inicializado corretamente.");
            return;
        }

        try {
            // ✅ Cancelar task do Redis
            if (redisHeartbeatTask != null) {
                redisHeartbeatTask.cancel();
                getLogger().info("Redis heartbeat cancelado.");
            }

            // ✅ Salvar tags dos jogadores online (cache pré-desligamento)
            if (!Bukkit.getOnlinePlayers().isEmpty()) {
                getLogger().info("Salvando tags de " + Bukkit.getOnlinePlayers().size() + " jogadores...");

                Bukkit.getOnlinePlayers().forEach(player -> {
                    try {
                        TagUtils.CachedTag currentTag = TagUtils.loadTag(player.getName());
                        if (currentTag == null) {
                            TagUtils.setTag(player);
                        }
                    } catch (Exception ex) {
                        getLogger().warning("Erro ao salvar tag de " + player.getName() + ": " + ex.getMessage());
                    }
                });
            }

            // ✅ Limpar NPCs
            getLogger().info("Removendo NPCs...");
            PlayNPC.listNPCs().forEach(npc -> {
                try {
                    npc.destroy();
                } catch (Exception ex) {
                    getLogger().warning("Erro ao destruir PlayNPC: " + ex.getMessage());
                }
            });

            DeliveryNPC.listNPCs().forEach(npc -> {
                try {
                    npc.destroy();
                } catch (Exception ex) {
                    getLogger().warning("Erro ao destruir DeliveryNPC: " + ex.getMessage());
                }
            });

            // ✅ Limpar tags visuais
            getLogger().info("Limpando tags...");
            TagUtils.reset();

            // ✅ Remover servidor do Redis
            if (RedisCache.isEnabled()) {
                try {
                    RedisCache.setOnlineCount(currentServerName, 0);
                    getLogger().info("Servidor removido do Redis.");
                } catch (Exception ex) {
                    getLogger().warning("Erro ao remover servidor do Redis: " + ex.getMessage());
                }
            }

            getLogger().info("Plugin desativado com sucesso!");

        } catch (Exception ex) {
            getLogger().severe("ERRO ao desativar plugin!");
            ex.printStackTrace();
        }
    }
}