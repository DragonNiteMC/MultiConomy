package com.caxerx.mc.interconomy.commandhandler.subcommand;

import com.caxerx.mc.interconomy.commandhandler.SubCommand;
import com.caxerx.mc.interconomy.InterConomy;
import com.caxerx.mc.interconomy.runnable.BalanceMessageRunnable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.UUID;

/**
 * Created by caxerx on 2017/4/1.
 */
public class BalanceOthersSubCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        UUID uuid = Bukkit.getPlayerUniqueId(args[0]);
        OfflinePlayer player = uuid != null ? Bukkit.getOfflinePlayer(uuid) : null;
        new BalanceMessageRunnable(sender, player).runTaskAsynchronously(InterConomy.getInstance());
    }

    @Override
    public List<String> getTabList(int arg) {
        return null;
    }

    @Override
    public String getPermission() {
        return "interconomy.check.other";
    }

    @Override
    public String getName() {
        return "balance";
    }
}
