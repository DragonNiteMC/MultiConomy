package com.ericlam.mc.multiconomy.runnable;

import com.ericlam.mc.multiconomy.MultiConomy;
import com.ericlam.mc.multiconomy.cache.CacheManager;
import com.ericlam.mc.multiconomy.cache.DataCachingException;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by caxerx on 2016/6/28.
 */
public class CacheUpdateRunnable extends BukkitRunnable {
    private final CacheManager cacheManager;
    private final OfflinePlayer player;
    private final Plugin plugin = MultiConomy.getPlugin();

    public CacheUpdateRunnable(CacheManager cacheManager, OfflinePlayer player) {
        this.cacheManager = cacheManager;
        this.player = player;
    }

    @Override
    public void run() {
        try {
            cacheManager.fetchBalance(player);
            cacheManager.removeOfflinePlayer();
        } catch (DataCachingException ex) {
            new CacheUpdateRunnable(cacheManager, player).runTaskAsynchronously(plugin);
        }
    }
}
