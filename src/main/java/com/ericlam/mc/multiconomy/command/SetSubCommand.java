package com.ericlam.mc.multiconomy.command;

import com.ericlam.mc.multiconomy.cache.TransitionalType;
import com.hypernite.mc.hnmc.core.misc.commands.CommandNode;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class SetSubCommand extends CommandNode {

    private final BalanceModifier modifier;
    public SetSubCommand(CommandNode parent, BalanceModifier modifier) {
        super(parent, "set", "multiconomy.modify", "設置金額", "<player> <amount>", "change");
        this.modifier = modifier;
    }

    @Override
    public boolean executeCommand(@NotNull CommandSender commandSender, @NotNull List<String> list) {
        modifier.execute(TransitionalType.SET, commandSender, list);
        return true;
    }

    @Override
    public List<String> executeTabCompletion(@NotNull CommandSender commandSender, @NotNull List<String> list) {
        return null;
    }
}
