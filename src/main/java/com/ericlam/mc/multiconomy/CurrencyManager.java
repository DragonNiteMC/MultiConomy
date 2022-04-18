package com.ericlam.mc.multiconomy;

import com.ericlam.mc.multiconomy.cache.CacheManager;
import com.ericlam.mc.multiconomy.cache.TransitionManager;
import com.ericlam.mc.multiconomy.command.CurrencyMainCommand;
import com.ericlam.mc.multiconomy.config.ConomyConfig;
import com.ericlam.mc.multiconomy.config.MessageConfig;
import com.ericlam.mc.multiconomy.runnable.CacheUpdateRunnable;
import com.ericlam.mc.multiconomy.sql.MYSQLController;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CurrencyManager {

    private final String tablePrefix;
    private final Map<String, CacheManager> currencyControllerMap = new ConcurrentHashMap<>();
    private final MultiConomy plugin;
    private final MessageConfig msg;
    private final TransitionManager transitionManager;

    public CurrencyManager(MultiConomy plugin, String tablePrefix, MessageConfig msg) {
        this.plugin = plugin;
        this.tablePrefix = tablePrefix;
        this.msg = msg;
        this.transitionManager = new TransitionManager(plugin);
    }

    public void register(String currency, ConomyConfig.Currency settings) {
        var table = tablePrefix + currency + "_userdata";
        var sqlController = new MYSQLController(table, settings.defaultBalance);
        var cacheManager = new CacheManager(plugin, sqlController, currency, transitionManager);
        if (currencyControllerMap.putIfAbsent(currency, cacheManager) == null) {
            try{
                PluginCommandHacker.registerCurrencyCommand(plugin, currency, settings);
                DragonNiteMC.getAPI().getCommandRegister().registerCommand(plugin, new CurrencyMainCommand(currency, settings.alias, cacheManager, msg));
                Bukkit.getScheduler().runTaskAsynchronously(plugin, sqlController::createTable);
            } catch (Exception e) {
                plugin.getLogger().warning("無法註冊指令 /"+currency+", 略過此貨幣的註冊。");
            }
        }
    }

    public CacheManager getCurrencyController(String currency) {
        return this.currencyControllerMap.get(currency);
    }

    public void fetchAll(Player player) {
        currencyControllerMap.values().forEach(manager -> new CacheUpdateRunnable(manager, player).runTaskAsynchronously(plugin));
    }


}
