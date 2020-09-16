package com.caxerx.mc.interconomy.commandhandler.subcommand;

import com.caxerx.mc.interconomy.commandhandler.SubCommand;
import com.caxerx.mc.interconomy.InterConomy;
import com.caxerx.mc.interconomy.InterConomyConfig;
import com.caxerx.mc.interconomy.runnable.BalanceMessageRunnable;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by caxerx on 2017/4/1.
 */
public class BalanceSelfSubCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            new BalanceMessageRunnable(sender, (Player) sender).runTaskAsynchronously(InterConomy.getInstance());
        } else {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', InterConomyConfig.getInstance().messageCommandArgsError));
        }

    }

    @Override
    public List<String> getTabList(int arg) {
        return null;
    }

    @Override
    public String getPermission() {
        return "interconomy.check";
    }

    @Override
    public String getName() {
        return "balance";
    }
}
