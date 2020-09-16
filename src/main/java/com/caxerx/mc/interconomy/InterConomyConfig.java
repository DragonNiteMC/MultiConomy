package com.caxerx.mc.interconomy;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by caxerx on 2016/6/27.
 */
public class InterConomyConfig {
    public final String mysqlHost;
    public final String mysqlDatabase;
    public final int mysqlPort;
    public final String mysqlUsername;
    public final String mysqlPassword;
    public final String mysqlUserdataTable;

    public final int connectionPoolMaxConnections;
    public final int connectionPoolMinConnections;
    public final long connectionPoolTimeout;

    public final double defaultBalance;
    public final double updateTimeout;

    public final boolean mysqlSslEnable;

    public final String messagePrefix;
    public final String messageBalance;
    public final String messageBalanceInsufficient;
    public final String messageTransitionalFailure;
    public final String messageTransitionalSuccess;
    public final String messageDataCaching;

    public final String messageCommandPermissionInsufficient;
    public final String messageCommandNotFound;
    public final String messageCommandArgsError;

    public FileConfiguration config;
    public FileConfiguration message;

    private static InterConomyConfig instance;

    public InterConomyConfig(Plugin plugin) {
        instance = this;
        File msgFile = new File(plugin.getDataFolder().getPath() + File.separator + "message.yml");
        File configFile = new File(plugin.getDataFolder().getPath() + File.separator + "config.yml");

            if (!configFile.exists()) {
                plugin.saveResource("config.yml", true);
            }
            if (!msgFile.exists()) {
                plugin.saveResource("message.yml", true);
            }
        config = plugin.getConfig();
        config.options().copyDefaults(true);
        plugin.saveConfig();
        message = YamlConfiguration.loadConfiguration(msgFile);

        mysqlHost = config.getString("mysql-config.host");
        mysqlPort = config.getInt("mysql-config.port");
        mysqlUsername = config.getString("mysql-config.username");
        mysqlPassword = config.getString("mysql-config.password");
        mysqlDatabase = config.getString("mysql-config.database");
        mysqlUserdataTable = config.getString("userdata-table");
        mysqlSslEnable = config.getBoolean("mysql-config.ssl");
        connectionPoolMaxConnections = config.getInt("connection-pool.minimum-connections");
        connectionPoolMinConnections = config.getInt("connection-pool.maximum-connections");
        connectionPoolTimeout = config.getLong("connection-pool.timeout-millis");

        defaultBalance = config.getDouble("default-balance");
        updateTimeout = config.getDouble("update-timeout");

        messagePrefix = message.getString("prefix");
        messageBalance = messagePrefix + message.getString("balance-message");
        messageBalanceInsufficient = messagePrefix + message.getString("balance-insufficient-message");
        messageTransitionalFailure = messagePrefix + message.getString("transitional-failure-message");
        messageTransitionalSuccess = messagePrefix + message.getString("transitional-success-message");
        messageDataCaching = messagePrefix + message.getString("data-caching-message");

        messageCommandPermissionInsufficient = messagePrefix + message.getString("command-permission-insufficient-message");
        messageCommandArgsError = messagePrefix + message.getString("command-augs-error-message");
        messageCommandNotFound = messagePrefix + message.getString("command-not-found-message");
    }


    public static InterConomyConfig getInstance() {
        return instance;
    }

}
