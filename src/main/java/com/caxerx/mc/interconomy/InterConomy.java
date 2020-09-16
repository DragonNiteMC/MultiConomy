package com.caxerx.mc.interconomy;

import com.caxerx.mc.interconomy.commandhandler.CommandHandler;
import com.caxerx.mc.interconomy.commandhandler.CommandManager;
import com.caxerx.mc.interconomy.commandhandler.TabCompletion;
import com.caxerx.mc.interconomy.commandhandler.subcommand.*;
import com.caxerx.mc.interconomy.api.InterConomyAPI;
import com.caxerx.mc.interconomy.api.VaultHandler;
import com.caxerx.mc.interconomy.cache.CacheManager;
import com.caxerx.mc.interconomy.cache.TransitionManager;
import com.caxerx.mc.interconomy.sql.MYSQLController;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

/**
 * Created by caxerx on 2016/6/27.
 */
public class InterConomy extends JavaPlugin {
    public static Economy econ = null;
    static CacheManager cacheManager;
    static InterConomyAPI api;
    static TransitionManager transitionManager;
    static MYSQLController sqlController;
    static InterConomyConfig config;

    private static InterConomy instance;

    public void onEnable() {
        instance = this;
        config = new InterConomyConfig(this);
        sqlController = new MYSQLController(config);
        cacheManager = new CacheManager(this);
        transitionManager = new TransitionManager(this);
        api = new InterConomyAPI(cacheManager);

        Optional.ofNullable(getServer().getPluginCommand("money")).ifPresent(c->c.setExecutor(new CommandHandler()));
        Optional.ofNullable(getServer().getPluginCommand("money")).ifPresent(c->c.setTabCompleter(new TabCompletion()));
        CommandManager commandManager = new CommandManager();
        commandManager.registerCommand("info", 0, new BalanceInfoSubCommand());
        commandManager.registerCommand("balance", 0, new BalanceSelfSubCommand());
        commandManager.registerCommand("balance", 1, new BalanceOthersSubCommand());
        commandManager.registerCommand("set", 1, new BalanceSetSubCommand());
        commandManager.registerCommand("take", 2, new BalanceWithdrawSubCommand());
        commandManager.registerCommand("withdraw", 2, new BalanceWithdrawSubCommand());
        commandManager.registerCommand("add", 2, new BalanceDepositSubCommand());
        commandManager.registerCommand("deposit", 2, new BalanceDepositSubCommand());

        getServer().getServicesManager().register(Economy.class, new VaultHandler(this), this, ServicePriority.High);

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        if (!setupEconomy()){
            getLogger().warning("Cannot setup econmy with vault");
        }
    }

    public static InterConomy getInstance() {
        return instance;
    }

    public void onDisable() {
        instance = null;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

}
