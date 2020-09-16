package com.caxerx.mc.interconomy.commandhandler.subcommand;

import com.caxerx.mc.interconomy.commandhandler.SubCommand;
import com.caxerx.mc.interconomy.cache.TransitionalType;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Created by caxerx on 2017/4/1.
 */
public class BalanceDepositSubCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        BalanceModifyCommand.execute(TransitionalType.DEPOSIT, sender, args);
    }

    @Override
    public List<String> getTabList(int arg) {
        return null;
    }

    @Override
    public String getPermission() {
        return "interconomy.modify";
    }

    @Override
    public String getName() {
        return "deposit";
    }
}
