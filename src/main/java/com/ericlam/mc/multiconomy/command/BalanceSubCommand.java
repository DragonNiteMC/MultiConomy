package com.ericlam.mc.multiconomy.command;

import com.ericlam.mc.multiconomy.MultiConomy;
import com.ericlam.mc.multiconomy.cache.CacheManager;
import com.ericlam.mc.multiconomy.config.MessageConfig;
import com.ericlam.mc.multiconomy.runnable.BalanceMessageRunnable;
import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.misc.commands.CommandNode;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public final class BalanceSubCommand extends CommandNode {

    private final MessageConfig msg;
    private final CacheManager cacheManager;

    public BalanceSubCommand(CommandNode parent, CacheManager cacheManager, MessageConfig msg) {
        super(parent, "balance", "multiconomy.check", "查詢餘額", "[player]", "check", "money");
        this.cacheManager = cacheManager;
        this.msg = msg;
    }

    @Override
    public boolean executeCommand(@NotNull CommandSender commandSender, @NotNull List<String> list) {
        UUID uuid;
        if (list.size() > 1) {
            String target = list.get(0);
            uuid = Bukkit.getPlayerUniqueId(target);
        } else {
            if (!(commandSender instanceof Player)){
                commandSender.sendMessage(msg.getPrefix() + HyperNiteMC.getAPI().getCoreConfig().getNotPlayer());
                return true;
            }
            uuid = ((Player)commandSender).getUniqueId();
        }
        if (uuid == null) {
            commandSender.sendMessage(msg.getPrefix() + HyperNiteMC.getAPI().getCoreConfig().getNotFoundPlayer());
            return true;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        commandSender.sendMessage(msg.get("balance-checking"));
        new BalanceMessageRunnable(cacheManager, commandSender, player, msg).runTaskAsynchronously(MultiConomy.getPlugin());
        return true;
    }

    @Override
    public List<String> executeTabCompletion(@NotNull CommandSender commandSender, @NotNull List<String> list) {
        return null;
    }
}
