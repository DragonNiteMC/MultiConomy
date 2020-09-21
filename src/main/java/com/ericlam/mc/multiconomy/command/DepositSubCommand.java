package com.ericlam.mc.multiconomy.command;

import com.ericlam.mc.multiconomy.cache.TransitionalType;
import com.hypernite.mc.hnmc.core.misc.commands.CommandNode;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class DepositSubCommand extends CommandNode {

    private final BalanceModifier modifier;

    public DepositSubCommand(CommandNode parent, BalanceModifier modifier) {
        super(parent, "deposit", "multiconomy.modify", "賦予金額", "<player> <amount>", "add", "give", "plus", "gain");
        this.modifier = modifier;
    }
    @Override
    public boolean executeCommand(@NotNull CommandSender commandSender, @NotNull List<String> list) {
        modifier.execute(TransitionalType.DEPOSIT, commandSender, list);
        return true;
    }

    @Override
    public List<String> executeTabCompletion(@NotNull CommandSender commandSender, @NotNull List<String> list) {
        return null;
    }
}
