package com.ericlam.mc.multiconomy.runnable;

import com.ericlam.mc.multiconomy.MultiConomy;
import com.ericlam.mc.multiconomy.cache.CacheManager;
import com.ericlam.mc.multiconomy.cache.DataCachingException;
import com.ericlam.mc.multiconomy.config.MessageConfig;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by caxerx on 2016/6/28.
 */
public class BalanceMessageRunnable extends BukkitRunnable {
    private final CacheManager cacheManager;
    private final OfflinePlayer player;
    private final CommandSender sender;
    private final MessageConfig messageConfig;

    public BalanceMessageRunnable(CacheManager cacheManager, CommandSender sender, OfflinePlayer player, MessageConfig messageConfig) {
        this.player = player;
        this.sender = sender;
        this.cacheManager = cacheManager;
        this.messageConfig = messageConfig;
    }

    @Override
    public void run() {
        try {
            if (player != null) {
                if (player.getPlayer() != null) {
                    var name = player.getPlayer().getName();
                    double balance  = cacheManager.getBalance(player);
                    String message = messageConfig.get("balance-message")
                            .replace("{money}", balance + "")
                            .replace("{player}", name)
                            .replace("{currency}", cacheManager.getCurrency());
                    sender.sendMessage(message);
                } else {
                    sender.sendMessage(messageConfig.get("command-augs-error-message"));
                }
            }
        } catch (DataCachingException e) {
            sender.sendMessage(messageConfig.get("data-caching-message"));
            new BalanceMessageRunnable(cacheManager, sender, player, messageConfig).runTaskLaterAsynchronously(MultiConomy.getPlugin(), 20L);
        }
    }
}
