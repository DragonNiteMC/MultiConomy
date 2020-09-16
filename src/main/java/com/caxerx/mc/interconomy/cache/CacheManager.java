package com.caxerx.mc.interconomy.cache;

import com.caxerx.mc.interconomy.InterConomy;
import com.caxerx.mc.interconomy.InterConomyConfig;
import com.caxerx.mc.interconomy.UpdateResult;
import com.caxerx.mc.interconomy.runnable.CacheUpdateRunnable;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.caxerx.mc.interconomy.UpdateResult.*;

/**
 * Created by caxerx on 2016/8/13.
 */
public class CacheManager {
    private static CacheManager instance;
    private InterConomy plugin;
    private ConcurrentHashMap<OfflinePlayer, InterConomyUser> users;

    public CacheManager(InterConomy plugin) {
        instance = this;
        this.plugin = plugin;
        users = new ConcurrentHashMap<>();
    }

    public static CacheManager getInstance() {
        return instance;
    }

    public InterConomyUser getPlayer(OfflinePlayer player) throws DataCachingException {
        if (playerCached(player)) {
            return users.get(player);
        }
        InterConomyUser user = new InterConomyUser(player);
        users.put(player, user);
        new CacheUpdateRunnable(player).runTaskAsynchronously(plugin);
        throw new DataCachingException();
    }

    public boolean playerCached(OfflinePlayer player) {
        return users.containsKey(player);
    }


    public UpdateResult withdrawPlayer(OfflinePlayer player, double value, String operator) {
        InterConomyUser cacheUser = null;
        try {
            cacheUser = getPlayer(player);
        } catch (DataCachingException e) {
            return DATA_CACHING;
        }

        if (cacheUser.hasInitialize()) {
            double cachedBalance = cacheUser.getCachedBalance();
            if (cachedBalance >= value) {
                cacheUser.cacheBalance(cachedBalance - value);
                TransitionManager.getInstance().offer(new TransitionAction(player, TransitionalType.WITHDRAW, value, operator));
                return SUCCESS;
            } else {
                return BALANCE_INSUFFICIENT;
            }
        } else {
            return DATA_CACHING;
        }
    }


    public UpdateResult depositPlayer(OfflinePlayer player, double value, String operator) {
        InterConomyUser cacheUser = null;
        try {
            cacheUser = getPlayer(player);
        } catch (DataCachingException e) {
            return DATA_CACHING;
        }

        if (cacheUser.hasInitialize()) {
            double cachedBalance = cacheUser.getCachedBalance();
            cacheUser.cacheBalance(cachedBalance - value);
            TransitionManager.getInstance().offer(new TransitionAction(player, TransitionalType.DEPOSIT, value, operator));
            return SUCCESS;
        } else {
            return DATA_CACHING;
        }
    }


    public UpdateResult setPlayer(OfflinePlayer player, double value, String operator) {
        InterConomyUser cacheUser = null;
        try {
            cacheUser = getPlayer(player);
        } catch (DataCachingException e) {
            return DATA_CACHING;
        }

        if (cacheUser.hasInitialize()) {
            double cachedBalance = cacheUser.getCachedBalance();
            cacheUser.cacheBalance(cachedBalance);
            TransitionManager.getInstance().offer(new TransitionAction(player, TransitionalType.SET, value, operator));
            return SUCCESS;
        } else {
            return DATA_CACHING;
        }
    }



    public void removeOfflinePlayer() {
        users.entrySet().removeIf(k->{
            InterConomyUser user = k.getValue();
            if (user == null) return false;
            return k.getKey().isOnline() && System.currentTimeMillis() - user.getLastCacheUpdate() > InterConomyConfig.getInstance().updateTimeout;
        });
    }
}
