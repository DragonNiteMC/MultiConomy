package com.ericlam.mc.multiconomy.command;

import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

public final class InfoSubCommand extends CommandNode {

    public InfoSubCommand(CommandNode parent) {
        super(parent, "info", null, "查看插件資料", null, "about");
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender commandSender, @Nonnull List<String> list) {
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eMultiConomy &bby EricLam"));
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6無限貨幣異步經濟插件"));
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8https://github.com/eric2788"));
        return true;
    }

    @Override
    public List<String> executeTabCompletion(@Nonnull CommandSender commandSender, @Nonnull List<String> list) {
        return null;
    }
}
