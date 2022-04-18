package com.ericlam.mc.multiconomy.command;

import com.ericlam.mc.multiconomy.api.CurrencyController;
import com.ericlam.mc.multiconomy.cache.CacheManager;
import com.ericlam.mc.multiconomy.config.MessageConfig;
import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BalanceMainCommand extends CommandNode {

    private final BalanceSubCommand proxy;

    public BalanceMainCommand(CurrencyController vaultCurrencyController, MessageConfig msg) {
        super(null, "money", "multiconomy.check", "查詢餘額", "[player]", "bal", "balance");
        this.proxy = new BalanceSubCommand(null, (CacheManager)vaultCurrencyController, msg);
    }

    @Override
    public boolean executeCommand(@NotNull CommandSender commandSender, @NotNull List<String> list) {
        return proxy.executeCommand(commandSender, list);
    }

    @Override
    public List<String> executeTabCompletion(@NotNull CommandSender commandSender, @NotNull List<String> list) {
        return null;
    }
}
