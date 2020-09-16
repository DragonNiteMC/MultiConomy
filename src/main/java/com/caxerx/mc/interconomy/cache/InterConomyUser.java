package com.caxerx.mc.interconomy.cache;

import com.caxerx.mc.interconomy.InterConomyConfig;
import com.caxerx.mc.interconomy.UpdateResult;
import com.caxerx.mc.interconomy.sql.MYSQLController;
import org.bukkit.OfflinePlayer;

/**
 * Created by caxerx on 2016/8/13.
 */
public class InterConomyUser {
    private final OfflinePlayer player;
    private double cachedBalance = 0.0d;
    private long lastCacheUpdate = -1;
    private static final CacheManager cacheManager = CacheManager.getInstance();

    public InterConomyUser(OfflinePlayer player) {
        this.player = player;
    }

    public boolean hasInitialize() {
        return !(lastCacheUpdate == -1);
    }

    public double getCachedBalance() {
        return cachedBalance;
    }

    public UpdateResult withdraw(double value, String operator) {
        return cacheManager.withdrawPlayer(player, value, operator);
    }

    public UpdateResult deposit(double value, String operator) {
        return cacheManager.depositPlayer(player, value, operator);
    }

    public UpdateResult set(double value, String operator) {
        return cacheManager.setPlayer(player, value, operator);
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