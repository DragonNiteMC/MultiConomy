package com.ericlam.mc.multiconomy;

import com.ericlam.mc.multiconomy.config.ConomyConfig;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginCommandHacker {
    public static void registerCurrencyCommand(JavaPlugin plugin, String currency, ConomyConfig.Currency setting) throws Exception {
        if (plugin.getCommand(currency) != null) return;
        var constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
        constructor.setAccessible(true);
        var pluginCommand = (PluginCommand) constructor.newInstance(currency, plugin);
        pluginCommand.setAliases(setting.alias);
        pluginCommand.setDescription(currency + " 的貨幣指令");
        plugin.getServer().getCommandMap().register(plugin.getDescription().getName(), pluginCommand);
    }
}
