package com.ericlam.mc.multiconomy.cache;

import com.ericlam.mc.multiconomy.MultiConomy;
import com.ericlam.mc.multiconomy.UpdateResult;
import com.ericlam.mc.multiconomy.api.CurrencyController;
import com.ericlam.mc.multiconomy.runnable.CacheUpdateRunnable;
import com.ericlam.mc.multiconomy.sql.MYSQLController;
import com.ericlam.mc.multiconomy.sql.TableLockedException;
import org.bukkit.OfflinePlayer;

import java.util.concurrent.ConcurrentHashMap;

import static com.ericlam.mc.multiconomy.UpdateResult.*;

/**
 * Created by caxerx on 2016/8/13.
 */
public class CacheManager implements CurrencyController {
    private final MultiConomy plugin;
    private final MYSQLController mysqlController;
    private final TransitionManager transitionManager;
    private final ConcurrentHashMap<OfflinePlayer, ConomyUser> users;
    private final long updateTimeout;
    private final String currency;

    public CacheManager(MultiConomy plugin, MYSQLController mysqlController, String currency, TransitionManager transitionManager) {
        this.plugin = plugin;
        this.mysqlController = mysqlController;
        this.transitionManager = transitionManager;
        this.updateTimeout = plugin.getUpdateTimeout();
        this.currency = currency;
        users = new ConcurrentHashMap<>();
    }

    public String getCurrency() {
        return currency;
    }

    private ConomyUser getPlayer(OfflinePlayer player) throws DataCachingException {
        if (playerCached(player)) {
            return users.get(player);
        }
        ConomyUser user = new ConomyUser(player);
        users.put(player, user);
        new CacheUpdateRunnable(this, player).runTaskAsynchronously(plugin);
        throw new DataCachingException();
    }

    private boolean playerCached(OfflinePlayer player) {
        return users.containsKey(player);
    }

    @Override
    public double getBalance(OfflinePlayer player) throws DataCachingException {
        return getPlayer(player).getCachedBalance();
    }

    public UpdateResult commitPlayerBalance(OfflinePlayer player, double value, boolean set, boolean forceUnlock) throws TableLockedException{
        return mysqlController.updatePlayer(player, value, set, forceUnlock);
    }


    @Override
    public UpdateResult withdrawPlayer(OfflinePlayer player, double value, String operator) {
        ConomyUser cacheUser;
        try {
            cacheUser = getPlayer(player);
        } catch (DataCachingException e) {
            return DATA_CACHING;
        }

        if (cacheUser.hasInitialize()) {
            double cachedBalance = cacheUser.getCachedBalance();
            if (cachedBalance >= value) {
                cacheUser.cacheBalance(cachedBalance - value);
                transitionManager.offer(this, new TransitionAction(player, TransitionalType.WITHDRAW, value, operator));
                return SUCCESS;
            } else {
                return BALANCE_INSUFFICIENT;
            }
        } else {
            return DATA_CACHING;
        }
    }

    public void fetchBalance(OfflinePlayer player, int times) throws DataCachingException, TableLockedException {
        getPlayer(player).cacheBalance(mysqlController.getBalance(player, times <= 0));
    }


    @Override
    public UpdateResult depositPlayer(OfflinePlayer player, double value, String operator) {
        ConomyUser cacheUser = null;
        try {
            cacheUser = getPlayer(player);
        } catch (DataCachingException e) {
            return DATA_CACHING;
        }

        if (cacheUser.hasInitialize()) {
            double cachedBalance = cacheUser.getCachedBalance();
            cacheUser.cacheBalance(cachedBalance - value);
            transitionManager.offer(this, new TransitionAction(player, TransitionalType.DEPOSIT, value, operator));
            return SUCCESS;
        } else {
            return DATA_CACHING;
        }
    }


    @Override
    public UpdateResult setPlayer(OfflinePlayer player, double value, String operator) {
        ConomyUser cacheUser = null;
        try {
            cacheUser = getPlayer(player);
        } catch (DataCachingException e) {
            return DATA_CACHING;
        }

        if (cacheUser.hasInitialize()) {
            double cachedBalance = cacheUser.getCachedBalance();
            cacheUser.cacheBalance(cachedBalance);
            transitionManager.offer(this, new TransitionAction(player, TransitionalType.SET, value, operator));
            return SUCCESS;
        } else {
            return DATA_CACHING;
        }
    }


    public void removeOfflinePlayer() {
        users.entrySet().removeIf(k -> {
            ConomyUser user = k.getValue();
            if (user == null) return false;
            return k.getKey().isOnline() && System.currentTimeMillis() - user.getLastCacheUpdate() > updateTimeout;
        });
    }
}
