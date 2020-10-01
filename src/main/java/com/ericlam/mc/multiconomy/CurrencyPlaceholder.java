package com.ericlam.mc.multiconomy;

import com.ericlam.mc.multiconomy.cache.DataCachingException;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class CurrencyPlaceholder extends PlaceholderExpansion {

    private final Plugin plugin;

    public CurrencyPlaceholder(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        var controller = MultiConomy.getAPI().getCurrency(params);
        if (controller == null) return "[未知貨幣: " + params + "]";
        try {
            return controller.getBalance(player) + "";
        } catch (DataCachingException e) {
            return "[資料更新中]";
        }
    }

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getName().toLowerCase();
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }
}
