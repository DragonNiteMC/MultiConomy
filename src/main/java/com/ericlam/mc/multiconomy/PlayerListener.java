package com.ericlam.mc.multiconomy;

import com.ericlam.mc.multiconomy.runnable.CacheUpdateRunnable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by caxerx on 2017/4/1.
 */
public class PlayerListener implements Listener {

    private final CurrencyManager currencyManager;

    public PlayerListener(CurrencyManager currencyManager) {
        this.currencyManager = currencyManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        currencyManager.fetchAll(e.getPlayer());
    }
}
