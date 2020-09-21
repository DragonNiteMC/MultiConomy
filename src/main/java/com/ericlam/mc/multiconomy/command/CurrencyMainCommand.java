package com.ericlam.mc.multiconomy.command;

import com.ericlam.mc.multiconomy.cache.CacheManager;
import com.ericlam.mc.multiconomy.config.MessageConfig;
import com.hypernite.mc.hnmc.core.misc.commands.DefaultCommand;

import java.util.List;

public class CurrencyMainCommand extends DefaultCommand {

    public CurrencyMainCommand(String currency, List<String> alias, CacheManager cacheManager, MessageConfig msg) {
        super(null, currency, null, currency+" 貨幣的指令");
        var modifier = new BalanceModifier(msg, cacheManager);
        this.addAllAliases(alias);
        this.addSub(new InfoSubCommand(this));
        this.addSub(new BalanceSubCommand(this, cacheManager, msg));
        this.addSub(new SetSubCommand(this, modifier));
        this.addSub(new DepositSubCommand(this, modifier));
        this.addSub(new WithdrawSubCommand(this, modifier));
    }

}
