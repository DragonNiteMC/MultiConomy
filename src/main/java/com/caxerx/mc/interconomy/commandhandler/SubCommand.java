package com.caxerx.mc.interconomy.commandhandler;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Created by caxerx on 2017/4/1.
 */
public interface SubCommand {
    void execute(CommandSender sender, String[] args);

    List<String> getTabList(int arg);

    String getPermission();

    String getName();
}
