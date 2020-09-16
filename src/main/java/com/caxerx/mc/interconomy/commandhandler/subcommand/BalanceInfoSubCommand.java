package com.caxerx.mc.interconomy.commandhandler.subcommand;

import com.caxerx.mc.interconomy.commandhandler.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Created by caxerx on 2017/4/1.
 */
public class BalanceInfoSubCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eInterConomy &bby caxerx"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6SQL-Based async economy plugin"));
    }

    @Override
    public List<String> getTabList(int arg) {
        return null;
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public String getName() {
        return "info";
    }
}
