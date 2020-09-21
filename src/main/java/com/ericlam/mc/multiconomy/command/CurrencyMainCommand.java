package com.ericlam.mc.multiconomy.command;

import com.ericlam.mc.multiconomy.cache.CacheManager;
import com.ericlam.mc.multiconomy.config.MessageConfig;
import com.hypernite.mc.hnmc.core.misc.commands.DefaultCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CurrencyMainCommand extends DefaultCommand {

    private final BalanceSubCommand checkBalanceProxy;

    public CurrencyMainCommand(String currency, List<String> alias, CacheManager cacheManager, MessageConfig msg) {
        super(null, currency, null, currency+" 貨幣的指令");
        var modifier = new BalanceModifier(msg, cacheManager);
        this.checkBalanceProxy = new BalanceSubCommand(this, cacheManager, msg);
        this.addAllAliases(alias);
        this.addSub(new InfoSubCommand(this));
        this.addSub(checkBalanceProxy);
        this.addSub(new SetSubCommand(this, modifier));
        this.addSub(new DepositSubCommand(this, modifier));
        this.addSub(new WithdrawSubCommand(this, modifier));
    }

    @Override
    public boolean executeCommand(@NotNull CommandSender sender, @NotNull List<String> args) {
        if (args.size() > 0 && args.get(0).equalsIgnoreCase("help")){
            return super.executeCommand(sender, args);
        }else{
            return checkBalanceProxy.executeCommand(sender, args);
        }
    }

    @Override
    public List<String> executeTabCompletion(@NotNull CommandSender sender, @NotNull List<String> args) {
        var tab = super.executeTabCompletion(sender, args);
        if (tab != null){
            tab.add("help");
        }
        return tab;
    }
}
