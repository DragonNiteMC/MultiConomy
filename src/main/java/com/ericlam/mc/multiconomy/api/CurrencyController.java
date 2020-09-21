package com.ericlam.mc.multiconomy.api;

import com.ericlam.mc.multiconomy.UpdateResult;
import com.ericlam.mc.multiconomy.cache.DataCachingException;
import org.bukkit.OfflinePlayer;

public interface CurrencyController {

    double getBalance(OfflinePlayer player) throws DataCachingException;

    UpdateResult withdrawPlayer(OfflinePlayer player, double value, String operator);

    UpdateResult depositPlayer(OfflinePlayer player, double value, String operator);

    UpdateResult setPlayer(OfflinePlayer player, double value, String operator);

}
