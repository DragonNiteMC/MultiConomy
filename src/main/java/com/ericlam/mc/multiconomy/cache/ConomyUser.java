package com.ericlam.mc.multiconomy.cache;

import com.ericlam.mc.multiconomy.UpdateResult;
import org.bukkit.OfflinePlayer;

/**
 * Created by caxerx on 2016/8/13.
 */
public class ConomyUser {
    private final OfflinePlayer player;
    private double cachedBalance = 0.0d;
    private long lastCacheUpdate = -1;

    public ConomyUser(OfflinePlayer player) {
        this.player = player;
    }

    public boolean hasInitialize() {
        return lastCacheUpdate != -1;
    }

    public double getCachedBalance() {
        return cachedBalance;
    }

    public void cacheBalance(double cache) {
        cachedBalance = cache;
        lastCacheUpdate = System.currentTimeMillis();
    }

    public long getLastCacheUpdate() {
        return lastCacheUpdate;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }


}