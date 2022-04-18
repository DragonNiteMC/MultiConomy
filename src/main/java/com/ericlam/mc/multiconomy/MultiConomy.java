package com.ericlam.mc.multiconomy;

import com.ericlam.mc.multiconomy.api.CurrencyController;
import com.ericlam.mc.multiconomy.api.MultiConomyAPI;
import com.ericlam.mc.multiconomy.api.VaultHandler;
import com.ericlam.mc.multiconomy.command.BalanceMainCommand;
import com.ericlam.mc.multiconomy.config.ConomyConfig;
import com.ericlam.mc.multiconomy.config.MessageConfig;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by caxerx on 2016/6/27.
 */
public class MultiConomy extends JavaPlugin implements MultiConomyAPI {
    private CurrencyManager currencyManager;
    private ConomyConfig config;
    private long updateTimeout;

    private static MultiConomy instance;

    public static MultiConomyAPI getAPI() {
        return instance;
    }

    public static Plugin getPlugin() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        var manager = DragonNiteMC.getAPI().getFactory().getConfigFactory(this)
                .register(ConomyConfig.class)
                .register(MessageConfig.class)
                .dump();
        this.config = manager.getConfigAs(ConomyConfig.class);
        MessageConfig msg = manager.getConfigAs(MessageConfig.class);
        this.updateTimeout = config.updateTimeout;
        this.currencyManager = new CurrencyManager(this, config.tablePrefix, msg);
        config.currencies.forEach(currencyManager::register);
        getServer().getPluginManager().registerEvents(new PlayerListener(currencyManager), this);
        if (getServer().getPluginManager().isPluginEnabled("Vault")){
            getLogger().info("Vault found, hooking into vault.");
            if (getVaultCurrency() == null) {
                getLogger().warning("Cannot setup Economy with vault: vault currency is null");
            } else {
                getServer().getServicesManager().register(Economy.class, new VaultHandler(getVaultCurrency(), msg), this, ServicePriority.High);
                DragonNiteMC.getAPI().getCommandRegister().registerCommand(this, new BalanceMainCommand(getVaultCurrency(), msg));
            }
        }
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")){
            getLogger().info("PlaceholderAPI is enabled. successfully registered for placeholders");
            new CurrencyPlaceholder(this).register();
        }
    }

    public long getUpdateTimeout() {
        return updateTimeout;
    }

    public void onDisable() {
        instance = null;
    }

    @Override
    public CurrencyController getCurrency(String currency) {
        return currencyManager.getCurrencyController(currency);
    }

    @Override
    public CurrencyController getVaultCurrency() {
        return currencyManager.getCurrencyController(config.vaultCurrency);
    }

    @Override
    public void registerCurrency(String currency, ConomyConfig.Currency setting) {
        currencyManager.register(currency, setting);
    }
}
