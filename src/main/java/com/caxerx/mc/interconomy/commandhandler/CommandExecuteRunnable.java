package com.caxerx.mc.interconomy.commandhandler;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by caxerx on 2017/4/1.
 */
public class CommandExecuteRunnable extends BukkitRunnable {
    SubCommand command;
    String[] args;
    CommandSender sender;

    public CommandExecuteRunnable(CommandSender sender, SubCommand command, String[] args) {
        this.command = command;
        this.args = args;
        this.sender = sender;
    }

    @Override
    public void run() {
        command.execute(sender, args);
    }
}
