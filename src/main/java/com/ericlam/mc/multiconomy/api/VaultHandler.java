package com.ericlam.mc.multiconomy.api;

import com.ericlam.mc.multiconomy.MultiConomy;
import com.ericlam.mc.multiconomy.UpdateResult;
import com.ericlam.mc.multiconomy.cache.DataCachingException;
import com.ericlam.mc.multiconomy.config.MessageConfig;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caxerx on 2016/6/27.
 */
public class VaultHandler implements Economy {
    private final CurrencyController controller;
    private final MessageConfig messageConfig;
    private static final Plugin plugin = MultiConomy.getPlugin();
    private final DecimalFormat decFormatter;

    public VaultHandler(CurrencyController controller, MessageConfig messageConfig) {
        this.controller = controller;
        this.messageConfig = messageConfig;
        decFormatter = new DecimalFormat("0");
    }

    @Override
    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    @Override
    public String getName() {
        return "MultiConomy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return decFormatter.format(v);
    }

    @Override
    public String currencyNamePlural() {
        return "$";
    }

    @Override
    public String currencyNameSingular() {
        return "$";
    }

    @Override
    @Deprecated
    public boolean hasAccount(String s) {
        return true;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return true;
    }

    @Override
    @Deprecated
    public boolean hasAccount(String s, String s1) {
        return true;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return true;
    }

    @Override
    @Deprecated
    public double getBalance(String s) {
        return getBalance(plugin.getServer().getOfflinePlayer(s));
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        try {
            return controller.getBalance(offlinePlayer);
        } catch (DataCachingException e) {
            return 0.0d;
        }
    }

    @Override
    @Deprecated
    public double getBalance(String s, String s1) {
        return getBalance(s);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        return getBalance(offlinePlayer);
    }

    @Override
    @Deprecated
    public boolean has(String s, double v) {
        return has(plugin.getServer().getOfflinePlayer(s), v);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        try {
            return controller.getBalance(offlinePlayer) >= v;
        } catch (DataCachingException e) {
            return false;
        }
    }

    @Override
    @Deprecated
    public boolean has(String s, String s1, double v) {
        return has(plugin.getServer().getOfflinePlayer(s), v);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return has(offlinePlayer, v);
    }

    @Override
    @Deprecated
    public EconomyResponse withdrawPlayer(String s, double v) {
        return withdrawPlayer(plugin.getServer().getOfflinePlayer(s), v);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        UpdateResult result = controller.withdrawPlayer(offlinePlayer, v, "Vault");
        return getResponse(result, v);
    }

    private EconomyResponse getResponse(UpdateResult result, double amount) {
        switch (result) {
            case SUCCESS:
                return new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, null);
            case BALANCE_INSUFFICIENT:
                return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, messageConfig.get("balance-insufficient-message"));
            case DATA_CACHING:
                return new EconomyResponse(amount, 0, EconomyResponse.ResponseType.FAILURE, messageConfig.get("data-caching-message"));
            default:
                return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "UNKNOWN ERROR");
        }
    }

    @Override
    @Deprecated
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return withdrawPlayer(plugin.getServer().getOfflinePlayer(s), v);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return withdrawPlayer(offlinePlayer, v);
    }

    @Override
    @Deprecated
    public EconomyResponse depositPlayer(String s, double v) {
        return depositPlayer(plugin.getServer().getOfflinePlayer(s), v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        UpdateResult result = controller.depositPlayer(offlinePlayer, v, "Vault");
        return getResponse(result, v);
    }

    @Override
    @Deprecated
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return depositPlayer(plugin.getServer().getOfflinePlayer(s), v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return depositPlayer(offlinePlayer, v);
    }


    @Override
    public EconomyResponse createBank(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not supported");
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not supported");
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not supported");
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not supported");
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not supported");
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not supported");
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not supported");
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not supported");
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not supported");
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not supported");
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Bank is not supported");
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return true;
    }

}
