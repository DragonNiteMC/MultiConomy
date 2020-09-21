package com.ericlam.mc.multiconomy;

import com.ericlam.mc.multiconomy.api.CurrencyController;
import com.ericlam.mc.multiconomy.api.MultiConomyAPI;
import com.ericlam.mc.multiconomy.api.VaultHandler;
import com.ericlam.mc.multiconomy.config.ConomyConfig;
import com.ericlam.mc.multiconomy.config.MessageConfig;
import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import net.milkbowl.vault.economy.Economy;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

/**
 * Created by caxerx on 2016/6/27.
 */
public class MultiConomy extends JavaPlugin implements MultiConomyAPI {
    public static Economy econ = null;
    private CurrencyManager currencyManager;
    private ConomyConfig config;
    private MessageConfig msg;
    private long updateTimeout;

    private static MultiConomy instance;

    public static MultiConomyAPI getAPI(){
        return instance;
    }

    public static Plugin getPlugin() {
        return instance;
    }

    @Override
    public void onLoad() {
        var manager = HyperNiteMC.getAPI().getFactory().getConfigFactory(this)
                .register(ConomyConfig.class)
                .register(MessageConfig.class)
                .dump();
        this.config = manager.getConfigAs(ConomyConfig.class);
        this.msg = manager.getConfigAs(MessageConfig.class);
        config.currencies.forEach((cmd, setting) -> getDescription().getCommands().put(cmd, Map.of(
                "description", cmd + " 貨幣指令",
                "aliases", setting.alias
        )));
    }

    public void onEnable() {
        instance = this;
        this.updateTimeout = config.updateTimeout;
        this.currencyManager = new CurrencyManager(this, config.tablePrefix, msg);
        config.currencies.forEach(currencyManager::register);
        getServer().getPluginManager().registerEvents(new PlayerListener(currencyManager), this);
        if (!setupEconomy()) {
            getLogger().warning("Cannot setup Economy with vault");
        } else {
            getServer().getServicesManager().register(Economy.class, new VaultHandler(getVaultCurrency(), msg), this, ServicePriority.High);
        }
    }

    public long getUpdateTimeout() {
        return updateTimeout;
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

    @Override
    public CurrencyController getCurrency(String currency) {
        return currencyManager.getCurrencyController(currency);
    }

    @Override
    public CurrencyController getVaultCurrency() {
        var vaultController = currencyManager.getCurrencyController(config.vaultCurrency);
        Validate.notNull(vaultController, "VaultController is null");
        return vaultController;
    }

    @Override
    public void registerCurrency(String currency, ConomyConfig.Currency setting) {
        currencyManager.register(currency, setting);
    }
}
